import { Component } from '@angular/core';
import { View } from '../view/view.const';
import { ViewService } from '../view/view.service';
import { LogoComponent } from './logo/logo.component';

@Component({
  selector: 'soo-toolbar',
  standalone: true,
  imports: [LogoComponent],
  templateUrl: './toolbar.component.html',
  styleUrl: './toolbar.component.scss'
})
export class ToolbarComponent {

  View = View;

  constructor(private viewService: ViewService) {
  }

  setView(view: View) {
    this.viewService.setView(view);
  }
}
