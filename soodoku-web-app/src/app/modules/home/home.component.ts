import { Component } from '@angular/core';
import { Store } from '@ngrx/store';
import { filter, from } from 'rxjs';
import { switchMap, tap } from 'rxjs/operators';
import { ModalService } from '../../core/modal/modal.service';
import { View } from '../../core/view/view.const';
import { LogoComponent } from '../../shared/components/logo/logo.component';
import { ViewAwareComponent } from '../../core/view/view.component';
import { ViewService } from '../../core/view/view.service';
import { fadeInAnimation } from '../../shared/animations/fade-in-animation';
import { IconComponent } from '../../shared/components/icon/icon.component';
import { DifficultyDialogComponent } from '../../shared/dialogs/difficulty-dialog/difficulty-dialog.component';
import { ACTION_SET_GAME, ACTION_SET_VIEW } from '../../store/app/app.actions';
import { Game } from '../game/game.interface';
import { GameService } from '../game/game.service';

@Component({
  selector: 'soo-home',
  standalone: true,
  imports: [IconComponent, LogoComponent],
  templateUrl: './home.component.html',
  styleUrl: './home.component.scss',
  animations: [fadeInAnimation]
})
export class HomeComponent extends ViewAwareComponent {

  constructor(viewService: ViewService, private modalService: ModalService, private gameService: GameService, private store: Store) {
    super(viewService);
  }

  newGame(): void {
    from(this.modalService.open(DifficultyDialogComponent))
      .pipe(
        filter((difficulty): difficulty is string => !!difficulty),
        switchMap(difficulty => this.gameService.new(difficulty)),
        tap((game: Game) => {
          this.store.dispatch(ACTION_SET_GAME({game: game, activeCell: undefined}));
          this.store.dispatch(ACTION_SET_VIEW({view: View.GAME}));
        })
      ).subscribe();
  }
}
