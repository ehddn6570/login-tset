package login.login.service;

import java.util.List;
import java.util.Optional;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import login.login.domain.User;
import login.login.dto.AddUserRequest;
import login.login.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Transactional
@Service
public class UserService {

	private final UserRepository userRepository;
	private final BCryptPasswordEncoder bCryptPasswordEncoder;

	// Email 중복 검사
	private void validateDuplicateEmail(String email) {
		Optional<User> findEmail = userRepository.findByEmail(email);
		if (findEmail.isPresent()) {
			throw new IllegalStateException("중복된 이메일입니다: " + email);
		}
	}

	// 닉네임 중복 검사
	private void validateDuplicateUser(String name) {
		Optional<User> findName = userRepository.findByName(name);
		if (findName.isPresent()) {
			throw new IllegalStateException("중복된 닉네임입니다: " + name);
		}
	}

	// 단일 사용자 저장
	public Long save(AddUserRequest dto) {
		validateDuplicateUser(dto.getName());
		validateDuplicateEmail(dto.getEmail());

		return userRepository.save(User.builder()
			.name(dto.getName())
			.email(dto.getEmail())
			.password(bCryptPasswordEncoder.encode(dto.getPassword()))
			.build()).getId();
	}

	// 다수의 사용자 저장
	public void saveAll(List<AddUserRequest> dtos) {
		for (AddUserRequest dto : dtos) {
			try {
				// 각각의 사용자에 대해 중복 검사 후 저장
				validateDuplicateUser(dto.getName());
				validateDuplicateEmail(dto.getEmail());

				userRepository.save(User.builder()
					.name(dto.getName())
					.email(dto.getEmail())
					.password(bCryptPasswordEncoder.encode(dto.getPassword()))
					.build());
			} catch (IllegalStateException e) {
				// 중복된 사용자일 경우 예외 메시지를 로그로 출력하고 건너뜀
				System.out.println("중복된 사용자 처리 중 오류 발생: " + e.getMessage());
			}
		}
	}
}
