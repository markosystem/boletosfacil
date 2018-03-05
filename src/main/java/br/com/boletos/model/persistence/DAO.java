package br.com.boletos.model.persistence;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;

public class DAO<T> {

    public EntityManager em = JPAFactory.getEntityManager();


    public T save(T t) throws Exception {
        try {
            em.getTransaction().begin();
            T entity = em.merge(t);
            em.getTransaction().commit();
            return entity;
        } catch (Exception e) {
            if (em.getTransaction().isActive())
                em.getTransaction().rollback();
            throw e;
        }
    }

    public List<T> saveAll(List<T> listT) throws Exception {
        try {
            em.getTransaction().begin();
            int i = 0;
            for (T entity : listT) {
                em.merge(entity);
                if (i % 200 == 0) {
                    em.flush();
                    em.clear();
                }
            }
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive())
                em.getTransaction().rollback();
            throw e;
        }
        return listT;
    }

    public List<T> find(String query) {
        return em.createQuery(query).getResultList();
    }

    public T findFirst(String query) {
        List<T> list = find(query);
        if (list.size() > 0)
            return list.get(0);
        return null;
    }

    public void remove(T t) throws Exception {
        try {
            em.getTransaction().begin();
            em.remove(t);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive())
                em.getTransaction().rollback();
            throw e;
        }
    }

    public Object searchFirst(Query query) {
        List list = query.getResultList();
        if (list == null || list.size() == 0)
            return null;
        return list.get(0);
    }
}
