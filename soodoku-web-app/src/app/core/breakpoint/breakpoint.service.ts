import { Injectable, NgZone } from '@angular/core';
import { BehaviorSubject, debounceTime, fromEvent, startWith } from 'rxjs';
import { map } from 'rxjs/operators';
import { Breakpoint } from './breakpoint.interface';

@Injectable({providedIn: 'root'})
export class BreakpointService {
  private breakpoint$ = new BehaviorSubject<Breakpoint>(Breakpoint.XL);

  constructor(private zone: NgZone) {
    this.zone.runOutsideAngular(() => {
      fromEvent(window, 'resize').pipe(
        debounceTime(100),
        map(() => this.getCurrentBreakpoint()),
        startWith(this.getCurrentBreakpoint())
      ).subscribe(breakpoint => {
        this.zone.run(() => this.breakpoint$.next(breakpoint));
      });
    });
  }

  get breakpointChanges$() {
    return this.breakpoint$.asObservable();
  }

  private getCurrentBreakpoint(): Breakpoint {
    const width = window.innerWidth;

    if (width < 576) return Breakpoint.XS;
    if (width < 768) return Breakpoint.SM;
    if (width < 992) return Breakpoint.MD;
    if (width < 1200) return Breakpoint.LG;
    return Breakpoint.XL;
  }
}
