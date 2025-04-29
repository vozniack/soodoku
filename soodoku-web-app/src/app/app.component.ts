import { Component } from '@angular/core';
import { ContentComponent } from './core/content/content.component';
import { ThemeService } from './core/theme/theme.service';

@Component({
  selector: 'soo-root',
  standalone: true,
  imports: [ContentComponent],
  templateUrl: './app.component.html',
  styleUrl: './app.component.scss'
})
export class AppComponent {

  constructor(private themeService: ThemeService) {
    this.themeService.applyTheme();
  }
}
