import { Injectable, Output, EventEmitter } from '@angular/core';

import { Message } from '../shared/message.model';
import { User } from '../shared/user.model';
import { UserServiceService} from './user-service.service';

@Injectable({
  providedIn: 'root'
})
export class MessageServiceService {

  friends: User[] = [];
  friendsMessages: Message[] = [];
  @Output() selectedMessage = new EventEmitter<Message>();



  constructor(private userservice: UserServiceService) {
  	this.friends = this.userservice.getFriends();
  	this.friendsMessages = [
	  	new Message(53.241763, 6.577156, this.friends[0], '14-11-1-2019'),
	  	new Message(53.223554, 6.554692, this.friends[1], '14-11-1-2019'),
	   	new Message(53.251083, 6.609280, this.friends[2], '14-11-1-2019'),
	  	new Message(53.191291, 6.482590, this.friends[3], '14-11-1-2019'),
  	];
   }

   getMessages(){
   	console.log(this.friends);
   	return this.friendsMessages;
   }

   onMessageSelected(message: Message){
    this.selectedMessage.emit(message);
   }



}
