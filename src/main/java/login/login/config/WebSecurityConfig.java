package login.login.config;

import lombok.RequiredArgsConstructor;
import login.login.service.UserDetailService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;


@RequiredArgsConstructor
@Configuration
public class WebSecurityConfig {

	private final UserDetailService userService;

	@Bean
	public WebSecurityCustomizer configure() {
		return (web) -> web.ignoring()
			.requestMatchers("/h2-console/**")  // toH2Console() 대신 직접 URL 패턴 사용
			.requestMatchers("/static/**");
	}

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		return http
			.authorizeHttpRequests(authorize -> authorize  // authorizeRequests() -> authorizeHttpRequests()로 변경
				.requestMatchers("/login", "/signup", "/user").permitAll()  // 공용 경로
				.anyRequest().authenticated()  // 나머지 요청은 인증 필요
			)
			.formLogin(form -> form
				.loginPage("/login")  // 커스텀 로그인 페이지
				.defaultSuccessUrl("/welcome", true)  // 성공 후 리다이렉트
			)
			.logout(logout -> logout
				.logoutSuccessUrl("/login")  // 로그아웃 후 리다이렉트
				.invalidateHttpSession(true)  // 세션 무효화
			)
			.csrf(csrf -> csrf.disable())  // CSRF 비활성화 (필요에 따라 조정)
			.build();
	}

	@Bean
	public AuthenticationManager authenticationManager(HttpSecurity http, BCryptPasswordEncoder bCryptPasswordEncoder) throws Exception {
		return http.getSharedObject(AuthenticationManagerBuilder.class)
			.userDetailsService(userService)
			.passwordEncoder(bCryptPasswordEncoder)
			.and()
			.build();
	}

	@Bean
	public BCryptPasswordEncoder bCryptPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}
}
