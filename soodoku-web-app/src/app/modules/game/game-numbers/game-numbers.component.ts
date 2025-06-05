import { NgForOf } from '@angular/common';
import { Component } from '@angular/core';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';
import { Store } from '@ngrx/store';
import { tap } from 'rxjs/operators';
import { Breakpoint } from '../../../core/breakpoint/breakpoint.interface';
import { SELECT_BREAKPOINT } from '../../../store/app/app.selectors';

@Component({
  selector: 'soo-game-numbers',
  standalone: true,
  imports: [NgForOf],
  templateUrl: './game-numbers.component.html',
  styleUrl: './game-numbers.component.scss'
})
export class GameNumbersComponent {

  breakpoint!: Breakpoint;

  numbers: number[] = [...Array(9).keys()].map(i => i + 1);

  constructor(private store: Store) {
    this.store.select(SELECT_BREAKPOINT).pipe(
      takeUntilDestroyed(),
      tap((breakpoint: Breakpoint) => this.breakpoint = breakpoint)
    ).subscribe();
  }
}
