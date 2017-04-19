package controllers;

import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Date;
import models.ResourceManagement;
import com.google.gson.Gson;
import models.PollDefination;
import models.PollVoteReply;
import play.Logger;
import play.Play;
import play.mvc.*;
import play.data.validation.*;
import play.libs.*;
import play.utils.*;

public class Secure extends Controller {

    @Before(unless={"login", "authenticate", "logout" , "loadPoll" , "voteReply" , "pollGraph" , "loadPollGraph", "loadPollGraphResult" , "resources"})
    static void checkAccess() throws Throwable {
        // Authent
    	play.Logger.debug("inside Secure.checkAccess()");
        if(!session.contains("username")) {
            flash.put("url", "GET".equals(request.method) ? request.url : Play.ctxPath + "/"); // seems a good default
            login();
        }
        // Checks
        Check check = getActionAnnotation(Check.class);
        if(check != null) {
            check(check);
        }
        check = getControllerInheritedAnnotation(Check.class);
        if(check != null) {
            check(check);
        }
    }

    private static void check(Check check) throws Throwable {
    	play.Logger.debug("inside Secure.check()");
        for(String profile : check.value()) {
            boolean hasProfile = (Boolean)Security.invoke("check", profile);
            if(!hasProfile) {
                Security.invoke("onCheckFailed", profile);
            }
        }
    }

    // ~~~ Login

    public static void login() throws Throwable {
    	play.Logger.debug("inside Secure.login()");
        Http.Cookie remember = request.cookies.get("rememberme");        
        if(remember != null) {        	
        	play.Logger.info(remember.toString() + " -Saved Cookie in current session.");
            int firstIndex = remember.value.indexOf("-");
            int lastIndex = remember.value.lastIndexOf("-");
            if (lastIndex > firstIndex) {
                String sign = remember.value.substring(0, firstIndex);
                String restOfCookie = remember.value.substring(firstIndex + 1);
                String username = remember.value.substring(firstIndex + 1, lastIndex);
                String time = remember.value.substring(lastIndex + 1);
                Date expirationDate = new Date(Long.parseLong(time)); // surround with try/catch?
                Date now = new Date();
                if (expirationDate == null || expirationDate.before(now)) {
                    logout();
                }
                if(Crypto.sign(restOfCookie).equals(sign)) {
                    session.put("username", username);
                    redirectToOriginalURL();
                }
            }
        }
        flash.keep("url");
        render();
    }

    public static void authenticate(@Required String username, String password, boolean remember) throws Throwable {
        // Check tokens
    	play.Logger.debug("inside Secure.authenticate() - " + username + " -- " + password);
        Boolean allowed = false;
        try {
            // This is the deprecated method name
            allowed = (Boolean)Security.invoke("authentify", username, password);
        } catch (UnsupportedOperationException e ) {
            // This is the official method name
            allowed = (Boolean)Security.invoke("authenticate", username, password);
        }
        if(validation.hasErrors() || !allowed) {
            flash.keep("url");
            flash.error("secure.error");
            params.flash();
            login();
        }
        // Mark user as connected
        session.put("username", username);
        // Remember if needed
        if(remember) {
            Date expiration = new Date();
            String duration = "30d";  // maybe make this override-able 
            expiration.setTime(expiration.getTime() + Time.parseDuration(duration));
            response.setCookie("rememberme", Crypto.sign(username + "-" + expiration.getTime()) + "-" + username + "-" + expiration.getTime(), duration);

        }
        // Redirect to the original URL (or /)
        redirectToOriginalURL();
    }

    public static void logout() throws Throwable {
        Security.invoke("onDisconnect");
        session.clear();
        response.removeCookie("rememberme");
        Security.invoke("onDisconnected");
        flash.success("secure.logout");
        login();
    }

    // ~~~ Utils

    static void redirectToOriginalURL() throws Throwable {
        Security.invoke("onAuthenticated");
        String url = flash.get("url");
        if(url == null) {
            url = Play.ctxPath + "/";
        }
        redirect(url);
    }

    public static class Security extends Controller {

