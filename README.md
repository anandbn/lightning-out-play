## Lightning Out application 

This is a sample Play (Java) application that displays a Lighting component using the Lightning Out capability. 


### Pre-requisites

- Java 8
- Activator
- Playframework (part of activator) 2.4.x

### Setup

#### Clone the app

Clone this app from https://github.com/anandbn/lightning-out-java.git


#### Create a Lighting component 

Create a lightning app called `HelloWorld`. The lighting component XML, JS etc. are include below

HelloWorld.cmp

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

