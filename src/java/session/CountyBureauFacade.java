/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package session;

import entity.CountyBureau;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author bob-macbook
 */
@Stateless
public class CountyBureauFacade extends AbstractFacade<CountyBureau> {
    @PersistenceContext(unitName = "BCC_BLOB_PU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public CountyBureauFacade() {
        super(CountyBureau.class);
    }
    
}
