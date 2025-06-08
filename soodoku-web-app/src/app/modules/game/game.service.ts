import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import { Game } from './game.interface';

@Injectable({
  providedIn: 'root'
})
export class GameService {

  baseUrl = `${environment.backendUrl}/api/games`;

  constructor(private httpClient: HttpClient) {
  }

  get(id: string): Observable<Game> {
    return this.httpClient.get<Game>(`${this.baseUrl}/${id}`);
  }

  new(difficulty: string): Observable<Game> {
    return this.httpClient.post<Game>(`${this.baseUrl}`, {difficulty: difficulty});
  }

  move(id: string, row: number, col: number, value: number): Observable<Game> {
    return this.httpClient.put<Game>(`${this.baseUrl}/${id}/move`, {row: row, col: col, value: value});
  }

  revert(id: string): Observable<Game> {
    return this.httpClient.put<Game>(`${this.baseUrl}/${id}/revert`, {});
  }
}
