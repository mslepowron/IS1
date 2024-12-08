import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DevideOrderComponent } from './devide-order.component';

describe('DevideOrderComponent', () => {
  let component: DevideOrderComponent;
  let fixture: ComponentFixture<DevideOrderComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [DevideOrderComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(DevideOrderComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
