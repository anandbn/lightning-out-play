package controllers;

import javax.inject.Inject;
import play.*;
import play.libs.F.Promise;
import play.libs.ws.WSClient;
import play.mvc.*;
import play.data.DynamicForm;
import play.data.Form;
import java.util.*;

import views.html.*;

public class Application extends Controller {
	private static final String LIGHTNING_BASE_URL="https://ananddevorg-developer-edition.lightning.force.com/";

	@Inject WSClient ws;
	
    public Result index() {
        return ok(index.render("Your new application is ready."));
    }
    
    public Result oauthcallback() {
        return ok(oauthcallback.render("Your new application is ready."));
    }
    
    public Promise<Result> salesforce(String reqPath){
    	/*ws.url(LIGHTNING_BASE_URL)
          .setHeader("Authorization", "OAuth "+request().getHeader("Authorization"))
          .setHeader("Accept","application/json")
          .get()
          .map(response -> ok(response.asJson()));    	
      */
      if(request().getHeader("Authorization") !=null){
          session("Authorization", request().getHeader("Authorization"));
      }
      System.out.println("Authorization  : "+session("Authorization"));
      System.out.println("URI : "+request().uri());
      String fullPath = request().uri();
      String pathRequested = fullPath.substring(12);
      System.out.println("Final Path : "+LIGHTNING_BASE_URL+pathRequested);

      return ws.url(LIGHTNING_BASE_URL+pathRequested)
        .setHeader("Authorization", "OAuth "+session("Authorization"))
        .get()
        .map(response -> {
          System.out.println("Response Status : "+response.getStatus());
          String respStr = response.getBody();
          respStr = respStr.replace("https://ananddevorg-developer-edition.lightning.force.com","/salesforce");
          //System.out.println(respStr);
          return ok(respStr);
        }); 
    }

    public Promise<Result> salesforcePost(String reqPath){
      /*ws.url(LIGHTNING_BASE_URL)
          .setHeader("Authorization", "OAuth "+request().getHeader("Authorization"))
          .setHeader("Accept","application/json")
          .get()
          .map(response -> ok(response.asJson()));      
      */
      if(request().getHeader("Authorization") !=null){
          session("Authorization", request().getHeader("Authorization"));
      }

      DynamicForm form = Form.form().bindFromRequest();
      String postData="";
      Map<String,String> formDataMap = form.data();
      for(Map.Entry<String, String> entry : formDataMap.entrySet()) {
        postData +=entry.getKey()+"="+entry.getValue()+"&";
      }
      System.out.println("POST request body  : "+postData);
      System.out.println("POST Authorization  : "+session("Authorization"));
      System.out.println("URI : "+request().uri());
      String fullPath = request().uri();
      String pathRequested = fullPath.substring(12);
      System.out.println("POST Final Path : "+LIGHTNING_BASE_URL+pathRequested);

      return ws.url(LIGHTNING_BASE_URL+pathRequested)
        .setHeader("Authorization", "OAuth "+session("Authorization"))
        .setContentType("application/x-www-form-urlencoded")
        .post(postData)
        .map(response -> {
          System.out.println("POST Response Status : "+response.getStatus());
          String respStr = response.getBody();
          respStr = respStr.replace("https://ananddevorg-developer-edition.lightning.force.com","/salesforce");
          //System.out.println(respStr);
          return ok(respStr);
        }); 
    }

}
