package com.tartangatickets.entities;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 *
 * @author ionut
 */
@Entity(name="Credential")
@Table(name="credentials", schema="tartanga_ticket_db")
@NamedQueries({
    @NamedQuery(
        name="findAllCredentials",
        query="SELECT c FROM Credential c"
    ),
    @NamedQuery(
        name="findCredentialByLogin",
        query="SELECT c FROM Credential c WHERE c.login = :login"
    ),
})
public class Credential implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    private String login;
    private Byte[] password;
    private Date lastAccess;
    private Date lastPassChange;

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public Byte[] getPassword() {
        return password;
    }

    public void setPassword(Byte[] password) {
        this.password = password;
    }

    public Date getLastAccess() {
        return lastAccess;
    }

    public void setLastAccess(Date lastAccess) {
        this.lastAccess = lastAccess;
    }

    public Date getLastPassChange() {
        return lastPassChange;
    }

    public void setLastPassChange(Date lastPassChange) {
        this.lastPassChange = lastPassChange;
    }
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (login != null ? login.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Credential)) {
            return false;
        }
        Credential other = (Credential) object;
        if ((this.login == null && other.login != null) || (this.login != null && !this.login.equals(other.login))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.tartangatickets.entities.Credential[ login=" + login + " ]";
    }
    
}
