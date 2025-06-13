import { Injectable } from '@angular/core';
import { Subject } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class SnackbarService {

  private snackbarSubject = new Subject<{ message: string; icon?: string, duration?: number }>();
  snackbarState$ = this.snackbarSubject.asObservable();

  show(message: string, icon?: string, duration?: number) {
    this.snackbarSubject.next({message, icon, duration});
  }
}
