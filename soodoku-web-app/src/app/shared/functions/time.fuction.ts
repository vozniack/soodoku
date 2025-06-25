import { Session } from '../../modules/game/game.interface';

export function formatDuration(sessions: Session[]): string {
  if (!sessions?.length) return '00:00';

  const totalMs = sessions.reduce((acc, session) => {
    const start = new Date(session.startedAt).getTime();
    const end = session.pausedAt ? new Date(session.pausedAt).getTime() : start;
    return acc + (end - start);
  }, 0);

  const totalSeconds = Math.floor(totalMs / 1000);
  const hours = Math.floor(totalSeconds / 3600);
  const minutes = Math.floor((totalSeconds % 3600) / 60);
  const seconds = totalSeconds % 60;

  const pad = (n: number) => String(n).padStart(2, '0');

  return hours > 0
    ? `${pad(hours)}:${pad(minutes)}:${pad(seconds)}`
    : `${pad(minutes)}:${pad(seconds)}`;
}
