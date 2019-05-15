package oop;

public class D8 extends Dice {
    @Override
    public int roll() {
        return randIntWithRange(1, 8);
    }

    @Override
    public int getMax() {
        return 8;
    }

}
