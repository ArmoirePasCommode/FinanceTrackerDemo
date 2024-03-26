package com.example.financemanager.controller;

import com.example.financemanager.db.ExpenseDAO;
import com.example.financemanager.db.IncomeDAO;
import com.example.financemanager.model.Expense;
import com.example.financemanager.model.Income;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.*;
import javafx.scene.control.ChoiceBox;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;

public class DashboardController {

    @FXML
    private PieChart pieChart;

    @FXML
    private LineChart<String, Float> lineChart;

    @FXML
    private BarChart<String, Float> barChart;

    @FXML
    private CategoryAxis xAxis;

    @FXML
    private NumberAxis yAxis;

    @FXML
    private CategoryAxis barChartXAxis;

    @FXML
    private NumberAxis barChartYAxis;

    @FXML
    private ChoiceBox<String> periodChoiceBox;

    private final static DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("MMM yy");
    private final static DateTimeFormatter FULL_DATE_FORMAT = DateTimeFormatter.ofPattern("MMMM yyyy");

    public void initialize() {
        LocalDate date = LocalDate.now();

        for (int i = 0; i < 12; i++) {
            periodChoiceBox.getItems().add(date.format(FULL_DATE_FORMAT));
            date = date.minusMonths(1);
        }

        loadExpenses(date);

        loadChartData(date);

        periodChoiceBox.getSelectionModel().selectFirst();
    }

    private void loadExpenses(LocalDate currentMonth) {
        List<Expense> lastExpenses = ExpenseDAO.findLastExpensesEndingAtCurrentMonth(12, currentMonth);
        if (!lastExpenses.isEmpty()) {
            // Utiliser la première dépense de la liste comme représentative du mois courant
            // Cette logique peut être ajustée selon vos besoins spécifiques
            Expense currentMonthExpense = lastExpenses.get(0);

            // Nettoyer le pie chart avant de le remplir à nouveau
            pieChart.getData().clear();

            // Ajouter les données au pie chart
            pieChart.getData().addAll(
                    new PieChart.Data("Logement", currentMonthExpense.getHousing()),
                    new PieChart.Data("Nourriture", currentMonthExpense.getFood()),
                    new PieChart.Data("Sortie", currentMonthExpense.getGoingOut()),
                    new PieChart.Data("Transport", currentMonthExpense.getTransportation()),
                    new PieChart.Data("Voyage", currentMonthExpense.getTravel()),
                    new PieChart.Data("Impôts", currentMonthExpense.getTax()),
                    new PieChart.Data("Autres", currentMonthExpense.getOther())
            );
        }
        // Initialiser les séries pour chaque catégorie de dépenses
        XYChart.Series<String, Float> seriesHousing = new XYChart.Series<>();
        seriesHousing.setName("Logement");
        XYChart.Series<String, Float> seriesFood = new XYChart.Series<>();
        seriesFood.setName("Nourriture");
        XYChart.Series<String, Float> seriesGoingOut = new XYChart.Series<>();
        seriesGoingOut.setName("Sortie");
        XYChart.Series<String, Float> seriesTransportation = new XYChart.Series<>();
        seriesTransportation.setName("Transport");
        XYChart.Series<String, Float> seriesTravel = new XYChart.Series<>();
        seriesTravel.setName("Voyage");
        XYChart.Series<String, Float> seriesTax = new XYChart.Series<>();
        seriesTax.setName("Impôts");
        XYChart.Series<String, Float> seriesOther = new XYChart.Series<>();
        seriesOther.setName("Autres");

        LocalDate dateIterator = currentMonth.minusMonths(11); // Commencez 11 mois en arrière

        while (!dateIterator.isAfter(currentMonth)) {
            String monthYearKey = dateIterator.format(DATE_FORMAT);
            Expense monthExpense = lastExpenses.stream()
                    .filter(expense -> expense.getDate().format(DATE_FORMAT).equals(monthYearKey))
                    .findFirst()
                    .orElse(null);

            // Ajouter les données au graphique pour chaque catégorie, pour le mois courant
            seriesHousing.getData().add(new XYChart.Data<>(monthYearKey, monthExpense != null ? monthExpense.getHousing() : 0));
            seriesFood.getData().add(new XYChart.Data<>(monthYearKey, monthExpense != null ? monthExpense.getFood() : 0));
            seriesGoingOut.getData().add(new XYChart.Data<>(monthYearKey, monthExpense != null ? monthExpense.getGoingOut() : 0));
            seriesTransportation.getData().add(new XYChart.Data<>(monthYearKey, monthExpense != null ? monthExpense.getTransportation() : 0));
            seriesTravel.getData().add(new XYChart.Data<>(monthYearKey, monthExpense != null ? monthExpense.getTravel() : 0));
            seriesTax.getData().add(new XYChart.Data<>(monthYearKey, monthExpense != null ? monthExpense.getTax() : 0));
            seriesOther.getData().add(new XYChart.Data<>(monthYearKey, monthExpense != null ? monthExpense.getOther() : 0));

            // Passer au mois suivant
            dateIterator = dateIterator.plusMonths(1);
        }

        lineChart.getData().clear();
        lineChart.getData().addAll(
                seriesHousing,
                seriesFood,
                seriesGoingOut,
                seriesTransportation,
                seriesTravel,
                seriesTax,
                seriesOther
        );
    }

    public void changePeriod(ActionEvent actionEvent) {
        var periodSelected = periodChoiceBox.getSelectionModel().getSelectedItem();
        LocalDate dateSelected = LocalDate.parse("01 " + periodSelected, DateTimeFormatter.ofPattern("dd MMMM yyyy"));
        loadExpenses(dateSelected);
        loadChartData(dateSelected);
    }

    private void loadChartData(LocalDate currentMonth) {
        List<Expense> lastExpenses = ExpenseDAO.findLastExpensesEndingAtCurrentMonth(12, currentMonth);
        List<Income> lastIncomes = IncomeDAO.findLastIncomeEndingAtCurrentMonth(12, currentMonth);

        barChart.getData().clear();

        XYChart.Series<String, Float> seriesIncome = new XYChart.Series<>();
        seriesIncome.setName("Revenus");
        XYChart.Series<String, Float> seriesExpense = new XYChart.Series<>();
        seriesExpense.setName("Dépenses");

        LocalDate dateIterator = currentMonth.minusMonths(11);

        while (!dateIterator.isAfter(currentMonth)) {
            String monthYearKey = dateIterator.format(DATE_FORMAT);
            float totalExpenses = 0;
            float totalIncome = 0;

            for (Expense expense : lastExpenses) {
                if (expense.getDate().format(DATE_FORMAT).equals(monthYearKey)) {
                    totalExpenses += expense.getTotal();
                }
            }

            for (Income income : lastIncomes) {
                if (income.getDate().format(DATE_FORMAT).equals(monthYearKey)) {
                    totalIncome += income.getTotal();
                }
            }

            seriesExpense.getData().add(new XYChart.Data<>(monthYearKey, totalExpenses));
            seriesIncome.getData().add(new XYChart.Data<>(monthYearKey, totalIncome));

            dateIterator = dateIterator.plusMonths(1);
        }

        barChart.getData().addAll(seriesExpense, seriesIncome);
    }
}
