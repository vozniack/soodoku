import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import { LoginRequest, LoginResponse, SignupRequest, SignupResponse } from './auth.interface';

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  baseUrl = `${environment.backendUrl}/api/auth`;

  constructor(private httpClient: HttpClient) {
  }

  login(request: LoginRequest): Observable<LoginResponse> {
    return this.httpClient.post<LoginResponse>(`${this.baseUrl}/login`, request);
  }

  signup(request: SignupRequest): Observable<SignupResponse> {
    return this.httpClient.post<LoginResponse>(`${this.baseUrl}/signup`, request);
  }
}
