package playground.layout.logic;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection="users")
public class UserEntity {
	private String email;
	private String playground;
	private String username;
	private String avatar;
	private String role;
	private long points;
	private int code;
	private boolean confirm;
	@Id
	private String identifier;
	
	public UserEntity() {
		this.avatar = "";
		this.points = 100;
		this.setConfirm(false);
	}

	public UserEntity(String playground, String email, String username, String role, String avatar) {
		this();
		this.playground = playground;
		this.email = email.toLowerCase().trim();
		this.username = username;
		this.role = role;
		this.avatar = avatar;
		this.setIdentifier(email.toLowerCase().trim()+"#"+playground);
	}

	public String getPlayground() {
		return playground;
	}
	
	public String getEmail() {
		return email;
	}

	public String getUsername() {
		return username;
	}

	public String getRole() {
		return role;
	}

	public long getPoints() {
		return points;
	}

	public String getAvatar() {
		return avatar;
	}

	public void setPlayground(String playground) {
		this.playground = playground;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public void setPoints(long points) {
		this.points = points;
	}

	public void setAvatar(String avater) {
		this.avatar = avater;
	}
	
	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}
	
	public boolean isConfirm() {
		return confirm;
	}

	public void setConfirm(boolean confirm) {
		this.confirm = confirm;
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
		return String.format("UserEntity [email=%s, playground=%s, username=%s, avatar=%s, role=%s, points=%s]",
				email, playground, username, avatar, role, points);
	}

	public void addPoints(int points) {
		this.points += points;
	}

	
}