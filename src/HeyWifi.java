
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

/**
 * PLEASE Read this Carefully !!!
 *
 * @author Younus Shalaby
 * @facebook fb.me/YounusShalaby
 * @youtube youtube.com/YounusShalaby
 * @youtube youtube.com/elprofeshnal
 *
 *
 * LICENCE: This Software is Licensed under GPL V3
 *
 * You Can Change, Modify and Redistribute This Source Code, But DO NOT Omit The
 * Author Copyrights including (@author, @facebook, @youtube).
 *
 */
public class HeyWifi extends Application {

    // Variables definition
    public String visiblePassword = new String();
    public String hiddenPassword = new String();

    static String networkName = null;
    static String networkKey = null;

    static TextField netNameField;
    static PasswordField netPassField;

    Actions action = new Actions();
    static Stage publicStage = new Stage();

    // Overriding Start method
    @Override
    public void start(Stage primaryStage) {

        HeyWifi.publicStage = primaryStage;

        ////    ****  Layout Design Start  ****    ////
        Label header = new Label();
        header.setText("======== Hey Wifi ========");
        header.setId("header");

        netNameField = new TextField();
        netNameField.setPromptText("Enter Hotspot Name");

        netPassField = new PasswordField();
        netPassField.setPromptText("Enter Password");

        TextField visiblePasswordField = new TextField();
        visiblePasswordField.setPromptText("Enter Password");

        Image imgpass = new Image(getClass().getResourceAsStream("hide.png"));
        Button btnShowPass = new Button();
        btnShowPass.setGraphic(new ImageView(imgpass));

        Image imgpassshow = new Image(getClass().getResourceAsStream("show.png"));

        Image imgStart = new Image(getClass().getResourceAsStream("start.png"));
        Button btnStart = new Button("", new ImageView(imgStart));

        Image imgStop = new Image(getClass().getResourceAsStream("stop.png"));
        Button btnStop = new Button("", new ImageView(imgStop));

        Image imgAbout = new Image(getClass().getResourceAsStream("info.png"));
        Button btnAbout = new Button("", new ImageView(imgAbout));
        btnAbout.setId("rm-bkgrnd");

        Label statusLbl = new Label();
        statusLbl.setText("Idle");

        Image imgCheck = new Image(getClass().getResourceAsStream("check.png"));
        Button btnCheck = new Button("", new ImageView(imgCheck));
        btnCheck.setId("rm-bkgrnd");

        GridPane grd = new GridPane();
        grd.add(netPassField, 0, 0);
        grd.add(btnShowPass, 1, 0);
        grd.setVgap(10);
        grd.setHgap(10);

        StackPane stkpn = new StackPane();
        stkpn.getChildren().addAll(header, netNameField, grd,
                btnStart, btnStop, btnAbout, statusLbl, btnCheck);

        StackPane.setAlignment(header, Pos.TOP_LEFT);
        StackPane.setAlignment(netNameField, Pos.TOP_LEFT);
        StackPane.setAlignment(grd, Pos.TOP_LEFT);
        StackPane.setAlignment(btnStart, Pos.TOP_CENTER);
        StackPane.setAlignment(btnStop, Pos.CENTER);
        StackPane.setAlignment(btnAbout, Pos.BOTTOM_LEFT);
        StackPane.setAlignment(statusLbl, Pos.BOTTOM_CENTER);
        StackPane.setAlignment(btnCheck, Pos.BOTTOM_RIGHT);

        StackPane.setMargin(header, new Insets(20, 0, 0, 12));
        StackPane.setMargin(netNameField, new Insets(80, 50, 0, 50));
        StackPane.setMargin(grd, new Insets(130, 50, 0, 50));
        StackPane.setMargin(btnStart, new Insets(200, 0, 0, 0));
        StackPane.setMargin(btnStop, new Insets(210, 0, 0, 0));
        StackPane.setMargin(btnAbout, new Insets(0, 0, -3, -20));
        StackPane.setMargin(statusLbl, new Insets(0, 0, 5, 0));
        StackPane.setMargin(btnCheck, new Insets(0, -20, -3, 0));

        BorderPane root = new BorderPane();
        root.setCenter(stkpn);

        Scene scene = new Scene(root, 350, 430);
        scene.getStylesheets().add("styles.css");

        netNameField.setFocusTraversable(true);
        netPassField.setFocusTraversable(false);
        visiblePasswordField.setFocusTraversable(false);
        ////    ****  Layout Design End  ****    ////

        ////    ****  Actions Start  ****    ////
        btnShowPass.setOnMousePressed(e -> {
            //
            Button button = (Button) e.getSource();
            button.setGraphic(new ImageView(imgpassshow));
            //
            this.visiblePassword = netPassField.getText();
            netPassField.setText(null);
            grd.getChildren().remove(netPassField);
            grd.getChildren().add(visiblePasswordField);
            visiblePasswordField.setText(visiblePassword);
        });

        btnShowPass.setOnMouseReleased(e -> {
            //
            Button button = (Button) e.getSource();
            button.setGraphic(new ImageView(imgpass));
            //
            this.hiddenPassword = visiblePasswordField.getText();
            visiblePasswordField.setText(null);
            grd.getChildren().remove(visiblePasswordField);
            grd.getChildren().add(netPassField);
            netPassField.setText(hiddenPassword);
        });

        btnStart.setOnAction(e -> {

            action.executeCommand("netsh wlan start hostednetwork");
            String activation = "The hosted network couldn't be started.";
            int index = activation.indexOf(action.outputString);

            if (!action.outputString.contains(activation)) {

                action.executeCommand("netsh wlan stop hostednetwork");

                if (!netNameField.getText().equals("") && netPassField.getText().length() >= 8) {
                    Actions.initializeHotspot();
                    action.startHotspot();
                    statusLbl.setText("Started...");

                } else if (netNameField.getText().equals("")) {
                    action.enterName();

                } else if (netPassField.getText().length() <= 8) {
                    action.eightChar();

                } else {
                    action.internalError();
                }
            } else {
                action.warning();
            }
        });

        btnStop.setOnAction(e -> {
            action.stopHotspot();
            statusLbl.setText("Stopped");
        });

        btnAbout.setOnAction(e -> {
            action.aboutMeStage();
        });

        btnCheck.setOnAction(e -> {
            action.checkingAvailability();
        });

        primaryStage.setTitle("Hey Wifi");
        primaryStage.getIcons().add(new Image("icon.png"));
        primaryStage.setScene(scene);
        primaryStage.sizeToScene();
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Application.launch(args);
    }

}
