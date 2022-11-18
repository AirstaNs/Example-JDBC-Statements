package org.example;

import org.sqlite.SQLiteDataSource;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Westeros {
    public static void main(String[] args) {
        // If the database does not exist, it will be created automatically.
        String url = "jdbc:sqlite:C:/sqlite/westeros.db";

        SQLiteDataSource dataSource = new SQLiteDataSource();
        dataSource.setUrl(url);
        try (Connection con = dataSource.getConnection()) {
            try (Statement statement = con.createStatement()) {


                deleteHOUSES(statement);

                createTableHOUSES(statement);

                int insertedRows = insertIntoHOUSES(statement);
                System.out.printf("Affected rows: %d %n", insertedRows);

                int UpdatedRows = updateHOUSES(statement);
                System.out.printf("Affected rows: %d %n", UpdatedRows);

                ResultSet result = selectHOUSES(statement);
                printAll(result);


            } catch (SQLException e) {
                e.printStackTrace();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void deleteHOUSES(Statement statement) throws SQLException {
        statement.executeUpdate("""
                DELETE FROM HOUSES
                WHERE  EXISTS(SELECT * FROM HOUSES);
                """);
    }

    public static void printAll(ResultSet result) throws SQLException {
        while (result.next()) {
            int id = result.getInt("id");
            String name = result.getString("name");
            String words = result.getString("words");

            System.out.printf("id: %d %n", id);
            System.out.printf("name: %s %n", name);
            System.out.printf("words: %s %n", words);
        }
    }

    public static ResultSet selectHOUSES(Statement statement) throws SQLException {
        return statement.executeQuery("SELECT * FROM houses");
    }

    public static int updateHOUSES(Statement statement) throws SQLException {
        return statement.executeUpdate("""
                UPDATE houses
                SET words = 'Winter is coming'
                WHERE id=2;
                """);
    }

    public static int insertIntoHOUSES(Statement statement) throws SQLException {
        return statement.executeUpdate("""
                INSERT INTO houses
                (id,name,words)
                VALUES
                (1,'Targaryen of King''s Landing', 'Fire and Blood'),
                (2, 'Stark of Winterfell', 'Summer is Coming'),
                (3, 'Lannister of Casterly Rock', 'Hear Me Roar!');
                """);
    }

    public static void createTableHOUSES(Statement statement) throws SQLException {
        statement.executeUpdate("""
                CREATE TABLE IF NOT EXISTS HOUSES(
                id INTEGER PRIMARY KEY,
                name TEXT NOT NULL,
                words TEXT NOT NULL)
                """);
    }
}