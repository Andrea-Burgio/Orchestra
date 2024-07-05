package tech.catenate.orchestra.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static tech.catenate.orchestra.domain.ConcertoTestSamples.*;
import static tech.catenate.orchestra.domain.FilmatoTestSamples.*;

import org.junit.jupiter.api.Test;
import tech.catenate.orchestra.web.rest.TestUtil;

class FilmatoTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Filmato.class);
        Filmato filmato1 = getFilmatoSample1();
        Filmato filmato2 = new Filmato();
        assertThat(filmato1).isNotEqualTo(filmato2);

        filmato2.setId(filmato1.getId());
        assertThat(filmato1).isEqualTo(filmato2);

        filmato2 = getFilmatoSample2();
        assertThat(filmato1).isNotEqualTo(filmato2);
    }

    @Test
    void concertoTest() {
        Filmato filmato = getFilmatoRandomSampleGenerator();
        Concerto concertoBack = getConcertoRandomSampleGenerator();

        filmato.setConcerto(concertoBack);
        assertThat(filmato.getConcerto()).isEqualTo(concertoBack);

        filmato.concerto(null);
        assertThat(filmato.getConcerto()).isNull();
    }
}
