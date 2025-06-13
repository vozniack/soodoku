import { NgClass, NgIf } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { IconComponent } from '../../shared/components/icon/icon.component';
import { SnackbarService } from './snackbar.service';

@Component({
  selector: 'soo-snackbar',
  standalone: true,
  imports: [NgIf, NgClass, IconComponent],
  templateUrl: './snackbar.component.html',
  styleUrl: './snackbar.component.scss'
})
export class SnackbarComponent implements OnInit {

  message!: string;
  icon?: string;

  visible = false;
  hiding = false;

  timeoutId: any;

  constructor(private snackbarService: SnackbarService) {
  }

  ngOnInit(): void {
    this.snackbarService.snackbarState$.subscribe(({message, icon, duration}) => {
      this.message = message;
      this.icon = icon;

      this.visible = true;
      this.hiding = false;

      if (this.timeoutId) {
        clearTimeout(this.timeoutId);
      }

      this.timeoutId = setTimeout(() => {
        this.hiding = true;

        setTimeout(() => {
          this.visible = false;
        }, 512);
      }, duration ?? 2048);
    });
  }
}
