# Play Framework - Module play-linkedin 0.2.1
by Felipe Oliveira
http://geeks.aretotally.in
http://twitter.com/_felipera

Easily integrate LinkedIn's OAuth authentication into any Java web application using the super cool Play Framework (http://playframework.org).


## Prerequisites

Register your application with LinkedIn (https://www.linkedin.com/secure/developer). 
LinkedIn's API documentation can be found at http://developer.linkedin.com/community/apis.


### Install the module

Install the linkedin module from the modules repository:

`
play install linkedin
`

### Enable the module

After installing the module, add the following to your application.conf to enable it:

`
module.linkedin=${play.path}/modules/linkedin-0.2.1
`

### Configure the module

And finally, you need to configure the module by setting these properties in your application.conf (Register API Key at https://www.linkedin.com/secure/developer):

     linkedin.apiKey=YOUR_API_KEY_HERE
     linkedin.secret=YOUR_APP_SECRET_HERE
     linkedin.model=models.User
     linkedin.landUrl=/

All of the properties are required.


## Usage

Step-by-Step Tutorial can be found at *http://geeks.aretotally.in/integrate-linkedins-oauth-authentication-with-a-java-app-using-play-linkedin-module-version-0-2-1*.


### Use the linkedin.button tag in your view

The linkedin.button tag outputs a link that will prompt your users to authenticate with LinkedIn when it is clicked.

`#{linkedin.button /}`

* label which defaults to button *http://developer.linkedin.com/servlet/JiveServlet/downloadImage/102-1182-2-1070/152-21/log-in-linkedin-small.png*
* cssClass which defaults to *play-linkedin-button*


### Define your OAuth callback

Your linkedin.model class needs to implement a static method called @linkedinOAuthCallback@. After a user has authenticated using LinkedIn, the module will call this method with a token (String). This is your opportunity to add the user to your database, add the user to your session, or do anything else you want.

		    public static User findByLinkedInId(String linkedInId) {
		    	return find("byLinkedInId", linkedInId).first();
		    }
		    
			public static void linkedinOAuthCallback(play.modules.linkedin.LinkedInProfile profile) {
				Logger.info("Handle LinkedIn OAuth Callback: " + profile);
				User user = findByLinkedInId(profile.getId());
				String username = "linkedin:" + profile.getId();
				if(user == null || user.linkedInToken == null) {
					user = new User();
					user.fullname = (new StringBuffer().append(profile.getFirstName()).append(" ").append(profile.getLastName())).toString();
					user.linkedInId = profile.getId();
					user.industry = profile.getIndustry();
					user.headline = profile.getHeadline();
					user.pictureUrl = profile.getPictureUrl();
					user.linkedInToken = profile.getAccessToken();
					user.isAdmin = true;
					user = user.save();
				} else {
					Logger.info("Found User: " + user);
					user.linkedInToken = profile.getAccessToken();
					user.save();
				}
				if ( user == null ) {
					throw new RuntimeException("Could not store or lookup user with LinkedIn Profile: " + profile);
				}
				Session.current().put("username", username);
			}


## Source Code

Fork it on Github https://github.com/feliperazeek/playframework-linkedin.
