package lab3.service;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import lab3.persistence.CalcResult;

@Stateless
public class CalcResultService {

    @PersistenceContext
    private EntityManager entityManager;

    public void create(CalcResult calcResult) {
        entityManager.persist(calcResult);
    }

    public List<CalcResult> list() {
        return entityManager
            .createQuery("FROM CalcResult m order by m.id desc", CalcResult.class)
            .getResultList();
    }
}
