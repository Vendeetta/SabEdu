package edu.javacourse.studentorder.domain;

import java.time.LocalDate;

public class Child extends Person{
    private String certificateNumber;
    private RegisterOffice issueDepartment;
    private LocalDate issueDate;

    public Child(String surname, String givenName, String patronymic, LocalDate dateOfBirthday) {
        super(surname, givenName, patronymic, dateOfBirthday);
    }

    public Child() {

    }

    public String getCertificateNumber() {
        return certificateNumber;
    }

    public void setCertificateNumber(String certificateNumber) {
        this.certificateNumber = certificateNumber;
    }

    public RegisterOffice getIssueDepartment() {
        return issueDepartment;
    }

    public void setIssueDepartment(RegisterOffice issueDepartment) {
        this.issueDepartment = issueDepartment;
    }

    public LocalDate getIssueDate() {
        return issueDate;
    }

    public void setIssueDate(LocalDate issueDate) {
        this.issueDate = issueDate;
    }
}
