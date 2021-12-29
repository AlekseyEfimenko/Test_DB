package com.steps.tests.utils;

import com.tables.AuthorTable;
import com.tables.ProjectTable;
import com.tables.TestTable;
import com.utils.DataBaseActions;
import org.apache.log4j.Logger;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class DataManager {
    private static final Logger LOGGER = Logger.getLogger(DataManager.class.getName());

    private DataManager() {}

    public static String printLog(String file) {
        StringBuilder st = new StringBuilder();
        try (Scanner in = new Scanner(new File(file))) {
            while (in.hasNext()) {
                st.append(in.nextLine()).append("\n");
            }
        } catch (FileNotFoundException ex) {
            LOGGER.error(ex.getMessage());
        }
        return st.toString();
    }

    public static PrintStream createFile(String name) {
        PrintStream ps = null;
        try {
            ps = new PrintStream(name);
        } catch (FileNotFoundException ex) {
            LOGGER.error(ex.getMessage());
        }
        return ps;
    }

    public static String getRandomIds(int start, int end, int count) {
        StringBuilder st = new StringBuilder();
        List<Integer> list = convertArrayToList(getTestIds(start, end), count);
        list.forEach(i -> st.append(i).append(", "));
        return st.substring(0, st.length()-2);
    }

    private static int[] getTestIds(int start, int end) {
        return  IntStream.range(start, end).filter(i -> (
                String.valueOf(i).charAt(0) == String.valueOf(i).charAt(1)
                        || String.valueOf(i).charAt(String.valueOf(i).length()-2) == String.valueOf(i).charAt(String.valueOf(i).length()-1)
        )).toArray();
    }

    private static List<Integer> convertArrayToList(int[] array, int length) {
        List<Integer> list = Arrays.stream(array).boxed().collect(Collectors.toList());
        Collections.shuffle(list);
        return list.subList(0, length);
    }

    public static List<TestTable> convertResultSetToList(ResultSet resultSet) {
        List<TestTable> list = new ArrayList<>();
        try {
            while (resultSet.next()) {
                list.add(new TestTable(resultSet));
            }
        } catch (SQLException ex) {
            LOGGER.info("Can't get access to database or incorrect SQL query");
        }
        return list;
    }

    public static void updateAuthorAndProject(TestTable table) {
        table.setAuthorId((long) DataBaseActions.getFirst("id", "author", String.format("email = '%s'", new AuthorTable().getEmail())));
        table.setProjectId((long) DataBaseActions.getFirst("id", "project", String.format("name = '%s'", new ProjectTable().getName())));
        table.setName(table.getName().replace("'", ""));
    }
}

