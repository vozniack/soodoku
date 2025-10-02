import { Injectable, OnDestroy } from '@angular/core';
import { Store } from '@ngrx/store';
import { Subscription } from 'rxjs';
import { environment } from '../../../environments/environment';
import { SELECT_AUTH_STATE } from '../../store/app/auth/auth.selectors';
import { AuthState } from '../../store/app/auth/auth.state';
import { registerFriendHandlers } from './sse.friend.handler';

@Injectable({
  providedIn: 'root'
})
export class SseService implements OnDestroy {

  private eventSource?: EventSource;
  private authSub: Subscription;

  baseUrl = `${environment.backendUrl}/api/notifications/sse`;

  constructor(private store: Store) {
    this.authSub = this.store.select(SELECT_AUTH_STATE).subscribe(auth => {
      this.handleAuthChange(auth);
    });
  }

  ngOnDestroy(): void {
    this.closeStream();
    this.authSub.unsubscribe();
  }

  public on<T>(eventName: string, handler: (payload: T) => void): void {
    this.eventSource?.addEventListener(eventName, (event: MessageEvent) => {
      try {
        const payload = JSON.parse(event.data) as T;
        handler(payload);
      } catch (exception) {
        console.error(`Failed to parse SSE payload for ${eventName}`, exception);
      }
    });
  }

  private handleAuthChange(auth: AuthState): void {
    if (auth?.accessToken) {
      this.openStream(auth.accessToken);
    } else {
      this.closeStream();
    }
  }

  private openStream(token: string): void {
    if (this.eventSource) {
      this.closeStream();
    }

    this.eventSource = new EventSource(`${this.baseUrl}?token=${token}`);

    registerFriendHandlers(this, this.store);

    this.eventSource.onerror = (error) => {
      console.error('SSE connection error', error);
    };
  }

  private closeStream(): void {
    if (this.eventSource) {
      this.eventSource.close();
      this.eventSource = undefined;
    }
  }
}
