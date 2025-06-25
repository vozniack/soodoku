export interface Game {
  id: string,
  userId?: string,

  board: number[][],
  solved?: number[][],
  locks: [number, number][],
  conflicts: Conflict[],
  notes: Note[],

  difficulty: string,

  hints: number,
  missing: number,

  sessions: Session[],
  moves: Move[],

  startedAt: string,
  updatedAt?: string,
  finishedAt?: string,

  paused: boolean,
  finished: boolean
}

export interface Conflict {
  type: 'ROW' | 'COL' | 'BOX',
  value: number,
  index: number,
  cells: [number, number][]
}

export interface Note {
  row: number,
  col: number,

  values: string[]
}

export interface Session {
  startedAt: string,
  pausedAt: string
}

export interface Move {
  row: number,
  col: number,

  before: number,
  after: number,

  type: 'NORMAL' | 'REVERT' | 'HINT';

  reverted: boolean;
}
