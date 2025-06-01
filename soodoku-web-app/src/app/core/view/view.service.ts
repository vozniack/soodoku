import { Injectable } from '@angular/core';
import { Store } from '@ngrx/store';
import { Subject } from 'rxjs';
import { ACTION_SET_VIEW } from '../../store/app/app.actions';
import { View } from './view.const';

@Injectable({
  providedIn: 'root'
})
export class ViewService {

  view!: View;
  viewChange: Subject<View> = new Subject();

  constructor(private store: Store) {
  }

  public setView(view: View) {
    this.store.dispatch(ACTION_SET_VIEW({view: this.view = view}));
    this.viewChange.next(this.view);
  }
}
