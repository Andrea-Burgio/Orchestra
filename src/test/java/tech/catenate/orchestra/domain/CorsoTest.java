package tech.catenate.orchestra.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static tech.catenate.orchestra.domain.CorsoTestSamples.*;

import org.junit.jupiter.api.Test;
import tech.catenate.orchestra.web.rest.TestUtil;

class CorsoTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Corso.class);
        Corso corso1 = getCorsoSample1();
        Corso corso2 = new Corso();
        assertThat(corso1).isNotEqualTo(corso2);

        corso2.setId(corso1.getId());
        assertThat(corso1).isEqualTo(corso2);

        corso2 = getCorsoSample2();
        assertThat(corso1).isNotEqualTo(corso2);
    }
}
