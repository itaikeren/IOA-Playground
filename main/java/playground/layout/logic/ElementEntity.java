package playground.layout.logic;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection="elements")
public class ElementEntity {
	private String playground;
	private String id;
	private double x;
	private double y;
	private String name;
	private Date creationDate;
	private Date expirationDate;
	private String type;
	private Map<String, Object> attributes;
	private String creatorPlayground;
	private String creatorEmail;
	private double distance;
	private String identifier;

	public ElementEntity() {
		this.x = 0;
		this.y = 0;
		this.creationDate = new Date();
		this.expirationDate = null;
		this.attributes = new HashMap<>();
		this.identifier = "none";
//		this.setDistance(-1);
	}
	
	public ElementEntity(double x, double y, String name, Date expirationDate, String type, Map<String, Object> attributes) {
		this();
		this.x = x;
		this.y = y;
		this.name = name;
		this.expirationDate = expirationDate;
		this.type = type;
		this.attributes = attributes;
	}

	public String getPlayground() {
		return playground;
	}

	public void setPlayground(String playground) {
		this.playground = playground;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public double getX() {
		return x;
	}

	public void setX(double x) {
		this.x = x;
	}

	public double getY() {
		return y;
	}

	public void setY(double y) {
		this.y = y;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	public Date getExpirationDate() {
		return expirationDate;
	}

	public void setExpirationDate(Date expirationDate) {
		this.expirationDate = expirationDate;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Map<String, Object> getAttributes() {
		return attributes;
	}

	public void setAttributes(Map<String, Object> attributes) {
		this.attributes = attributes;
	}

	public String getCreatorPlayground() {
		return creatorPlayground;
	}

	public void setCreatorPlayground(String creatorPlayground) {
		this.creatorPlayground = creatorPlayground;
	}

	public String getCreatorEmail() {
		return creatorEmail;
	}

	public void setCreatorEmail(String creatorEmail) {
		this.creatorEmail = creatorEmail;
	}

	public double getDistance() {
		return distance;
	}

	public void setDistance(double distance) {
		this.distance = distance;
	}

	@Id
	public String getIdentifier() {
		return identifier;
	}

	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}

	@Override
	public String toString() {
		return String.format(
				"ElementEntity [playground=%s, id=%s, x=%s, y=%s, name=%s, creationDate=%s, expirationDate=%s, type=%s, attributes=%s, creatorPlayground=%s, creatorEmail=%s]",
				playground, id, x, y, name, creationDate, expirationDate, type, attributes, creatorPlayground,
				creatorEmail);
	}
}
