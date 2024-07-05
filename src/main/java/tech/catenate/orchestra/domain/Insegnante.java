package tech.catenate.orchestra.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A Insegnante.
 */
@Entity
@Table(name = "insegnante")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Insegnante implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "nome")
    private String nome;

    @Column(name = "cognome")
    private String cognome;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "insegnante")
    @JsonIgnoreProperties(value = { "insegnante", "corso" }, allowSetters = true)
    private Set<InsegnanteCorso> insegnanteCorsos = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Insegnante id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return this.nome;
    }

    public Insegnante nome(String nome) {
        this.setNome(nome);
        return this;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCognome() {
        return this.cognome;
    }

    public Insegnante cognome(String cognome) {
        this.setCognome(cognome);
        return this;
    }

    public void setCognome(String cognome) {
        this.cognome = cognome;
    }

    public Set<InsegnanteCorso> getInsegnanteCorsos() {
        return this.insegnanteCorsos;
    }

    public void setInsegnanteCorsos(Set<InsegnanteCorso> insegnanteCorsos) {
        if (this.insegnanteCorsos != null) {
            this.insegnanteCorsos.forEach(i -> i.setInsegnante(null));
        }
        if (insegnanteCorsos != null) {
            insegnanteCorsos.forEach(i -> i.setInsegnante(this));
        }
        this.insegnanteCorsos = insegnanteCorsos;
    }

    public Insegnante insegnanteCorsos(Set<InsegnanteCorso> insegnanteCorsos) {
        this.setInsegnanteCorsos(insegnanteCorsos);
        return this;
    }

    public Insegnante addInsegnanteCorso(InsegnanteCorso insegnanteCorso) {
        this.insegnanteCorsos.add(insegnanteCorso);
        insegnanteCorso.setInsegnante(this);
        return this;
    }

    public Insegnante removeInsegnanteCorso(InsegnanteCorso insegnanteCorso) {
        this.insegnanteCorsos.remove(insegnanteCorso);
        insegnanteCorso.setInsegnante(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Insegnante)) {
            return false;
        }
        return getId() != null && getId().equals(((Insegnante) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Insegnante{" +
            "id=" + getId() +
            ", nome='" + getNome() + "'" +
            ", cognome='" + getCognome() + "'" +
            "}";
    }
}
