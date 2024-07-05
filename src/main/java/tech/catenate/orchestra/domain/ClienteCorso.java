package tech.catenate.orchestra.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.io.Serializable;

/**
 * A ClienteCorso.
 */
@Entity
@Table(name = "cliente_corso")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ClienteCorso implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "mese")
    private Integer mese;

    @Column(name = "pagato")
    private Boolean pagato;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "clienteCorsos" }, allowSetters = true)
    private Cliente cliente;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "concertos", "insegnanteCorsos", "clienteCorsos" }, allowSetters = true)
    private Corso corso;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public ClienteCorso id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getMese() {
        return this.mese;
    }

    public ClienteCorso mese(Integer mese) {
        this.setMese(mese);
        return this;
    }

    public void setMese(Integer mese) {
        this.mese = mese;
    }

    public Boolean getPagato() {
        return this.pagato;
    }

    public ClienteCorso pagato(Boolean pagato) {
        this.setPagato(pagato);
        return this;
    }

    public void setPagato(Boolean pagato) {
        this.pagato = pagato;
    }

    public Cliente getCliente() {
        return this.cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public ClienteCorso cliente(Cliente cliente) {
        this.setCliente(cliente);
        return this;
    }

    public Corso getCorso() {
        return this.corso;
    }

    public void setCorso(Corso corso) {
        this.corso = corso;
    }

    public ClienteCorso corso(Corso corso) {
        this.setCorso(corso);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ClienteCorso)) {
            return false;
        }
        return getId() != null && getId().equals(((ClienteCorso) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ClienteCorso{" +
            "id=" + getId() +
            ", mese=" + getMese() +
            ", pagato='" + getPagato() + "'" +
            "}";
    }
}
