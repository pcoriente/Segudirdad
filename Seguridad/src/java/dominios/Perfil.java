/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dominios;

import java.io.Serializable;

/**
 *
 * @author Comodoro
 */
public class Perfil implements Serializable {

    private int idPerfiles;
    private String perfil;
    private int idUsuario;

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public int getIdPerfiles() {
        return idPerfiles;
    }

    public void setIdPerfiles(int idPerfiles) {
        this.idPerfiles = idPerfiles;
    }

    public String getPerfil() {
        return perfil;
    }

    public void setPerfil(String perfil) {
        this.perfil = perfil;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 79 * hash + this.idPerfiles;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Perfil other = (Perfil) obj;
        if (this.idPerfiles != other.idPerfiles) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return perfil;
    }
}
