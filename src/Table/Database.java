package Table;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Database {
    private final Connection connection;

    public Database(String path) throws ClassNotFoundException, SQLException {
        Class.forName("org.sqlite.JDBC");
        connection = DriverManager.getConnection("jdbc:sqlite:" + path);
        if (connection == null)
            throw new RuntimeException("Не удалось подключиться к базе данных");
    }

    public void createFromCsv(String path, String tableName) throws SQLException {
        var parser = new Parser(path);
        var table = parser.parse(tableName);
        var statement = connection.createStatement();
        statement.setQueryTimeout(30);
        statement.executeUpdate("DROP TABLE IF EXISTS " + tableName);
        statement.executeUpdate(table.getSQLCreateTableQuery());
        var preparedStatement = connection.prepareStatement(table.getSQLInsertQuery());
        for (var row : table.getRows()) {
            for (int i = 0; i < row.getLength(); i++)
                preparedStatement.setString(i + 1, row.get(i));
            preparedStatement.addBatch();
        }
        preparedStatement.executeBatch();
        preparedStatement.close();
        statement.close();
    }

    public ResultSet get(String query) throws SQLException {
        var statement = connection.createStatement();
        statement.setQueryTimeout(30);
        return statement.executeQuery(query);
    }
}
