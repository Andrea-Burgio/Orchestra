package tech.catenate.orchestra.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static tech.catenate.orchestra.domain.ClienteCorsoTestSamples.*;
import static tech.catenate.orchestra.domain.CorsoTestSamples.*;
import static tech.catenate.orchestra.domain.InsegnanteCorsoTestSamples.*;

import java.util.HashSet;
import java.util.Set;
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

    @Test
    void insegnanteCorsoTest() {
        Corso corso = getCorsoRandomSampleGenerator();
        InsegnanteCorso insegnanteCorsoBack = getInsegnanteCorsoRandomSampleGenerator();

        corso.addInsegnanteCorso(insegnanteCorsoBack);
        assertThat(corso.getInsegnanteCorsos()).containsOnly(insegnanteCorsoBack);
        assertThat(insegnanteCorsoBack.getCorso()).isEqualTo(corso);

        corso.removeInsegnanteCorso(insegnanteCorsoBack);
        assertThat(corso.getInsegnanteCorsos()).doesNotContain(insegnanteCorsoBack);
        assertThat(insegnanteCorsoBack.getCorso()).isNull();

        corso.insegnanteCorsos(new HashSet<>(Set.of(insegnanteCorsoBack)));
        assertThat(corso.getInsegnanteCorsos()).containsOnly(insegnanteCorsoBack);
        assertThat(insegnanteCorsoBack.getCorso()).isEqualTo(corso);

        corso.setInsegnanteCorsos(new HashSet<>());
        assertThat(corso.getInsegnanteCorsos()).doesNotContain(insegnanteCorsoBack);
        assertThat(insegnanteCorsoBack.getCorso()).isNull();
    }

    @Test
    void clienteCorsoTest() {
        Corso corso = getCorsoRandomSampleGenerator();
        ClienteCorso clienteCorsoBack = getClienteCorsoRandomSampleGenerator();

        corso.addClienteCorso(clienteCorsoBack);
        assertThat(corso.getClienteCorsos()).containsOnly(clienteCorsoBack);
        assertThat(clienteCorsoBack.getCorso()).isEqualTo(corso);

        corso.removeClienteCorso(clienteCorsoBack);
        assertThat(corso.getClienteCorsos()).doesNotContain(clienteCorsoBack);
        assertThat(clienteCorsoBack.getCorso()).isNull();

        corso.clienteCorsos(new HashSet<>(Set.of(clienteCorsoBack)));
        assertThat(corso.getClienteCorsos()).containsOnly(clienteCorsoBack);
        assertThat(clienteCorsoBack.getCorso()).isEqualTo(corso);

        corso.setClienteCorsos(new HashSet<>());
        assertThat(corso.getClienteCorsos()).doesNotContain(clienteCorsoBack);
        assertThat(clienteCorsoBack.getCorso()).isNull();
    }
}
