package Table;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;

import com.opencsv.CSVReader;

public class Parser {
    private final String path;

    public Parser (String path) {
        this.path = path;
    }

    public Table parse(String tableName) {
        String[] headers = null;
        var rows = new ArrayList<Row>();
        try {
            var br = new BufferedReader(new FileReader(path));
            var reader = new CSVReader(br);
            String[] line;

            while ((line = reader.readNext()) != null) {
                if (headers == null) {
                    headers = line;
                } else {
                    var row = new Row(line);
                    rows.add(row);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Файл не найден: " + path);
        }

        if (headers == null)
            throw new RuntimeException("Файл пустой: " + path);

        return new Table(tableName, headers, rows.toArray(new Row[0]));
    }
}
