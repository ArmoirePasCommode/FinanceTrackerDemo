package com.example.financemanager.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Income {
    private final LocalDate date;
    private final float total;
    private final float salary;
    private final float helpers;
    private final float autoBusiness;
    private final float passiveIncome;
    private final float other;

    private final static String PRICE_FORMAT = "%.2f €";

    private final static DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("MMMM yyyy");

    public Income(LocalDate date, float salary, float helpers, float autoBusiness, float passiveIncome, float other) {
        this.date = date;
        this.total = salary + helpers + autoBusiness + passiveIncome + other;
        this.salary = salary;
        this.helpers = helpers;
        this.autoBusiness = autoBusiness;
        this.passiveIncome = passiveIncome;
        this.other = other;
    }

    public StringProperty dateProperty() {
        return new SimpleStringProperty(date.format(DATE_FORMAT));
    }

    public StringProperty totalProperty() {
        return formatAmount(total);
    }

    private SimpleStringProperty formatAmount(float amount) {
        return new SimpleStringProperty(String.format(PRICE_FORMAT, amount));
    }

    public StringProperty salaryProperty() {
        return formatAmount(salary);
    }

    public StringProperty helpersProperty() {
        return formatAmount(helpers);
    }

    public StringProperty autoBusinessProperty() {
        return formatAmount(autoBusiness);
    }

    public StringProperty passiveIncomeProperty() {
        return formatAmount(passiveIncome);
    }

    public StringProperty otherProperty() {
        return formatAmount(other);
    }

    public LocalDate getDate() {
        return date;
    }

    public float getTotal() {
        return total;
    }

    public float getSalary() {
        return salary;
    }

    public float getHelpers() {
        return helpers;
    }

    public float getAutoBusiness() {
        return autoBusiness;
    }

    public float getPassiveIncome() {
        return passiveIncome;
    }

    public float getOther() {
        return other;
    }

    @Override
    public String toString() {
        return "Income{" +
                "date=" + date +
                ", total=" + total +
                ", salary=" + salary +
                ", helpers=" + helpers +
                ", autoBusiness=" + autoBusiness +
                ", passiveIncome=" + passiveIncome +
                ", other=" + other +
                '}';
    }

    public int compareTo(Income income) {
        return -this.date.compareTo(income.date);
    }
}
