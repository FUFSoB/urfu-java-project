import Table.Database;

import java.sql.SQLException;

public class Main {
    public static void main(String[] args) throws SQLException, ClassNotFoundException {
        var database = new Database("data.sqlite");
        database.createFromCsv("data.csv", "countryData");
    }
}