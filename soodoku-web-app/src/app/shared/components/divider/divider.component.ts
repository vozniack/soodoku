import { NgClass } from '@angular/common';
import { Component, Input } from '@angular/core';

@Component({
  selector: 'soo-divider',
  standalone: true,
  imports: [NgClass],
  templateUrl: './divider.component.html',
  styleUrl: './divider.component.scss'
})
export class DividerComponent {

  @Input() vertical: boolean = false;

  @Input() width: string = '100%';
  @Input() maxWidth: string = '';

  @Input() height: string = '100%';
}
