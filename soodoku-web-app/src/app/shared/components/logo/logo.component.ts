import { Component, Input } from '@angular/core';

@Component({
  selector: 'soo-logo',
  standalone: true,
  imports: [],
  templateUrl: './logo.component.html',
  styleUrl: './logo.component.scss'
})
export class LogoComponent {

  @Input() width: string = '100%';
  @Input() height: string = '100%';
}
