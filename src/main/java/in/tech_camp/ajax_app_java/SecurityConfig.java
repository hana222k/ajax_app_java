package in.tech_camp.ajax_app_java;

import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.stereotype.Controller;

@Controller
@EnableWebSecurity
public class SecurityConfig {
  @Bean
  // securityFilterChainメソッドでアプリのセキュリティ設定
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http
      // authorizeHttpRequests()：全てのリクエストに対して認証が必要という設定
      .authorizeHttpRequests(authorizeRequests -> authorizeRequests
        .anyRequest().authenticated()
      )
      // ログイン画面へのアクセスを許可
      .formLogin(form -> form
        .permitAll() 
      )
      // 本アプリでBasic認証を使用する
      .httpBasic(httpBasicCustomizer -> { });

    return http.build();
  }
  
  @Bean
  // userDetailsServiceメソッドでBasic認証に使用するユーザー情報の指定
  public UserDetailsService userDetailsService(PasswordEncoder encoder) {
    String username = System.getenv("BASIC_AUTH_USER");
    String password = System.getenv("BASIC_AUTH_PASSWORD");

    UserDetails user = User.withUsername(username)
        .password(encoder.encode(password))
        .roles("ADMIN")
        .build();
    return new InMemoryUserDetailsManager(user);
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }
}
