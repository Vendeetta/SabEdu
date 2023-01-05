package edu.javacourse.studentorder.validators;

import edu.javacourse.studentorder.domain.Person;
import edu.javacourse.studentorder.domain.register.AnswerCityRegister;
import edu.javacourse.studentorder.domain.Child;
import edu.javacourse.studentorder.domain.StudentOrder;
import edu.javacourse.studentorder.domain.register.AnswerCityRegisterItem;
import edu.javacourse.studentorder.domain.register.CityRegisterResponse;
import edu.javacourse.studentorder.exception.CityRegisterException;
import edu.javacourse.studentorder.exception.TransportException;

public class CityRegisterValidator {

    private String hostName;
    private String login;
    private String password;
    private static final String NO_GRN = "NO_GRN";
    private CityRegisterChecker personChecker;

    public CityRegisterValidator() {
        this.personChecker = new FakeCityRegisterChecker();
    }

    public AnswerCityRegister checkCityRegister(StudentOrder so) {
        AnswerCityRegister answerCityRegister = new AnswerCityRegister();
        answerCityRegister.addItem(checkPerson(so.getHusband()));
        answerCityRegister.addItem(checkPerson(so.getWife()));
        for (Child ch : so.getChildren()) {
            answerCityRegister.addItem(checkPerson(ch));
        }
        System.out.println("Заявка №" + so.getStudentOrderId() + ". Город проверен");


        return answerCityRegister;
    }

    private AnswerCityRegisterItem checkPerson(Person person) {
        AnswerCityRegisterItem.CityStatus status = null;
        AnswerCityRegisterItem.CityError error = null;
        try {
            CityRegisterResponse tmp = personChecker.checkPerson(person);
            status = tmp.isExisting() ? AnswerCityRegisterItem.CityStatus.YES : AnswerCityRegisterItem.CityStatus.NO;
        } catch (CityRegisterException e) {
            e.printStackTrace();
            status = AnswerCityRegisterItem.CityStatus.ERROR;
            error = new AnswerCityRegisterItem.CityError(e.getCode(), e.getMessage());
        } catch (TransportException e) {
            e.printStackTrace();
            status = AnswerCityRegisterItem.CityStatus.ERROR;
            error = new AnswerCityRegisterItem.CityError(NO_GRN, e.getMessage());
        }
        AnswerCityRegisterItem item = new AnswerCityRegisterItem(status, person, error);
        return item;
    }
}
