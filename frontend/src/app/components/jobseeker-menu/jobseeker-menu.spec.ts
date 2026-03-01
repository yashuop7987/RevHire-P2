import { ComponentFixture, TestBed } from '@angular/core/testing';

import { JobseekerMenu } from './jobseeker-menu';

describe('JobseekerMenu', () => {
  let component: JobseekerMenu;
  let fixture: ComponentFixture<JobseekerMenu>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [JobseekerMenu]
    })
    .compileComponents();

    fixture = TestBed.createComponent(JobseekerMenu);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
