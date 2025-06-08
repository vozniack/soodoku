import { ApplicationRef, Injectable, Type, createComponent, } from '@angular/core';
import { take } from 'rxjs';
import { Breakpoint } from '../breakpoint/breakpoint.interface';
import { ModalComponent } from './modal.component';

@Injectable({providedIn: 'root'})
export class ModalService {

  breakpoint!: Breakpoint;

  constructor(private appRef: ApplicationRef) {
  }

  open<T, R = any>(component: Type<T>, inputs?: Partial<T>): Promise<R> {
    return new Promise<R>((resolve) => {
      const modalRef = createComponent(ModalComponent, {
        environmentInjector: this.appRef.injector,
      });

      this.appRef.attachView(modalRef.hostView);
      document.body.appendChild(modalRef.location.nativeElement);

      const childCompRef = modalRef.instance.loadChild(component, inputs);

      modalRef.instance.closed.subscribe(() => {
        this.appRef.detachView(modalRef.hostView);
        modalRef.destroy();
        resolve(null as any);
      });

      if ((childCompRef.instance as any).result) {
        (childCompRef.instance as any).result.pipe(take(1)).subscribe((value: any) => {
          modalRef.instance.close();
          resolve(value);
        });
      }
    });
  }
}
