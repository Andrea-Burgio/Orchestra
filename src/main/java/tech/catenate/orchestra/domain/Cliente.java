package tech.catenate.orchestra.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A Cliente.
 */
@Entity
@Table(name = "cliente")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Cliente implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "nome")
    private String nome;

    @Column(name = "cognome")
    private String cognome;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "cliente")
    @JsonIgnoreProperties(value = { "cliente", "corso" }, allowSetters = true)
    private Set<ClienteCorso> clienteCorsos = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Cliente id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return this.nome;
    }

    public Cliente nome(String nome) {
        this.setNome(nome);
        return this;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCognome() {
        return this.cognome;
    }

    public Cliente cognome(String cognome) {
        this.setCognome(cognome);
        return this;
    }

    public void setCognome(String cognome) {
        this.cognome = cognome;
    }

    public Set<ClienteCorso> getClienteCorsos() {
        return this.clienteCorsos;
    }

    public void setClienteCorsos(Set<ClienteCorso> clienteCorsos) {
        if (this.clienteCorsos != null) {
            this.clienteCorsos.forEach(i -> i.setCliente(null));
        }
        if (clienteCorsos != null) {
            clienteCorsos.forEach(i -> i.setCliente(this));
        }
        this.clienteCorsos = clienteCorsos;
    }

    public Cliente clienteCorsos(Set<ClienteCorso> clienteCorsos) {
        this.setClienteCorsos(clienteCorsos);
        return this;
    }

    public Cliente addClienteCorso(ClienteCorso clienteCorso) {
        this.clienteCorsos.add(clienteCorso);
        clienteCorso.setCliente(this);
        return this;
    }

    public Cliente removeClienteCorso(ClienteCorso clienteCorso) {
        this.clienteCorsos.remove(clienteCorso);
        clienteCorso.setCliente(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Cliente)) {
            return false;
        }
        return getId() != null && getId().equals(((Cliente) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Cliente{" +
            "id=" + getId() +
            ", nome='" + getNome() + "'" +
            ", cognome='" + getCognome() + "'" +
            "}";
    }
}
