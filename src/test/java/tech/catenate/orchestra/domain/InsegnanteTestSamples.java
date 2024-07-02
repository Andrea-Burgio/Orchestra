package tech.catenate.orchestra.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class InsegnanteTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Insegnante getInsegnanteSample1() {
        return new Insegnante().id(1L).nome("nome1").cognome("cognome1");
    }

    public static Insegnante getInsegnanteSample2() {
        return new Insegnante().id(2L).nome("nome2").cognome("cognome2");
    }

    public static Insegnante getInsegnanteRandomSampleGenerator() {
        return new Insegnante().id(longCount.incrementAndGet()).nome(UUID.randomUUID().toString()).cognome(UUID.randomUUID().toString());
    }
}