        /**
         * @Deprecated
         * 
         * @param username
         * @param password
         * @return
         */
        static boolean authentify(String username, String password) {
            throw new UnsupportedOperationException();
        }

        /**
         * This method is called during the authentication process. This is where you check if
         * the user is allowed to log in into the system. This is the actual authentication process
         * against a third party system (most of the time a DB).
         *
         * @param username
         * @param password
         * @return true if the authentication process succeeded
         */
        static boolean authenticate(String username, String password) {
        	play.Logger.debug("inside Secure.java");
            return true;
        }

        /**
         * This method checks that a profile is allowed to view this page/method. This method is called prior
         * to the method's controller annotated with the @Check method. 
         *
         * @param profile
         * @return true if you are allowed to execute this controller method.
         */
        static boolean check(String profile) {
            return true;
        }

        /**
         * This method returns the current connected username
         * @return
         */
        static String connected() {
            return session.get("username");
        }

        /**
         * Indicate if a user is currently connected
         * @return  true if the user is connected
         */
        static boolean isConnected() {
            return session.contains("username");
        }

        /**
         * This method is called after a successful authentication.
         * You need to override this method if you with to perform specific actions (eg. Record the time the user signed in)
         */
        static void onAuthenticated() {
        }

         /**
         * This method is called before a user tries to sign off.
         * You need to override this method if you wish to perform specific actions (eg. Record the name of the user who signed off)
         */
        static void onDisconnect() {
        }

         /**
         * This method is called after a successful sign off.
         * You need to override this method if you wish to perform specific actions (eg. Record the time the user signed off)
         */
        static void onDisconnected() {
        }

        /**
         * This method is called if a check does not succeed. By default it shows the not allowed page (the controller forbidden method).
         * @param profile
         */
        static void onCheckFailed(String profile) {
            forbidden();
        }

        private static Object invoke(String m, Object... args) throws Throwable {
        	play.Logger.debug("invoke inside public static class Secure with function - " + m);
            try {
                return Java.invokeChildOrStatic(Security.class, m, args);       
            } catch(InvocationTargetException e) {
                throw e.getTargetException();
            }
        }

    }

    public static void pollGraph(){


        List<PollDefination> pollDefinationList = PollDefination.find(" status= 0 ").fetch();
        List<String> ageList = new ArrayList<String>();

        ageList.add(0,"All");
        ageList.add(1, "Below 18'+");
        ageList.add(2, "Above 18'+");

        List<String> genderList = new ArrayList<String>();

        genderList.add(0,"All");
        genderList.add(1, "Male");
        genderList.add(2, "Female");
        genderList.add(3,"Other");

        render(pollDefinationList,ageList,genderList);

        //render();
    }

    public static String loadPoll() throws SQLException {

        String mp = "";

        mp = PollDefination.getPollReport();

        Gson gson = new Gson();

        return gson.toJson(mp);
    }



    public static String loadPollGraph() throws SQLException {


        // Logger.info("gender: " + gender + " age: " + age);


        String mp = "";

        mp = PollDefination.getPollGraphData();

        Gson gson = new Gson();

        return gson.toJson(mp);

    }

    public static String loadPollGraphResult(String pollId,String gender,String age)throws SQLException {

        String mp = "";

        mp = PollDefination.getPollGraphResultData(pollId,gender,age);

        Gson gson = new Gson();

        return gson.toJson(mp);
    }
    public static void voteReply(String gender, String option, String age,String pollId){


        pollId=pollId.trim();
        Logger.info("PollId: " + pollId);
        Long pollid= Long.parseLong(pollId);
        PollDefination polldef = PollDefination.findById(pollid);

        PollVoteReply pollvotereply = new PollVoteReply();

        pollvotereply.gender=gender;
        pollvotereply.age=age;
        pollvotereply.poll=polldef;
        pollvotereply.answer=option;

        pollvotereply.save();





        //Forms.landingPage();
    }


 public static void resources() {
        List<ResourceManagement> resourceManagementList = ResourceManagement.findAll();

        render(resourceManagementList);
    }



}