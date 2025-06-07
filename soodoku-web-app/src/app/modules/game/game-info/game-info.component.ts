import { NgIf } from '@angular/common';
import { Component } from '@angular/core';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';
import { Store } from '@ngrx/store';
import { tap } from 'rxjs/operators';
import { Breakpoint } from '../../../core/breakpoint/breakpoint.interface';
import { ModalService } from '../../../core/modal/modal.service';
import { AvatarComponent } from '../../../shared/components/avatar/avatar.component';
import { ButtonComponent } from '../../../shared/components/button/button.component';
import { DifficultyDialogComponent } from '../../../shared/dialogs/difficulty-dialog/difficulty-dialog.component';
import { SELECT_BREAKPOINT } from '../../../store/app/app.selectors';

@Component({
  selector: 'soo-game-info',
  standalone: true,
  imports: [ButtonComponent, AvatarComponent, NgIf,],
  templateUrl: './game-info.component.html',
  styleUrl: './game-info.component.scss'
})
export class GameInfoComponent {

  breakpoint!: Breakpoint;

  constructor(private store: Store, private modalService: ModalService) {
    this.store.select(SELECT_BREAKPOINT).pipe(
      takeUntilDestroyed(),
      tap((breakpoint: Breakpoint) => this.breakpoint = breakpoint)
    ).subscribe();
  }

  newGame(): void {
    this.modalService.open(DifficultyDialogComponent);
  }
}
