package oop;
import java.util.Random;

public abstract class Dice {
    abstract int roll();

    abstract int getMax();

    /**
     * @param min suvalise arvu alampiir
     * @param max suvalise arvu ülempiir
     * @return suvaline arv lõigus [min, max]
     */
    static int randIntWithRange(int min, int max) {
        Random generaator = new Random();
        return generaator.nextInt((max - min) + 1) + min;
    }
}
