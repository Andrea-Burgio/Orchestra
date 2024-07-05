package tech.catenate.orchestra.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

/**
 * A Concerto.
 */
@Entity
@Table(name = "concerto")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Concerto implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "data")
    private LocalDate data;

    @Column(name = "nome")
    private String nome;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "concertos", "insegnanteCorsos", "clienteCorsos" }, allowSetters = true)
    private Corso corso;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "concerto")
    @JsonIgnoreProperties(value = { "concerto" }, allowSetters = true)
    private Set<Foto> fotos = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Concerto id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getData() {
        return this.data;
    }

    public Concerto data(LocalDate data) {
        this.setData(data);
        return this;
    }

    public void setData(LocalDate data) {
        this.data = data;
    }

    public String getNome() {
        return this.nome;
    }

    public Concerto nome(String nome) {
        this.setNome(nome);
        return this;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Corso getCorso() {
        return this.corso;
    }

    public void setCorso(Corso corso) {
        this.corso = corso;
    }

    public Concerto corso(Corso corso) {
        this.setCorso(corso);
        return this;
    }

    public Set<Foto> getFotos() {
        return this.fotos;
    }

    public void setFotos(Set<Foto> fotos) {
        if (this.fotos != null) {
            this.fotos.forEach(i -> i.setConcerto(null));
        }
        if (fotos != null) {
            fotos.forEach(i -> i.setConcerto(this));
        }
        this.fotos = fotos;
    }

    public Concerto fotos(Set<Foto> fotos) {
        this.setFotos(fotos);
        return this;
    }

    public Concerto addFoto(Foto foto) {
        this.fotos.add(foto);
        foto.setConcerto(this);
        return this;
    }

    public Concerto removeFoto(Foto foto) {
        this.fotos.remove(foto);
        foto.setConcerto(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Concerto)) {
            return false;
        }
        return getId() != null && getId().equals(((Concerto) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Concerto{" +
            "id=" + getId() +
            ", data='" + getData() + "'" +
            ", nome='" + getNome() + "'" +
            "}";
    }
}
