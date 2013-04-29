package models;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import play.data.validation.Constraints;
import play.db.ebean.Model;

@Entity
@Table(name = "order_table")
public class Order extends Model {
	@Id
	public Long id;

	@Constraints.Required
	public Date date;

	@ManyToOne
	public User user;
	
	public Order(Date date, User user) {
		this.date = date;
		this.user = user;
	}

	public static Order create(Date date, User user) {
		Order order = new Order(date, user);
		order.save();
		return order;
	}
	
	public void fill(Date date, User user) {
		this.user = user;
		this.date = date;
		this.update();
	}
	
	public static void updateTimeStamp(Long id) {
		Order order = find.byId(id);
		order.date = new Date();
		order.update();
	}

	public static void remove(Long id) {
		Order.find.byId(id).delete();
	}
	
	public String getDate() {
		DateFormat dt = new SimpleDateFormat("dd-MM-yyyy");
		return dt.format(date);
	}

		/**
	 * Generic query helper for entity Computer with id Long
	 */
	public static Finder<Long, Order> find = new Finder<Long, Order>(
			Long.class, Order.class);

	public static List<Order> findInvolving(String user) {
		return find.where().eq("user.login", user).findList();
	}
	
	public static List<Order> findAll() {
		return find.all();
	}
	
	public static boolean isFree(Date date) {
        return find.where()
            .eq("date", date)
            .findRowCount() < 10;
    }
	
	public static List<Order> findByDate(Date date) {
		return find.where().eq("date", date).findList();
	}

}
