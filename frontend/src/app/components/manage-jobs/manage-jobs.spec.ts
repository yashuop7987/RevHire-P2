import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ManageJobs } from './manage-jobs';

describe('ManageJobs', () => {
  let component: ManageJobs;
  let fixture: ComponentFixture<ManageJobs>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ManageJobs]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ManageJobs);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
