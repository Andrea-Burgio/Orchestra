package tech.catenate.orchestra.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class ConcertoTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Concerto getConcertoSample1() {
        return new Concerto().id(1L).nome("nome1");
    }

    public static Concerto getConcertoSample2() {
        return new Concerto().id(2L).nome("nome2");
    }

    public static Concerto getConcertoRandomSampleGenerator() {
        return new Concerto().id(longCount.incrementAndGet()).nome(UUID.randomUUID().toString());
    }
}
