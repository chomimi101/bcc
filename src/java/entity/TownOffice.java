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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author bob-macbook
 */
@Entity
@Table(name = "town_office")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TownOffice.findAll", query = "SELECT t FROM TownOffice t"),
    @NamedQuery(name = "TownOffice.findByTownOfficeId", query = "SELECT t FROM TownOffice t WHERE t.townOfficeId = :townOfficeId"),
    @NamedQuery(name = "TownOffice.findByName", query = "SELECT t FROM TownOffice t WHERE t.name = :name")})
public class TownOffice implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "town_office_id")
    private Integer townOfficeId;
    @Size(max = 45)
    @Column(name = "name")
    private String name;
    @JoinColumn(name = "county_bureau_id", referencedColumnName = "county_bureau_id")
    @ManyToOne(optional = false)
    private CountyBureau countyBureauId;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "townOfficeId")
    private Collection<TownOfficer> townOfficerCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "townOfficeId")
    private Collection<VillageOffice> villageOfficeCollection;

    public TownOffice() {
    }

    public TownOffice(Integer townOfficeId) {
        this.townOfficeId = townOfficeId;
    }

    public Integer getTownOfficeId() {
        return townOfficeId;
    }

    public void setTownOfficeId(Integer townOfficeId) {
        this.townOfficeId = townOfficeId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public CountyBureau getCountyBureauId() {
        return countyBureauId;
    }

    public void setCountyBureauId(CountyBureau countyBureauId) {
        this.countyBureauId = countyBureauId;
    }

    @XmlTransient
    public Collection<TownOfficer> getTownOfficerCollection() {
        return townOfficerCollection;
    }

    public void setTownOfficerCollection(Collection<TownOfficer> townOfficerCollection) {
        this.townOfficerCollection = townOfficerCollection;
    }

    @XmlTransient
    public Collection<VillageOffice> getVillageOfficeCollection() {
        return villageOfficeCollection;
    }

    public void setVillageOfficeCollection(Collection<VillageOffice> villageOfficeCollection) {
        this.villageOfficeCollection = villageOfficeCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (townOfficeId != null ? townOfficeId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TownOffice)) {
            return false;
        }
        TownOffice other = (TownOffice) object;
        if ((this.townOfficeId == null && other.townOfficeId != null) || (this.townOfficeId != null && !this.townOfficeId.equals(other.townOfficeId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.TownOffice[ townOfficeId=" + townOfficeId + " ]";
    }
    
}
