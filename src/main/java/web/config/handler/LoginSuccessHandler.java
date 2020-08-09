package web.config.handler;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class LoginSuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest httpServletRequest,
                                        HttpServletResponse httpServletResponse,
                                        Authentication authentication) throws IOException {
//        httpServletResponse.sendRedirect("/");
        httpServletResponse.setStatus(HttpServletResponse.SC_OK);
        System.out.println("LoginSuccessHandler ");
        boolean admin = false;

        for (GrantedAuthority auth : authentication.getAuthorities()) {
            System.out.println("authentication "+ auth.getAuthority());
            if ("ROLE_ADMIN".equals(auth.getAuthority())){
                admin = true;
            }
        }

        if(admin){
            System.out.println("LoginSuccessHandler /admin");
            httpServletResponse.sendRedirect("/admin");
        }else{
            System.out.println("LoginSuccessHandler /user");
            httpServletResponse.sendRedirect("/user");
        }
    }

}