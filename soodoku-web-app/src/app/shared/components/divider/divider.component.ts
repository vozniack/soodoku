import { Component, Input } from '@angular/core';

@Component({
  selector: 'soo-divider',
  standalone: true,
  imports: [],
  templateUrl: './divider.component.html',
  styleUrl: './divider.component.scss'
})
export class DividerComponent {

  @Input() width: string = '100%';
  @Input() maxWidth: string = '';
}
