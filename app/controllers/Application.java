package controllers;

import java.util.List;

import models.Exam;
import models.User;
import play.data.Form;
import play.mvc.*;
import views.html.*;
import views.xml.*;

public class Application extends Controller {

	public static Result index() {
		return redirect(routes.Application.login());
	}

	public static Result login() {
		return ok(login.render(form(Login.class)));
	}

	public static Result logout() {
		//Exam.deleteGarbage();
		session().clear();
		flash("logout", "Boli ste odhlásený.");
		return redirect(routes.Application.login());
	}

	public static Result authenticate() {
		Form<Login> loginForm = form(Login.class).bindFromRequest();
		if (loginForm.hasErrors()) {
			return badRequest(login.render(loginForm));
		} else {
			session("login", loginForm.get().login);
			return redirect(routes.Menu.show());
		}
	}

	// validacny formular
	public static class Login {

		public String login;
		public String password;

		public String validate() {
			if (User.authenticate(login, password) == null) {
				return "Neplatné meno alebo heslo";
			}
			return null;
		}
	}
}