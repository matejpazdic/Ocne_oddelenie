package controllers;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.springframework.format.datetime.DateFormatter;

import models.Order;
import models.Role;
import models.Exam;
import models.Dioptrie;
import models.User;
import play.data.Form;
import play.data.validation.Constraints;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.menu.*;

public class Menu extends Controller {

	public static Result show() {
		User user = null;
		List<Order> orders = null;
		String userId = null;

		if ((userId = ctx().session().get("login")) == null) {
			return forbidden("Nie ste prihlásený");
		} else {
			user = User.find.byId(userId);
			if(user.role == Role.Doctor) {
				orders = Order.findAll();
				return ok(menu.render(user, orders));
			} else {
				Order order = null;
				int size = user.orders.size();
				if(size > 0) {
					order = user.orders.get(size-1);
				} 				
				return ok(menuUser.render(user, order));
			}
		}
	}

	public static Result add() {
		return ok(add.render(User.find.byId(ctx().session().get("login")),
				form(OrderForm.class)));
	}

	public static Result addSubmit() {
		Form<OrderForm> orderForm = form(OrderForm.class)
				.bindFromRequest();

		if (orderForm.hasErrors()) {
			return badRequest(add.render(
					User.find.byId(ctx().session().get("login")),
					form(OrderForm.class)));
		} else {
			// uloz rozvrh
			User user = User.find.byId(ctx().session().get("login"));
			
			SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
			Date date = new Date();
			String dateString = orderForm.get().date;
			System.out.println("Kraaaaaaasny datum "+ dateString);
			try {
				date = sdf.parse(dateString);
			} catch (Exception e) {
				e.printStackTrace();
			}
			Order.create(date, user);
			
			return redirect(routes.Menu.show());
		}
	}

	public static Result addExam(String id, Long order) {
		return ok(addExam.render(User.find.byId(ctx().session().get("login")),
				form(ExamForm.class), id, order));
	}
	
	public static Result addExamSubmit(String patient, Long order) {
		Form<ExamForm> examForm = form(ExamForm.class).bindFromRequest();
		
		if (examForm.hasErrors()) {
			return badRequest(addExam.render(
					User.find.byId(ctx().session().get("login")), form(ExamForm.class), patient, order));
		} else {
			Order.remove(order);
			User user = User.find.byId(examForm.get().login);
			String dioptrie = examForm.get().dioptrie;
			if(!dioptrie.equals("")) {
				Dioptrie.create(dioptrie, new Date(), user);
			}
			Exam.create(examForm.get().findings, user);
			return redirect(routes.Menu.show());
		}
	}
		
		
	public static Result exams() {
		return ok(exams.render(User.find.byId(ctx().session().get("login"))));
	}
	
	
	public static Result delete(Long id) {
		Order.remove(id);
		flash("table_removed", "Objednávka zrušená");
		return redirect(routes.Menu.show());
	}

	public static class OrderForm {
		public String date;

		public String validate() {
			flash().clear();
			
			Date date1 = null;
			try {
				date1 = new SimpleDateFormat("dd.MM.yyyy").parse(date);
			} catch (ParseException e) {
				e.printStackTrace();
			}
			if(!Order.isFree(date1)) {
				return "Tento dátum je už plný";
			}
			
			if (date1 == null || date == null || date.equals("")) {
				flash("table_name_err", "Vyplňte dátum");
				return "Vyplňte dátum";
			}
			return null;
		}
	}

	public static class ExamForm {
		public String findings;
		public String login;
		public String dioptrie;

		public String validate() {
			flash().clear();
			return null;
		}
	}

}
