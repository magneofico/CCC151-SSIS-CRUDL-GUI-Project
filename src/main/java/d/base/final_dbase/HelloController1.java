package d.base.final_dbase;

import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.File;

/**
 * {@code HelloController1.java} is the Java class controller of Scene 1, the main scene of the application.
 * This class handles the login functionality for the administrator to access and manage student and course information.
 * It performs significant actions such as logging in/signing in to enable editing and management of registered students and courses.
 * Additionally, this controller initializes UI components and manages user interactions in Scene 1.
 *
 * <p>
 * The main responsibilities of this class include:
 * <ul>
 *     <li>Handling login functionality for the administrator.</li>
 *     <li>Initializing UI components in Scene 1.</li>
 *     <li>Managing user interactions for logging in/signing in.</li>
 * </ul>
 * </p>
 *
 * <p>
 * @author  Kristoffer Neo Senyahan | BSCS2 | kristofferneo.senyahan@g.msuiit.edu.ph | SSIS CCC151 Project
 * </p>
 */
public class HelloController1 {

    private HelloApplication helloApplication;

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private ImageView image;

    /** Initializes the controller.*/
    public void initialize() {
        File file = new File("/Users/kristofferneo/Downloads/SSIS-JavaFX-Final/Final_dbase/src/main/java/d/base/final_dbase/assets/background.png");
        String fileUrl = file.toURI().toString();
        Image image2 = new Image(fileUrl);
        image.setImage(image2); // sets image of javaFX imageFrame for GUI design purposes.
    }

    public void setHelloApplication(HelloApplication helloApplication) {
        this.helloApplication = helloApplication;
    }

    /** Handles the login process.*/
    @FXML
    private void handleLogin() {
        String username = usernameField.getText();
        String password = passwordField.getText();

        // Authentication logic and logIn information.
        if ("admin".equals(username) && "admin".equals(password)) {
            helloApplication.switchToScene2();
        } else {
            //Displays invalid credentials if user-inputted information doesn't match the set username and password.
            System.out.println("Invalid credentials");
        }
    }
}
