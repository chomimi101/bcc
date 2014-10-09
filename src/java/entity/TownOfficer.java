/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author bob-macbook
 */
@Entity
@Table(name = "town_officer")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TownOfficer.findAll", query = "SELECT t FROM TownOfficer t"),
    @NamedQuery(name = "TownOfficer.findByTownOfficerId", query = "SELECT t FROM TownOfficer t WHERE t.townOfficerId = :townOfficerId"),
    @NamedQuery(name = "TownOfficer.findByName", query = "SELECT t FROM TownOfficer t WHERE t.name = :name"),
    @NamedQuery(name = "TownOfficer.findByPassword", query = "SELECT t FROM TownOfficer t WHERE t.password = :password")})
public class TownOfficer implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "town_officer_id")
    private Integer townOfficerId;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "name")
    private String name;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "password")
    private String password;
    @JoinColumn(name = "town_office_id", referencedColumnName = "town_office_id")
    @ManyToOne(optional = false)
    private TownOffice townOfficeId;

    public TownOfficer() {
    }

    public TownOfficer(Integer townOfficerId) {
        this.townOfficerId = townOfficerId;
    }

    public TownOfficer(Integer townOfficerId, String name, String password) {
        this.townOfficerId = townOfficerId;
        this.name = name;
        this.password = password;
    }

    public Integer getTownOfficerId() {
        return townOfficerId;
    }

    public void setTownOfficerId(Integer townOfficerId) {
        this.townOfficerId = townOfficerId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public TownOffice getTownOfficeId() {
        return townOfficeId;
    }

    public void setTownOfficeId(TownOffice townOfficeId) {
        this.townOfficeId = townOfficeId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (townOfficerId != null ? townOfficerId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TownOfficer)) {
            return false;
        }
        TownOfficer other = (TownOfficer) object;
        if ((this.townOfficerId == null && other.townOfficerId != null) || (this.townOfficerId != null && !this.townOfficerId.equals(other.townOfficerId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.TownOfficer[ townOfficerId=" + townOfficerId + " ]";
    }
    
}
