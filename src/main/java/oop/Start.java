package oop;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class Start extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        GridPane root = new GridPane();
        primaryStage.setMinWidth(500);
        primaryStage.setMinHeight(500);
        root.setVgap(50);
        root.setHgap(100);


        ColumnConstraints columb1 = new ColumnConstraints();
        columb1.setPercentWidth(33);
        ColumnConstraints columb2 = new ColumnConstraints();
        columb2.setPercentWidth(33);
        ColumnConstraints columb3 = new ColumnConstraints();
        columb3.setPercentWidth(33);

        root.getColumnConstraints().addAll(columb1, columb2, columb3);

        root.setPadding(new Insets(200, 200, 200, 200));
        Scene scene = new Scene(root, 600, 600);


        Alert intro = new Alert(Alert.AlertType.INFORMATION);
        intro.setHeaderText("Sissejuhatus");
        intro.setContentText("Täringumäng: created by Jaanus and Johan. " +
                "Täringumäng on mäng, kus sina ja su sõbrad veeretavad kordamööda teie poolt valitud täringut. " +
                "Esimese asjane valige täring, peale seda sisestage mängijate nimed. " +
                "Siis saate valida, mitu korda täringut veeretada. Kui veeretad 1, siis sinu skoor läheb 0. " +
                "Mängu eesmärk on saada skooriks 91. Kes saab esimesena skooriks 91 on mängu võitja.");
        //intro.showAndWait();


        List<Player> players = createPlayers(root);
        if (players.size() == 0) {
            System.exit(0);
        }

        Collections.shuffle(players);


        System.out.println(players);
        Text turn = new Text("Praegu veeretab: " + players.get(0).getName());
        Text nimi1 = new Text(players.get(0).getName());
        Text nimi2 = new Text(players.get(1).getName());
        Text score1 = new Text("Score: " + players.get(0).getScore());
        Text score2 = new Text("Score: " + players.get(1).getScore());
        root.add(nimi1, 0, 0);
        root.add(turn, 1, 0);
        root.add(nimi2, 3, 0);
        root.add(score1, 0, 1);
        root.add(score2, 3, 1);
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
            out.add(new Player(result.get().getKey(), 0));
            out.add(new Player(result.get().getValue(), 0));
        }
        return out;
    }


}
