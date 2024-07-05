package tech.catenate.orchestra.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static tech.catenate.orchestra.domain.InsegnanteCorsoTestSamples.*;
import static tech.catenate.orchestra.domain.InsegnanteTestSamples.*;

import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;
import tech.catenate.orchestra.web.rest.TestUtil;

class InsegnanteTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Insegnante.class);
        Insegnante insegnante1 = getInsegnanteSample1();
        Insegnante insegnante2 = new Insegnante();
        assertThat(insegnante1).isNotEqualTo(insegnante2);

        insegnante2.setId(insegnante1.getId());
        assertThat(insegnante1).isEqualTo(insegnante2);

        insegnante2 = getInsegnanteSample2();
        assertThat(insegnante1).isNotEqualTo(insegnante2);
    }

    @Test
    void insegnanteCorsoTest() {
        Insegnante insegnante = getInsegnanteRandomSampleGenerator();
        InsegnanteCorso insegnanteCorsoBack = getInsegnanteCorsoRandomSampleGenerator();

        insegnante.addInsegnanteCorso(insegnanteCorsoBack);
        assertThat(insegnante.getInsegnanteCorsos()).containsOnly(insegnanteCorsoBack);
        assertThat(insegnanteCorsoBack.getInsegnante()).isEqualTo(insegnante);

        insegnante.removeInsegnanteCorso(insegnanteCorsoBack);
        assertThat(insegnante.getInsegnanteCorsos()).doesNotContain(insegnanteCorsoBack);
        assertThat(insegnanteCorsoBack.getInsegnante()).isNull();

        insegnante.insegnanteCorsos(new HashSet<>(Set.of(insegnanteCorsoBack)));
        assertThat(insegnante.getInsegnanteCorsos()).containsOnly(insegnanteCorsoBack);
        assertThat(insegnanteCorsoBack.getInsegnante()).isEqualTo(insegnante);

        insegnante.setInsegnanteCorsos(new HashSet<>());
        assertThat(insegnante.getInsegnanteCorsos()).doesNotContain(insegnanteCorsoBack);
        assertThat(insegnanteCorsoBack.getInsegnante()).isNull();
    }
}
