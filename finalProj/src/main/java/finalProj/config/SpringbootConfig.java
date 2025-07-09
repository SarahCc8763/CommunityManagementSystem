package finalProj.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class SpringbootConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("*") // 或改成 "http://192.168.36.85:5173" 較安全
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // 🔥加上 OPTIONS 很關鍵！
                .allowedHeaders("*") // 🔥允許所有 headers，避免預檢失敗
                .maxAge(3600); // 預檢快取 1 小時（可加可不加）
    }
}
