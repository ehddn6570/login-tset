package login.login.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import login.login.domain.User;
import login.login.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Transactional
@Service
public class UserDetailService implements UserDetailsService {

	private final UserRepository userRepository;

	@Override
	public User loadUserByUsername(String email) {
		return userRepository.findByEmail(email)
			.orElseThrow(() -> new IllegalArgumentException("계정을 찾지 못했엉: " + email));
	}
}