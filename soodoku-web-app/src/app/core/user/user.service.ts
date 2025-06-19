import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import { Game } from '../../modules/game/game.interface';
import { Slice } from '../../shared/models/slice';
import {
  User,
  UserUpdateLanguageRequest,
  UserUpdatePasswordRequest,
  UserUpdateThemeRequest,
  UserUpdateUsernameRequest
} from './user.interface';

@Injectable({
  providedIn: 'root'
})
export class UserService {

  baseUrl = `${environment.backendUrl}/api/users`;

  constructor(private httpClient: HttpClient) {
  }

  getLoggedUser(): Observable<User> {
    return this.httpClient.get<User>(`${this.baseUrl}`);
  }

  getLastGame(): Observable<Game> {
    return this.httpClient.get<Game>(`${this.baseUrl}/lastGame`);
  }

  getGames(finished: boolean, page: number, size: number): Observable<Slice<Game>> {
    return this.httpClient.get<Slice<Game>>(`${this.baseUrl}/games?finished=${finished}&page=${page}&size=${size}`);
  }

  updateUsername(id: string, request: UserUpdateUsernameRequest): Observable<User> {
    return this.httpClient.put<User>(`${this.baseUrl}/${id}/username`, request);
  }

  updatePassword(id: string, request: UserUpdatePasswordRequest): Observable<User> {
    return this.httpClient.put<User>(`${this.baseUrl}/${id}/password`, request);
  }

  updateLanguage(id: string, request: UserUpdateLanguageRequest): Observable<User> {
    return this.httpClient.put<User>(`${this.baseUrl}/${id}/language`, request);
  }

  updateTheme(id: string, request: UserUpdateThemeRequest): Observable<User> {
    return this.httpClient.put<User>(`${this.baseUrl}/${id}/theme`, request);
  }
}
