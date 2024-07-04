package tech.catenate.orchestra.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class InsegnanteCorsoTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static InsegnanteCorso getInsegnanteCorsoSample1() {
        return new InsegnanteCorso().id(1L).mese(1);
    }

    public static InsegnanteCorso getInsegnanteCorsoSample2() {
        return new InsegnanteCorso().id(2L).mese(2);
    }

    public static InsegnanteCorso getInsegnanteCorsoRandomSampleGenerator() {
        return new InsegnanteCorso().id(longCount.incrementAndGet()).mese(intCount.incrementAndGet());
    }
}
