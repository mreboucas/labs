package br.com.openmind.database;

import java.sql.*;

public class LocalDataBase {

    private final Connection connection;

    public LocalDataBase(String name) throws SQLException {
        var url = "jdbc:sqlite:target/" +name+ ".db";
        this.connection = DriverManager.getConnection(url);
    }

    public void createIfNotExists(String sql) {
        try {
            this.connection.createStatement().execute(sql);
        } catch(SQLException e) {
            //Be careful, the sql could be wrong, be really careful.
            e.printStackTrace();
        }

    }

    public void update(String sql, String ... params) throws SQLException {
        PreparedStatement prepareStatement = prepare(sql, params);
        prepareStatement.execute();
    }

    public ResultSet query(String sql, String... params) throws SQLException {
        PreparedStatement prepareStatement = prepare(sql, params);
        return prepareStatement.executeQuery();
    }

    private PreparedStatement prepare(String sql, String[] params) throws SQLException {
        var prepareStatement = connection.prepareStatement(sql);
        for (int i = 0; i < params.length; i++) {
            prepareStatement.setString(i + 1, params[i]);
        }
        return prepareStatement;
    }
}
