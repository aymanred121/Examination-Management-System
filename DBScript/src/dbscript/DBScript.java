/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dbscript;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 *
 * @author Ziad Khobeiz
 */
public class DBScript {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        String path = "database_script.txt";

        try (Stream<String> lines = Files.lines(Paths.get(path))) {

            String fileContent = lines.collect(Collectors.joining(System.lineSeparator()));
            String SQLstatement = "";

            for(int i = 0; i < fileContent.length(); ++i) {
                if(fileContent.charAt(i) == ';') {
                   try{
                       Class.forName("oracle.jdbc.driver.OracleDriver");
                       Connection myConnection = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521/orcl","hr","hr");
                       Statement myStatement = myConnection.createStatement();
                       myStatement.executeQuery(SQLstatement);
                       myConnection.close();
                   } catch(Exception e) {
                       System.out.println(e);
                   }
                    SQLstatement = "";
                } else {
                    SQLstatement += fileContent.charAt(i);
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
