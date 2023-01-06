package edu.javacourse.studentorder.dao;

import edu.javacourse.studentorder.config.Config;
import edu.javacourse.studentorder.domain.*;
import edu.javacourse.studentorder.exception.DaoException;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class StudentOrderDaoImpl implements StudentOrderDao {

    private static final String INSERT_ORDER = "INSERT INTO jc_student_order(" +
            "student_order_status, student_order_date, h_sur_name, h_given_name, h_patronymic, h_date_of_birth, h_passport_seria, h_passport_number, h_passport_date, h_passport_office_id, h_post_index, h_street_code, h_building, h_extension, h_apartment, h_university_id, h_student_number, w_sur_name, w_given_name, w_patronymic, w_date_of_birth, w_passport_seria, w_passport_number, w_passport_date, w_passport_office_id, w_post_index, w_street_code, w_building, w_extension, w_apartment, w_university_id, w_student_number, certificate_id, register_office_id, marriage_date)" +
            "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";

    private static final String INSERT_CHILD = "INSERT INTO public.jc_student_child(" +
            "student_order_id, c_sur_name, c_given_name, c_patronymic, c_date_of_birth, c_certificate_number, c_certificate_date, c_register_office_id, c_post_index, c_street_code, c_building, c_extension, c_apartment)" +
            "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";

    private static final String SELECT_ORDER =
            "SELECT so.*, ro.r_office_area_id, ro.r_office_name, " +
                    "po_h.p_office_area_id h_p_office_area, po_h.p_office_name h_p_office_name, " +
                    "po_w.p_office_area_id w_p_office_area, po_h.p_office_name w_p_office_name " +
                    "FROM jc_student_order so " +
                    "INNER JOIN jc_register_office ro ON ro.r_office_id = so.register_office_id " +
                    "INNER JOIN jc_passport_office po_h ON po_h.p_office_id = so.h_passport_office_id " +
                    "INNER JOIN jc_passport_office po_w ON po_w.p_office_id = so.w_passport_office_id " +
                    "WHERE student_order_status = ? " +
                    "ORDER BY student_order_date";

    private static final String SELECT_CHILD =
            "SELECT soc.*, ro.r_office_area_id, ro.r_office_name " +
                    "FROM jc_student_child soc " +
                    "INNER JOIN jc_register_office ro ON ro.r_office_id = soc.c_register_office_id  " +
                    "WHERE soc.student_order_id IN ";


    //TODO refactoring - make one method.
    private Connection getConnection() throws SQLException {
        Connection con = DriverManager.getConnection(Config.getProperty(Config.DB_URL), Config.getProperty(Config.DB_LOGIN), Config.getProperty(Config.DB_PASSWORD));
        return con;
    }

    @Override
    public Long saveStudentOrder(StudentOrder so) throws DaoException {
        Long result = null;
        try (Connection con = getConnection()) {

            con.setAutoCommit(false);
            PreparedStatement stmnt = con.prepareStatement(INSERT_ORDER, new String[]{"student_order_id"});

            try {
                //Header
                stmnt.setInt(1, StudentOrderStatus.START.ordinal());
                stmnt.setTimestamp(2, Timestamp.valueOf(LocalDateTime.now()));

                //Husband
                setParamsForAdult(stmnt, 3, so.getHusband());

                //Wife
                setParamsForAdult(stmnt, 18, so.getWife());

                //Marriage
                stmnt.setString(33, so.getMarriageCertificateId());
                stmnt.setLong(34, so.getMarriageOffice().getOfficeId());
                stmnt.setDate(35, Date.valueOf(so.getMarriageDate()));

                stmnt.executeUpdate();

                ResultSet gkRs = stmnt.getGeneratedKeys();
                if (gkRs.next()) {
                    result = gkRs.getLong(1);
                }

                saveChildren(con, so, result);
                con.commit();
            } catch (SQLException e) {
                con.rollback();
                throw e;
            }

        } catch (SQLException e) {
            throw new DaoException(e);
        }
        return result;
    }

    private void saveChildren(Connection con, StudentOrder so, Long soId) throws SQLException {
        try (PreparedStatement stmt = con.prepareStatement(INSERT_CHILD)) {
            for (Child child : so.getChildren()) {
                stmt.setLong(1, soId);
                setParamsForChild(child, stmt);
                stmt.addBatch();
            }
            stmt.executeBatch();
        }
    }

    private void setParamsForChild(Child child, PreparedStatement stmt) throws SQLException {
        setParamsForPerson(stmt, 2, child);
        stmt.setString(6, child.getCertificateNumber());
        stmt.setDate(7, Date.valueOf(child.getIssueDate()));
        stmt.setLong(8, child.getIssueDepartment().getOfficeId());
        setParamsForAddress(stmt, 9, child);
    }

    private void setParamsForAdult(PreparedStatement stmnt, int start, Adult adult) throws SQLException {
        start = setParamsForPerson(stmnt, start, adult);
        stmnt.setString(start++, adult.getPassportSeria());
        stmnt.setString(start++, adult.getPassportNumber());
        stmnt.setDate(start++, Date.valueOf(adult.getIssueDate()));
        stmnt.setLong(start++, adult.getIssueDepartment().getOfficeId());
        start = setParamsForAddress(stmnt, start, adult);
        stmnt.setLong(start++, adult.getUniversity().getUniversity_id());
        stmnt.setString(start, adult.getStudentId());
    }

    private static int setParamsForAddress(PreparedStatement stmnt, int start, Person person) throws SQLException {
        Address address = person.getAddress();
        stmnt.setString(start++, address.getPostCode());
        stmnt.setLong(start++, address.getStreet().getStreet_code());
        stmnt.setString(start++, address.getBuilding());
        stmnt.setString(start++, address.getExtension());
        stmnt.setString(start++, address.getApartment());
        return start;
    }

    private static int setParamsForPerson(PreparedStatement stmnt, int start, Person person) throws SQLException {
        stmnt.setString(start++, person.getSurname());
        stmnt.setString(start++, person.getGivenName());
        stmnt.setString(start++, person.getPatronymic());
        stmnt.setDate(start++, Date.valueOf(person.getDateOfBirthday()));
        return start;
    }

    @Override
    public List<StudentOrder> getStudentOrders() throws DaoException {
        List<StudentOrder> result = new LinkedList<>();
        try (Connection con = getConnection()) {
            con.setAutoCommit(false);
            PreparedStatement stmnt = con.prepareStatement(SELECT_ORDER);
            stmnt.setInt(1, StudentOrderStatus.START.ordinal());
            ResultSet rs = stmnt.executeQuery();

            while (rs.next()) {
                StudentOrder so = new StudentOrder();
                fillStudentOrder(rs, so);
                fillMarriage(rs, so);
                Adult husband = fillAdult(rs, "h_");
                Adult wife = fillAdult(rs, "w_");
                so.setHusband(husband);
                so.setWife(wife);
                result.add(so);
            }

            findChildren(con, result);
            rs.close();

        } catch (SQLException e) {
            throw new DaoException(e);
        }

        return result;
    }

    private void findChildren(Connection con, List<StudentOrder> result) throws SQLException {
        String cl = "(" + result.stream().map(so -> String.valueOf(so.getStudentOrderId()))
                .collect(Collectors.joining(",")) + ")";

        Map<Long, StudentOrder> soMap = result.stream().collect(Collectors
                .toMap(so -> so.getStudentOrderId(), so -> so));

        try (PreparedStatement statement = con.prepareStatement(SELECT_CHILD + cl)) {
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                StudentOrder so = soMap.get(rs.getLong("student_order_id"));
                Child child = fillChild(rs);
                so.addChild(child);
            }
        }
    }

    private Child fillChild(ResultSet rs) throws SQLException {
        Child child = new Child();
        child.setCertificateNumber(rs.getString("c_certificate_number"));
        child.setSurname(rs.getString("c_sur_name"));
        child.setGivenName(rs.getString("c_given_name"));
        child.setPatronymic(rs.getString("c_patronymic"));
        child.setDateOfBirthday(rs.getDate("c_date_of_birth").toLocalDate());
        RegisterOffice ro = new RegisterOffice();
        ro.setOfficeAreaId(rs.getString("r_office_area_id"));
        ro.setOfficeName(rs.getString("r_office_name"));
        ro.setOfficeId(rs.getLong("c_register_office_id"));
        child.setIssueDepartment(ro);
        child.setIssueDate(rs.getDate("c_certificate_date").toLocalDate());
        String postCode = rs.getString("c_post_index");
        Long streetCode = rs.getLong("c_street_code");
        String building = rs.getString("c_building");
        String extension = rs.getString("c_extension");
        String apartment = rs.getString("c_apartment");
        Street street = new Street();
        street.setStreet_code(streetCode);
        Address address = new Address(postCode, street, building, extension, apartment);
        child.setAddress(address);
        return child;
    }

    private Adult fillAdult(ResultSet rs, String pref) throws SQLException {
        Adult adult = new Adult();
        adult.setSurname(rs.getString(pref + "sur_name"));
        adult.setGivenName(rs.getString(pref + "given_name"));
        adult.setPatronymic(rs.getString(pref + "patronymic"));
        adult.setDateOfBirthday(rs.getDate(pref + "date_of_birth").toLocalDate());
        adult.setPassportSeria(rs.getString(pref + "passport_seria"));
        adult.setPassportNumber(rs.getString(pref + "passport_number"));
        adult.setIssueDate(rs.getDate(pref + "passport_date").toLocalDate());
        PassportOffice issueDepartment = new PassportOffice();
        issueDepartment.setOfficeId(rs.getLong(pref + "passport_office_id"));
        issueDepartment.setOfficeName(rs.getString(pref + "p_office_name"));
        issueDepartment.setOfficeAreaId(rs.getString(pref + "p_office_area"));
        adult.setIssueDepartment(issueDepartment);
        String postCode = rs.getString(pref + "post_index");
        Long streetCode = rs.getLong(pref + "street_code");
        String building = rs.getString(pref + "building");
        String extension = rs.getString(pref + "extension");
        String apartment = rs.getString(pref + "apartment");
        Street street = new Street();
        street.setStreet_code(streetCode);
        Address address = new Address(postCode, street, building, extension, apartment);
        adult.setAddress(address);
        University university = new University(rs.getLong(pref + "university_id"), "");
        adult.setUniversity(university);
        adult.setStudentId(rs.getString(pref + "student_number"));
        return adult;
    }

    private void fillMarriage(ResultSet rs, StudentOrder so) throws SQLException {
        so.setMarriageCertificateId(rs.getString("certificate_id"));
        so.setMarriageDate(rs.getDate("marriage_date").toLocalDate());
        RegisterOffice ro = new RegisterOffice();
        ro.setOfficeId(rs.getLong("register_office_id"));
        ro.setOfficeName(rs.getString("r_office_name"));
        ro.setOfficeAreaId(rs.getString("r_office_area_id"));
        so.setMarriageOffice(ro);
    }

    private void fillStudentOrder(ResultSet rs, StudentOrder so) throws SQLException {
        so.setStudentOrderId(rs.getLong("student_order_id"));
        so.setStudentOrderDate(rs.getTimestamp("student_order_date").toLocalDateTime());
        so.setStatus(StudentOrderStatus.fromValue(rs.getInt("student_order_status")));
    }
}
