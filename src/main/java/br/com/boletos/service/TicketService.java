package br.com.boletos.service;

import br.com.boletos.application.Messages;
import br.com.boletos.model.Ticket;
import br.com.boletos.model.persistence.DAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.xml.bind.ValidationException;
import java.util.Date;
import java.util.List;

@Component
public class TicketService extends DAO<Ticket> {

    @Autowired
    private UserService userService;

    /**
     * Método que busca uma lista de boletos no banco de dados, a partir do usuário logado
     *
     * @return
     */
    public List<Ticket> getAllByIdUser(String hashJwt) {
        TypedQuery<Ticket> query = em.createQuery("SELECT t FROM Ticket t WHERE t.user.id = ?1", Ticket.class);
        query.setParameter(1, AuthService.getIdUserLogged(hashJwt));
        return query.getResultList();
    }

    /**
     * Método que busca um boleto no banco de dados
     *
     * @param idBoleto
     * @return
     */
    public Ticket getSingleById(Integer idBoleto, String hashJwt) throws Exception {
        TypedQuery<Ticket> query = em.createQuery("SELECT t FROM Ticket t WHERE t.id = ?1 AND t.user.id = ?2", Ticket.class);
        query.setParameter(1, idBoleto);
        query.setParameter(2, AuthService.getIdUserLogged(hashJwt));
        query.setMaxResults(1);
        Ticket ticket = (Ticket) searchFirst(query);
        if (ticket == null)
            throw new ValidationException(Messages.MESSAGE_NOTREGISTER);
        return ticket;
    }

    /**
     * Método que verifica se é um boleto novo ou antigo, realiza alterações e chama o método saveEntity
     *
     * @param idBoleto
     * @param ticket
     * @return
     * @throws Exception
     */
    public Ticket save(Integer idBoleto, Ticket ticket, String hashJwt) throws Exception {
        if (idBoleto != null) {
            Ticket ticketBanco = getSingleById(idBoleto, hashJwt);
            ticketBanco.setTitle(ticket.getTitle());
            ticketBanco.setBarcode(ticket.getBarcode());
            ticketBanco.setSituation(ticket.getSituation());
            ticketBanco.setDateEdit(new Date());
            ticketBanco.setDateDue(ticket.getDateDue());
            ticket = ticketBanco;
        }
        return saveEntity(ticket, hashJwt);
    }

    /**
     * Método que persiste o boleto no banco de dados
     *
     * @param ticket
     * @return
     * @throws Exception
     */
    public Ticket saveEntity(Ticket ticket, String hashJwt) throws Exception {
        if (ticket.getId() == null)
            ticket.setDateAdd(new Date());
        ticket.setDateEdit(new Date());
        ticket.setUser(userService.getSingleById(AuthService.getIdUserLogged(hashJwt)));
        return super.save(ticket);
    }

    /**
     * Método que exclui o boleto do banco de dados
     *
     * @param idBoleto
     * @return
     * @throws Exception
     */
    public Ticket delete(Integer idBoleto, String hashJwt) throws Exception {
        Ticket ticket = getSingleById(idBoleto, hashJwt);
        super.remove(ticket);
        return null;
    }

    /**
     * Meetodo que soma quantos boletos existe para uma macarção
     *
     * @param idMarketing
     * @param hashJwt
     * @return
     */
    public Long countByMarketing(Integer idMarketing, String hashJwt) {
        Query query = em.createQuery("SELECT count (t) FROM Ticket t WHERE t.marking.id = ?1 AND t.user.id = ?2 AND t.marking.user.id = ?2 ", Ticket.class);
        query.setParameter(1, idMarketing);
        query.setParameter(2, AuthService.getIdUserLogged(hashJwt));
        query.setMaxResults(1);
        return (Long) query.getSingleResult();
    }

    /**
     * Método que retorna todas as situações dos boletos
     *
     * @return
     */
    public Ticket.Situation[] getAllSituations() {
        return Ticket.Situation.values();
    }

}
