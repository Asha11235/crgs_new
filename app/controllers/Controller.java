package controllers;

import java.lang.reflect.Method;
import java.util.Arrays;

import controllers.deadbolt.Unrestricted;

import annotations.Mobile;

import models.User;
import play.Logger;
import play.Play;
import play.mvc.ActionInvoker;
import play.mvc.Before;
import play.mvc.Http;

public class Controller extends play.mvc.Controller {

	@Before
	public static void global() {

	    Method m = (Method) ActionInvoker.getActionMethod(Http.Request.current().action)[1];
        if(m.isAnnotationPresent(Mobile.class)) {
			if (!DigestRequest.isAuthorized(request)) {
				throw new UnauthorizedDigest("Super Secret Stuff");
			}
		} else {
			if(Security.isConnected()) {
				User currentUser = User.findByName(Security.connected());
				renderArgs.put("currentUser", currentUser);
			}
		}
	}
	
}