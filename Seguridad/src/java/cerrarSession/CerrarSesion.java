/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cerrarSession;

import dominios.Login;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Susana
 */
public class CerrarSesion {
    
   public String logout() {
        FacesContext context = FacesContext.getCurrentInstance();
        ExternalContext externalContext = context.getExternalContext();
        HttpSession httpSession = (HttpSession) externalContext.getSession(false);
        
        Login usuarioSesion = (Login) httpSession.getAttribute("usuarioSesion");
        if (usuarioSesion == null) {
            usuarioSesion = new Login();
            httpSession.setAttribute("usuarioSesion", usuarioSesion);
        } else if(usuarioSesion.getUsuario()!=null) {
            usuarioSesion.setUsuario(null);
        }
        usuarioSesion.setJndi("jdbc/__systemWeb");
        httpSession.invalidate();
        return "index.xhtml";
    } 
}
