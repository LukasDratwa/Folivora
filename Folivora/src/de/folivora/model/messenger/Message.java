package de.folivora.model.messenger;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;

import org.bson.types.ObjectId;

import com.google.gson.JsonObject;

import de.folivora.model.SearchRequest;
import de.folivora.model.User;

/**
 * Class to represent a message.
 * 
 * <hr>Created on 14.01.2017<hr>
 * @author <a href="mailto:lukasdratwa@yahoo.de">Lukas Dratwa</a>
 */
@Entity
public class Message {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name = "id", updatable = false, nullable = false)
	private ObjectId id;
	private String title,
				   text;
	@OneToOne(targetEntity=User.class)
	private User sender,
				 receiver;
	private boolean seen;
	private Date seenTimestamp,
				 creationTimestamp;
	@OneToOne(targetEntity=SearchRequest.class)
	private SearchRequest referencedSr;
	
	public Message(String title, String text, User sender, User receiver, SearchRequest referencedSr) {
		this.title = title;
		this.text = text;
		this.sender = sender;
		this.receiver = receiver;
		this.seen = false;
		this.seenTimestamp = null;
		this.creationTimestamp = new Date();
		this.referencedSr = referencedSr;
	}
	
	protected Message() {
		
	}
	
	public JsonObject getAsJsonObject() {
		JsonObject jo = new JsonObject();
		
		jo.addProperty("id", this.getId().toString());
		jo.addProperty("title", this.getTitle());
		jo.addProperty("text", this.getText());
		jo.addProperty("seen", this.isSeen());
		if(this.getSeenTimestamp() != null) jo.addProperty("seenTimestamp", this.getSeenTimestamp().toString());
		
		JsonObject sender = new JsonObject();
		User userSender = this.getSender();
		sender.addProperty("id", userSender.getId().toString());
		sender.addProperty("name", userSender.getName());
		sender.addProperty("hometown", userSender.getHometown());
		jo.add("sender", sender);
		
		JsonObject receiver = new JsonObject();
		User userCreator = this.getReceiver();
		receiver.addProperty("id", userCreator.getId().toString());
		receiver.addProperty("name", userCreator.getName());
		receiver.addProperty("hometown", userCreator.getHometown());
		jo.add("receiver", receiver);
		
		JsonObject referencedSr = this.getReferencedSr().getAsJsonObject();
		jo.add("referencedSr", referencedSr);
		
		return jo;
	}
	
	@Override
	public String toString() {
		return "[id=" + this.id + ", title=" + this.title + ", sender=" + this.sender + ", receiver=" + this.receiver + "]";
	}
	
	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}
	
	/**
	 * @param title the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}
	
	/**
	 * @return the text
	 */
	public String getText() {
		return text;
	}
	
	/**
	 * @param text the text to set
	 */
	public void setText(String text) {
		this.text = text;
	}
	
	/**
	 * @return the sender
	 */
	public User getSender() {
		return sender;
	}
	
	/**
	 * @param sender the sender to set
	 */
	public void setSender(User sender) {
		this.sender = sender;
	}
	
	/**
	 * @return the receiver
	 */
	public User getReceiver() {
		return receiver;
	}
	
	/**
	 * @param receiver the receiver to set
	 */
	public void setReceiver(User receiver) {
		this.receiver = receiver;
	}
	
	/**
	 * @return the seen
	 */
	public boolean isSeen() {
		return seen;
	}
	
	/**
	 * @param seen the seen to set
	 */
	public void setSeen(boolean seen) {
		this.seen = seen;
	}
	
	/**
	 * @return the seenTimestamp
	 */
	public Date getSeenTimestamp() {
		return seenTimestamp;
	}
	
	/**
	 * @param seenTimestamp the seenTimestamp to set
	 */
	public void setSeenTimestamp(Date seenTimestamp) {
		this.seenTimestamp = seenTimestamp;
	}
	
	/**
	 * @return the creationTimestamp
	 */
	public Date getCreationTimestamp() {
		return creationTimestamp;
	}

	/**
	 * @return the referencedSr
	 */
	public SearchRequest getReferencedSr() {
		return referencedSr;
	}

	/**
	 * @param referencedSr the referencedSr to set
	 */
	public void setReferencedSr(SearchRequest referencedSr) {
		this.referencedSr = referencedSr;
	}

	/**
	 * @return the id
	 */
	public ObjectId getId() {
		return id;
	}
}