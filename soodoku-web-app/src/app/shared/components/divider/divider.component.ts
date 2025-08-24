import { NgClass, NgIf } from '@angular/common';
import { Component, Input } from '@angular/core';

@Component({
  selector: 'soo-divider',
  standalone: true,
  imports: [NgClass, NgIf],
  templateUrl: './divider.component.html',
  styleUrl: './divider.component.scss'
})
export class DividerComponent {

  @Input() text?: string;

  @Input() vertical: boolean = false;

  @Input() width: string = '100%';
  @Input() maxWidth: string = '';

  @Input() height: string = '100%';
}
