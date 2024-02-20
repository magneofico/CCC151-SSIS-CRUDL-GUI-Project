package d.base.final_dbase;

import javafx.fxml.FXML;

public class CrudlImplementation {

    private HelloApplication helloApplication;

    public void setHelloApplication(HelloApplication helloApplication) {
        this.helloApplication = helloApplication;
    }




    @FXML
    private void backToTable() {
        helloApplication.switchToScene2();
    }
}

