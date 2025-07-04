package finalProj.jwt;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

	@Autowired
	private JsonWebTokenInterceptor jwtInterceptor;

	@Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(jwtInterceptor)
//                .addPathPatterns("/users/**"); // 只攔截受保護的路徑
        .excludePathPatterns("/users/**","/api/**","/**");

    }
	
	@Override
	public void addCorsMappings(CorsRegistry registry) {
		
		registry.addMapping("/**")
		.allowedOrigins("http://localhost:5173")
		.allowedMethods("GET","POST","PUT","DELETE")
		.allowedHeaders("*")
		.allowCredentials(true);
	}
	
	
	
}
