package d.base.final_dbase;

import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.File;

public class HelloController1 {

    private HelloApplication helloApplication;

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private ImageView image;

    public void initialize() {
        File file = new File("/Users/kristofferneo/Downloads/SSIS-JavaFX-Final/Final_dbase/src/main/java/d/base/final_dbase/assets/background.png");
        String fileUrl = file.toURI().toString();
        Image image2 = new Image(fileUrl);
        image.setImage(image2);
    }

    public void setHelloApplication(HelloApplication helloApplication) {
        this.helloApplication = helloApplication;
    }

    @FXML
    private void handleLogin() {
        // Add your authentication logic here
        // For simplicity, let's assume username and password are "admin"
        String username = usernameField.getText();
        String password = passwordField.getText();

        if ("admin".equals(username) && "admin".equals(password)) {
            helloApplication.switchToScene2();
        } else {
            // You can display an error message or handle unsuccessful login here
            System.out.println("Invalid credentials");
        }
    }


}
