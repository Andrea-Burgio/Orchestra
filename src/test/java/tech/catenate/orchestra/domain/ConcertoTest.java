package tech.catenate.orchestra.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static tech.catenate.orchestra.domain.ConcertoTestSamples.*;
import static tech.catenate.orchestra.domain.CorsoTestSamples.*;
import static tech.catenate.orchestra.domain.FotoTestSamples.*;

import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;
import tech.catenate.orchestra.web.rest.TestUtil;

class ConcertoTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Concerto.class);
        Concerto concerto1 = getConcertoSample1();
        Concerto concerto2 = new Concerto();
        assertThat(concerto1).isNotEqualTo(concerto2);

        concerto2.setId(concerto1.getId());
        assertThat(concerto1).isEqualTo(concerto2);

        concerto2 = getConcertoSample2();
        assertThat(concerto1).isNotEqualTo(concerto2);
    }

    @Test
    void corsoTest() {
        Concerto concerto = getConcertoRandomSampleGenerator();
        Corso corsoBack = getCorsoRandomSampleGenerator();

        concerto.setCorso(corsoBack);
        assertThat(concerto.getCorso()).isEqualTo(corsoBack);

        concerto.corso(null);
        assertThat(concerto.getCorso()).isNull();
    }

    @Test
    void fotoTest() {
        Concerto concerto = getConcertoRandomSampleGenerator();
        Foto fotoBack = getFotoRandomSampleGenerator();

        concerto.addFoto(fotoBack);
        assertThat(concerto.getFotos()).containsOnly(fotoBack);
        assertThat(fotoBack.getConcerto()).isEqualTo(concerto);

        concerto.removeFoto(fotoBack);
        assertThat(concerto.getFotos()).doesNotContain(fotoBack);
        assertThat(fotoBack.getConcerto()).isNull();

        concerto.fotos(new HashSet<>(Set.of(fotoBack)));
        assertThat(concerto.getFotos()).containsOnly(fotoBack);
        assertThat(fotoBack.getConcerto()).isEqualTo(concerto);

        concerto.setFotos(new HashSet<>());
        assertThat(concerto.getFotos()).doesNotContain(fotoBack);
        assertThat(fotoBack.getConcerto()).isNull();
    }
}
