import { ApplicationRef, Injectable, Type, createComponent, } from '@angular/core';
import { take } from 'rxjs';
import { Breakpoint } from '../breakpoint/breakpoint.interface';
import { DialogComponent } from './dialog.component';

@Injectable({providedIn: 'root'})
export class DialogService {

  breakpoint!: Breakpoint;

  constructor(private appRef: ApplicationRef) {
  }

  open<T, R = any>(component: Type<T>, inputs?: Partial<T>): Promise<R> {
    return new Promise<R>((resolve) => {
      const dialogRef = createComponent(DialogComponent, {
        environmentInjector: this.appRef.injector,
      });

      this.appRef.attachView(dialogRef.hostView);
      document.body.appendChild(dialogRef.location.nativeElement);

      const childCompRef = dialogRef.instance.loadChild(component, inputs);

      dialogRef.instance.closed.subscribe(() => {
        this.appRef.detachView(dialogRef.hostView);
        dialogRef.destroy();
        resolve(null as any);
      });

      if ((childCompRef.instance as any).result) {
        (childCompRef.instance as any).result.pipe(take(1)).subscribe((value: any) => {
          dialogRef.instance.close();
          resolve(value);
        });
      }
    });
  }
}
