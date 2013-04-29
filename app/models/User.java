package models;

import java.util.*;

import javax.persistence.*;

import play.data.validation.Constraints;
import play.db.ebean.Model;

@Entity
public class User extends Model {

	@Id
	public String login;

	public String name;

	public String surname;
	
	public String titul;
	
	public String address;
	
	@Constraints.Required
	public String password;

	@Constraints.Required
	public Role role;

	@OneToMany
	public List<Exam> findings;
	
	@OneToMany 
	public List<Dioptrie> dioptries;
	
	@OneToMany
	public List<Order> orders;

	public User(String login, String name, String surname, String password, String titul, String address,
			Role role) {
		this.login = login;
		this.name = name;
		this.surname = surname;
		this.password = password;
		this.titul = titul;
		this.address = address;
		this.role = role;
	}

	public static User addUser(String login, String name, String surname,
			String password, String titul, String address, Role rola) {
		User user = new User(login, name, surname, password, titul, address, rola);
		user.save();
		return user;
	}

	public static User authenticate(String login, String password) {
		return find.where().eq("login", login).eq("password", password)
				.findUnique();
	}

	public static List<User> findAll() {
		return find.all();
	}

	public static void remove(String login) {
		List<Exam> tables = Exam.findInvolving(login);
		for (Exam table : tables) {
			Exam.remove(table.id);
		}
		List<Dioptrie> dioptries = Dioptrie.findInvolving(login);
		for (Dioptrie dio : dioptries) {
			Exam.remove(dio.id);
		}
		
		User.find.byId(login).delete();
	}

	/**
	 * Generic query helper for entity Computer with id Long
	 */
	public static Finder<String, User> find = new Finder<String, User>(
			String.class, User.class);

}
