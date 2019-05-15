package oop;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;


public class Animations {
    public static int animateDice(Dice dice, Canvas canvas) throws InterruptedException {
        Image[] pildid = new Image[]{new Image("File:pilt/1.jpg"),
                new Image("File:pilt/2.jpg"),
                new Image("File:pilt/3.jpg"),
                new Image("File:pilt/4.jpg"),
                new Image("File:pilt/5.jpg"),
                new Image("File:pilt/6.jpg")};

        GraphicsContext gc = canvas.getGraphicsContext2D();
        for (int i = 0, j = 0; i < 50; i++, j++) {

            if (j == 6) {
                System.out.println(j);
                j = 0;
            }
            Thread.sleep(200);
        }
        int roll = dice.roll();
        return roll;
    }
}
