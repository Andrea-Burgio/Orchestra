package tech.catenate.orchestra.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static tech.catenate.orchestra.domain.ClienteCorsoTestSamples.*;
import static tech.catenate.orchestra.domain.ClienteTestSamples.*;
import static tech.catenate.orchestra.domain.CorsoTestSamples.*;

import org.junit.jupiter.api.Test;
import tech.catenate.orchestra.web.rest.TestUtil;

class ClienteCorsoTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ClienteCorso.class);
        ClienteCorso clienteCorso1 = getClienteCorsoSample1();
        ClienteCorso clienteCorso2 = new ClienteCorso();
        assertThat(clienteCorso1).isNotEqualTo(clienteCorso2);

        clienteCorso2.setId(clienteCorso1.getId());
        assertThat(clienteCorso1).isEqualTo(clienteCorso2);

        clienteCorso2 = getClienteCorsoSample2();
        assertThat(clienteCorso1).isNotEqualTo(clienteCorso2);
    }

    @Test
    void clienteTest() {
        ClienteCorso clienteCorso = getClienteCorsoRandomSampleGenerator();
        Cliente clienteBack = getClienteRandomSampleGenerator();

        clienteCorso.setCliente(clienteBack);
        assertThat(clienteCorso.getCliente()).isEqualTo(clienteBack);

        clienteCorso.cliente(null);
        assertThat(clienteCorso.getCliente()).isNull();
    }

    @Test
    void corsoTest() {
        ClienteCorso clienteCorso = getClienteCorsoRandomSampleGenerator();
        Corso corsoBack = getCorsoRandomSampleGenerator();

        clienteCorso.setCorso(corsoBack);
        assertThat(clienteCorso.getCorso()).isEqualTo(corsoBack);

        clienteCorso.corso(null);
        assertThat(clienteCorso.getCorso()).isNull();
    }
}
