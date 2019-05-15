package oop;

public class Animations {
    public static int animateDice(Dice dice) throws InterruptedException {
        int j = 1;
        for (int i = 0; i < 50; i++) {
            System.out.print(j + "   \r");
            if (j == dice.getMax()) {
                j = 1;
            } else j++;
            Thread.sleep(20);
        }
        System.out.print("   \r"); // clear last line
        int roll = dice.roll();
        System.out.println(roll);
        return roll;
    }
}
