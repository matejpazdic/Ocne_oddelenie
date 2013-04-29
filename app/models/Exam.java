package models;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

import javax.persistence.*;

import play.data.validation.Constraints;
import play.db.ebean.Model;
import scala.Unit;

@Entity
public class Exam extends Model {

	public static final String DATE_FORMAT = "dd.MM.yyyy";

	@Id
	public Long id;

	@Constraints.Required
	public String findings;

	@Constraints.Required
	public Date create;

	@ManyToOne
	public User user;

	public Exam(String findings, User user) {
		this.findings = findings;
		this.create = new Date();
		this.user = user;
	}
	
	public Exam() {
	}

	public static Exam create() {
		Exam table = new Exam();
		table.save();
		return table;
	}
	
	public static Exam create(String findings, User user) {
		Exam table = new Exam(findings, user);
		table.save();
		return table;
	}
	
	public void fill(String findings, User user) {
		this.findings = findings;
		this.user = user;
		this.create = new Date();
		this.update();
	}

	public static void deleteGarbage() {
		List<Exam> tables = Exam.find.where().eq("name", null)
				.findList();
		for (Exam table : tables) {
			Exam.remove(table.id);
		}
	}
	
	public static void updateTimeStamp(Long id) {
		Exam table = find.byId(id);
		table.create = new Date();
		table.update();
	}

	public static void remove(Long id) {
		Exam.find.byId(id).delete();
	}
	
	public String getDate() {
		DateFormat dt = new SimpleDateFormat("hh:mm dd-MM-yyyy");
		return dt.format(create);
	}

		/**
	 * Generic query helper for entity Computer with id Long
	 */
	public static Finder<Long, Exam> find = new Finder<Long, Exam>(
			Long.class, Exam.class);

	public static List<Exam> findInvolving(String user) {
		return find.where().eq("user.login", user).findList();
	}
	
	public static List<Exam> findAll() {
		return find.all();
	}

}
