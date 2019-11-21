package com.modular.http.security;

import com.google.gson.Gson;
import org.apache.catalina.realm.GenericPrincipal;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;
import java.io.IOException;
import java.security.Principal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

@WebFilter("/console/*")
public class PostLoginFilter implements Filter {
    private static final String USER_QUERY = "SELECT idUSer, type FROM user WHERE name = ? and password = ?";

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain filterChain) throws IOException, ServletException {
        HttpSession session = ((HttpServletRequest)req).getSession(false);
        if(session != null) {
            if(!cookieExists((HttpServletRequest) req, "session")) {
                Map<String, Integer> user = getAuthenticatedUser(((HttpServletRequest)req).getUserPrincipal());
                Cookie cookie = new Cookie("session", user.get("idUser") + "|" + user.get("userType"));
                //cookie.setMaxAge(0);
                ((HttpServletResponse)res).addCookie(cookie);
            }
        }
        filterChain.doFilter(req, res);
    }

    @Override
    public void destroy() {
    }

    private boolean cookieExists(HttpServletRequest req, String cookie) {
        Cookie[] cookies = req.getCookies();
        if(cookies != null) {
            for(Cookie c : cookies) {
                if(cookie.equals(c.getName())) {
                    return true;
                }
            }
        }
        return false;
    }

    private Map<String, Integer> getAuthenticatedUser(Principal principal) {
        try {
            Context initCtx = new InitialContext();
            Context envCtx = (Context) initCtx.lookup("java:comp/env");

            DataSource ds = (DataSource)
                    envCtx.lookup("myDataSource");

            Connection conn = ds.getConnection();
            PreparedStatement stm = conn.prepareStatement(USER_QUERY);
            stm.setString(1, principal.getName());
            stm.setString(2, ((GenericPrincipal)principal).getPassword());
            ResultSet rs = stm.executeQuery();

            Map<String, Integer> result = new HashMap<>();
            if(rs.next()) {
                result.put("idUser", rs.getInt(1));
                result.put("userType", rs.getInt(2));
            }
            conn.close();
            return  result;
        } catch (NamingException ne) {
            ne.printStackTrace();
        } catch (SQLException se) {
            se.printStackTrace();
        }
        return null;
    }

}
