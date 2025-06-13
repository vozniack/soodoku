import { NgIf } from '@angular/common';
import { Component, EventEmitter, Output } from '@angular/core';
import { Store } from '@ngrx/store';
import { tap } from 'rxjs/operators';
import { ACTION_GAME_NEW } from '../../../store/app/game/game.actions';
import { SELECT_USER_STATE } from '../../../store/app/user/user.selector';
import { UserState } from '../../../store/app/user/user.state';
import { ButtonComponent } from '../../components/button/button.component';

@Component({
  selector: 'soo-difficulty-dialog',
  standalone: true,
  templateUrl: './difficulty-dialog.component.html',
  styleUrl: './difficulty-dialog.component.scss',
  imports: [ButtonComponent, NgIf,]
})
export class DifficultyDialogComponent {

  @Output() result = new EventEmitter<boolean>();

  loggedIn = false;

  constructor(private store: Store) {
    this.store.select(SELECT_USER_STATE).pipe(
      tap((userState: UserState) => this.loggedIn = userState?.user != undefined)
    ).subscribe();
  }

  select(difficulty: string) {
    this.store.dispatch(ACTION_GAME_NEW({difficulty}));
    this.result.emit(true);
  }
}
