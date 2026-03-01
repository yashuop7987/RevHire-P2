import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SeekerProfile } from './seeker-profile';

describe('SeekerProfile', () => {
  let component: SeekerProfile;
  let fixture: ComponentFixture<SeekerProfile>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [SeekerProfile]
    })
    .compileComponents();

    fixture = TestBed.createComponent(SeekerProfile);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
