
Steps
-------
In order to make the project ready for your app you need to:

Facebook
-------

- Create an Facebook app at https://developers.facebook.com/apps/;
- Replace the value of facebook_app_id at strings.xml;
 

Google
-------
- Go to the Google Developers Console at https://console.developers.google.com/ and create a new project (or get to one you already have);
- In the left sidebar, select APIs & auth (the APIs sub-item is automatically selected) find the Google+ API and set its status to ON;
- Get a configuration file at [Google Developers](https://developers.google.com/mobile/add?platform=android&cntapi=signin&cnturl=https:%2F%2Fdevelopers.google.com%2Fidentity%2Fsign-in%2Fandroid%2Fsign-in%3Fconfigured%3Dtrue&cntlbl=Continue%20Adding%20Sign-In) by:     
		a) Select the correct project and package then click on Continue;    
		b) Click the button on the bottom to Generate configuration files    
		c) Download the google-services.json for your app and put it at the app/ or mobile/ module directory in your Android project. (The file contains configuration details, such as keys and identifiers, for the services you just enabled)
		

