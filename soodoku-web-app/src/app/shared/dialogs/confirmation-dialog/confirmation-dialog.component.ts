import { Component, EventEmitter, Input, Output } from '@angular/core';
import { ButtonComponent } from '../../components/button/button.component';

@Component({
  selector: 'soo-confirmation-dialog',
  standalone: true,
  imports: [ButtonComponent],
  templateUrl: './confirmation-dialog.component.html',
  styleUrl: './confirmation-dialog.component.scss'
})
export class ConfirmationDialogComponent {

  @Input() title = 'Are you sure?';
  @Input() description = '';

  @Output() result = new EventEmitter<boolean>();

  close(result: boolean) {
    this.result.emit(result);
  }
}
