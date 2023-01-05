package edu.javacourse.studentorder.dao;

import edu.javacourse.studentorder.config.Config;
import edu.javacourse.studentorder.domain.CountryArea;
import edu.javacourse.studentorder.domain.PassportOffice;
import edu.javacourse.studentorder.domain.RegisterOffice;
import edu.javacourse.studentorder.domain.Street;
import edu.javacourse.studentorder.exception.DaoException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DictionaryDaoImpl implements DictionaryDao {
    private static final String GET_STREET = "SELECT street_code, street_name FROM jc_street WHERE UPPER(street_name) LIKE UPPER(?)";
    private static final String GET_PASSPORT = "SELECT * FROM jc_passport_office WHERE p_office_area_id = ?";
    private static final String GET_REGISTER = "SELECT * FROM jc_register_office WHERE r_office_area_id = ?";
    private static final String GET_AREA = "select * from jc_country_struct where area_id like ? and area_id <> ?";

    //TODO refactoring - make one method.
    private Connection getConnection() throws SQLException {
        Connection con = DriverManager.getConnection(Config.getProperty(Config.DB_URL), Config.getProperty(Config.DB_LOGIN), Config.getProperty(Config.DB_PASSWORD));
        return con;
    }

    @Override
    public List<Street> findStreets(String pattern) throws DaoException {
        List<Street> result = new ArrayList<>();
        try (Connection con = getConnection()) {
            PreparedStatement stmnt = con.prepareStatement(GET_STREET);
            stmnt.setString(1, "%" + pattern + "%");
            ResultSet rs = stmnt.executeQuery();
            while (rs.next()) {
                result.add(new Street(rs.getLong("street_code"), rs.getString("street_name")));
            }
        } catch (SQLException e) {
            throw new DaoException(e);
        }
        return result;
    }

    @Override
    public List<PassportOffice> findPassportOffices(String areaId) throws DaoException {
        List<PassportOffice> result = new ArrayList<>();
        try (Connection con = getConnection()) {
            PreparedStatement stmnt = con.prepareStatement(GET_PASSPORT);
            stmnt.setString(1, areaId);
            ResultSet rs = stmnt.executeQuery();
            while (rs.next()) {
                result.add(new PassportOffice(rs.getLong("p_office_id"), rs.getString("p_office_area_id"), rs.getString("p_office_name")));
            }
        } catch (SQLException e) {
            throw new DaoException(e);
        }
        return result;
    }

    @Override
    public List<RegisterOffice> findRegisterOffices(String areaId) throws DaoException {
        List<RegisterOffice> result = new ArrayList<>();
        try (Connection con = getConnection()) {
            PreparedStatement stmnt = con.prepareStatement(GET_REGISTER);
            stmnt.setString(1, areaId);
            ResultSet rs = stmnt.executeQuery();
            while (rs.next()) {
                result.add(new RegisterOffice(rs.getLong("r_office_id"), rs.getString("r_office_area_id"), rs.getString("r_office_name")));
            }
        } catch (SQLException e) {
            throw new DaoException(e);
        }
        return result;
    }

    @Override
    public List<CountryArea> findAreas(String areaId) throws DaoException {
        List<CountryArea> result = new ArrayList<>();
        try (Connection con = getConnection()) {
            PreparedStatement stmnt = con.prepareStatement(GET_AREA);
            String param1 = buildParam(areaId);
            String param2 = areaId;
            stmnt.setString(1, param1);
            stmnt.setString(2, param2);
            ResultSet rs = stmnt.executeQuery();
            while (rs.next()) {
                result.add(new CountryArea(rs.getString("area_id"), rs.getString("area_name")));
            }
        } catch (SQLException e) {
            throw new DaoException(e);
        }
        return result;

    }

    private String buildParam(String areaId) throws SQLException {
        if (areaId == null || areaId.trim().isEmpty()){
            return "__0000000000";
        }
        else if(areaId.endsWith("0000000000")){
            return areaId.substring(0, 2) + "___0000000";
        } else if (areaId.endsWith("0000000")) {
            return areaId.substring(0, 5) + "___0000";
        } else if (areaId.endsWith("0000")) {
            return areaId.substring(0, 8) + "____";
        }
        throw new SQLException("Invalid parameter areaId" + areaId);
    }
}
