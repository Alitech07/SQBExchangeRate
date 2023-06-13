package org.example.service;

import org.example.entity.User;

import java.sql.*;

import static org.example.service.DataBaseService.getDbConnection;

public class UsersService {
    static Connection conn = getDbConnection();
    Statement statement;
    public User getUser(Long id) throws Exception {
        String query ="select * from users where id="+id;
        statement = conn.createStatement();
        ResultSet resultSet = statement.executeQuery(query);
        User user = new User();
        while (resultSet.next()){
            user.setId(resultSet.getString(1));
            user.setFirstName(resultSet.getString(2));
            user.setLastName(resultSet.getString(3));
            user.setUsername(resultSet.getString(4));
            user.setStep(resultSet.getString(6));
        }
        conn.close();
        statement.close();
        return user;
    }

    public boolean addUser(User user){
        PreparedStatement preparedStatement;
        String query = "insert into users(id,first_name,last_name,username,step,photo_id) values(?,?,?,?,?,?)";
        try {
            preparedStatement = conn.prepareStatement(query);
            preparedStatement.setString(1,user.getId());
            preparedStatement.setString(2,user.getFirstName());
            preparedStatement.setString(3, user.getLastName());
            preparedStatement.setString(4, user.getUsername());
            preparedStatement.setString(5, user.getStep());
            preparedStatement.setLong(6,user.getPhotoId());

            preparedStatement.executeUpdate();
            conn.close();
            preparedStatement.close();
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error add user");
            return false;
        }
    }
    public boolean editUser(Long id,String step) throws SQLException {
        statement = conn.createStatement();
        String query = "update users set step="+step+" where id="+id;
        statement.executeUpdate(query);
        conn.close();
        statement.close();
        return true;
    }

    public boolean chackUser(Integer id){
        String query = "select count(*) from users where id"+id;
        try {
            statement = conn.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            int count = 0;
            while(resultSet.next()){
                count = resultSet.getInt(1);
            }
            if (count>0){
                return true;
            }

        } catch (SQLException e) {
            System.out.println("Find user error");
            e.printStackTrace();
        }
        return false;
    }
}
