export class User{

	username: string;
	userId: number;
	firstName: string;
	lastName: string;
	userMail: string;
	dateOfBirth: string;
	profilePicturePath: string;

	constructor(username: string, userId: number, firstName: string, lastName: string, userMail: string, dateOfBirth: string, profilePicturePath: string){
		this.username = username;
		this.userId = userId;
		this.firstName = firstName;
		this.lastName = lastName;
		this.userMail = userMail;
		this.dateOfBirth = dateOfBirth;
		this.profilePicturePath = profilePicturePath;
	}


}