import { Component } from '@angular/core';
import { LogoComponent } from './logo/logo.component';

@Component({
  selector: 'soo-toolbar',
  standalone: true,
  imports: [LogoComponent],
  templateUrl: './toolbar.component.html',
  styleUrl: './toolbar.component.scss'
})
export class ToolbarComponent {
}
