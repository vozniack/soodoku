import { NgIf } from '@angular/common';
import { Component, EventEmitter, Input, Output } from '@angular/core';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';
import { Store } from '@ngrx/store';
import { tap } from 'rxjs/operators';
import { Breakpoint } from '../../../core/breakpoint/breakpoint.interface';
import { SELECT_APP_BREAKPOINT } from '../../../store/app/app.selectors';
import { fadeInAnimation } from '../../animations/fade-in-animation';
import { IconComponent } from '../icon/icon.component';

@Component({
  selector: 'soo-user',
  standalone: true,
  imports: [IconComponent, NgIf],
  templateUrl: './user.component.html',
  styleUrl: './user.component.scss',
  animations: [fadeInAnimation]
})
export class UserComponent {

  @Input() username!: string;
  @Input() avatar?: string;

  @Input() active = false;
  @Input() removable = false;
  @Input() selectable = false;

  @Output() toRemove = new EventEmitter<void>();

  breakpoint!: Breakpoint;

  constructor(private store: Store) {
    this.store.select(SELECT_APP_BREAKPOINT).pipe(
      takeUntilDestroyed(),
      tap((breakpoint: Breakpoint) => this.breakpoint = breakpoint)
    ).subscribe();
  }

  setToRemove(): void {
    this.toRemove.emit();
  }
}
