/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tartangatickets.entities;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import static javax.persistence.EnumType.ORDINAL;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import org.hibernate.annotations.NamedQueries;
import org.hibernate.annotations.NamedQuery;




/**
 *  Mantiene los datos que contienen los tickets
 *  <ul>
 *      <li><strong>id</strong>Número de identificación  </li>
 *      <li><strong>createDate</strong> Fecha de creación del pedido</li>
 *      <li><strong>endDate</strong> Fecha de  </li>
 *      <li><strong>repartidors</strong> Lista de repartidores {@link  gestionrepartidores.entity.Repartidor} que reparten en ese área</li>
 *      <li><strong>pedidos</strong> Collección de pedidos {@link  gestionrepartidores.entity.Pedido} existentes en ese área</li>
 *  </ul>
 *
 * @author Sergio López
 */
@Entity(name="Ticket")
@Table(name="tickets", schema="tartanga_ticket_db")
@NamedQueries({
    @NamedQuery(
            name="findAllTickets",
            query="SELECT u FROM Ticket u ORDER BY u.createDate"
    ),
    @NamedQuery(
            name="findTicketsByUser",
            query="SELECT t FROM Ticket t WHERE t.user.credential.login = :login "
                    + "ORDER BY t.createDate"
    ),
    @NamedQuery(
            name="findTicketsByTechnician",
            query="SELECT t FROM Ticket t WHERE t.technician.credential.login = :login "
                    + "ORDER BY t.createDate"
    ),
    @NamedQuery(
            name="findTicketsByState",
            query="SELECT t FROM Ticket t WHERE t.state = :state "
                    + "ORDER BY t.createDate"
    )
 })

public class Ticket implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    @Temporal(TemporalType.DATE)
    private Date createDate;
    @Temporal(TemporalType.DATE)
    private Date endDate;
    private String machineCode;
    @ManyToOne
    private Department department;
    private String location;
    @Enumerated(ORDINAL)
    private State state;
    @ManyToOne
    private User user;
    @OneToMany(mappedBy ="id", cascade = CascadeType.ALL)
    private List<Message> messages;
    @ManyToOne
    private Technician technician;

    public Ticket() {
    }

    public Ticket(Date createDate, Date endDate, String machineCode, Department department, String location, State state, User user, List<Message> messages, Technician technician) {
        this.createDate = createDate;
        this.endDate = endDate;
        this.machineCode = machineCode;
        this.department = department;
        this.location = location;
        this.state = state;
        this.user = user;
        this.messages = messages;
        this.technician = technician;
    }


    
    
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getMachineCode() {
        return machineCode;
    }

    public void setMachineCode(String machineCode) {
        this.machineCode = machineCode;
    }

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<Message> getMessages() {
        return messages;
    }

    public void setMessages(List<Message> messages) {
        this.messages = messages;
    }

    public Technician getTechnician() {
        return technician;
    }

    public void setTechnician(Technician technician) {
        this.technician = technician;
    }

    public String toString(){
        return id.toString();
    }

    

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Ticket)) {
            return false;
        }
        Ticket other = (Ticket) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    } 
    
    
}
