/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package session;

import entity.TownOffice;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author bob-macbook
 */
@Stateless
public class TownOfficeFacade extends AbstractFacade<TownOffice> {
    @PersistenceContext(unitName = "BCC_BLOB_PU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public TownOfficeFacade() {
        super(TownOffice.class);
    }
    
}
