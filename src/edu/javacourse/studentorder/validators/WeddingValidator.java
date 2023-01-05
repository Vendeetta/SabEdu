package edu.javacourse.studentorder.validators;

import edu.javacourse.studentorder.domain.AnswerWeddingRegister;
import edu.javacourse.studentorder.domain.StudentOrder;

public class WeddingValidator {

    public AnswerWeddingRegister checkWedding(StudentOrder so) {
        AnswerWeddingRegister awr = new AnswerWeddingRegister();
        System.out.println("Заявка №" + so.getStudentOrderId() + ". Семья проверена");
        return awr;
    }
}
