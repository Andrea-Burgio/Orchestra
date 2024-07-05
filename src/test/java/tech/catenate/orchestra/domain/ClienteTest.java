package tech.catenate.orchestra.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static tech.catenate.orchestra.domain.ClienteCorsoTestSamples.*;
import static tech.catenate.orchestra.domain.ClienteTestSamples.*;

import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;
import tech.catenate.orchestra.web.rest.TestUtil;

class ClienteTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Cliente.class);
        Cliente cliente1 = getClienteSample1();
        Cliente cliente2 = new Cliente();
        assertThat(cliente1).isNotEqualTo(cliente2);

        cliente2.setId(cliente1.getId());
        assertThat(cliente1).isEqualTo(cliente2);

        cliente2 = getClienteSample2();
        assertThat(cliente1).isNotEqualTo(cliente2);
    }

    @Test
    void clienteCorsoTest() {
        Cliente cliente = getClienteRandomSampleGenerator();
        ClienteCorso clienteCorsoBack = getClienteCorsoRandomSampleGenerator();

        cliente.addClienteCorso(clienteCorsoBack);
        assertThat(cliente.getClienteCorsos()).containsOnly(clienteCorsoBack);
        assertThat(clienteCorsoBack.getCliente()).isEqualTo(cliente);

        cliente.removeClienteCorso(clienteCorsoBack);
        assertThat(cliente.getClienteCorsos()).doesNotContain(clienteCorsoBack);
        assertThat(clienteCorsoBack.getCliente()).isNull();

        cliente.clienteCorsos(new HashSet<>(Set.of(clienteCorsoBack)));
        assertThat(cliente.getClienteCorsos()).containsOnly(clienteCorsoBack);
        assertThat(clienteCorsoBack.getCliente()).isEqualTo(cliente);

        cliente.setClienteCorsos(new HashSet<>());
        assertThat(cliente.getClienteCorsos()).doesNotContain(clienteCorsoBack);
        assertThat(clienteCorsoBack.getCliente()).isNull();
    }
}
