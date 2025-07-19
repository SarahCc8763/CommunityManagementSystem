package finalProj.jwt;

import java.util.ArrayList;
import java.util.List;

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
				// .addPathPatterns("/users/**"); // 只攔截受保護的路徑
				.excludePathPatterns("/users/**", "/api/**", "/**");

	}

	@Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins(buildAllowedOrigins())
                .allowedMethods("*")
                .allowedHeaders("*")
                .allowCredentials(true);
    }

    private String[] buildAllowedOrigins() {
        List<String> origins = new ArrayList<>();
        origins.add("http://localhost:5173");
        origins.add("https://payment-stage.ecpay.com.tw");
        origins.add("https://payment.ecpay.com.tw");
		origins.add("null");		

        for (int i = 1; i <= 255; i++) {
            origins.add("http://192.168.36." + i);
			origins.add("https://192.168.36." + i);
        }

        return origins.toArray(new String[0]);
    }



}
