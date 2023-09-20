import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { MapComponent } from '../map/map.component';

@Injectable({
  providedIn: 'root'
})
export class PowerPlantService {
  private apiUrl: string;

  constructor(private http: HttpClient) {
    this.apiUrl = MapComponent.getApiUrl();
  }

  updatePowerPlant(id: number, powerplant: any): Observable<any> {
    return this.http.put(this.apiUrl + id, powerplant);
  }
  deletePowerPlant(id: number): Observable<any> {
    return this.http.delete(this.apiUrl + id);
  }
  createPowerPlant(powerplant: any): Observable<any> {
    return this.http.post(this.apiUrl, powerplant);
  }
}
