import { NgForOf, NgIf } from '@angular/common';
import { Component } from '@angular/core';
import { IconComponent } from '../../../shared/components/icon/icon.component';

@Component({
  selector: 'soo-game-numbers',
  standalone: true,
  imports: [NgForOf, NgIf, IconComponent],
  templateUrl: './game-numbers.component.html',
  styleUrl: './game-numbers.component.scss'
})
export class GameNumbersComponent{

  numbers: number[] = [...Array(9).keys()].map(i => i + 1).concat(0);
}
