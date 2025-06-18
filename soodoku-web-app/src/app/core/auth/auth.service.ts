import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import { AuthResponse, LoginRequest, RefreshRequest, SignupRequest } from './auth.interface';

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  baseUrl = `${environment.backendUrl}/api/auth`;

  constructor(private httpClient: HttpClient) {
  }

  login(request: LoginRequest): Observable<AuthResponse> {
    return this.httpClient.post<AuthResponse>(`${this.baseUrl}/login`, request);
  }

  signup(request: SignupRequest): Observable<AuthResponse> {
    return this.httpClient.post<AuthResponse>(`${this.baseUrl}/signup`, request);
  }

  refresh(request: RefreshRequest): Observable<AuthResponse> {
    return this.httpClient.post<AuthResponse>(`${this.baseUrl}/refresh`, request, {
      headers: new HttpHeaders().set('Authorization', `Bearer ${request.refreshToken}`)
    });
  }
}
