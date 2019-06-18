import { Component, OnInit } from '@angular/core';

import { MessageServiceService } from './services/message-service.service';
import { Message } from './shared/message.model';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent implements OnInit {

	latitude = 53.216978;
	longitude = 6.567029;
	zoom = 9;

	messagesToDisplay: Message[] = [];

	constructor(private messageservice: MessageServiceService){
	}

	ngOnInit(){
		this.messagesToDisplay = this.messageservice.getMessages();
		this.messageservice.selectedMessage.subscribe(
			(message: Message) => (this.changeCoords(message)) 
			);
	}

	changeCoords(message: Message){
		this.latitude = message.latitude;
		this.longitude = message.longitude;
		this.zoom = 18;
	}

	

}
