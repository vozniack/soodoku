import { Component, EventEmitter, Output, Type, ViewChild, ViewContainerRef, } from '@angular/core';
import { Store } from '@ngrx/store';
import { tap } from 'rxjs/operators';
import { ButtonComponent } from '../../shared/components/button/button.component';
import { SELECT_BREAKPOINT } from '../../store/app/app.selectors';
import { Breakpoint } from '../breakpoint/breakpoint.interface';

@Component({
  selector: 'soo-modal',
  standalone: true,
  templateUrl: './modal.component.html',
  styleUrls: ['./modal.component.scss'],
  imports: [ButtonComponent]
})
export class ModalComponent {

  @Output() closed = new EventEmitter<void>();

  @ViewChild('container', {read: ViewContainerRef, static: true})
  container!: ViewContainerRef;

  breakpoint!: Breakpoint;

  hidden = false;

  constructor(private store: Store) {
    this.store.select(SELECT_BREAKPOINT).pipe(
      tap((breakpoint: Breakpoint) => this.breakpoint = breakpoint)
    ).subscribe();
  }

  loadChild<T>(component: Type<T>, inputs?: Partial<T>) {
    this.container.clear();

    const compRef = this.container.createComponent(component);

    if (inputs) {
      Object.assign(compRef.instance as object, inputs);
    }

    return compRef;
  }

  onBackdropClick(event: MouseEvent) {
    if (!this.isMobile) {
      return;
    }

    if ((event.target as HTMLElement).classList.contains('backdrop')) {
      this.close();
    }
  }

  close() {
    this.hidden = true;

    setTimeout(() => this.closed.emit(), 128);
  }

  get isMobile(): boolean {
    return this.breakpoint == 'xs';
  }
}
