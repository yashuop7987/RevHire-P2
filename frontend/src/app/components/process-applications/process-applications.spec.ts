import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ProcessApplications } from './process-applications';

describe('ProcessApplications', () => {
  let component: ProcessApplications;
  let fixture: ComponentFixture<ProcessApplications>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ProcessApplications]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ProcessApplications);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
