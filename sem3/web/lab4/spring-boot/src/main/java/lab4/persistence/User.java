package lab4.persistence;

import java.io.Serializable;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * Entity implementation class for Entity: CalcResult
 */
@Entity
@Table(name = "users_lab4")
public class User implements Serializable {

	private static final long serialVersionUID = 1L;

    @Id
    private String login;
    private String password;
    private String token;

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}
	
}
