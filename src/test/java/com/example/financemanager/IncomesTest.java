package com.example.financemanager;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.MenuBar;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.stage.Stage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;
import org.testfx.matcher.control.MenuItemMatchers;
import org.testfx.robot.Motion;
import org.testfx.util.NodeQueryUtils;
import org.testfx.util.WaitForAsyncUtils;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.testfx.api.FxAssert.verifyThat;
import static org.testfx.matcher.base.NodeMatchers.isVisible;
import static org.testfx.matcher.control.LabeledMatchers.hasText;

@ExtendWith(ApplicationExtension.class)
public class IncomesTest {

    @Start
    public void start(Stage stage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(FinanceTrackerApplication.class.getResource("income-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setScene(scene);
        stage.show();
    }

    @BeforeEach
    public void setUp(FxRobot robot) throws TimeoutException {
        robot.clickOn("Navigation");

        WaitForAsyncUtils.waitFor(2, TimeUnit.SECONDS, () -> robot.lookup("Revenus").match(NodeQueryUtils.isVisible()).tryQuery().isPresent());

        robot.clickOn("Revenus", Motion.VERTICAL_FIRST, MouseButton.PRIMARY);
    }

    @Test
    public void shouldHaveMenu(FxRobot robot) {
        verifyThat(".menu-bar", isVisible());
        robot.lookup(".menu-bar").queryAs(MenuBar.class).getMenus().forEach(menu -> {
            verifyThat(menu, MenuItemMatchers.hasText("Navigation"));
            verifyThat(menu.getItems().get(0), MenuItemMatchers.hasText("Tableau de bord"));
            verifyThat(menu.getItems().get(1), MenuItemMatchers.hasText("Dépenses"));
            verifyThat(menu.getItems().get(2), MenuItemMatchers.hasText("Revenus"));
        });
    }

    @Test
    public void shouldChangeStageWhenClickOnMenu(FxRobot robot) throws TimeoutException {
        robot.clickOn("Navigation");

        WaitForAsyncUtils.waitFor(2, TimeUnit.SECONDS, () -> robot.lookup("Dépenses").match(NodeQueryUtils.isVisible()).tryQuery().isPresent());

        robot.clickOn("Dépenses", Motion.VERTICAL_FIRST, MouseButton.PRIMARY);

        verifyThat(".title-text", hasText("Tableau récapitulatif des dépenses"));
    }

    @Test
    public void shouldChangeStageWhenClickOnMenu2(FxRobot robot) throws TimeoutException {
        robot.clickOn("Navigation");

        WaitForAsyncUtils.waitFor(2, TimeUnit.SECONDS, () -> robot.lookup("Tableau de bord").match(NodeQueryUtils.isVisible()).tryQuery().isPresent());

        robot.clickOn("Tableau de bord", Motion.VERTICAL_FIRST, MouseButton.PRIMARY);

        verifyThat(".title-text", hasText("Tableau de bord"));
    }

    @Test
    public void shouldHaveTitle() {
        verifyThat(".title-text", hasText("Tableau récapitulatif des revenus"));
    }

    @Test
    public void shouldHaveButtonAdd(FxRobot robot) {
        verifyThat("Ajouter", isVisible());

        robot.clickOn("Ajouter");

        verifyThat(".title-text", hasText("Tableau récapitulatif des revenus"));

        verifyThat("#dateField", isVisible());

        TextField textField = robot.lookup("#dateField").query();

        String inputText = "01/01/21";
        robot.clickOn("#dateField").write(inputText);

        assertThat("La valeur du champ de texte est incorrecte.", textField.getText(), equalTo(inputText));
        verifyThat("Annuler", isVisible());

        robot.clickOn("Annuler");
        robot.sleep(1000);
    }

    @Test
    public void shouldInsertIncomes(FxRobot robot) {
        verifyThat("Ajouter", isVisible());
        robot.clickOn("Ajouter");
        verifyThat(".title-text", hasText("Tableau récapitulatif des revenus"));

        verifyThat("#dateField", isVisible());
        TextField textField = robot.lookup("#dateField").query();
        String inputText = "01/01/21";
        robot.clickOn("#dateField").write(inputText);
        assertThat("La valeur du champ de texte est incorrecte.", textField.getText(), equalTo(inputText));

        verifyThat("#salaryField", isVisible());
        TextField textField2 = robot.lookup("#salaryField").query();
        String inputText2 = "50.0";
        robot.clickOn("#salaryField").write(inputText2);
        assertThat("La valeur du champ de texte est incorrecte.", textField2.getText(), equalTo(inputText2));

        verifyThat("#helpersField", isVisible());
        TextField textField3 = robot.lookup("#helpersField").query();
        String inputText3 = "60.0";
        robot.clickOn("#helpersField").write(inputText3);
        assertThat("La valeur du champ de texte est incorrecte.", textField3.getText(), equalTo(inputText3));

        verifyThat("#autoBusinessField", isVisible());
        TextField textField4 = robot.lookup("#autoBusinessField").query();
        String inputText4 = "70.0";
        robot.clickOn("#autoBusinessField").write(inputText4);
        assertThat("La valeur du champ de texte est incorrecte.", textField4.getText(), equalTo(inputText4));

        verifyThat("#passiveIncomeField", isVisible());
        TextField textField5 = robot.lookup("#passiveIncomeField").query();
        String inputText5 = "80.0";
        robot.clickOn("#passiveIncomeField").write(inputText5);
        assertThat("La valeur du champ de texte est incorrecte.", textField5.getText(), equalTo(inputText5));

        verifyThat("#otherField", isVisible());
        TextField textField6 = robot.lookup("#otherField").query();
        String inputText6 = "90.0";
        robot.clickOn("#otherField").write(inputText6);
        assertThat("La valeur du champ de texte est incorrecte.", textField6.getText(), equalTo(inputText6));

        verifyThat("Créer", isVisible());
        robot.clickOn("Créer");
        robot.sleep(1000);
    }

    @Test
    public void shouldHaveIncomes(FxRobot robot) {
        verifyThat("#IncomeTable", isVisible());

        TableView<?> expenseTable = robot.lookup("#IncomeTable").queryAs(TableView.class);

        assertColumnExists(expenseTable, "Période");
        assertColumnExists(expenseTable, "Total");
        assertColumnExists(expenseTable, "Salaire");
        assertColumnExists(expenseTable, "Aides");
        assertColumnExists(expenseTable, "Auto-entreprise");
        assertColumnExists(expenseTable, "Revenus passifs");
        assertColumnExists(expenseTable, "Autres");
    }

    private void assertColumnExists(TableView<?> table, String columnName) {
        TableColumn<?, ?> column = table.getColumns().stream()
                .filter(c -> c.getText().equals(columnName))
                .findFirst()
                .orElse(null);
        assertNotNull(column, "La colonne " + columnName + " n'a pas été trouvée dans la table.");
    }
}