package oop;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class D6 extends Dice {
    private int lastRoll;
    private ImageView dieFace;
    private Image[] images = new Image[]{new Image("File:pilt/1.jpg"),
            new Image("File:pilt/2.jpg"),
            new Image("File:pilt/3.jpg"),
            new Image("File:pilt/4.jpg"),
            new Image("File:pilt/5.jpg"),
            new Image("File:pilt/6.jpg")};

    public D6() {
        dieFace = new ImageView(images[0]);
    }

    public ImageView getDieFace() {
        return dieFace;
    }

    public void setDieFace(int dieFaceValue) {
        dieFace.setImage(this.images[dieFaceValue - 1]);
        lastRoll = dieFaceValue;
    }

    public int getLastRoll() {
        return lastRoll;
    }

    @Override
    public int roll() {
        return randIntWithRange(1, 6);
    }

    @Override
    public int getMax() {
        return 6;
    }
}
