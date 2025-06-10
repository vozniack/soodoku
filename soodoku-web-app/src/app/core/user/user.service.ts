import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import { User } from './user.interface';

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
}
