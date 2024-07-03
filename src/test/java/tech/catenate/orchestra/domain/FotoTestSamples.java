package tech.catenate.orchestra.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class FotoTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Foto getFotoSample1() {
        return new Foto().id(1L).nome_file("nome_file1");
    }

    public static Foto getFotoSample2() {
        return new Foto().id(2L).nome_file("nome_file2");
    }

    public static Foto getFotoRandomSampleGenerator() {
        return new Foto().id(longCount.incrementAndGet()).nome_file(UUID.randomUUID().toString());
    }
}
