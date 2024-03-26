package com.example.financemanager.model;

import java.time.LocalDate;
import java.util.Objects;

public class Income implements Comparable<Income> {

    private LocalDate date;
    private float salary;
    private float aid;
    private float selfEmployment;
    private float passiveIncome;

    public Income(LocalDate date, float salary, float aid, float selfEmployment, float passiveIncome) {
        this.date = date;
        this.salary = salary;
        this.aid = aid;
        this.selfEmployment = selfEmployment;
        this.passiveIncome = passiveIncome;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public float getSalary() {
        return salary;
    }

    public void setSalary(float salary) {
        this.salary = salary;
    }

    public float getAid() {
        return aid;
    }

    public void setAid(float aid) {
        this.aid = aid;
    }

    public float getSelfEmployment() {
        return selfEmployment;
    }

    public void setSelfEmployment(float selfEmployment) {
        this.selfEmployment = selfEmployment;
    }

    public float getPassiveIncome() {
        return passiveIncome;
    }

    public void setPassiveIncome(float passiveIncome) {
        this.passiveIncome = passiveIncome;
    }

    @Override
    public int compareTo(Income otherIncome) {
        return this.date.compareTo(otherIncome.date);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Income income = (Income) o;
        return Float.compare(income.salary, salary) == 0 &&
                Float.compare(income.aid, aid) == 0 &&
                Float.compare(income.selfEmployment, selfEmployment) == 0 &&
                Float.compare(income.passiveIncome, passiveIncome) == 0 &&
                Objects.equals(date, income.date);
    }

    @Override
    public int hashCode() {
        return Objects.hash(date, salary, aid, selfEmployment, passiveIncome);
    }

    @Override
    public String toString() {
        return "Income{" +
                "date=" + date +
                ", salary=" + salary +
                ", aid=" + aid +
                ", selfEmployment=" + selfEmployment +
                ", passiveIncome=" + passiveIncome +
                '}';
    }
}
