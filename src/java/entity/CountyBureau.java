/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author bob-macbook
 */
@Entity
@Table(name = "county_bureau")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "CountyBureau.findAll", query = "SELECT c FROM CountyBureau c"),
    @NamedQuery(name = "CountyBureau.findByCountyBureauId", query = "SELECT c FROM CountyBureau c WHERE c.countyBureauId = :countyBureauId"),
    @NamedQuery(name = "CountyBureau.findByName", query = "SELECT c FROM CountyBureau c WHERE c.name = :name")})
public class CountyBureau implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "county_bureau_id")
    private Integer countyBureauId;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "name")
    private String name;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "countyBureauId")
    private Collection<Business> businessCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "countyBureauId")
    private Collection<BureauClerk> bureauClerkCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "countyBureauId")
    private Collection<TownOffice> townOfficeCollection;

    public CountyBureau() {
    }

    public CountyBureau(Integer countyBureauId) {
        this.countyBureauId = countyBureauId;
    }

    public CountyBureau(Integer countyBureauId, String name) {
        this.countyBureauId = countyBureauId;
        this.name = name;
    }

    public Integer getCountyBureauId() {
        return countyBureauId;
    }

    public void setCountyBureauId(Integer countyBureauId) {
        this.countyBureauId = countyBureauId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @XmlTransient
    public Collection<Business> getBusinessCollection() {
        return businessCollection;
    }

    public void setBusinessCollection(Collection<Business> businessCollection) {
        this.businessCollection = businessCollection;
    }

    @XmlTransient
    public Collection<BureauClerk> getBureauClerkCollection() {
        return bureauClerkCollection;
    }

    public void setBureauClerkCollection(Collection<BureauClerk> bureauClerkCollection) {
        this.bureauClerkCollection = bureauClerkCollection;
    }

    @XmlTransient
    public Collection<TownOffice> getTownOfficeCollection() {
        return townOfficeCollection;
    }

    public void setTownOfficeCollection(Collection<TownOffice> townOfficeCollection) {
        this.townOfficeCollection = townOfficeCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (countyBureauId != null ? countyBureauId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CountyBureau)) {
            return false;
        }
        CountyBureau other = (CountyBureau) object;
        if ((this.countyBureauId == null && other.countyBureauId != null) || (this.countyBureauId != null && !this.countyBureauId.equals(other.countyBureauId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.CountyBureau[ countyBureauId=" + countyBureauId + " ]";
    }
    
}
