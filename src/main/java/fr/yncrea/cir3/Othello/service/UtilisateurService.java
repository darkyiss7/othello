package fr.yncrea.cir3.Othello.service;

import fr.yncrea.cir3.Othello.domain.Utilisateur;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

@Service
public class UtilisateurService {
    @PersistenceContext
    private EntityManager em;

    @Transactional
    public void register() {
        Utilisateur u = new Utilisateur();
        u.setUsername("clzga");
        u.setEmail("theo@live.fr");
        u.setPassword("idinahui");

        em.persist(u);
    }
}
