package tech.catenate.orchestra.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A Corso.
 */
@Entity
@Table(name = "corso")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Corso implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "anno")
    private Integer anno;

    @Column(name = "nome")
    private String nome;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "corso")
    @JsonIgnoreProperties(value = { "corso", "fotos", "filmatoes" }, allowSetters = true)
    private Set<Concerto> concertos = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "corso")
    @JsonIgnoreProperties(value = { "insegnante", "corso" }, allowSetters = true)
    private Set<InsegnanteCorso> insegnanteCorsos = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "corso")
    @JsonIgnoreProperties(value = { "cliente", "corso" }, allowSetters = true)
    private Set<ClienteCorso> clienteCorsos = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Corso id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getAnno() {
        return this.anno;
    }

    public Corso anno(Integer anno) {
        this.setAnno(anno);
        return this;
    }

    public void setAnno(Integer anno) {
        this.anno = anno;
    }

    public String getNome() {
        return this.nome;
    }

    public Corso nome(String nome) {
        this.setNome(nome);
        return this;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Set<Concerto> getConcertos() {
        return this.concertos;
    }

    public void setConcertos(Set<Concerto> concertos) {
        if (this.concertos != null) {
            this.concertos.forEach(i -> i.setCorso(null));
        }
        if (concertos != null) {
            concertos.forEach(i -> i.setCorso(this));
        }
        this.concertos = concertos;
    }

    public Corso concertos(Set<Concerto> concertos) {
        this.setConcertos(concertos);
        return this;
    }

    public Corso addConcerto(Concerto concerto) {
        this.concertos.add(concerto);
        concerto.setCorso(this);
        return this;
    }

    public Corso removeConcerto(Concerto concerto) {
        this.concertos.remove(concerto);
        concerto.setCorso(null);
        return this;
    }

    public Set<InsegnanteCorso> getInsegnanteCorsos() {
        return this.insegnanteCorsos;
    }

    public void setInsegnanteCorsos(Set<InsegnanteCorso> insegnanteCorsos) {
        if (this.insegnanteCorsos != null) {
            this.insegnanteCorsos.forEach(i -> i.setCorso(null));
        }
        if (insegnanteCorsos != null) {
            insegnanteCorsos.forEach(i -> i.setCorso(this));
        }
        this.insegnanteCorsos = insegnanteCorsos;
    }

    public Corso insegnanteCorsos(Set<InsegnanteCorso> insegnanteCorsos) {
        this.setInsegnanteCorsos(insegnanteCorsos);
        return this;
    }

    public Corso addInsegnanteCorso(InsegnanteCorso insegnanteCorso) {
        this.insegnanteCorsos.add(insegnanteCorso);
        insegnanteCorso.setCorso(this);
        return this;
    }

    public Corso removeInsegnanteCorso(InsegnanteCorso insegnanteCorso) {
        this.insegnanteCorsos.remove(insegnanteCorso);
        insegnanteCorso.setCorso(null);
        return this;
    }

    public Set<ClienteCorso> getClienteCorsos() {
        return this.clienteCorsos;
    }

    public void setClienteCorsos(Set<ClienteCorso> clienteCorsos) {
        if (this.clienteCorsos != null) {
            this.clienteCorsos.forEach(i -> i.setCorso(null));
        }
        if (clienteCorsos != null) {
            clienteCorsos.forEach(i -> i.setCorso(this));
        }
        this.clienteCorsos = clienteCorsos;
    }

    public Corso clienteCorsos(Set<ClienteCorso> clienteCorsos) {
        this.setClienteCorsos(clienteCorsos);
        return this;
    }

    public Corso addClienteCorso(ClienteCorso clienteCorso) {
        this.clienteCorsos.add(clienteCorso);
        clienteCorso.setCorso(this);
        return this;
    }

    public Corso removeClienteCorso(ClienteCorso clienteCorso) {
        this.clienteCorsos.remove(clienteCorso);
        clienteCorso.setCorso(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Corso)) {
            return false;
        }
        return getId() != null && getId().equals(((Corso) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Corso{" +
            "id=" + getId() +
            ", anno=" + getAnno() +
            ", nome='" + getNome() + "'" +
            "}";
    }
}
