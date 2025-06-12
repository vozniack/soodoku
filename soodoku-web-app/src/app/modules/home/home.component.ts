import { Component } from '@angular/core';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';
import { Store } from '@ngrx/store';
import { from } from 'rxjs';
import { tap } from 'rxjs/operators';
import { Breakpoint } from '../../core/breakpoint/breakpoint.interface';
import { DialogService } from '../../core/dialog/dialog.service';
import { LogoComponent } from '../../shared/components/logo/logo.component';
import { ViewAwareComponent } from '../../core/view/view.component';
import { ViewService } from '../../core/view/view.service';
import { fadeInAnimation } from '../../shared/animations/fade-in-animation';
import { IconComponent } from '../../shared/components/icon/icon.component';
import { DifficultyDialogComponent } from '../../shared/dialogs/difficulty-dialog/difficulty-dialog.component';
import { SELECT_BREAKPOINT } from '../../store/app/app.selectors';

@Component({
  selector: 'soo-home',
  standalone: true,
  imports: [IconComponent, LogoComponent],
  templateUrl: './home.component.html',
  styleUrl: './home.component.scss',
  animations: [fadeInAnimation]
})
export class HomeComponent extends ViewAwareComponent {

  breakpoint!: Breakpoint;

  constructor(viewService: ViewService, private store: Store, private dialogService: DialogService) {
    super(viewService);

    this.store.select(SELECT_BREAKPOINT).pipe(
      takeUntilDestroyed(),
      tap((breakpoint: Breakpoint) => this.breakpoint = breakpoint)
    ).subscribe();
  }

  newGame(): void {
    from(this.dialogService.open(DifficultyDialogComponent)).subscribe();
  }
}
