package Table;

public class Table {
    private final String name;
    private final String[] headers;
    private final String[] camelCaseHeaders;
    private String[] headerTypes = null;
    private final Row[] rows;

    public Table(String name, String[] headers, Row[] rows) {
        this.name = name;
        this.headers = headers;
        this.rows = rows;

        camelCaseHeaders = new String[headers.length];
        for (int i = 0; i < headers.length; i++) {
            var current = headers[i].split("[^a-zA-Z0-9\\s]")[0];
            var words = current.split("\\s+");
            for (int j = 1; j < words.length; j++) {
                words[j] = words[j].substring(0, 1).toUpperCase() + words[j].substring(1);
            }
            words[0] = words[0].toLowerCase();
            camelCaseHeaders[i] = String.join("", words);
        }

        for (var row : rows) {
            if (row.isFullyDefined()) {
                headerTypes = row.guessTypes();
                break;
            }
        }
        if (headerTypes == null) {
            headerTypes = new String[headers.length];
            for (int i = 0; i < headers.length; i++) {
                headerTypes[i] = "TEXT";
            }
        }
    }

    public String getName() {
        return name;
    }

    public Row[] getRows() {
        return rows;
    }

    public String getSQLCreateTableQuery() {
        var sb = new StringBuilder();
        sb.append("CREATE TABLE ").append(name).append(" (");
        for (int i = 0; i < headers.length; i++) {
            sb.append(camelCaseHeaders[i]).append(" ").append(headerTypes[i]);
            if (i != headers.length - 1)
                sb.append(", ");
        }
        sb.append(");");
        return sb.toString();
    }

    public String getSQLInsertQuery() {
        var sb = new StringBuilder();
        sb.append("INSERT INTO ").append(name).append(" (");
        for (int i = 0; i < headers.length; i++) {
            sb.append(camelCaseHeaders[i]);
            if (i != headers.length - 1)
                sb.append(", ");
        }
        sb.append(") VALUES (");
        for (int i = 0; i < headers.length; i++) {
            sb.append("?");
            if (i != headers.length - 1)
                sb.append(", ");
        }
        sb.append(");");
        return sb.toString();
    }
}
