import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { fadeInAnimation } from '../../shared/animations/fade-in-animation';
import { ToolbarComponent } from '../toolbar/toolbar.component';

@Component({
  selector: 'soo-content',
  standalone: true,
  imports: [RouterOutlet, ToolbarComponent],
  templateUrl: './content.component.html',
  styleUrl: './content.component.scss',
  animations: [fadeInAnimation]
})
export class ContentComponent {
}
