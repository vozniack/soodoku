import { NgForOf } from '@angular/common';
import { Component, HostListener, OnInit } from '@angular/core';

@Component({
  selector: 'soo-game-numbers',
  standalone: true,
  imports: [NgForOf],
  templateUrl: './game-numbers.component.html',
  styleUrl: './game-numbers.component.scss'
})
export class GameNumbersComponent implements OnInit {

  numbers: string[] = [...Array(9).keys()].map(i => (i + 1).toString()).concat('x');
  cellSize: number = 16;

  ngOnInit(): void {
    this.calculateCellSize();
  }

  @HostListener('window:resize')
  resize(): void {
    this.calculateCellSize();
  }

  // Supporting methods

  private calculateCellSize(): void {
    const width = (window.innerWidth < 1000 ? window.innerWidth : 1000) - 100;
    const height = window.innerHeight - 356;

    this.cellSize = (height > width ? width / 10 : height / 10) - 2;
  }
}
