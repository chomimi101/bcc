/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package session;

import entity.VillageAgent;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author bob-macbook
 */
@Stateless
public class VillageAgentFacade extends AbstractFacade<VillageAgent> {
    @PersistenceContext(unitName = "BCC_BLOB_PU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public VillageAgentFacade() {
        super(VillageAgent.class);
    }

    public VillageAgent findVillageAgentByName(String userAccountName) {
        Query query = em.createNamedQuery("VillageAgent.findByName");
        query.setParameter("name", userAccountName);
        try {
            VillageAgent agent = (VillageAgent) query.getSingleResult();
            return agent;
        } catch (Exception e) {
            return null;
        }
    }
    
}
