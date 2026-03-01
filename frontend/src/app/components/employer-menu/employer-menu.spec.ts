import { ComponentFixture, TestBed } from '@angular/core/testing';

import { EmployerMenu } from './employer-menu';

describe('EmployerMenu', () => {
  let component: EmployerMenu;
  let fixture: ComponentFixture<EmployerMenu>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [EmployerMenu]
    })
    .compileComponents();

    fixture = TestBed.createComponent(EmployerMenu);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
