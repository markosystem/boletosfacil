package br.com.boletos.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(schema = "public", name = "marking")
public class Marking {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "public.seqidmarking")
    @SequenceGenerator(name = "public.seqidmarking", sequenceName = "public.seqidmarking", allocationSize = 1)
    private Integer id;
    @Column(name = "name", nullable = false, length = 50)
    private String name;
    @Column(name = "dateadd", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateAdd;
    @Column(name = "dateedit", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateEdit;
    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "iduser", nullable = false)
    private User user;
    @Column(name = "situation", nullable = false)
    private Situation situation;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getDateAdd() {
        return dateAdd;
    }

    public void setDateAdd(Date dateAdd) {
        this.dateAdd = dateAdd;
    }

    public Date getDateEdit() {
        return dateEdit;
    }

    public void setDateEdit(Date dateEdit) {
        this.dateEdit = dateEdit;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Situation getSituation() {
        return situation;
    }

    public void setSituation(Situation situation) {
        this.situation = situation;
    }

    public enum Situation {
        Active(0, "Ativa"),
        Inactive(1, "Inativa");

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
        Marking user = (Marking) o;
        return id != null ? id.equals(user.id) : user.id == null;
    }

}
