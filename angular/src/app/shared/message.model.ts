import { User } from './user.model';

export class Message{

	latitude: number;
	longitude: number;
	owner: User;
	datetime: string; 

	constructor(lat: number, long: number, owner: User, datetime: string){
		this.latitude = lat;
		this.longitude = long;
		this.owner = owner;
		this.datetime = datetime;
	}

}