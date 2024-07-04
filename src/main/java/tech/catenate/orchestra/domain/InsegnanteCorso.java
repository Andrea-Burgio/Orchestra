package tech.catenate.orchestra.domain;

import jakarta.persistence.*;
import java.io.Serializable;

/**
 * A InsegnanteCorso.
 */
@Entity
@Table(name = "insegnante_corso")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class InsegnanteCorso implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "mese")
    private Integer mese;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public InsegnanteCorso id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getMese() {
        return this.mese;
    }

    public InsegnanteCorso mese(Integer mese) {
        this.setMese(mese);
        return this;
    }

    public void setMese(Integer mese) {
        this.mese = mese;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof InsegnanteCorso)) {
            return false;
        }
        return getId() != null && getId().equals(((InsegnanteCorso) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "InsegnanteCorso{" +
            "id=" + getId() +
            ", mese=" + getMese() +
            "}";
    }
}
