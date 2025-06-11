import { Component } from '@angular/core';
import { from } from 'rxjs';
import { DialogService } from '../../core/dialog/dialog.service';
import { LogoComponent } from '../../shared/components/logo/logo.component';
import { ViewAwareComponent } from '../../core/view/view.component';
import { ViewService } from '../../core/view/view.service';
import { fadeInAnimation } from '../../shared/animations/fade-in-animation';
import { IconComponent } from '../../shared/components/icon/icon.component';
import { DifficultyDialogComponent } from '../../shared/dialogs/difficulty-dialog/difficulty-dialog.component';

@Component({
  selector: 'soo-home',
  standalone: true,
  imports: [IconComponent, LogoComponent],
  templateUrl: './home.component.html',
  styleUrl: './home.component.scss',
  animations: [fadeInAnimation]
})
export class HomeComponent extends ViewAwareComponent {

  constructor(viewService: ViewService, private dialogService: DialogService) {
    super(viewService);
  }

  newGame(): void {
    from(this.dialogService.open(DifficultyDialogComponent)).subscribe();
  }
}
