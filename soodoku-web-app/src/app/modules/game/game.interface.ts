export interface Game {
  id: string,
  userId?: string,

  board: number[][],
  locks: [number, number][],
  conflicts: Conflict[]

  difficulty: string,

  hints: number,
  missing: number,

  moves: Move[],

  createdAt: string,
  updatedAt?: string,
  finishedAt?: string
}

export interface Conflict {
  type: 'ROW' | 'COL' | 'BOX';
  value: number;
  index: number;
  cells: [number, number][];
}

export interface Move {
  row: number,
  col: number,

  before: number,
  after: number,

  type: 'NORMAL' | 'REVERT' | 'HINT';

  reverted: boolean;
}
