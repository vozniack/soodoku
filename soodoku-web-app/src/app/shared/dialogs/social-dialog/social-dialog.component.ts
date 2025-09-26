import { Component } from '@angular/core';
import { SocialFriendInvitationsComponent } from './social-friend-invitations/social-friend-invitations.component';
import { SocialFriendsComponent } from './social-friends/social-friends.component';
import { SocialInviteComponent } from './social-invite/social-invite.component';

@Component({
  selector: 'soo-social-dialog',
  standalone: true,
  imports: [SocialFriendsComponent, SocialFriendInvitationsComponent, SocialInviteComponent],
  templateUrl: './social-dialog.component.html',
  styleUrl: './social-dialog.component.scss'
})
export class SocialDialogComponent {
}
