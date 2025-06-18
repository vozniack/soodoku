import { NgOptimizedImage } from '@angular/common';
import { Component, Input } from '@angular/core';
import { Language } from '../../../../core/language/language.const';

@Component({
  selector: 'soo-settings-dialog-language',
  standalone: true,
  imports: [NgOptimizedImage],
  templateUrl: './settings-dialog-language.component.html',
  styleUrl: './settings-dialog-language.component.scss'
})
export class SettingsDialogLanguageComponent {

  @Input() language!: Language;

  name(): string {
    switch (this.language) {
      case Language.POLISH:
        return 'Polski';

      case Language.ENGLISH:
        return 'English';

      case Language.GERMAN:
        return 'Deutsch';

      case Language.ITALIAN:
        return 'Italiano';

      case Language.SPANISH:
        return 'Español';
    }
  }

  flag(): string {
    return `flags/${this.language.split('_')[0]}.svg`;
  }
}
