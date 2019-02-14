package playground.layout;

import playground.layout.logic.UserEntity;

public class UserTO {
	private String email;
	private String playground;
	private String username;
	private String avatar;
	private String role;
	private long points;
	
	public static final String adminRole = "Admin";
	public static final String userRole = "Member";
	
	public UserTO() {
		this.playground = "2019A.itaikeren";
		this.points = 100;
	}

	public UserTO(String email, String username, String role, String avatar) {
		this();
		this.email = email;
		this.username = username;
		setRole(role);
		this.avatar = avatar;
		
	}

	public UserTO(UserEntity entity) {
		if (entity != null) {
			this.email = entity.getEmail();
			this.playground = entity.getPlayground();
			this.username = entity.getUsername();
			this.avatar = entity.getAvatar();
			setRole(entity.getRole());
			this.points = entity.getPoints();
		}
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
		if(role != null && role.equalsIgnoreCase(adminRole)) {
			this.role = adminRole;
		} else {
			this.role = userRole;
		}
		
	}

	public void setPoints(long points) {
		this.points = points;
	}

	public void setAvatar(String avater) {
		this.avatar = avater;
	}
	
	public UserEntity toEntity () {
		UserEntity rv = new UserEntity();
		rv.setEmail(this.email);
		rv.setPlayground(this.playground);
		rv.setUsername(this.username);
		rv.setAvatar(this.avatar);
		rv.setRole(this.role);
		rv.setPoints(this.points);
		rv.setIdentifier(this.email+"#"+this.playground);
		return rv;
	}

	@Override
	public String toString() {
		return String.format("UserTO [email=%s, playground=%s, username=%s, avatar=%s, role=%s, points=%s]", email,
				playground, username, avatar, role, points);
	}


	
}