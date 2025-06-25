import { BehaviorSubject } from 'rxjs';
import { Session } from '../game.interface';

export function updateElapsedTime(sessions: Session[], elapsedTime$: BehaviorSubject<string>) {
  const now = Date.now();

  const totalMs = sessions.reduce((acc, session) => {
    const started = new Date(session.startedAt).getTime();
    const paused = session.pausedAt ? new Date(session.pausedAt).getTime() : now;
    return acc + (paused - started);
  }, 0);

  const totalSeconds = Math.floor(totalMs / 1000);
  const hours = Math.floor(totalSeconds / 3600);
  const minutes = Math.floor((totalSeconds % 3600) / 60);
  const seconds = totalSeconds % 60;

  const pad = (n: number) => String(n).padStart(2, '0');

  const formatted = hours > 0
    ? `${pad(hours)}:${pad(minutes)}:${pad(seconds)}`
    : `${pad(minutes)}:${pad(seconds)}`;

  elapsedTime$.next(formatted);
}
