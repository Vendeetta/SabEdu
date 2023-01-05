package edu.javacourse.studentorder.domain;

import java.time.LocalDate;

public abstract class Person {
    private String surname;
    private String givenName;
    private String patronymic;
    private LocalDate dateOfBirthday;
    private Address address;

    Person(){

    }

    public Person(String surname, String givenName, String patronymic, LocalDate dateOfBirthday) {
        this.surname = surname;
        this.givenName = givenName;
        this.patronymic = patronymic;
        this.dateOfBirthday = dateOfBirthday;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getGivenName() {
        return givenName;
    }

    public void setGivenName(String givenName) {
        this.givenName = givenName;
    }

    public String getPatronymic() {
        return patronymic;
    }

    public void setPatronymic(String patronymic) {
        this.patronymic = patronymic;
    }

    public LocalDate getDateOfBirthday() {
        return dateOfBirthday;
    }

    public void setDateOfBirthday(LocalDate dateOfBirthday) {
        this.dateOfBirthday = dateOfBirthday;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }
}
