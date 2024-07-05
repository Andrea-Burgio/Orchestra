package tech.catenate.orchestra.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class ClienteCorsoTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static ClienteCorso getClienteCorsoSample1() {
        return new ClienteCorso().id(1L).mese(1);
    }

    public static ClienteCorso getClienteCorsoSample2() {
        return new ClienteCorso().id(2L).mese(2);
    }

    public static ClienteCorso getClienteCorsoRandomSampleGenerator() {
        return new ClienteCorso().id(longCount.incrementAndGet()).mese(intCount.incrementAndGet());
    }
}
