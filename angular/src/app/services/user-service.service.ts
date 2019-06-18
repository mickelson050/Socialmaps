import { Injectable } from '@angular/core';

import { User } from '../shared/user.model';

@Injectable({
  providedIn: 'root'
})
export class UserServiceService {

  friends: User[] = [
  	new User('joost01',1,'Joost','Bakker','joost.bakker@hotmail.com','24-09-1997','testing'),
  	new User('levi01',3,'Levi','Staal','levi.staal@hotmail.com','24-09-1997','testing'),
  	new User('kevin01',3,'Kevin','Houkema','kevin.houkema@hotmail.com','24-09-1997','testing'),
  	new User('mike01',4,'Mike','Wierenga','mike.wierenga@hotmail.com','24-09-1997','testing')
  ];

  constructor() { }

  getFriends(){
  	return this.friends;
  }






}
