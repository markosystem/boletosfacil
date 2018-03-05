package br.com.boletos.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(schema = "public", name = "ticket")
public class Ticket {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "public.seqidticket")
    @SequenceGenerator(name = "public.seqidticket", sequenceName = "public.seqidticket", allocationSize = 1)
    private Integer id;
    @Column(name = "title", nullable = false, length = 100)
    private String title;
    @Column(name = "dateadd", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateAdd;
    @Column(name = "dateedit", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateEdit;
    @Column(name = "datedue", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateDue;
    @Column(name = "datepay", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date datePay;
    @Column(name = "barcode", nullable = false, length = 50)
    private String barcode;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idmarking", nullable = false)
    private Marking marking;
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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

    public Date getDateDue() {
        return dateDue;
    }

    public void setDateDue(Date dateDue) {
        this.dateDue = dateDue;
    }

    public Date getDatePay() {
        return datePay;
    }

    public void setDatePay(Date datePay) {
        this.datePay = datePay;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public Marking getMarking() {
        return marking;
    }

    public void setMarking(Marking marking) {
        this.marking = marking;
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
        AwaitingPayment(0, "Aguardando Pagamento"),
        Paid(1, "Pago"),
        Deleted(2, "Excluido");

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
        Ticket user = (Ticket) o;
        return id != null ? id.equals(user.id) : user.id == null;
    }

}
