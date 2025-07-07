package finalProj.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import jakarta.servlet.http.HttpServletRequest;

@Controller
public class SpaFallbackController {
    @RequestMapping(value = { "/{path:^(?!api).*$}", "/{path:^(?!api).*$}/**" })
    public String redirect(HttpServletRequest request) {
        String uri = request.getRequestURI();
        // 如果請求的是靜態資源（有副檔名），不 forward
        if (uri.matches(".*\\.[a-zA-Z0-9]+$")) {
            return null;
        }
        return "forward:/index.html";
    }
}