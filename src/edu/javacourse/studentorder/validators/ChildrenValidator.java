package edu.javacourse.studentorder.validators;

import edu.javacourse.studentorder.domain.AnswerChildrenRegister;
import edu.javacourse.studentorder.domain.StudentOrder;

public class ChildrenValidator {

    public AnswerChildrenRegister checkChildren(StudentOrder so) {
        System.out.println("Заявка №" + so.getStudentOrderId() + ". Дите проверили");
        AnswerChildrenRegister cr = new AnswerChildrenRegister();
        return cr;
    }
}
