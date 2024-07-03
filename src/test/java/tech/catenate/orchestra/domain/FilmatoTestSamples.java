package tech.catenate.orchestra.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class FilmatoTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Filmato getFilmatoSample1() {
        return new Filmato().id(1L).nome_file("nome_file1");
    }

    public static Filmato getFilmatoSample2() {
        return new Filmato().id(2L).nome_file("nome_file2");
    }

    public static Filmato getFilmatoRandomSampleGenerator() {
        return new Filmato().id(longCount.incrementAndGet()).nome_file(UUID.randomUUID().toString());
    }
}
