package edu.javacourse.studentorder;

import edu.javacourse.studentorder.domain.*;
import edu.javacourse.studentorder.domain.register.AnswerCityRegister;
import edu.javacourse.studentorder.mail.MailSender;
import edu.javacourse.studentorder.validators.ChildrenValidator;
import edu.javacourse.studentorder.validators.CityRegisterValidator;
import edu.javacourse.studentorder.validators.StudentValidator;
import edu.javacourse.studentorder.validators.WeddingValidator;

import java.util.LinkedList;
import java.util.List;

public class StudentOrderValidator {

    private ChildrenValidator cv;
    private WeddingValidator wv;
    private CityRegisterValidator crv;
    private StudentValidator sv;
    private MailSender sm;

    public StudentOrderValidator() {
        this.cv = new ChildrenValidator();
        this.wv = new WeddingValidator();
        this.crv = new CityRegisterValidator();
        this.sv = new StudentValidator();
        this.sm = new MailSender();
    }

    public static void main(String[] args) {
        StudentOrderValidator sov = new StudentOrderValidator();
        sov.checkAll();
    }

    private void checkAll() {
        List<StudentOrder> so = readStudenstOrder();
        for (StudentOrder oneSO : so)
            if(oneSO != null)
            checkOneOrder(oneSO);
    }

    public void checkOneOrder(StudentOrder so){
        AnswerCityRegister answerCityRegister = checkCityRegister(so);
        System.out.println(answerCityRegister.getItems());
//        AnswerWeddingRegister answerWeddingRegister = checkWedding(so);
//        AnswerChildrenRegister answerChildrenRegister = checkChildren(so);
//        AnswerStudentRegister answerStudentRegister = checkStudent(so);
//        sendMail(so);

    }
    public List<StudentOrder> readStudenstOrder(){

        List<StudentOrder> soList = new LinkedList<>();
        for (int i = 0; i < 5; i++) {
            soList.add(SaveStudentOrder.buildStudentOrder(i));
        }
        return  soList;
    }
    public AnswerCityRegister checkCityRegister(StudentOrder so){
        return crv.checkCityRegister(so);
    }
    private AnswerStudentRegister checkStudent(StudentOrder so) {
        return sv.checkStudent(so);
    }

    private AnswerChildrenRegister checkChildren(StudentOrder so) {
       return cv.checkChildren(so);
    }

    private AnswerWeddingRegister checkWedding(StudentOrder so) {
        return wv.checkWedding(so);
    }

    private void sendMail(StudentOrder so) {
        System.out.println("Письмо отправлено по заявке №" + so.getStudentOrderId() + ". ");
    }
}
