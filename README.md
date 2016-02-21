## Lightning Out application 

This is a sample Play (Java) application that displays a Lighting component using the Lightning Out capability. 


### Pre-requisites

- Java 8
- Activator
- Playframework (part of activator) 2.4.x

### Setup

#### 1. Clone the app

Clone this app from https://github.com/anandbn/lightning-out-java.git


#### 2. Create a `HelloWorld` lightning component 

Create a lightning component called `HelloWorld`. The lighting component XML, JS etc. are include below

Make sure you upload [Boostrap CSS](https://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/css/bootstrap.min.css) and name it `bootstrap_css` as it's referenced in the component below.

__HelloWorld.cmp__

```
<aura:component controller="HelloWorldController">
    <ltng:require styles="/resource/bootstrap_css" />
    <aura:attribute name="contacts" type="Contact[]"/>
	<aura:handler name="init" value="{!this}" action="{!c.getContacts}"/>
    <table class="table table-bordered">
    	<thead> 
            <tr> <th>#</th> <th>First Name</th> <th>Last Name</th><th>Email</th></tr>
        </thead>
		<tbody>
            <aura:iteration var="contact" items="{!v.contacts}">
                <tr><td></td><td>{!contact.FirstName}</td><td> {!contact.LastName}</td><td>{!contact.Email}</td></tr>
            </aura:iteration>
        </tbody>
	</table>
</aura:component>

```

__HelloWorldController.cls__

```
public class HelloWorldController {

    @AuraEnabled
    public static List<Contact> getMyContacts() {
        return [select Id,FirstName,LastName,Email from Contact limit 10];
    }

}

```

HelloWorldController.js

```
({
    
    "getContacts": function(cmp){
        console.log('getContacts called');
        var action = cmp.get("c.getMyContacts");
        action.setCallback(this, function(response){
        		console.log('getMyContacts callback called');
            	var state = response.getState();
            	if (state === "SUCCESS") {
        			console.log('response');
                    console.log(response.getReturnValue());
                	cmp.set("v.contacts", response.getReturnValue());
            	}
        });
	 	$A.enqueueAction(action);
        
    }
                            
                        
})

```

#### 3. Create `LoTest` Lightning app

__LoTest.app__

```
<aura:application access="GLOBAL" extends="ltng:outApp"> 
    <aura:dependency resource="forceChatter:feed"
							type="COMPONENT"/>
    <aura:dependency resource="c:HelloWorld"
							type="COMPONENT"/>
</aura:application>

```

#### 4. Create the Salesforce connected app

Refer to instructions [here](http://help.salesforce.com/apex/HTViewHelpDoc?id=connected_app_create.htm)  for more infor on setting up a connected app. 

Make sure the callback url is `http://localhost:9000/oauthcallback` when you configure the OAuth settings in the connected app.


#### 5. Run the app

The app uses two environment variables:

- LIGHTNING_APP_NAME : This is the Lightning App Name you created above. Use `c:LoTest` or whatever you name your Lightning app in step-3
- OAUTH_CONSUMER_KEY : Put in the Oauth consumer key from Step#4 after you setup the connected app

Run the app using the below command:

```
activator run -DLIGHTNING_APP_NAME=c:LoTest -DOAUTH_CONSUMER_KEY=[Use Oauth Consumer Key from Connected App]"

```

