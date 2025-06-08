import { BehaviorSubject } from 'rxjs';

export function updateElapsedTime(createdAt: string, elapsedTime$: BehaviorSubject<string>) {
  const diffMs = Date.now() - new Date(createdAt).getTime();

  const totalSeconds = Math.floor(diffMs / 1000);
  const hours = Math.floor(totalSeconds / 3600);
  const minutes = Math.floor((totalSeconds % 3600) / 60);
  const seconds = totalSeconds % 60;

  const pad = (n: number) => String(n).padStart(2, '0');

  const formatted = hours > 0
    ? `${pad(hours)}:${pad(minutes)}:${pad(seconds)}`
    : `${pad(minutes)}:${pad(seconds)}`;

  elapsedTime$.next(formatted);
}
