/*smap.component.ts*/
import {Component, OnInit, ViewChild, ElementRef, AfterViewInit, OnDestroy, Output, EventEmitter, Input} from '@angular/core';
import { Map, NavigationControl, Marker } from 'maplibre-gl';
import { WebSocketService } from "../services/web-socket.service";
import {PowerPlantService} from "../services/powerplant.service";


export interface Powerplant {
  id: number;
  country: string;
  country_long: string;
  name: string;
  gppd_idnr: string;
  capacity_mw: number;
  latitude: number;
  longitude: number;
  primary_fuel: string;
}

@Component({
  selector: 'app-map',
  templateUrl: './map.component.html',
  styleUrls: ['./map.component.scss']
})
export class MapComponent implements OnInit, AfterViewInit, OnDestroy {
  map: Map | undefined;
  @Input() selectedPowerplant: Powerplant | null = null;
  @Input() updatedPowerplant: Powerplant | null = null;
  @Output() powerplantSelected = new EventEmitter<Powerplant>();
  @Output() formSubmit: EventEmitter<Powerplant> = new EventEmitter();


  private powerplants = new Array<Powerplant>();
  private markers = new Array<Marker>();
  private markerMap: { [key: string]: Marker } = {};
  showEditForm = false;
  editMode: boolean = false;

  showSidebar = false;

  //private static apiUrl = 'assets/data/powerplants.json'; // (a)  (in case the producer is not running)
  private static apiUrl = 'http://localhost:8081/api/powerplants/'; //(b)
  @ViewChild('map')
  private mapContainer!: ElementRef<HTMLElement>;

  public static getApiUrl(): string {
    return MapComponent.apiUrl;
  }

  //update the markers based on the severity of the incidents.
  constructor(private webSocketService: WebSocketService) {
  }
  onEditPowerplant(powerplant: Powerplant) {
    this.showEditForm = true;
    this.selectedPowerplant = powerplant;
  }

  onCancelEdit() {
    this.showEditForm = false;
  }

  onSaveEdit(updatedPowerplant: Powerplant) {
    // This function will update the powerplant data and save it to the database.
    this.updatePowerplantMarker(updatedPowerplant);

  }
  updatePowerplantMarker(updatedPowerplant: Powerplant) {
    const marker = this.markerMap[updatedPowerplant.id];
    if (marker) {
      marker.setLngLat([updatedPowerplant.longitude, updatedPowerplant.latitude]);
      if (this.map) {
        this.map.flyTo({ center: [updatedPowerplant.longitude, updatedPowerplant.latitude], zoom: 3 });
      }
    } else {
      console.error(`Could not find marker for powerplant with id: ${updatedPowerplant.id}`);
    }
  }
  removePowerplantMarker(deletedPowerplant: Powerplant) {
    const marker = this.markerMap[deletedPowerplant.id];
    if (marker) {
      marker.remove();
      delete this.markerMap[deletedPowerplant.id];
      this.showSidebar = false;

    } else {
      console.error(`Could not find marker for powerplant with id: ${deletedPowerplant.id}`);
    }
  }



  async ngOnInit() {
    // Wait for connection
    await this.webSocketService.connectionEstablished();
    // subscribe
    this.webSocketService.connect();

    this.webSocketService.getObservable().subscribe({
      next: (message: any) => {
        const randomDataString = JSON.parse(message.body);
        if(typeof randomDataString === 'string') {
          console.log('Received message from messages topic:', randomDataString);
        }
        else
        {
          console.log('Received message from incidents topic:', message);
          this.updateMarker(message.powerPlantId, message.severity);
        }

      },
      error: (err: any) => console.log('WebSocket error: ', err),
      complete: () => console.log('WebSocket connection closed from the client side!!!!')
    });

  }
  ngAfterViewInit() {
    const initialState = { longitude: 25.6533, latitude: 36.6444, zoom: 1.6 };

    this.map = new Map({
      container: this.mapContainer.nativeElement,
      style: `https://api.maptiler.com/maps/streets-v2/style.json?key=Ejkru5WEGHwVUcxxZHkQ`,
      center: [initialState.longitude, initialState.latitude],
      zoom: initialState.zoom
    });
    this.map.addControl(new NavigationControl({}), 'top-right');

    this.loadPowerplantsData()
      .then(data => {
        this.addMarkers(data)
      })
      .catch(error => {
        console.error(error)
        // maybe print a message to the user as well?
      });
  }
  ngOnDestroy() {

    this.webSocketService.disconnect();
    this.map?.remove();
  }
  onFormSubmit(powerplant: Powerplant) {
    // Call   service to save powerplant changes to the backend
    this.formSubmit.emit(powerplant);
    console.log('Form submit:', powerplant);
  }

