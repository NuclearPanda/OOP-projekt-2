package oop;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.util.Pair;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static javafx.scene.text.Font.getFontNames;
import static javax.swing.text.StyleConstants.Italic;
import static javax.swing.text.html.parser.DTDConstants.MS;


/**
 * Peaklass kust kõik tööle läheb
 */
public class Start extends Application {

    private int currentPlayerIndex = 0;

    @Override
    public void start(Stage primaryStage) throws Exception {

        /** Võtame täringu*/
        D6 die = new D6();

        /** Loome paanid */
        GridPane gridPane = new GridPane();
        primaryStage.setMinWidth(500);
        primaryStage.setMinHeight(260);
        gridPane.setVgap(20);
        gridPane.setHgap(20);
        gridPane.setPadding(new Insets(20, 20, 20, 20));

        primaryStage.setTitle("Täringumäng");

        /** Paanide scaling ja padding*/
        ColumnConstraints columb1 = new ColumnConstraints();
        columb1.setPercentWidth(33);
        ColumnConstraints columb2 = new ColumnConstraints();
        columb2.setPercentWidth(33);
        ColumnConstraints columb3 = new ColumnConstraints();
        columb3.setPercentWidth(33);
;
        RowConstraints row1 = new RowConstraints();
        row1.setPercentHeight(25);
        RowConstraints row2 = new RowConstraints();
        row2.setPercentHeight(25);
        RowConstraints row3 = new RowConstraints();
        row3.setPercentHeight(25);
        RowConstraints row4 = new RowConstraints();
        row4.setPercentHeight(25);


        gridPane.getColumnConstraints().addAll(columb1, columb2, columb3);
        gridPane.getRowConstraints().addAll(row1, row2, row3);

        Scene scene = new Scene(gridPane, 600, 220);

        /** Väljastame õpetuse */
        intro();

        /** Loome mängijate listi ja segame selle*/
        List<Player> players = createPlayers(gridPane);

        Collections.shuffle(players);
        //System.out.println(players);

        /** Loome andmeväljad mida loodud paanidesse panna */
        Text praeguseMängijaText = new Text("Praegu veeretab: " + players.get(0).getName());
        praeguseMängijaText.setFont(Font.loadFont("file:font/segoepr.ttf", 20));

        Text nimi1 = new Text(players.get(0).getName());
        nimi1.setFont(Font.loadFont("file:font/segoepr.ttf", 14));

        Text score1 = new Text("Punktid: " + players.get(0).getScore());
        score1.setFont(Font.loadFont("file:font/segoepr.ttf", 14));

        Text nimi2 = new Text(players.get(1).getName());
        nimi2.setFont(Font.loadFont("file:font/segoepr.ttf", 14));

        Text score2 = new Text("Punktid: " + players.get(1).getScore());
        score2.setFont(Font.loadFont("file:font/segoepr.ttf", 14));

        Text[] nimed = new Text[]{nimi1, nimi2};
        Text[] scores = new Text[]{score1, score2};

        /** Loome nupu cancel ja määrame, mis seda vajutades juhtub
         *
         * Kui vajutatakse nuppu Anna käik üle, siis kasutab meetodit changeplayer
         * */
        Button cancelButton = new Button("Anna käik üle");
        cancelButton.setOnAction((ActionEvent event) -> changePlayer(nimed, praeguseMängijaText));


        /** Tegevused mis juhtuvad, kui vajutada 'Veereta täringut' */
        // Loome nupu
        Button rollButton = new Button("Veereta täringut");

        // Kui vajutatakse, siis
        rollButton.setOnAction((ActionEvent event) -> { //TODO kood ilusaks

            // disable nupud
            cancelButton.setDisable(true);
            rollButton.setDisable(true);

            // veereta ka pane käima animatsioon
            Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(.1), (actionEvent) -> {
                int roll = die.roll();
                die.setDieFace(roll);
            }));
            timeline.setCycleCount(10);
            timeline.play();

            // kui animatsioon on läbi, siis tee järgmised toimingud
            timeline.setOnFinished(actionEvent -> {

                // enable nupud
                rollButton.setDisable(false);
                cancelButton.setDisable(false);

                // Leia praegusemängijaindeks
                Player currentPlayer = players.get(currentPlayerIndex);

                // Kui veeretati 1, siis nulli skoor ja vaheta mängija
                if (die.getLastRoll() == 1) {
                    currentPlayer.resetScore();
                    scores[currentPlayerIndex].setText("Punktid: " + currentPlayer.getScore());
                    changePlayer(nimed, praeguseMängijaText);

                // Lisa praegusele mängijale skoori
                } else {
                    currentPlayer.addToScore(die.getLastRoll() *10);
                    scores[currentPlayerIndex].setText("Punktid: " + currentPlayer.getScore());
                }

                // Kui kirjutamisel läks midagi valesti, siis anna veateade
                try {

                    // Kui skoor on üle 90, siis kutsu välja failikirjutamise ja võidumeetodid
                    if (currentPlayer.getScore() > 90) {
                        writeScoresToFile(players);
                        victory(currentPlayer.getName());
                    }
                } catch (IOException e){
                    Alert error = new Alert(Alert.AlertType.ERROR);
                    error.setHeaderText("Error");
                    error.setContentText("Tekkis viga logifaili salvestamisel, errori info on konsoolis");
                    System.out.println(e);
                    error.setOnHidden(event1 -> Platform.exit());
                }
            });
        });

        /** Lisa paanid andmeväljad paanidesse */

        gridPane.add(nimi1, 0, 1);

        gridPane.add(praeguseMängijaText, 1, 0);
        GridPane.setHalignment(praeguseMängijaText, HPos.CENTER);

        gridPane.add(nimi2, 2, 1);
        GridPane.setHalignment(nimi2, HPos.RIGHT);

        gridPane.add(score1, 0, 2);

        gridPane.add(score2, 2, 2);
        GridPane.setHalignment(score2, HPos.RIGHT);

        gridPane.add(die.getDieFace(), 1, 2);
        GridPane.setHalignment(die.getDieFace(), HPos.CENTER);

        gridPane.add(rollButton, 0, 3);

        gridPane.add(cancelButton, 2, 3);
        GridPane.setHalignment(cancelButton, HPos.RIGHT);

        primaryStage.setScene(scene);
        primaryStage.show();

    }

    public static void main(String[] args) {
        launch(args);
    }

    public static List<Player> createPlayers(GridPane root) {
        List<Player> out = new ArrayList<>();
        Dialog<Pair<String, String>> dialog = new Dialog<>();
        dialog.setTitle("Sisesta nimed");
        dialog.setHeaderText("Sisesta mängijate nimed");
        ButtonType confirmNamesButton = new ButtonType("Valmis", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(confirmNamesButton, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));


        TextField nimi1 = new TextField();
        nimi1.setPromptText("Esimene nimi");
        TextField nimi2 = new TextField();
        nimi2.setPromptText("Teine nimi");
        grid.add(new Label("Esimene nimi:"), 0, 0);
        grid.add(nimi1, 1, 0);
        grid.add(new Label("Teine nimi:"), 0, 1);
        grid.add(nimi2, 1, 1);


        dialog.getDialogPane().setContent(grid);
        Platform.runLater(nimi1::requestFocus);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == confirmNamesButton) {
                return new Pair<>(nimi1.getText(), nimi2.getText());
            }
            return null;
        });
        Optional<Pair<String, String>> result = dialog.showAndWait();
        if (result.isPresent()) {
            String esimene = result.get().getKey().toUpperCase();
            String teine = result.get().getValue().toUpperCase();
            if (esimene.trim().length() == 0){
                esimene = "mängija 1";
            }
            if (teine.trim().length() == 0){
                teine = "mängija 2";
            }
            out.add(new Player(esimene, 0));
            out.add(new Player(teine, 0));
        }
        return out;
    }

    private void changePlayer(Text[] nimed, Text turn) {
        if (currentPlayerIndex == 1) {
            currentPlayerIndex = 0;
            turn.setText("Praegu veeretab: " + nimed[0].getText());
        } else {
            currentPlayerIndex = 1;
            turn.setText("Praegu veeretab: " + nimed[1].getText());

        }
    }

    private void victory(String nimi) {
        Alert win = new Alert(Alert.AlertType.INFORMATION);
        win.setHeaderText("Palju õnne");
        win.setContentText("Võitis mängija " + nimi);
        win.setOnHidden(event -> Platform.exit());
        win.show();
    }

    private void writeScoresToFile(List<Player> players) throws IOException {
        try (BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("skoorid.txt", true), StandardCharsets.UTF_8))) {
            bw.write("Mängija " + players.get(currentPlayerIndex).getName() + " võitis mängijat " +
                    players.get(1 - currentPlayerIndex).getName() + " skooriga "
                    + players.get(currentPlayerIndex).getScore() + " : "
                    + players.get(1 - currentPlayerIndex).getScore());
            bw.newLine();
        }
    }

    private void intro() {
        Alert intro = new Alert(Alert.AlertType.INFORMATION);
        intro.setHeaderText("Sissejuhatus");
        intro.setContentText("Täringumäng: created by Jaanus and Johan. " +
                "Täringumäng on mäng, kus sina ja su sõbrad veeretavad kordamööda täringut. " +
                "Esimese asjane sisestage mängijate nimed. " +
                "Siis saate hakata veeretama. Kui veeretad 1, siis sinu skoor läheb 0. " +
                "Mängu eesmärk on saada skooriks 91. Kes saab esimesena skooriks 91 on mängu võitja.");
        intro.showAndWait();
    }

}
