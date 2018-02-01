package com.tartangatickets.entities;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 *
 * @author Sergio López
 */
@Entity(name="Ticket")
@Table(name="tickets", schema="tartanga_ticket_db")
@NamedQueries({
    @NamedQuery(
            name="findAllTicket",
            query="SELECT u FROM Ticket u ORDER BY u.createDate"
    )
})
public class Ticket implements Serializable{
    
    
    private Integer id;
    private Date createDate;
    private Date endDate;
    private String machineCode;
    private String department;
    private String location;
    private State state;
    @ManyToOne
    private User user;
    @OneToMany(mappedBy="id",cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Message> messages;
    @ManyToOne
    private Technician technician;

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

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
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
    
    
    
}