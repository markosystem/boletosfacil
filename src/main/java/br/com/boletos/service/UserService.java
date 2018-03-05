package br.com.boletos.service;

import br.com.boletos.model.User;
import br.com.boletos.model.persistence.DAO;
import org.springframework.stereotype.Component;

import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.validation.ValidationException;
import java.util.List;

@Component
public class UserService extends DAO<User> {

    /**
     * Método que busca um usuario no banco de dados a partir do username e password do usuario
     *
     * @param username
     * @param password
     * @return
     */
    public User getSingleByUsernameAndPassword(String username, String password) {
        Query query = this.em.createQuery("SELECT u FROM User u WHERE u.username LIKE ?1 AND u.password LIKE ?2");
        query.setParameter(1, username);
        query.setParameter(2, password);
        query.setMaxResults(1);
        return (User) searchFirst(query);
    }

    /**
     * Método que busca uma lista de usuarios do banco de dados
     *
     * @return
     */
    public List<User> getAll() {
        return this.find("SELECT u FROM User u");
    }


    /**
     * Método que persiste um usuario no banco de dados
     *
     * @param user
     * @return
     * @throws Exception
     */
    public User save(User user) throws Exception {
        if (getSingleByUsername(user.getUsername()) != null)
            throw new ValidationException("O 'username' informado já está em uso!");
        user.setPassword(AuthService.generateHash(user.getPassword()));
        return super.save(user);
    }

    /**
     * Método que busca um usuário a partir do seu id
     *
     * @param idUser
     * @return
     */
    public User getSingleById(Integer idUser) {
        TypedQuery<User> query = em.createQuery("SELECT u FROM User u WHERE u.id = ?1", User.class);
        query.setParameter(1, idUser);
        query.setMaxResults(1);
        return (User) super.searchFirst(query);
    }

    /**
     * Método que busca um usuário a partir do seu username
     *
     * @param username
     * @return
     */
    public User getSingleByUsername(String username) {
        TypedQuery<User> query = em.createQuery("SELECT u FROM User u WHERE u.username LIKE ?1", User.class);
        query.setParameter(1, username);
        query.setMaxResults(1);
        return (User) searchFirst(query);
    }

    /**
     * Método que retorna todas as situações dos usuarios
     *
     * @return
     */
    public User.Situation[] getAllSituations() {
        return User.Situation.values();
    }
}