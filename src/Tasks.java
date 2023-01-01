import Table.Database;

import org.knowm.xchart.*;
import org.knowm.xchart.style.Styler;

import java.sql.SQLException;
import java.util.ArrayList;

public class Tasks {
    private final String tableName;
    private final Database database;

    public Tasks(String tableName, Database database) {
        this.tableName = tableName;
        this.database = database;
    }
    public void executeAll() throws SQLException {
        executeTask1();
        executeTask2();
        executeTask3();
    }

    public void executeTask1() throws SQLException {
        // Сформируйте график по показателю экономики объеденив их по странам
        var query = """
                SELECT country, economy
                FROM %s
                GROUP BY country
                """.formatted(tableName);
        var resultSet = database.get(query);
        var countries = new ArrayList<String>();
        var economies = new ArrayList<Double>();
        while (resultSet.next()) {
            var country = resultSet.getString("country");
            var economy = resultSet.getDouble("economy");
            countries.add(country);
            economies.add(economy);
        }

        var chart = new CategoryChartBuilder()
                .width(800)
                .height(600)
                .title("Задание 1")
                .xAxisTitle("Country")
                .yAxisTitle("Economy")
                .build();
        chart.addSeries("Economy", countries, economies);

        chart.getStyler().setToolTipsEnabled(true);
        chart.getStyler().setToolTipType(Styler.ToolTipType.xAndYLabels);
        chart.getStyler().setLegendVisible(false);
        chart.getStyler().setXAxisTicksVisible(false);

        new SwingWrapper<>(chart).displayChart();

        resultSet.close();
    }

    public void executeTask2() throws SQLException {
        // Выведите в консоль страну с самым высоким показателем экономики среди "Latin America and Caribbean" и "Eastern Asia"
        var query = """
                SELECT country
                FROM %s
                WHERE region IN ('Latin America and Caribbean', 'Eastern Asia')
                ORDER BY economy DESC
                LIMIT 1
                """.formatted(tableName);
        var resultSet = database.get(query);
        System.out.println("Задание 2: " + resultSet.getString("country"));
        resultSet.close();
    }

    public void executeTask3() throws SQLException {
        // Найдите страну с "самыми средними показателями" среди "Western Europe" и "North America"
        var query = """
                SELECT country
                FROM %s
                WHERE region IN ('Western Europe', 'North America')
                ORDER BY (economy + family + health + freedom + trust + generosity) / 6 DESC
                """.formatted(tableName);
        var resultSet = database.get(query);
        var countries = new ArrayList<String>();
        while (resultSet.next())
            countries.add(resultSet.getString("country"));
        System.out.println("Задание 3: " + countries.get(countries.size() / 2));
        resultSet.close();
    }
}
