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
      Iterator<Http.Cookie> theCookies = request().cookies().iterator();
      Http.Cookie cookie;
      while(theCookies.hasNext()){
        cookie = theCookies.next();
        System.out.println("Cookie "+cookie.name()+", value="+cookie.value());

      }
      String instanceUrl = request().cookie("instanceUrl").value();
      //String sessionId = request().cookie("access_token").value();
      
      String sessionId = request().getHeader("Authorization");
      if(sessionId != null){
        session("Authorization",sessionId);
      }
      System.out.println("Authorization  : "+ session("Authorization"));
      System.out.println("instanceUrl    : "+ instanceUrl);
      System.out.println("URI            : "+ request().uri());
      String fullPath = request().uri();
      String pathRequested = fullPath.substring(12);
      System.out.println("Final Path     : "+LIGHTNING_BASE_URL+"/"+pathRequested);

      return ws.url(instanceUrl+"/"+pathRequested)
        .setHeader("Authorization", "OAuth "+ session("Authorization"))
        .get()
        .map(response -> {
          System.out.println("Response Status : "+response.getStatus());
          String respStr = response.getBody();
          respStr = respStr.replace(instanceUrl,"/salesforce");
          //System.out.println(respStr);
          return ok(respStr);
        }); 
    }

    public Promise<Result> salesforcePost(String reqPath){
      System.out.println("Cookies:"+request().cookies());
      String instanceUrl = request().cookie("instanceUrl").value();
      
      DynamicForm form = Form.form().bindFromRequest();
      String postData="";
      Map<String,String> formDataMap = form.data();
      for(Map.Entry<String, String> entry : formDataMap.entrySet()) {
        postData +=entry.getKey()+"="+entry.getValue()+"&";
      }

      String sessionId = request().getHeader("Authorization");
      if(sessionId != null){
        session("Authorization",sessionId);
      }
      System.out.println("instanceUrl         : "+instanceUrl);
      System.out.println("POST request body   : "+postData);
      System.out.println("POST Authorization  : "+session("Authorization"));
      System.out.println("POST URI            : "+request().uri());
      String fullPath = request().uri();
      String pathRequested = fullPath.substring(12);
      System.out.println("POST Final Path : "+instanceUrl+"/"+pathRequested);

      return ws.url(instanceUrl+"/"+pathRequested)
        .setHeader("Authorization", "OAuth "+session("Authorization"))
        .setContentType("application/x-www-form-urlencoded")
        .post(postData)
        .map(response -> {
          System.out.println("POST Response Status : "+response.getStatus());
          String respStr = response.getBody();
          respStr = respStr.replace(instanceUrl,"/salesforce");
          return ok(respStr);
        }); 
    }

}
