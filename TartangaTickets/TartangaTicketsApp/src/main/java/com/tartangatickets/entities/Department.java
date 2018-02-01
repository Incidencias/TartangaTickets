/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tartangatickets.entities;

import com.sun.istack.internal.NotNull;
import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *The Department class encapsulates the data of each department:
 * <ul>
 *  <li><stron>code</strong> is the identifier of the department.</li>
 *  <li><stron>name</strong> is the name of the department.</li>
 * </ul>
 * 
 * @author Iker Jon Mediavilla
 */
@Entity(name="Department")
@Table(name="departments", schema="tartanga_ticket_db")
@NamedQueries({
    @NamedQuery(
            name="findAllDepartmentsOrderByName",
            query="SELECT d FROM Department d order by d.name"
    ),
    @NamedQuery(
            name="findDepartmentsByCodeOrderByName",
            query="SELECT d FROM Department d where d.code = :code order by d.name"
    ),
    @NamedQuery(
            name="findDepartmentsByNameOrderByName",
            query="SELECT d FROM Department d where d.name = :name order by d.name"
    )
})



public class Department implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    private String code;
    @NotNull
    private String name;
    
    public Department() {
    }
    
    public Department(String code, String name){
        this.code = code;
        this.name = name;
    }
    
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (code != null ? code.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Department)) {
            return false;
        }
        Department other = (Department) object;
        if ((this.code == null && other.code != null) || (this.code != null && !this.code.equals(other.code))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.tartangatickets.entities.Department[ code=" + code + " ]";
    }
    
}
