package oop;

public class D10 extends Dice {
    @Override
    public int roll() {
        return randIntWithRange(1, 10);
    }

    @Override
    public int getMax() {
        return 10;
    }
}
