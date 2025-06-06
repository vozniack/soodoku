export interface Cell {
  row: number;
  col: number;
}

export interface Conflict {
  type: 'ROW' | 'COL' | 'BOX';
  value: number;
  index: number;
  cells: Cell[];
}
