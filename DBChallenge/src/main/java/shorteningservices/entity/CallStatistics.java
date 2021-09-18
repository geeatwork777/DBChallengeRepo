package shorteningservices.entity;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;

import shorteningservices.entity.URL;
import shorteningservices.entity.User;

@Entity
public class CallStatistics {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int ID;

	private User creator;

	private LocalDateTime creationTime;

	@ElementCollection(targetClass = LocalDateTime.class)
	private List<LocalDateTime> callTimes;

	private int callsTotal;

	@ManyToMany(cascade = CascadeType.ALL)
	private List<User> callers;

	public User getCreator() {
		return creator;
	}

	public void setCreator(User creator) {
		this.creator = creator;
	}

	public LocalDateTime getCreationTime() {
		return creationTime;
	}

	public void setCreationTime(LocalDateTime creationTime) {
		this.creationTime = creationTime;
	}

	public List<LocalDateTime> getCallTimes() {
		return callTimes;
	}

	public void setCallTimes(List<LocalDateTime> callTimes) {
		this.callTimes = callTimes;
	}

	public int getCallsTotal() {
		return callsTotal;
	}

	public void setCallsTotal(int callsTotal) {
		this.callsTotal = callsTotal;
	}

	public List<User> getCallers() {
		return callers;
	}

	public void setCallers(List<User> callers) {
		this.callers = callers;
	}

	public URL getUrl() {
		return url;
	}

	public void setUrl(URL url) {
		this.url = url;
	}

	@OneToOne(mappedBy = "stats", cascade = CascadeType.PERSIST)
	private URL url;

	public CallStatistics() {
	};

	public CallStatistics(User createdBy, LocalDateTime create, List<LocalDateTime> calls, int total,
			List<User> users) {
		this.creator = createdBy;
		this.creationTime = create;
		this.callTimes = calls;
		this.callsTotal = total;
		this.callers = users;
	}

	public void recordCall(User caller, LocalDateTime callTime) {
		this.callers.add(caller);
		this.callsTotal = this.callsTotal+1;
		this.callTimes.add(callTime);
	}

	@Override
	public String toString() {
		List<String> usernames = new LinkedList<String>();
		List<String> dates = new LinkedList<String>();
		callers.forEach(c -> {
			if (c == null) {
				usernames.add("Unknown User");
			} else {
				usernames.add(c.getusername());
			}
		});
		callTimes.forEach(t -> dates.add(t.toString()));
		if (this.creator != null) {
			return "The URL with an original value of " + url.getOriginal() + " and an alias " + url.getAlias()
					+ " was created by " + creator.getusername() + " at " + creationTime.toString()
					+ "\nFor this entry, " + callsTotal + " calls were received. These took place at "
					+ dates.toString() + " and were issued by " + usernames.toString();
		} else {
			return "The URL with an original value of " + url.getOriginal() + " and an alias " + url.getAlias()
					+ " was created by an Unknown User at " + creationTime.toString() + "\nFor this entry, "
					+ callsTotal + " calls were received. These took place at " + dates.toString()
					+ " and were issued by " + usernames.toString();
		}
	}
}