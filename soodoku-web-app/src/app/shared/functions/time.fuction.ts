export function formatDurationBetween(date1: string, date2: string): string {
  const parseSafeDate = (input: string) => new Date(input.replace(/(\.\d{3})\d+/, '$1'));

  const d1 = parseSafeDate(date1);
  const d2 = parseSafeDate(date2);

  const diffMs = d2.getTime() - d1.getTime();
  const totalSeconds = Math.floor(Math.abs(diffMs) / 1000);

  const hours = Math.floor(totalSeconds / 3600);
  const minutes = Math.floor((totalSeconds % 3600) / 60);
  const seconds = totalSeconds % 60;

  const pad = (n: number) => String(n).padStart(2, '0');

  return hours > 0
    ? `${pad(hours)}:${pad(minutes)}:${pad(seconds)}`
    : `${pad(minutes)}:${pad(seconds)}`;
}
