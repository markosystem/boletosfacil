package br.com.boletos.controller;

import br.com.boletos.application.Messages;
import br.com.boletos.application.ResponseObject;
import br.com.boletos.model.Ticket;
import br.com.boletos.service.AuthService;
import br.com.boletos.service.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ValidationException;


@RestController
public class TicketController {
    @Autowired
    private TicketService ticketService;

    /**
     * Método que salva um boleto novo
     *
     * @param ticket
     * @return
     */
    @PostMapping("api/tickets")
    public ResponseObject saveTicket(@RequestBody Ticket ticket, HttpServletRequest request) {
        try {
            if (!AuthService.isLoggedIn(request))
                return AuthService.responseNotAuthorized();
            return new ResponseObject(200, Messages.MESSAGE_SUCCESS, ticketService.save(null, ticket, request.getHeader("token")));
        } catch (ValidationException e) {
            return new ResponseObject(406, e.getMessage(), null);
        } catch (Exception e) {
            return new ResponseObject(500, Messages.MESSAGE_FAIL, null);
        }
    }

    /**
     * Método que retorna um boleto a partir do seu id
     *
     * @param idTicket
     * @return
     */
    @GetMapping("api/tickets/{idTicket}")
    public ResponseObject getTicketById(@PathVariable Integer idTicket, HttpServletRequest request) {
        try {
            return new ResponseObject(200, Messages.MESSAGE_SUCCESS, ticketService.getSingleById(idTicket, request.getHeader("token")));
        } catch (ValidationException e) {
            return new ResponseObject(406, e.getMessage(), null);
        } catch (Exception e) {
            return new ResponseObject(500, Messages.MESSAGE_FAIL, null);
        }
    }

    /**
     * Método que retorna uma lista de boletos do usuário logado
     *
     * @return
     */
    @GetMapping("api/tickets")
    public ResponseObject getTicketsByUser(HttpServletRequest request) {
        try {
            if (!AuthService.isLoggedIn(request))
                return AuthService.responseNotAuthorized();
            return new ResponseObject(200, Messages.MESSAGE_SUCCESS, ticketService.getAllByIdUser(request.getHeader("token")));
        } catch (Exception e) {
            return new ResponseObject(500, Messages.MESSAGE_FAIL, null);
        }
    }

    /**
     * Método que salva um boleto existente
     *
     * @param idTicket
     * @param ticket
     * @return
     */
    @PutMapping("api/tickets/{idTicket}")
    public ResponseObject saveTicket(@PathVariable Integer idTicket, @RequestBody Ticket ticket, HttpServletRequest request) {
        try {
            return new ResponseObject(200, Messages.MESSAGE_SUCCESS, ticketService.save(idTicket, ticket, request.getHeader("token")));
        } catch (ValidationException e) {
            return new ResponseObject(406, e.getMessage(), null);
        } catch (Exception e) {
            return new ResponseObject(500, Messages.MESSAGE_FAIL, null);
        }
    }

    /**
     * Método que apaga um boleto a partir do seu id
     *
     * @param idTicket
     * @return
     */
    @DeleteMapping("api/tickets/{idTicket}")
    public ResponseObject removeTicketById(@PathVariable Integer idTicket, HttpServletRequest request) {
        try {
            return new ResponseObject(200, Messages.MESSAGE_SUCCESS, ticketService.delete(idTicket, request.getHeader("token")));
        } catch (Exception e) {
            return new ResponseObject(500, Messages.MESSAGE_FAIL, null);
        }
    }

    /**
     * Método que altera a situação do boleto a partir do seu id
     *
     * @param idTicket
     * @param situacao
     * @return
     */
    @PutMapping("api/tickets/{idTicket}/{situacao}")
    public ResponseObject saveTicketSituacao(@PathVariable Integer idTicket, @PathVariable Integer situacao, HttpServletRequest request) {
        try {
            Ticket ticket = ticketService.getSingleById(idTicket, request.getHeader("token"));
            ticket.setSituation(Ticket.Situation.values()[situacao]);
            return new ResponseObject(200, Messages.MESSAGE_SUCCESS, ticketService.save(null, ticket, request.getHeader("token")));
        } catch (ValidationException e) {
            return new ResponseObject(406, e.getMessage(), null);
        } catch (Exception e) {
            return new ResponseObject(500, Messages.MESSAGE_FAIL, null);
        }
    }

    /**
     * Método que retorna todas as situações das marcações
     *
     * @return
     */
    @GetMapping("api/tickets/situations")
    public ResponseObject getSituations() {
        try {
            return new ResponseObject(200, Messages.MESSAGE_SUCCESS, ticketService.getAllSituations());
        } catch (Exception e) {
            return new ResponseObject(500, Messages.MESSAGE_FAIL, null);
        }
    }
}