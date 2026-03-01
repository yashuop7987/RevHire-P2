import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SeekerNotification } from './seeker-notification';

describe('SeekerNotification', () => {
  let component: SeekerNotification;
  let fixture: ComponentFixture<SeekerNotification>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [SeekerNotification]
    })
    .compileComponents();

    fixture = TestBed.createComponent(SeekerNotification);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
