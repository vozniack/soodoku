import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import { Slice } from '../../shared/models/slice';
import { Game } from './game.interface';

@Injectable({
  providedIn: 'root'
})
export class GameService {

  baseUrl = `${environment.backendUrl}/api/games`;

  constructor(private httpClient: HttpClient) {
  }

  getGame(id: string): Observable<Game> {
    return this.httpClient.get<Game>(`${this.baseUrl}/${id}`);
  }

  getGames(page: number, size: number): Observable<Slice<Game>> {
    return this.httpClient.get<Slice<Game>>(`${this.baseUrl}?page=${page}&size=${size}`);
  }

  getLast(): Observable<Game> {
    return this.httpClient.get<Game>(`${this.baseUrl}/last`);
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

  note(id: string, row: number, col: number, values: string[]): Observable<Game> {
    return this.httpClient.put<Game>(`${this.baseUrl}/${id}/note`, {row, col, values});
  }

  wipeNotes(id: string): Observable<Game> {
    return this.httpClient.delete<Game>(`${this.baseUrl}/${id}/note`);
  }

  hint(id: string): Observable<Game> {
    return this.httpClient.put<Game>(`${this.baseUrl}/${id}/hint`, {});
  }

  end(id: string): Observable<Game> {
    return this.httpClient.put<Game>(`${this.baseUrl}/${id}/end`, {});
  }
}
