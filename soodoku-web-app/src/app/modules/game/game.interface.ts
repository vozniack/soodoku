export interface Game {
  id: string,
  userId?: string,

  board: number[][],
  locks: [number, number][],
  conflicts: Conflict[]

  difficulty: string,
  missing: number,
  moves: number,
  realMoves: number,

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
