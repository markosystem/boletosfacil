package br.com.boletos.service;

import br.com.boletos.model.Marking;
import br.com.boletos.model.persistence.DAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.persistence.TypedQuery;
import javax.validation.ValidationException;
import java.util.Date;
import java.util.List;

@Component
public class MarkingService extends DAO<Marking> {

    @Autowired
    private UserService userService;
    @Autowired
    private TicketService ticketService;

    /**
     * Método que busca uma lista de Marcações no banco de dados, a partir do usuário logado
     *
     * @return
     */
    public List<Marking> getAllByIdUser(String hashJwt) {
        TypedQuery<Marking> query = em.createQuery("SELECT m FROM Marking m WHERE m.user.id = ?1", Marking.class);
        query.setParameter(1, AuthService.getIdUserLogged(hashJwt));
        return query.getResultList();
    }

    /**
     * Método que busca uma marcação no banco de dados
     *
     * @param idMarking
     * @return
     */
    public Marking getSingleById(Integer idMarking, String hashJwt) {
        TypedQuery<Marking> query = em.createQuery("SELECT m FROM Marking m WHERE m.id = ?1 AND m.user.id = ?2", Marking.class);
        query.setParameter(1, idMarking);
        query.setParameter(2, AuthService.getIdUserLogged(hashJwt));
        query.setMaxResults(1);
        return (Marking) searchFirst(query);
    }

    /**
     * Método que verifica se é uma marcação nova ou antiga, realiza alterações e chama o método saveEntity
     *
     * @param idMarking
     * @param marking
     * @return
     * @throws Exception
     */
    public Marking save(Integer idMarking, Marking marking, String hashJwt) throws Exception {
        if (idMarking != null) {
            Marking markingBanco = getSingleById(idMarking, hashJwt);
            markingBanco.setName(marking.getName());
            markingBanco.setSituation(marking.getSituation());
            markingBanco.setDateEdit(new Date());
            marking = markingBanco;
        }
        Marking markingExist = getSingleByName(marking.getName(), hashJwt);
        if (markingExist != null && marking.getId() != markingExist.getId())
            throw new ValidationException("O 'name' informado já está em uso!");
        return saveEntity(marking, hashJwt);
    }

    /**
     * Método que persiste a marcação no banco de dados
     *
     * @param marking
     * @return
     * @throws Exception
     */
    public Marking saveEntity(Marking marking, String hashJwt) throws Exception {
        if (marking.getId() == null)
            marking.setDateAdd(new Date());
        marking.setDateEdit(new Date());
        marking.setUser(userService.getSingleById(AuthService.getIdUserLogged(hashJwt)));
        return super.save(marking);
    }

    /**
     * Método que exclui o boleto do banco de dados
     *
     * @param idMarking
     * @return
     * @throws Exception
     */
    public Marking delete(Integer idMarking, String hashJwt) throws Exception {
        Marking markingCount = getSingleById(idMarking, hashJwt);
        if (markingCount == null)
            throw new ValidationException("Nenhuma marcação foi encontrada com Identificador " + idMarking + "!");
        Long cont = ticketService.countByMarketing(markingCount.getId(), hashJwt);
        if (cont > 0)
            throw new ValidationException("A marcação não poderá ser excluída, pois está vinculada a Boletos!");
        super.remove(markingCount);
        return null;
    }

    /**
     * Método que busca uma marcação a partir do seu name
     *
     * @param name
     * @return
     */
    public Marking getSingleByName(String name, String hashJwt) {
        TypedQuery<Marking> query = em.createQuery("SELECT m FROM Marking m WHERE m.name LIKE ?1 AND m.user.id = ?2 ", Marking.class);
        query.setParameter(1, name);
        query.setParameter(2, userService.getSingleById(AuthService.getIdUserLogged(hashJwt)).getId());
        query.setMaxResults(1);
        return (Marking) searchFirst(query);
    }

    /**
     * Método que retorna todas as situações das marcações
     *
     * @return
     */
    public Marking.Situation[] getAllSituations() {
        return Marking.Situation.values();
    }

}
