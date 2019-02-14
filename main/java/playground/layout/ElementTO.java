package playground.layout;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import playground.layout.logic.ElementEntity;
import playground.layout.logic.Location;

public class ElementTO {
	private String playground;
	private String id;
	private Location location;
	private String name;
	private Date creationDate;
	private Date expirationDate;
	private String type;
	private Map<String, Object> attributes;
	private String creatorPlayground;
	private String creatorEmail;

	public ElementTO() {
		this.location = new Location(0, 0);
		this.name = "Default Element";
		this.creationDate = new Date();
		this.expirationDate = new Date();
		this.type = "default";
		this.attributes = new HashMap<>();
	}

	public ElementTO(Location location, String name, Date expirationDate, String type, Map<String, Object> attributes, String creatorPlayground, String creatorEmail) {
		this();
		this.location = location;
		this.name = name;
		this.expirationDate = expirationDate;
		this.type = type;
		this.attributes = attributes;
		this.creatorPlayground = creatorPlayground;
		this.creatorEmail = creatorEmail;
	}
	
	public ElementTO(ElementEntity entity) {
		this();
		if (entity != null) {
			this.playground = entity.getPlayground();
			this.id = entity.getId();
			this.location = new Location(entity.getX(), entity.getY());
			this.name = entity.getName();
			this.creationDate = entity.getCreationDate();
			this.expirationDate = entity.getExpirationDate();
			this.type = entity.getType();
			this.attributes = entity.getAttributes();
			this.creatorPlayground = entity.getCreatorPlayground();
			this.creatorEmail = entity.getCreatorEmail();
		}
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

	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
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
	
	public ElementEntity toEntity () {
		ElementEntity rv = new ElementEntity();
		rv.setPlayground(this.playground);
		rv.setId(this.id);
		rv.setX(this.location.getX());
		rv.setY(this.location.getY());
		rv.setName(this.name);
		rv.setCreationDate(this.creationDate);
		rv.setExpirationDate(this.expirationDate);
		rv.setType(this.type);
		rv.setAttributes(this.attributes);
		rv.setCreatorPlayground(this.creatorPlayground);
		rv.setCreatorEmail(this.creatorEmail);
		return rv;
	}
}