  onIncidentOccurred({powerPlantId, severity}: {powerPlantId: number, severity: string}) {
    this.updateMarker(powerPlantId, severity);
  }
  private updateMarker(powerPlantId: number, severity: string): void {
    console.log(`Updating marker for powerPlantId: ${powerPlantId}, severity: ${severity}`);

    const oldMarker = this.markerMap[powerPlantId];
    if (oldMarker) {
      oldMarker.remove();
      const newMarkerColor = this.getMarkerColor(severity);

      const powerplant = this.powerplants.find(p => p.id === powerPlantId);
      if (powerplant && this.map) {
        const newMarker = this.createMarker(powerplant, newMarkerColor);
        newMarker.addTo(this.map);
        this.markerMap[powerPlantId] = newMarker;
      }
    } else {
      console.log(`No marker found for powerPlantId: ${powerPlantId}`);
    }
  }

  private getMarkerColor(severity: string): string {
    switch (severity) {
      case 'High':
        return '#FF0000';
      case 'Medium':
        return '#FF8800';
      case 'Low':
        return '#ffea00';
      default:
        return '#00c603';
    }
  }




  private async loadPowerplantsData(): Promise<Powerplant[]> {
    const response = await fetch(MapComponent.apiUrl);
    if (!response.ok) {
      throw new Error('Failed to load powerplants data');
    }

    const data = await response.json();
    this.powerplants = data;

    return data;
  }
  private addMarkers(_powerplants: Powerplant[]) {
    this.powerplants.forEach((powerplant: Powerplant) => {
      if (this.map) {
        const marker = this.createMarker(powerplant);

        marker.addTo(this.map);

        this.markers.push(marker);
        this.markerMap[powerplant.id] = marker;
      }
    });
  }

  private createMarker(powerplant: Powerplant, color: string = "#00c603"): Marker {

    const marker = new Marker({ color: color })
      .setLngLat([powerplant.longitude, powerplant.latitude])

    marker.getElement().addEventListener('click', (event) => {
      setTimeout(() => {
        this.selectedPowerplant = powerplant;
      }, 0);
      this.selectedPowerplant = powerplant;
      this.showSidebar = true;
      event.stopPropagation();  // prevents the closeSidebar() from being called
      console.log("the sidebar should be open");

      // TODO put this in a separate method sometime
      fetch(MapComponent.apiUrl + 'start-production/' + `${powerplant.id}`, { method: 'POST' })
        .then(response => {
          if (!response.ok) {
            throw new Error('Failed to call the API endpoint');
          }
          // handle successful response
        })
        .catch(error => {
          console.error(error);
        });
      console.log("production started for powerplant with id: " + powerplant.id);
    });

    return marker;
  }


  closeSidebar() {
    this.showSidebar = false;
    console.log("the sidebar should be closed");

    // TODO place this one in a separate method sometime as well
    fetch(MapComponent.apiUrl + 'stop-production', { method: 'POST' })
      .then(response => {
        if (!response.ok) {
          throw new Error('Failed to call the API endpoint');
        }
        // again maybe try to handle the response somehow? if necessary??
      })
      .catch(error => {
        console.error(error);
      });
    console.log("production stopped");
  }


}
// TODO: delete the comments and console logs in the future (;
