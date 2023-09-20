/* edit-dialog-component.component.ts*/

import {Component, EventEmitter, Input, OnInit, Output, ViewEncapsulation} from '@angular/core';
import { Powerplant } from '../map/map.component';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {MatDialogRef} from "@angular/material/dialog";
import {PowerPlantService} from "../services/powerplant.service";
import {firstValueFrom} from "rxjs";
import { Inject } from '@angular/core';
import {MatSnackBar} from "@angular/material/snack-bar";
import { MAT_DIALOG_DATA } from '@angular/material/dialog';

@Component({
  selector: 'app-edit-dialog-component',
  templateUrl: './edit-dialog-component.component.html',
  styleUrls: ['./edit-dialog-component.component.scss'],
  encapsulation: ViewEncapsulation.None

})
export class EditDialogComponentComponent {
  @Input() selectedPowerplant!: Powerplant | null;
  @Output() saveEvent = new EventEmitter<Powerplant>();

  editForm: FormGroup;

  constructor(
    private fb: FormBuilder,
    private dialogRef: MatDialogRef<EditDialogComponentComponent>,
    private powerPlantService: PowerPlantService,
    private _snackBar: MatSnackBar,

    @Inject(MAT_DIALOG_DATA) public data: Powerplant


) {  this.selectedPowerplant = data; // Assign the passed data to the  selectedPowerplant

    this.editForm = this.fb.group({
      name: ['', Validators.required],
      country_long: ['', Validators.required],
      country: ['', Validators.required],
      primary_fuel: ['', Validators.required],
      capacity_mw: ['', Validators.required],
      longitude: ['', Validators.required],
      latitude: ['', Validators.required]
    });
  }

  cancel() {
    this.dialogRef.close();
  }

  async saveFromEditForm() {
    if (this.editForm.valid) {
      if (this.selectedPowerplant) {
        const powerplant: Powerplant = {...this.selectedPowerplant, ...this.editForm.value};

        await firstValueFrom(this.powerPlantService.updatePowerPlant(this.selectedPowerplant.id, powerplant));

        this.saveEvent.emit(powerplant);  // Emit the save event
        this.dialogRef.close(powerplant);
        this._snackBar.open('Power plant has been updated', 'Close', {
          duration: 3000,
        });

      } else {

         console.error('Cannot save because selectedPowerplant is null');
      }
    }
  }
}
