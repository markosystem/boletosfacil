package br.com.boletos.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;

@Entity
@Table(schema = "public", name = "user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "public.seqiduser")
    @SequenceGenerator(name = "public.seqiduser", sequenceName = "public.seqiduser", allocationSize = 1)
    private Integer id;
    @Column(name = "username", nullable = false, length = 50)
    private String username;
    @Column(name = "password", nullable = false)
    private String password;
    @Column(name = "email", nullable = false)
    private String email;
    @Column(name = "remember_token")
    private String remember_token;
    @Column(name = "image")
    private String image;
    @Column(name = "situation", nullable = false)
    private Situation situation = Situation.Active;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRemember_token() {
        return remember_token;
    }

    public void setRemember_token(String remember_token) {
        this.remember_token = remember_token;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Situation getSituation() {
        return situation;
    }

    public void setSituation(Situation situation) {
        this.situation = situation;
    }

    public enum Situation {
        Active(0, "Ativo"),
        Inactive(1, "Inativo");

        private final int valor;
        private final String label;

        Situation(Integer valorOpcao, String labelOpcao) {
            valor = valorOpcao;
            label = labelOpcao;
        }

        public Integer getValor() {
            return valor;
        }

        public String getLabel() {
            return label;
        }
    }

    @JsonIgnore
    public Situation[] getSituationValues() {
        return Situation.values();
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        User user = (User) o;
        return id != null ? id.equals(user.id) : user.id == null;
    }

}
