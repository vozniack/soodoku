import { ApplicationRef, Injectable, Type, createComponent, } from '@angular/core';
import { Breakpoint } from '../breakpoint/breakpoint.interface';
import { ModalComponent } from './modal.component';

@Injectable({providedIn: 'root'})
export class ModalService {

  breakpoint!: Breakpoint;

  constructor(private appRef: ApplicationRef) {
  }

  open<T>(component: Type<T>, inputs?: Partial<T>) {
    const modalRef = createComponent(ModalComponent, {
      environmentInjector: this.appRef.injector,
    });

    this.appRef.attachView(modalRef.hostView);
    document.body.appendChild(modalRef.location.nativeElement);

    modalRef.instance.loadChild(component, inputs);

    modalRef.instance.closed.subscribe(() => {
      this.appRef.detachView(modalRef.hostView);
      modalRef.destroy();
    });
  }
}
