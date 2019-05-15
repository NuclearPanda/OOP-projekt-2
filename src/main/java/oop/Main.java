package oop;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;
/*Täringumäng
 *
 * Meie projekt on lihtne täringumäng.
 * 1. Alguses küsime, et mis täringut kasutada ja mitu mängijat võtab mängust osa.
 * 2. Luuakse mängijate objektid
 * 3. Valitakse välja, kes alustab shuffle abil
 * 4. Edasine töö toimub meetodis run
 * 5. meetod runis küsitakse mängijalt mitu korda ta soovib täringut veeretada. Iga veeretamiskorraga
 *    saab ta klass Dice abil juhusliku numbri, mis liidetakse tema skoorile, kui see number on 1, siis
 *    meetod resetScore abil nullitakse mängija skoor. Kui summa ületab 91, siis on mängija võitnud.
 *    Täringul on ka animatsioon.
 *
 * */

public class Main {
    public static void main(String[] args) throws InterruptedException {

        System.out.println("Täringumäng: created by Jaanus and Johan" +
                "\n   Täringumäng on mäng, kus sina ja su sõbrad veeretavad kordamööda teie poolt valitud täringut" +
                "\n   Esimese asjane valige täring, peale seda sisestage mängijate nimed" +
                "\n   Siis saate valida, mitu korda täringut veeretada. Kui veeretad 1, siis sinu skoor läheb 0" +
                "\n   Mängu eesmärk on saada skooriks 91. Kes saab esimesena skooriks 91 on mängu võitja\n");


        // peameetod, kus mäng jookseb
        run(chooseDice());
    }


    /**
     * Meetod täringu suuruse valimiseks.
     *
     * @return tagastab valitud täringu objekti
     */
    public static Dice chooseDice() {
        System.out.print("Vali mitmetahulist täringut kasutada. (6, 8 või 10): ");
        Scanner info = new Scanner(System.in);
        int answer = info.nextInt();
        switch (answer) {
            case 6:
                return new D6();
            case 8:
                return new D8();
            case 10:
                return new D10();
            default:
                return chooseDice();
        }
    }

    /**
     * Meetod mängija objektide loomiseks
     * Küsib konstruktoriks vajaliku info kasutajalt
     *
     * @return tagastab listi loodud mängija objektidest
     */
    public static List<Player> createPlayers() {
        List<Player> players = new ArrayList<>();
        Scanner info = new Scanner(System.in);
        System.out.print("Sisesta mängijate arv: ");
        int playerCount = info.nextInt();

        for (int i = 0; i < playerCount; i++) {
            System.out.print("Sisesta " + (i + 1) + ". mängija nimi: ");
            String name = info.next();
            players.add(new Player(name, 0));
        }
        return players;
    }


    /**
     * Meetod, mis määrab mängijate järjekorra ning väljastab alustava mängija nime
     *
     * @param players list mängijate objektidest
     */
    public static void whoBegins(List<Player> players) {
        if (players.size() == 0) {
            return;
        }
        Collections.shuffle(players);
        System.out.println("Alustab " + players.get(0).getName() + ".");
    }

    /**
     * Meetod, kus toimub mängi põhitöö teiste meetodite abil.
     *
     * Tehtud paariprogrammeerides
     *
     * @param dice täringuobjekt
     * @throws InterruptedException - vajalik animatsioonide jaoks
     */
    private static void run(Dice dice) throws InterruptedException {
        List<Player> players = createPlayers();
        whoBegins(players);
        Scanner input = new Scanner(System.in);
        Player winner = players.get(0); //TODO kontrollida kas võitja on olemas
        boolean running = true;
        boolean rolled1;
        int index = 0;
        while (running) { // põhitsükkel, kus toimub töö
            System.out.println("\nNüüd on " + players.get(index).getName() + " kord. Sinu skoor on "
                    + players.get(index).getScore() + ".");
            rolled1 = false;
            System.out.print("Mitu korda tahad veeretada: ");
            int mituKorda = input.nextInt(); // veeretamiskorra input
            if (mituKorda == 0) {
                if (index >= players.size() - 1) { // kui indeks läheb liiga suureks, siis pane indeks nulliks
                    index = 0;
                } else index++;  // vastasel juhul liida indeksile 1.
                continue;
            }
            System.out.print("\n");
            for (int i = 0; i < mituKorda; i++) { // veeretamiskorra tsükkel ja kontrollid
                int roll = Animations.animateDice(dice);
                if (roll == 1) {
                    rolled1 = true;
                    players.get(index).resetScore();
                    break;
                } else {
                    players.get(index).addToScore(roll);
                    if (players.get(index).getScore() >= 91) {
                        running = false;
                        winner = players.get(index);
                        break;
                    }
                }

            }
            if (rolled1) { // kui veeretati 1, siis minnakse edasi järgmise mängija juurde
                if (index >= players.size() - 1) {
                    index = 0;
                } else {
                    index++;
                    System.out.println("-------------------------------------");
                }
            }
        }
        System.out.println("Võitja on " + winner.getName() + " skooriga " + winner.getScore() + "!!!");
    }

}
