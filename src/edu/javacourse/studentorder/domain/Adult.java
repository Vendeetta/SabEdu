package edu.javacourse.studentorder.domain;

import java.time.LocalDate;

public class Adult extends Person{
    private String passportSeria;
    private String passportNumber;
    private PassportOffice issueDepartment;
    private LocalDate issueDate;
    private University university;
    private String studentId;

    public Adult(String surname, String givenName, String patronymic, LocalDate dateOfBirthday) {
        super(surname, givenName, patronymic, dateOfBirthday);
    }

    public Adult() {
    }

    public String getPassportSeria() {
        return passportSeria;
    }

    public void setPassportSeria(String passportSeria) {
        this.passportSeria = passportSeria;
    }

    public String getPassportNumber() {
        return passportNumber;
    }

    public void setPassportNumber(String passportNumber) {
        this.passportNumber = passportNumber;
    }

    public PassportOffice getIssueDepartment() {
        return issueDepartment;
    }

    public void setIssueDepartment(PassportOffice issueDepartment) {
        this.issueDepartment = issueDepartment;
    }

    public LocalDate getIssueDate() {
        return issueDate;
    }

    public void setIssueDate(LocalDate issueDate) {
        this.issueDate = issueDate;
    }

    public University getUniversity() {
        return university;
    }

    public void setUniversity(University university) {
        this.university = university;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    @Override
    public String toString() {
        return "Adult{" +
                "passportSeria='" + passportSeria + '\'' +
                ", passportNumber='" + passportNumber + '\'' +
                ", issueDepartment=" + issueDepartment +
                ", issueDate=" + issueDate +
                ", university=" + university +
                ", studentId='" + studentId + '\'' +
                '}';
    }
}
