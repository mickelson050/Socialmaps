import { Component, OnInit } from '@angular/core';

import { MessageServiceService } from '../services/message-service.service';
import { Message } from '../shared/message.model';
import { User } from '../shared/user.model';


@Component({
  selector: 'app-map-menu',
  templateUrl: './map-menu.component.html',
  styleUrls: ['./map-menu.component.css']
})
export class MapMenuComponent implements OnInit { 

  availableMessages: Message[] = [];

  constructor(private messageservice: MessageServiceService) {

   }

  ngOnInit() {
  	this.availableMessages = this.messageservice.getMessages();
  }

  zoomInOnMessage(message: Message){
  	this.messageservice.onMessageSelected(message);
  }

}
