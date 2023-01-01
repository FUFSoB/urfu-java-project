import Table.Database;

import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.SQLException;

public class Main {
    public static void main(String[] args) throws SQLException, ClassNotFoundException {
        var tableName = "countryData";
        var sqlPath = "data.sqlite";
        var csvPath = "data.csv";
        var sqlExists = Files.exists(Path.of(sqlPath));

        var database = new Database(sqlPath);
        if (!sqlExists)
            database.createFromCsv(csvPath, tableName);

        var tasks = new Tasks(tableName, database);
        tasks.executeAll();
    }
}