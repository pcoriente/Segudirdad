/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Filtros;

import dominios.Login;
import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


/**
 *
 * @author Susana
 */
public class Filtro implements Filter{

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {}

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        
        String destino=httpRequest.getRequestURL().toString();
        String pathInfo = httpRequest.getRequestURI().substring(httpRequest.getContextPath().length());
        if (pathInfo.startsWith("/faces/includes") || pathInfo.startsWith("/faces/javax.faces.resource")) {
            chain.doFilter(request, response);
            return;
        }

        HttpSession httpSession = httpRequest.getSession(true);
        Login usuarioSesion = (Login) httpSession.getAttribute("usuarioSesion");

        if (usuarioSesion == null) {
            usuarioSesion = new Login();
            usuarioSesion.setJndi("jdbc/__systemWeb");
            httpSession.setAttribute("usuarioSesion", usuarioSesion);
        }
        if(destino.endsWith("/")) {
            chain.doFilter(request, response);
        } else if(usuarioSesion.getUsuario()==null && !destino.contains("index.xhtml")) {
            httpResponse.sendRedirect("/Seguridad/faces/index.xhtml");
        } else {
            request.setCharacterEncoding("UTF-8");
            response.setCharacterEncoding("UTF-8");
            chain.doFilter(request, response);
        }
    }

    @Override
    public void destroy() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    
    
}
