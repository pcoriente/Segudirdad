/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package managedBeas;

import daoPermisos.DaoPer;
import dominios.Pais;
import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

/**
 *
 * @author Susana
 */
@ManagedBean
@SessionScoped
public class MbPaises implements Serializable {

    private ArrayList<Pais> listaPais = new ArrayList<>();

    public MbPaises() {
        DaoPer daoPermisos = new DaoPer();
        try {
            listaPais = daoPermisos.dameListaPaises();
        } catch (SQLException ex) {
            Logger.getLogger(MbPaises.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public ArrayList<Pais> getListaPais() {

        return listaPais;
    }

    public void setListaPais(ArrayList<Pais> listaPais) {
        this.listaPais = listaPais;
    }
}
