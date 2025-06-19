import { NgForOf, NgIf } from '@angular/common';
import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { fadeInAnimation } from '../../animations/fade-in-animation';
import { GroupButton } from './button-group.interface';

@Component({
  selector: 'soo-button-group',
  standalone: true,
  imports: [NgIf, NgForOf],
  templateUrl: './button-group.component.html',
  styleUrl: './button-group.component.scss',
  animations: [fadeInAnimation]
})
export class ButtonGroupComponent implements OnInit {

  @Input() buttons!: GroupButton[];
  @Input() active?: GroupButton;
  @Output() buttonChange = new EventEmitter<GroupButton>();

  ngOnInit(): void {
    if (this.active == null) {
      this.active = this.buttons[0];
    }
  }

  setActive(active: GroupButton): void {
    this.buttonChange.emit(this.active = active);
  }
}
