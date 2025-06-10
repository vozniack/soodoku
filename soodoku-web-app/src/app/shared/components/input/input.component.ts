import { NgIf } from '@angular/common';
import { Component, Input } from '@angular/core';
import { FormControl, ReactiveFormsModule } from '@angular/forms';
import { IconComponent } from '../icon/icon.component';

@Component({
  selector: 'soo-input',
  standalone: true,
  imports: [ReactiveFormsModule, IconComponent, NgIf],
  templateUrl: './input.component.html',
  styleUrl: './input.component.scss'
})
export class InputComponent {

  @Input() control: FormControl = new FormControl();

  @Input() id: string = '';
  @Input() name: string = '';

  @Input() type: string = 'text';

  @Input() placeholder: string = '';
  @Input() placeholderShrink = true;

  @Input() maxLength: number = 128;

  @Input() icon: string = '';

  @Input() password: boolean = false;
  @Input() autocomplete: string = 'on';

  @Input() width: string = '100%';
  @Input() maxWidth: string = '';

  switchVisibility(): void {
    this.type = this.type == 'password' ? 'text' : 'password';
  }
}
