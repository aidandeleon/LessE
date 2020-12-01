package awgdl.lessE;

import awgdl.lessE.controllers.MainMenuController;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
    static final int MAIN_MENU_WIDTH = 800;
    static final int MAIN_MENU_HEIGHT = 600;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/MainMenu.fxml"));
        Parent root = loader.load();
        MainMenuController mainMenuController = loader.getController();
        mainMenuController.initData();

        primaryStage.setTitle("LessE");
        primaryStage.setScene(new Scene(root, MAIN_MENU_WIDTH, MAIN_MENU_HEIGHT));
        primaryStage.setResizable(false);
        primaryStage.setOnCloseRequest(e -> handleExit());
        primaryStage.show();
    }

    private void handleExit() {
        Platform.exit();
        System.exit(0);
    }
}
