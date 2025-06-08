import { Component, EventEmitter, Output } from '@angular/core';

@Component({
  selector: 'soo-difficulty-dialog',
  standalone: true,
  templateUrl: './difficulty-dialog.component.html',
  styleUrl: './difficulty-dialog.component.scss',
})
export class DifficultyDialogComponent {

  @Output() result = new EventEmitter<string>();

  select(level: string) {
    this.result.emit(level);
  }
}
