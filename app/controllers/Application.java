package controllers;

import javax.inject.Inject;
import play.*;
import play.libs.F.Promise;
import play.libs.ws.WSClient;
import play.mvc.*;

import views.html.*;

public class Application extends Controller {
	private static final String LIGHTNING_BASE_URL="https://ananddevorg-developer-edition.lightning.force.com";

	@Inject WSClient ws;
	
    public Result index() {
        return ok(index.render("Your new application is ready."));
    }
    
    public Result oauthcallback() {
        return ok(oauthcallback.render("Your new application is ready."));
    }
    
    public Result salesforce(String reqPath){
    	/*ws.url(LIGHTNING_BASE_URL)
          .setHeader("Authorization", "OAuth "+request().getHeader("Authorization"))
          .setHeader("Accept","application/json")
          .get()
          .map(response -> ok(response.asJson()));    	
      */
      System.out.println("Authorization  : "+request().getHeader("Authorization"));
      System.out.println("URI : "+request().uri());
      String fullPath = request().uri();
      String pathRequested = fullPath.substring(17);

      ws.url(LIGHTNING_BASE_URL+pathRequested)
        .setHeader("Authorization", "OAuth "+request().getHeader("Authorization"))
        .setHeader("Accept","application/json")
        .get()
        .map(response -> ok(response.getBody())); 
    }

}
