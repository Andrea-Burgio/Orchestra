package tech.catenate.orchestra.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static tech.catenate.orchestra.domain.InsegnanteTestSamples.*;

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
}
