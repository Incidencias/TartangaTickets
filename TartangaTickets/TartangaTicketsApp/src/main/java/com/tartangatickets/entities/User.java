package com.tartangatickets.entities;

import java.io.Serializable;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.Table;


/**
 *The User class encapsulates the data of each user:
 * <ul>
 *  <li><stron>name</strong> is the name of the user.</li>
 *  <li><stron>lastName1</strong> is the fist last name of the user.</li>
 *  <li><stron>lastName2</strong> is the second last name of the user.</li>
 *  <li><stron>department</strong>is the code department of the user.</li>
 *  <li><stron>credential</strong>is the Credential of the user.</li>
 *  <li><stron>createdTickets</strong>are the created tickets of the user.</li>
 * </ul>
 * 
 * @author Iker Jon Mediavilla
 */
@Entity(name="User")
@Table(name="users", schema="tartanga_ticket_db")
@NamedQueries({
    @NamedQuery(
            name="findAllUsers",
            query="SELECT u FROM User u order by u.lastName1, u.lastName2, u.name"
    ),
    @NamedQuery(
            name="findUserById",
            query="SELECT u FROM User u WHERE u.credential.login = :login"
    ),
    @NamedQuery(
            name="findUsersByName",
            query="SELECT u FROM User u WHERE u.name = :name order by u.lastName1, u.lastName2, u.name"
    ),
    @NamedQuery(
            name="findUsersByLastName1",
            query="SELECT u FROM User u WHERE u.lastName1 = :lastName1 order by u.lastName1, u.lastName2, u.name"
    ),
    @NamedQuery(
            name="findUsersByLastName2",
            query="SELECT u FROM User u WHERE u.lastName2 = :lastName2 order by u.lastName1, u.lastName2, u.name"
    ),
    @NamedQuery(
            name="findUsersByDepartment",
            query="SELECT u FROM User u WHERE u.department = :department order by u.lastName1, u.lastName2, u.name"
    ),
    @NamedQuery(
            name="findUserByLogin",
            query="SELECT u FROM User u WHERE u.credential.login = :login AND u.credential.password = :password"
    )
})
public class User implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @OneToOne(cascade = CascadeType.ALL)
    @MapsId
    private Credential credential;
    private String name;
    private String lastName1;
    private String lastName2;
    @ManyToOne
    private Department department;
    @OneToMany(mappedBy="user",cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Ticket> createdTickets;
    
    public User(){
        
    }
    
    public User(String name, String lastName1, String lastName2, Credential credential, List<Ticket> createdTickets){
        this.name = name;
        this.lastName1 = lastName1;
        this.lastName2 = lastName2;
        this.credential = credential;
        this.createdTickets = createdTickets;
    }
    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastName1() {
        return lastName1;
    }

    public void setLastName1(String lastName1) {
        this.lastName1 = lastName1;
    }

    public String getLastName2() {
        return lastName2;
    }

    public void setLastName2(String lastName2) {
        this.lastName2 = lastName2;
    }

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    public Credential getCredential() {
        return credential;
    }

    public void setCredential(Credential credential) {
        this.credential = credential;
    }

    public List<Ticket> getCreatedTickets() {
        return createdTickets;
    }

    public void setCreatedTickets(List<Ticket> createdTickets) {
        this.createdTickets = createdTickets;
    }
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (credential.getLogin() != null ? credential.getLogin().hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof User)) {
            return false;
        }
        User other = (User) object;
        if ((this.credential.getLogin() == null && other.credential.getLogin() != null) || (this.credential.getLogin() != null && !this.credential.getLogin().equals(other.credential.getLogin()))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.tartangatickets.entities.User[ id=" + credential.getLogin() + " ]";
    }
}
