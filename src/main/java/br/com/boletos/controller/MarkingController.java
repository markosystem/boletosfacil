package br.com.boletos.controller;

import br.com.boletos.application.Messages;
import br.com.boletos.application.ResponseObject;
import br.com.boletos.model.Marking;
import br.com.boletos.model.Ticket;
import br.com.boletos.service.AuthService;
import br.com.boletos.service.MarkingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ValidationException;


@RestController
public class MarkingController {
    @Autowired
    private MarkingService markingService;

    /**
     * Método que salva uma marcação nova
     *
     * @param marking
     * @return
     */
    @PostMapping("api/markings")
    public ResponseObject saveMarking(@RequestBody Marking marking, HttpServletRequest request) {
        try {
            if (!AuthService.isLoggedIn(request))
                return AuthService.responseNotAuthorized();
            return new ResponseObject(200, Messages.MESSAGE_SUCCESS, markingService.save(null, marking, request.getHeader("token")));
        } catch (ValidationException e) {
            return new ResponseObject(406, e.getMessage(), null);
        } catch (Exception e) {
            return new ResponseObject(500, Messages.MESSAGE_FAIL, null);
        }
    }

    /**
     * Método que retorna uma marcação a partir do seu id
     *
     * @param idMarking
     * @return
     */
    @GetMapping("api/markings/{idMarking}")
    public ResponseObject getMarkingById(@PathVariable Integer idMarking, HttpServletRequest request) {
        try {
            return new ResponseObject(200, Messages.MESSAGE_SUCCESS, markingService.getSingleById(idMarking, request.getHeader("token")));
        } catch (Exception e) {
            return new ResponseObject(406, Messages.MESSAGE_FAIL, null);
        }
    }

    /**
     * Método que retorna uma lista de marcações do usuário logado
     *
     * @return
     */
    @GetMapping("api/markings")
    public ResponseObject getMarkingsByUser(HttpServletRequest request) {
        try {
            if (!AuthService.isLoggedIn(request))
                return AuthService.responseNotAuthorized();
            return new ResponseObject(200, Messages.MESSAGE_SUCCESS, markingService.getAllByIdUser(request.getHeader("token")));
        } catch (Exception e) {
            return new ResponseObject(500, Messages.MESSAGE_FAIL, null);
        }
    }

    /**
     * Método que salva uma marcação existente
     *
     * @param idMarking
     * @param marking
     * @return
     */
    @PutMapping("api/markings/{idMarking}")
    public ResponseObject saveMarking(@PathVariable Integer idMarking, @RequestBody Marking marking, HttpServletRequest request) {
        try {
            return new ResponseObject(200, Messages.MESSAGE_SUCCESS, markingService.save(idMarking, marking, request.getHeader("token")));
        } catch (ValidationException e) {
            return new ResponseObject(406, e.getMessage(), null);
        } catch (Exception e) {
            return new ResponseObject(500, Messages.MESSAGE_FAIL, null);
        }
    }

    /**
     * Método que apaga uma marcação a partir do seu id
     *
     * @param idMarking
     * @return
     */
    @DeleteMapping("api/markings/{idMarking}")
    public ResponseObject removeMarkingById(@PathVariable Integer idMarking, HttpServletRequest request) {
        try {
            return new ResponseObject(200, Messages.MESSAGE_SUCCESS, markingService.delete(idMarking, request.getHeader("token")));
        } catch (ValidationException e) {
            return new ResponseObject(406, e.getMessage(), null);
        } catch (Exception e) {
            return new ResponseObject(500, Messages.MESSAGE_FAIL, null);
        }
    }

    /**
     * Método que altera a situação do boleto a partir do seu id
     *
     * @param idMarking
     * @param situacao
     * @return
     */
    @PutMapping("api/markings/{idMarking}/{situacao}")
    public ResponseObject saveMarkingSituacao(@PathVariable Integer idMarking, @PathVariable Integer situacao, HttpServletRequest request) {
        try {
            Marking marking = markingService.getSingleById(idMarking, request.getHeader("token"));
            marking.setSituation(Marking.Situation.values()[situacao]);
            return new ResponseObject(200, Messages.MESSAGE_SUCCESS, markingService.save(null, marking, request.getHeader("token")));
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
    @GetMapping("api/markings/situations")
    public ResponseObject getSituations() {
        try {
            return new ResponseObject(200, Messages.MESSAGE_SUCCESS, markingService.getAllSituations());
        } catch (Exception e) {
            return new ResponseObject(500, Messages.MESSAGE_FAIL, null);
        }
    }
}