/*sidebar.component.ts*/
import {ChangeDetectorRef, Component, EventEmitter, Input, Output} from '@angular/core';
import { Powerplant } from '../map/map.component';
import { PowerPlantService } from '../services/powerplant.service';
import { WebSocketService } from '../services/web-socket.service';
import { MatDialog } from '@angular/material/dialog';
import { EditDialogComponentComponent } from '../edit-dialog-component/edit-dialog-component.component';
import {MatSnackBar} from "@angular/material/snack-bar";


@Component({
  selector: 'app-sidebar',
  templateUrl: './sidebar.component.html',
  styleUrls: ['./sidebar.component.scss']
})
export class SidebarComponent {
  selectedTab = 'details';
  editMode = false;
  @Input() selectedPowerplant: Powerplant | null = null;
  originalPowerPlantData: Powerplant | null = null;
  incidentsDataForCurrentPowerPlant: any[] = [];
  incidentsDataForCurrentSession: any[] = [];
  @Output() edit = new EventEmitter<Powerplant>();
  @Output() incidentOccurred = new EventEmitter<{powerPlantId: number, severity: string}>();
  @Output() updatedPowerplant = new EventEmitter<Powerplant>();
  @Output() deletedPowerplant = new EventEmitter<Powerplant>();


  // randomData: any[] = [];

  constructor(private dialog: MatDialog,
              private powerPlantService: PowerPlantService,
              private webSocketService: WebSocketService,
              private _snackBar: MatSnackBar,
              private changeDetectorRef: ChangeDetectorRef
  ) {
  }

  ngOnInit() {
    this.subscribeToIncidentsTopic();
    // this.subscribeToRandomDataTopic();
  }

  ngOnChanges() {
    this.incidentsDataForCurrentPowerPlant = [];

    // check if the selected powerplant already has incidents in the incidentsDataForCurrentSession array

    // reset the randomData array
    // this.randomData = [];
  }
  parseToFloat(value: string): number {
    return parseFloat(value);
  }

  subscribeToIncidentsTopic() {
    this.webSocketService.subscribeToTopic('/topic/incidents', (message) => {
      console.log('Received message from incidents topic:', message);
      // add the incident to the incidentsDataForCurrentSession array

      // check if the incident is for the current powerplant.
      if (message.powerPlant.id === this.selectedPowerplant?.id) {
        const incident = {
          timestamp: message.timestamp || '',
          description: message.description || '',
          severity: message.severity || '',
          status: message.status || '',
        };
        this.incidentsDataForCurrentPowerPlant.push(incident);

        // Trigger change detection
        this.changeDetectorRef.detectChanges();
        this.incidentOccurred.emit({powerPlantId: message.powerPlant.id, severity: message.severity});

      }
    });
  }




  // subscribeToRandomDataTopic() {
  //   this.webSocketService.subscribeToTopic('/topic/random_data', (message) => {
  //     console.log('Received message from randomdata topic:', message);
  //     this.randomData.push(message);
  //   });
  // }

  cancelEdit() {
    if (this.originalPowerPlantData) {
      this.selectedPowerplant = this.originalPowerPlantData;
      this.originalPowerPlantData = null;
      this.editMode = false;
    }

    console.log('the edit has been cancelled');
  }

  async savePowerplant() {
    this.editMode = false;
    if (this.selectedPowerplant) {
      try {
        if(this.selectedPowerplant.id) {
          await this.powerPlantService.updatePowerPlant(this.selectedPowerplant.id, this.selectedPowerplant).toPromise();
          this.updatedPowerplant.emit(this.selectedPowerplant);

          this._snackBar.open('Power plant has been updated', 'Close', {
            duration: 3000,
          });
        } else {
          await this.powerPlantService.createPowerPlant(this.selectedPowerplant).toPromise();
          this._snackBar.open('New power plant has been created', 'Close', {
            duration: 3000,
          });
        }
      } catch (error) {
        console.log('an error occurred while saving the powerplant data:', error);
      }
    }
  }


  editPowerplant() {
    const dialogRef = this.dialog.open(EditDialogComponentComponent, {
      width: '500px',
      data: this.selectedPowerplant
    });

    dialogRef.componentInstance.saveEvent.subscribe((updatedPowerplant: Powerplant) => {
       this.selectedPowerplant = updatedPowerplant;
       this.updatedPowerplant.emit(this.selectedPowerplant);
    });


      dialogRef.afterClosed().subscribe(result => {
      if (result) {
        this.selectedPowerplant = result;
      }
    });
  }



  addPowerplant() {
    this.selectedPowerplant = null; // for add new power plant
    this.editMode = true;
  }

  async deletePowerplant() {
    if (this.selectedPowerplant) {
      try {
        await this.powerPlantService.deletePowerPlant(this.selectedPowerplant.id).toPromise();
        this.deletedPowerplant.emit(this.selectedPowerplant);

        console.log('the powerplant has been deleted');
        this.selectedPowerplant = null; // TODO: or emit an event to reload the powerplant list

        this._snackBar.open('Power plant has been deleted', 'Close', {
          duration: 3000,
        });
      } catch (error) {
        console.log('An error occurred while deleting the powerplant:', error);
      }
    }
  }


}
