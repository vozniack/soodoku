import { Component, Input } from '@angular/core';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';
import { Store } from '@ngrx/store';
import { from } from 'rxjs';
import { tap } from 'rxjs/operators';
import { AvatarComponent } from '../../shared/components/avatar/avatar.component';
import { IconComponent } from '../../shared/components/icon/icon.component';
import { HelpDialogComponent } from '../../shared/dialogs/help-dialog/help-dialog.component';
import { SettingsDialogComponent } from '../../shared/dialogs/settings-dialog/settings-dialog.component';
import { SELECT_APP_BREAKPOINT } from '../../store/app/app.selectors';
import { Breakpoint } from '../breakpoint/breakpoint.interface';
import { DialogService } from '../dialog/dialog.service';

@Component({
  selector: 'soo-toolbar',
  standalone: true,
  imports: [AvatarComponent, IconComponent],
  templateUrl: './toolbar.component.html',
  styleUrl: './toolbar.component.scss'
})
export class ToolbarComponent {

  @Input() withUsername = true;

  breakpoint!: Breakpoint;

  constructor(private store: Store, private dialogService: DialogService) {
    this.store.select(SELECT_APP_BREAKPOINT).pipe(
      takeUntilDestroyed(),
      tap((breakpoint: Breakpoint) => this.breakpoint = breakpoint)
    ).subscribe();
  }

  openSettings(): void {
    from(this.dialogService.open(SettingsDialogComponent)).subscribe();
  }

  openHelp(): void {
    from(this.dialogService.open(HelpDialogComponent)).subscribe();
  }
}
