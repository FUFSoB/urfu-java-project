package Table;

public class Row {
    private final String[] data;

    public Row(String[] row) {
        this.data = row;
    }

    public boolean isFullyDefined() {
        for (var cell : data) {
            if (cell.isEmpty())
                return false;
        }
        return true;
    }

    public int getLength() {
        return data.length;
    }

    public String get(int index) {
        return data[index];
    }

    public String toString() {
        return String.join(", ", data);
    }

    public String[] guessTypes() {
        var result = new String[data.length];
        for (int i = 0; i < data.length; i++) {
            var cell = data[i];
            if (cell.matches("^\\d+$"))
                result[i] = "INTEGER";
            else if (cell.matches("^\\d+\\.\\d+$"))
                result[i] = "REAL";
            else
                result[i] = "TEXT";
        }
        return result;
    }
}
