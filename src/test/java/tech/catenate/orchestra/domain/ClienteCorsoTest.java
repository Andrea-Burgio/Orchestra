package tech.catenate.orchestra.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static tech.catenate.orchestra.domain.ClienteCorsoTestSamples.*;

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
}
