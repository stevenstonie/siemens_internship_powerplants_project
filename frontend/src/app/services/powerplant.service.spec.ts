import { TestBed } from '@angular/core/testing';

import { PowerPlantService } from './powerplant.service';

describe('PowerplantService', () => {
  let service: PowerPlantService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(PowerPlantService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
