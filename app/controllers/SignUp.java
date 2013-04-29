package controllers;

import models.Role;
import models.User;
import play.data.Form;
import play.data.validation.Constraints;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.signup.signup;

public class SignUp extends Controller {

	public static Result show() {
		String userId;
		User user;
		if ((userId = ctx().session().get("login")) == null) {
			return forbidden("Nie ste prihlásený");
		} else {
			user = User.find.byId(userId);
			return ok(signup.render(user, form(SignUpForm.class)));
		}
		
	}
	
	public static Result submit() {
		Form<SignUpForm> signUpForm = form(SignUpForm.class).bindFromRequest();
        if(signUpForm.hasErrors()) {
            //return badRequest(signup.render(signUpForm));
        	return badRequest();
        } else {
        	
        	Role rola = null;
        	if(signUpForm.get().role == 0) {
        		rola = Role.Doctor;
        	} else {
        		rola = Role.User;
        	}
        	
            User.addUser(signUpForm.get().login, 
            			signUpForm.get().name, 
            			signUpForm.get().surname,
            			signUpForm.get().password,
            			signUpForm.get().titul,
            			signUpForm.get().address,
            			rola);
            
            flash("user_created", "Registrácia bola úspešná");
        	return redirect(
                routes.Menu.show()
            );
        }
	}
	
	public static class SignUpForm {
		
		public String login;
		public String name;
		public String surname;
		public String password;
		public String passwordRe;
		public String titul;
		public String address;		
		public int role;
		
		public String validate() {
			if(login.equals("") || password.equals("") ) {
				return "Vyplňte všetky políčka označené hviezdičkou!";
			}
			
			if(User.find.byId(login) != null) {
				return "Používateľ so zadaným používateľským menom už existuje.";
			}
			
			if(!password.equals(passwordRe)) {
				System.out.println("heslo " + password + " heslo2 " + passwordRe);
				return "Hesla sa nezhodujú";
			}
			
			return null;
		}
	}
	
}
