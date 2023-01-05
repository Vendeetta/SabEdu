package edu.javacourse.studentorder.validators;

import edu.javacourse.studentorder.domain.AnswerStudentRegister;
import edu.javacourse.studentorder.domain.StudentOrder;

public class StudentValidator {

    public AnswerStudentRegister checkStudent(StudentOrder so) {
        AnswerStudentRegister asr = new AnswerStudentRegister();
        System.out.println("Заявка №" + so.getStudentOrderId() + ". Студент проверен");
        return asr;
    }
}
