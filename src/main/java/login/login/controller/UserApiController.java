package login.login.controller;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import login.login.dto.AddUserRequest;
import login.login.service.UserService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@Controller
public class UserApiController {

	private final UserService userService;

	@PostMapping("/user")
	public String signup(AddUserRequest request, Model model) {
		try {
			userService.save(request);
			return "redirect:/login";
		} catch (IllegalStateException e) {
			model.addAttribute("errorMessage", e.getMessage());
			return "signup";
		} catch (Exception e) {
			model.addAttribute("errorMessage", "회원가입에 실패하였습니다. 다시 시도해주세요.");
			return "signup";
		}
	}

	@GetMapping("/logout")
	public String logout(HttpServletRequest request, HttpServletResponse response) {
		new SecurityContextLogoutHandler().logout(request, response, SecurityContextHolder.getContext().getAuthentication());
		return "redirect:/login";
	}


	@PostMapping("/upload-students")
	public String uploadStudents(@RequestParam("file") MultipartFile file, Model model) {
		if (file.isEmpty()) {
			model.addAttribute("errorMessage", "파일이 업로드되지 않았습니다.");
			return "welcome";
		}

		try {
			List<AddUserRequest> students = new ArrayList<>();
			// CSV 파일을 읽어오기
			BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8));
			String line;
			while ((line = reader.readLine()) != null) {
				String[] data = line.split(","); // CSV의 각 필드를 쉼표로 구분
				// CSV 데이터의 구조가 "이름, 이메일, 비밀번호"라고 가정하고 파싱
				AddUserRequest student = new AddUserRequest(data[0], data[1], data[2]);
				students.add(student);
			}

			// 전체 학생 추가 로직
			userService.saveAll(students); // 모든 학생을 저장하는 로직

			return "redirect:/welcome";
		} catch (Exception e) {
			model.addAttribute("errorMessage", "파일 처리 중 오류가 발생했습니다.");
			return "welcome";
		}
	}
}
