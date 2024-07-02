package tech.catenate.orchestra.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class CorsoTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static Corso getCorsoSample1() {
        return new Corso().id(1L).anno(1).nome("nome1");
    }

    public static Corso getCorsoSample2() {
        return new Corso().id(2L).anno(2).nome("nome2");
    }

    public static Corso getCorsoRandomSampleGenerator() {
        return new Corso().id(longCount.incrementAndGet()).anno(intCount.incrementAndGet()).nome(UUID.randomUUID().toString());
    }
}
