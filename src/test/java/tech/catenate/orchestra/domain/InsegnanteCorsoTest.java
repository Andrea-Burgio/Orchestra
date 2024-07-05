package tech.catenate.orchestra.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static tech.catenate.orchestra.domain.CorsoTestSamples.*;
import static tech.catenate.orchestra.domain.InsegnanteCorsoTestSamples.*;
import static tech.catenate.orchestra.domain.InsegnanteTestSamples.*;

import org.junit.jupiter.api.Test;
import tech.catenate.orchestra.web.rest.TestUtil;

class InsegnanteCorsoTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(InsegnanteCorso.class);
        InsegnanteCorso insegnanteCorso1 = getInsegnanteCorsoSample1();
        InsegnanteCorso insegnanteCorso2 = new InsegnanteCorso();
        assertThat(insegnanteCorso1).isNotEqualTo(insegnanteCorso2);

        insegnanteCorso2.setId(insegnanteCorso1.getId());
        assertThat(insegnanteCorso1).isEqualTo(insegnanteCorso2);

        insegnanteCorso2 = getInsegnanteCorsoSample2();
        assertThat(insegnanteCorso1).isNotEqualTo(insegnanteCorso2);
    }

    @Test
    void insegnanteTest() {
        InsegnanteCorso insegnanteCorso = getInsegnanteCorsoRandomSampleGenerator();
        Insegnante insegnanteBack = getInsegnanteRandomSampleGenerator();

        insegnanteCorso.setInsegnante(insegnanteBack);
        assertThat(insegnanteCorso.getInsegnante()).isEqualTo(insegnanteBack);

        insegnanteCorso.insegnante(null);
        assertThat(insegnanteCorso.getInsegnante()).isNull();
    }

    @Test
    void corsoTest() {
        InsegnanteCorso insegnanteCorso = getInsegnanteCorsoRandomSampleGenerator();
        Corso corsoBack = getCorsoRandomSampleGenerator();

        insegnanteCorso.setCorso(corsoBack);
        assertThat(insegnanteCorso.getCorso()).isEqualTo(corsoBack);

        insegnanteCorso.corso(null);
        assertThat(insegnanteCorso.getCorso()).isNull();
    }
}
