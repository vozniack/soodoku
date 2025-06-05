import { Component, Input } from '@angular/core';

@Component({
  selector: 'soo-icon',
  standalone: true,
  imports: [],
  templateUrl: './icon.component.html',
  styleUrl: './icon.component.scss'
})
export class IconComponent {

  @Input()
  name!: string;

  @Input()
  size: number = 24;

  @Input()
  weight: number = 300;
}
