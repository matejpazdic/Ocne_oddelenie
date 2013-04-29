package controllers;

import models.Role;
import models.User;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.settings.settings;


public class Settings extends Controller {
	
	public static Result show() {
		User user = User.find.byId(ctx().session().get("login"));
		
		SettingsForm form = new SettingsForm();
		form.login = user.login;
		form.name = user.name;
		form.surname = user.surname;
		
		return ok(settings.render(user, form(SettingsForm.class).fill(form)));
	}
	
	public static Result submit() {
		Form<SettingsForm> settingsForm = form(SettingsForm.class).bindFromRequest();
		User user = User.find.byId(ctx().session().get("login"));
				
		if(settingsForm.hasErrors()) {
            return badRequest(settings.render(user, settingsForm));
        } else {
        	user.name = settingsForm.get().name;
        	user.surname = settingsForm.get().surname;
        	if(!settingsForm.get().newPassword.equals("")) {
        		user.password = settingsForm.get().newPassword;
        	}
        	user.update();
        	        	
            flash("settings_successful", "Zmena profilu bola úspešná");
        	return redirect(routes.Menu.show()
            );
        }
	}
	
	public static class SettingsForm extends SignUp.SignUpForm {
		
		public String newPassword;
				
		@Override
		public String validate() {
			if(password.equals("")) {
				return "Pre zmenu údajov musíte zadať aktuálne heslo";
			}
			
			if(!User.find.byId(ctx().session().get("login")).password.equals(password)) {
				return "Nesprávne aktuálne heslo";
			}
			
			if(!newPassword.equals(passwordRe)) {
				return "Nove heslá sa nezhodujú";
			}
			return null;
		}
	}
	
	
}
