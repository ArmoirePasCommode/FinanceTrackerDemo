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
        verifyThat("#", isVisible());

        robot.clickOn("");

        verifyThat("#", isVisible());

        verifyThat(".title-text", hasText("Tableau récapitulatif des revenus"));

        verifyThat("#myTextField", isVisible());

        TextField textField = robot.lookup("#myTextField").query();

        String inputText = "Test Input";
        robot.clickOn("#myTextField").write(inputText);

        assertThat("La valeur du champ de texte est incorrecte.", textField.getText(), equalTo(inputText));

        robot.press(KeyCode.X).release(KeyCode.X);
        robot.sleep(1000);
    }

    @Test
    public void shouldHaveIncomes(FxRobot robot) {
        verifyThat("#expenseTable", isVisible());

        TableView<?> expenseTable = robot.lookup("#expenseTable").queryAs(TableView.class);

        assertColumnExists(expenseTable, "Période");
        assertColumnExists(expenseTable, "Total");
        assertColumnExists(expenseTable, "Logement");
        assertColumnExists(expenseTable, "Nourriture");
        assertColumnExists(expenseTable, "Sorties");
        assertColumnExists(expenseTable, "Transport");
    }

    private void assertColumnExists(TableView<?> table, String columnName) {
        TableColumn<?, ?> column = table.getColumns().stream()
                .filter(c -> c.getText().equals(columnName))
                .findFirst()
                .orElse(null);
        assertNotNull(column, "La colonne " + columnName + " n'a pas été trouvée dans la table.");
    }
}