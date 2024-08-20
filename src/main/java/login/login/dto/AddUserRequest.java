package login.login.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class AddUserRequest {

	private String name;
	private String email;
	private String password;

	// 인자를 받는 생성자 추가
	public AddUserRequest(String name, String email, String password) {
		this.name = name;
		this.email = email;
		this.password = password;
	}

}
