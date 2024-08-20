package login.login.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import login.login.domain.User;

@Controller
public class UserViewController {

	@GetMapping("/login")
	public String login() {
		return "login";
	}

	@GetMapping("/signup")
	public String signup() {
		return "signup";
	}

	@GetMapping("/welcome")
	public String welcome(Model model, @AuthenticationPrincipal User user) {
		model.addAttribute("userName", user.getName());
		return "welcome";
	}
}
