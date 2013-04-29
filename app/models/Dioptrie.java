package models;

import java.text.SimpleDateFormat;
import java.util.*;

import javax.persistence.*;

import play.data.validation.Constraints;
import play.db.ebean.Model;
import scala.Unit;

@Entity
public class Dioptrie extends Model {

	@Id
	public Long id;

	public String details;
	
	public Date date;

	@ManyToOne
	public User user;

	public Dioptrie(String details, Date date, User user) {
		this.details = details;
		this.date = date;
		this.user = user;
	}

	public static Dioptrie create(String details, Date date, User user) {
		Dioptrie unit = new Dioptrie(details, date, user);
		unit.save();
		return unit;
	}

	public static void remove(Long id) {
		Dioptrie.find.byId(id).delete();
	}
	
	public static List<Dioptrie> findInvolving(String login) {
		return find.where().eq("user.login", login).findList();
	}

	/**
	 * Generic query helper for entity Computer with id Long
	 */
	public static Finder<Long, Dioptrie> find = new Finder<Long, Dioptrie>(
			Long.class, Dioptrie.class);
}
