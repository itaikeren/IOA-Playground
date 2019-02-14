package playground.layout;

public class NewUserForm {
	private String email;
	private String username;
	private String avatar;
	private String role;
	
	public NewUserForm() {
	}
	
	public NewUserForm(String email, String username, String avatar, String role) {
		this.email = email;
		this.username = username;
		this.avatar = avatar;
		this.role = role;
	}

	public String getEmail() {
		return email;
	}

	public String getUsername() {
		return username;
	}

	public String getAvatar() {
		return avatar;
	}

	public String getRole() {
		return role;
	}
}