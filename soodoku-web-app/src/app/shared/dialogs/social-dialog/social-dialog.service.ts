import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../../../../environments/environment';
import { UserSimple } from '../../../core/user/user.interface';
import { FriendInvitation } from './social-friend-invitations/social-friend-invitations.interface';
import { Friend } from './social-friends/social-friends.interface';

@Injectable({
  providedIn: 'root'
})
export class FriendService {

  baseUrl = `${environment.backendUrl}/api/friends`;

  constructor(private httpClient: HttpClient) {
  }

  getFriends(): Observable<Friend[]> {
    return this.httpClient.get<Friend[]>(`${this.baseUrl}`);
  }

  getFriendCandidates(search: string): Observable<UserSimple[]> {
    return this.httpClient.get<UserSimple[]>(`${this.baseUrl}/candidates`, {
      params: {search}
    });
  }

  removeFriend(id: string) {
    return this.httpClient.delete<Friend>(`${this.baseUrl}/${id}`);
  }

  getSent(): Observable<FriendInvitation[]> {
    return this.httpClient.get<FriendInvitation[]>(`${this.baseUrl}/invitations/sent`);
  }

  getReceived(): Observable<FriendInvitation[]> {
    return this.httpClient.get<FriendInvitation[]>(`${this.baseUrl}/invitations/received`);
  }

  invite(username: string): Observable<FriendInvitation> {
    return this.httpClient.post<FriendInvitation>(`${this.baseUrl}/invitations`, {receiverUsername: username});
  }

  accept(id: string): Observable<FriendInvitation> {
    return this.httpClient.put<FriendInvitation>(`${this.baseUrl}/invitations/${id}/accept`, {});
  }

  reject(id: string): Observable<FriendInvitation> {
    return this.httpClient.put<FriendInvitation>(`${this.baseUrl}/invitations/${id}/reject`, {});
  }

  delete(id: string) {
    return this.httpClient.delete(`${this.baseUrl}/invitations/${id}`);
  }
}
