import { Component, Input } from '@angular/core';
import { IconComponent } from '../../../shared/components/icon/icon.component';

@Component({
  selector: 'soo-home-section',
  standalone: true,
  imports: [
    IconComponent
  ],
  templateUrl: './home-section.component.html',
  styleUrl: './home-section.component.scss'
})
export class HomeSectionComponent {

  @Input() text!: string;
  @Input() icon!: string;

  @Input() size = 36;

  @Input() disabled = false;
}
