import { ComponentFixture, TestBed } from '@angular/core/testing';

import { BadtasksComponent } from './badtasks.component';

describe('BadtasksComponent', () => {
  let component: BadtasksComponent;
  let fixture: ComponentFixture<BadtasksComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ BadtasksComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(BadtasksComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
