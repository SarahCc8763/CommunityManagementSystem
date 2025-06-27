package finalProj.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // @Bean
    // public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    // http
    // .csrf(csrf -> csrf.disable())
    // .authorizeHttpRequests(auth -> auth
    // .requestMatchers("/api/**", "/users/**").permitAll()
    // .anyRequest().authenticated())
    // .httpBasic(Customizer.withDefaults());

    // return http.build();
    // }
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // 關閉 CSRF
                .authorizeHttpRequests(auth -> auth
                        .anyRequest().permitAll() // 所有請求都允許
                )
                .httpBasic(httpBasic -> httpBasic.disable()) // 關閉 HTTP Basic 驗證
                .formLogin(form -> form.disable()) // 關閉登入頁面
                .logout(logout -> logout.disable()); // 關閉登出邏輯

        return http.build();
    }
}