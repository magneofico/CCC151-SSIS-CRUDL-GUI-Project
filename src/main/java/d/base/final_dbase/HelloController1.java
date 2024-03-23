package d.base.final_dbase;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
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

    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private ImageView image;

    /**
     * Initializes the controller.
     */
    public void initialize() {
        setImage();
    }

    /**
     * Sets the image for the image view.
     */
    private void setImage() {
        File file = new File("src/main/java/d/base/final_dbase/assets/background.png");
        String fileUrl = file.toURI().toString();
        Image image2 = new Image(fileUrl);
        image.setImage(image2); // sets image of javaFX imageFrame for GUI design purposes.
    }

    /**
     * Sets the HelloApplication instance.
     *
     * @param helloApplication The HelloApplication instance to set.*/
    public void setHelloApplication(HelloApplication helloApplication) {
        this.helloApplication = helloApplication;
    }

    /**Handles the login process.*/
    @FXML
    private void handleLogin() {
        String username = usernameField.getText();
        String password = passwordField.getText();

        // Authentication logic and logIn information.
        if (isValidCredentials(username, password)) {
            helloApplication.switchToScene2();
        } else {
            // Display error pop-up for invalid credentials
            showErrorAlert(getErrorMessage(username, password));
        }
    }

    /**
     * Checks if the provided credentials are valid.
     *
     * @param username The username to check.
     * @param password The password to check.
     * @return True if the credentials are valid, false otherwise.*/
    private boolean isValidCredentials(String username, String password) {
        return "admin".equals(username) && "admin".equals(password);
    }

    /**
     * Constructs an error message based on the provided credentials.
     *
     * @param username The username entered by the user.
     * @param password The password entered by the user.
     * @return The error message indicating the incorrect username or password.*/
    private String getErrorMessage(String username, String password) {
        StringBuilder errorMessage = new StringBuilder("The ");
        if (!"admin".equals(username)) {
            errorMessage.append("username ");
        }
        if (!"admin".equals(password)) {
            if (!"admin".equals(username)) {
                errorMessage.append("and ");
            }
            errorMessage.append("password ");
        }
        errorMessage.append("you entered is incorrect. Please try again.");
        return errorMessage.toString();
    }

    /**
     * Displays an error alert with the specified title and content.
     *
     * @param content The content of the alert.*/
    private void showErrorAlert(String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Invalid Credentials");
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
