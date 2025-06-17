import { Component, Input } from '@angular/core';
import { FormControl, ReactiveFormsModule } from '@angular/forms';
import { IconComponent } from '../icon/icon.component';

@Component({
  selector: 'soo-switch',
  standalone: true,
  imports: [IconComponent, ReactiveFormsModule],
  templateUrl: './switch.component.html',
  styleUrl: './switch.component.scss'
})
export class SwitchComponent {

  @Input() control: FormControl = new FormControl();

  @Input() iconOff?: string;
  @Input() iconOn?: string;

  @Input() width = '80px';

  getIcon(): string {
    if (this.control.value === true) {
      return this.iconOn ?? '';
    } else if (this.control.value === false) {
      return this.iconOff ?? '';
    }

    return '';
  }
}
