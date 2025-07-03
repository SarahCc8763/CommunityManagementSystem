package finalProj.jwt;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JsonWebTokenInterceptor implements HandlerInterceptor {

    @Autowired
    private JsonWebTokenUtility jwtUtility;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {

        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7); // 去掉 "Bearer "

            try {
                jwtUtility.verifyToken(token);
                return true; // 放行
            } catch (Exception e) {
                response.setStatus(HttpStatus.UNAUTHORIZED.value());
                response.getWriter().write("invalid token");
                return false;
            }
        }

        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.getWriter().write("token needed");
        return false;
    }
}

