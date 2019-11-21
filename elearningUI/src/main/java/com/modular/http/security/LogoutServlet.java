package com.modular.http.security;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

@WebServlet("/logout")
public class LogoutServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        if(session != null) {
            session.invalidate();
            Cookie[] cookies = req.getCookies();
            if(cookies != null) {
                for (Cookie c : cookies) {
                    c.setMaxAge(0);
                }
            }
            //resp.sendRedirect("/elearningUI/html/log_in.html");
        }else {
            //resp.sendRedirect("/elearningUI/html/log_in.html");
        }
    }
}
