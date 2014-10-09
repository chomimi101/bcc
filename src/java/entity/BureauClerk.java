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
@Table(name = "bureau_clerk")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "BureauClerk.findAll", query = "SELECT b FROM BureauClerk b"),
    @NamedQuery(name = "BureauClerk.findByBureauClerkId", query = "SELECT b FROM BureauClerk b WHERE b.bureauClerkId = :bureauClerkId"),
    @NamedQuery(name = "BureauClerk.findByName", query = "SELECT b FROM BureauClerk b WHERE b.name = :name"),
    @NamedQuery(name = "BureauClerk.findByPassword", query = "SELECT b FROM BureauClerk b WHERE b.password = :password")})
public class BureauClerk implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "bureau_clerk_id")
    private Integer bureauClerkId;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "name")
    private String name;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "password")
    private String password;
    @JoinColumn(name = "county_bureau_id", referencedColumnName = "county_bureau_id")
    @ManyToOne(optional = false)
    private CountyBureau countyBureauId;

    public BureauClerk() {
    }

    public BureauClerk(Integer bureauClerkId) {
        this.bureauClerkId = bureauClerkId;
    }

    public BureauClerk(Integer bureauClerkId, String name, String password) {
        this.bureauClerkId = bureauClerkId;
        this.name = name;
        this.password = password;
    }

    public Integer getBureauClerkId() {
        return bureauClerkId;
    }

    public void setBureauClerkId(Integer bureauClerkId) {
        this.bureauClerkId = bureauClerkId;
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

    public CountyBureau getCountyBureauId() {
        return countyBureauId;
    }

    public void setCountyBureauId(CountyBureau countyBureauId) {
        this.countyBureauId = countyBureauId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (bureauClerkId != null ? bureauClerkId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof BureauClerk)) {
            return false;
        }
        BureauClerk other = (BureauClerk) object;
        if ((this.bureauClerkId == null && other.bureauClerkId != null) || (this.bureauClerkId != null && !this.bureauClerkId.equals(other.bureauClerkId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.BureauClerk[ bureauClerkId=" + bureauClerkId + " ]";
    }
    
}
