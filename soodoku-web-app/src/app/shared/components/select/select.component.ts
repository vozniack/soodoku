import { NgForOf, NgIf } from '@angular/common';
import { Component, ElementRef, HostListener, Input } from '@angular/core';
import { FormControl } from '@angular/forms';
import { fadeInAnimation } from '../../animations/fade-in-animation';
import { IconComponent } from '../icon/icon.component';

@Component({
  selector: 'soo-select',
  standalone: true,
  imports: [IconComponent, NgIf, NgForOf],
  templateUrl: './select.component.html',
  styleUrl: './select.component.scss',
  animations: [fadeInAnimation]
})
export class SelectComponent {

  @Input() control!: FormControl;

  @Input() options: { name: string; value: any }[] = [];

  @Input() icon?: string;
  @Input() placeholder?: string;

  @Input() width?: string;
  @Input() maxWidth?: string;

  @Input() allowEmpty = false;

  active = false;

  constructor(public elementRef: ElementRef) {
  }

  getOptionName(): string | null {
    const option = this.options.find(o => o.value === this.control.value);
    return option ? option.name : null;
  }

  setValue(value: any): void {
    this.control.setValue(value);
    this.active = false;
  }

  toggleDropdown(): void {
    if (!this.control.disabled) {
      this.active = !this.active;
    }
  }

  @HostListener('document:click', ['$event'])
  handleClickOutside(event: MouseEvent): void {
    if (!this.elementRef.nativeElement.contains(event.target)) {
      this.active = false;
    }
  }
}
