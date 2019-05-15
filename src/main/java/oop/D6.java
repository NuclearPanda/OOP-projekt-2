package oop;
public class D6 extends Dice {
    @Override
    public int roll() {
        return randIntWithRange(1, 6);
    }

    @Override
    public int getMax() {
        return 6;
    }
}
