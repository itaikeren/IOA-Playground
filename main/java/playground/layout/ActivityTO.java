package playground.layout;

import java.util.Map;

import playground.layout.logic.ActivityEntity;

public class ActivityTO {
	private String playground;
	private String id;
	private String elementPlayground;
	private String elementId;
	private String type;
	private String playerPlayground;
	private String playerEmail;
	private Map<String, Object> attributes;
	
	public ActivityTO() {
	}
	
	public ActivityTO(String playground, String elementPlayground, String elementId, String type,
			String playerPlayground, String playerEmail, Map<String, Object> attributes) {
		super();
		this.playground = playground;
		this.elementPlayground = elementPlayground;
		this.elementId = elementId;
		this.type = type;
		this.playerPlayground = playerPlayground;
		this.playerEmail = playerEmail;
		this.attributes = attributes;
	}



	public ActivityTO(ActivityEntity entity) {
		this();
		if (entity != null) {
			this.playground = entity.getPlayground();
			this.id = entity.getId();
			this.elementPlayground = entity.getElementPlayground();
			this.elementId = entity.getElementId();
			this.type = entity.getType();
			this.playerPlayground = entity.getPlayerPlayground();
			this.playerEmail = entity.getPlayerEmail();
			this.attributes = entity.getAttributes();
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

	public String getElementPlayground() {
		return elementPlayground;
	}

	public void setElementPlayground(String elementPlayground) {
		this.elementPlayground = elementPlayground;
	}

	public String getElementId() {
		return elementId;
	}

	public void setElementId(String elementId) {
		this.elementId = elementId;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getPlayerPlayground() {
		return playerPlayground;
	}

	public void setPlayerPlayground(String playerPlayground) {
		this.playerPlayground = playerPlayground;
	}

	public String getPlayerEmail() {
		return playerEmail;
	}

	public void setPlayerEmail(String playerEmail) {
		this.playerEmail = playerEmail;
	}

	public Map<String, Object> getAttributes() {
		return attributes;
	}

	public void setAttributes(Map<String, Object> attributes) {
		this.attributes = attributes;
	}
	
	public ActivityEntity toEntity () {
		ActivityEntity rv = new ActivityEntity();
		rv.setPlayground(this.playground);
		rv.setId(this.id);
		rv.setElementPlayground(this.elementPlayground);
		rv.setElementId(this.elementId);
		rv.setType(this.type);
		rv.setPlayerPlayground(this.playerPlayground);
		rv.setPlayerEmail(this.playerEmail);
		rv.setAttributes(this.attributes);
		rv.setIdentifier(this.id+"#"+this.playground);
		return rv;
	}
}
