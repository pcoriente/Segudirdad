/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package managedBeas;

import dominios.DominioUsuario;
import java.io.Serializable;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

/**
 *
 * @author Comodoro
 */
@ManagedBean
@SessionScoped
public class MbUsuarios implements Serializable{
    private DominioUsuario usuario = new DominioUsuario();
    /**
     * Creates a new instance of MbUsuarios
     */
    public MbUsuarios() {
    }

    public DominioUsuario getUsuario() {
        return usuario;
    }

    public void setUsuario(DominioUsuario usuario) {
        this.usuario = usuario;
    }
    
    
}
