package com.ps;

import org.apache.commons.dbcp2.BasicDataSource;

import javax.sql.DataSource;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


public class MyTransactionDao extends MySqlDaoBase {
    private static BasicDataSource dataSource;

    public MyTransactionDao(DataSource dataSource) {
        super(dataSource);
    }

    public static int createTransaction(Transaction transaction) {
        int id = -1;
        try (Connection connection = dataSource.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement
                ("INSERT INTO transactions(transaction_id, date, time, description, type_of_transaction" + "vendor, amount" + "VALUES (?, ?, ?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);) {
            preparedStatement.setInt(1, transaction.getId());
            preparedStatement.setDate(2, transaction.getDate());
            preparedStatement.setTime(3, transaction.getTime());
            preparedStatement.setString(4, transaction.getDescription());
            preparedStatement.setString(5, transaction.getTransactionType());
            preparedStatement.setString(6, transaction.getVendor());
            preparedStatement.setDouble(7, transaction.getAmount());
            preparedStatement.executeUpdate();

            try (ResultSet keys = preparedStatement.getGeneratedKeys()) {
                while (keys.next()) {
                    id = keys.getInt(1);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return id;
    }

    public Transaction searchByTransaction(int id) {
        BasicDataSource dataSource = null;
        Transaction transaction = null;
        try (Connection connection = dataSource.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM Transactions WHERE transaction_id = ? "

        );) {
            preparedStatement.setInt(1, id);
            try (ResultSet resultSet = preparedStatement.executeQuery();) {
                while (resultSet.next()) {
                    transaction = generateTransactionFromRS(resultSet);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return transaction;
    }


    // 5)
    public static List<Transaction> searchByDateRange(Date firstDate, Date secondDate) {
        List <Transaction> transactions = new ArrayList<>();

        try (
                Connection connection = dataSource.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(
                        "SELECT * FROM transactions WHERE date BETWEEN ? AND ?"
                );
        ) {
            preparedStatement.setDate(1, firstDate);
            preparedStatement.setDate(2, secondDate);

            try (
                    ResultSet resultSet = preparedStatement.executeQuery();
            ) {
                while (resultSet.next()) {
                    Transaction transaction = generateTransactionFromRS(resultSet);
                    transactions.add(transaction);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return transactions;
    }


    // 1)
    public static List<Transaction> searchByThisMonth() {
        List <Transaction> transactions = new ArrayList<>();
        try (
                Connection connection = dataSource.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(
                        "SELECT * FROM transactions WHERE date BETWEEN ? AND ?"
                );
        ) {
            LocalDate localDate = LocalDate.now();
            Date date = Date.valueOf(localDate);
            int year = localDate.getYear();
            int month = localDate.getMonthValue();

            LocalDate firstDayOfMonth = LocalDate.of(year, month, 1);

            // Convert the new LocalDate to java.sql.Date
            Date firstDateOfMonth = Date.valueOf(firstDayOfMonth);

            preparedStatement.setDate(1, firstDateOfMonth);
            preparedStatement.setDate(2, date);

            try (
                    ResultSet resultSet = preparedStatement.executeQuery();
            ) {
                while (resultSet.next()) {
                    Transaction transaction = generateTransactionFromRS(resultSet);
                    transactions.add(transaction);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return transactions;
    }

    // 3)
    public static List<Transaction> searchByThisYear() {
        List <Transaction> transactions = new ArrayList<>();
        try (
                Connection connection = dataSource.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(
                        "SELECT * FROM transactions WHERE date BETWEEN ? AND ?"
                );
        ) {
            LocalDate localDate = LocalDate.now();
            Date date = Date.valueOf(localDate);
            int year = localDate.getYear();


            LocalDate firstDayOfYear = LocalDate.of(year, 1, 1);

            // Convert the new LocalDate to java.sql.Date
            Date firstDay = Date.valueOf(firstDayOfYear);

            preparedStatement.setDate(1, firstDay);
            preparedStatement.setDate(2, date);

            try (
                    ResultSet resultSet = preparedStatement.executeQuery();
            ) {
                while (resultSet.next()) {
                    Transaction transaction = generateTransactionFromRS(resultSet);
                    transactions.add(transaction);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return transactions;
    }

    // 4)
    public static List<Transaction> searchByYear() {
        List <Transaction> transactions = new ArrayList<>();
        Scanner scanner = new Scanner(System.in);
        System.out.println("Which year would you like to search from?");
        int year = scanner.nextInt();

        try (
                Connection connection = dataSource.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(
                        "SELECT * FROM transactions WHERE date BETWEEN ? AND ?"
                );
        ) {




            LocalDate firstDayOfYear = LocalDate.of(year, 1, 1);

            // Convert the new LocalDate to java.sql.Date
            Date firstDay = Date.valueOf(firstDayOfYear);
            LocalDate lastDayOfYear = LocalDate.of(year,12, 31);
            Date lastDay = Date.valueOf(lastDayOfYear);


            preparedStatement.setDate(1, firstDay);
            preparedStatement.setDate(2, lastDay);

            try (
                    ResultSet resultSet = preparedStatement.executeQuery();
            ) {
                while (resultSet.next()) {
                    Transaction transaction = generateTransactionFromRS(resultSet);
                    transactions.add(transaction);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return transactions;
    }



    public List<Transaction> searchByDescription(String description) {
        List <Transaction> transactions = new ArrayList<>();
        try (
                Connection connection = dataSource.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(
                        "SELECT * FROM transactions WHERE description = ?"
                );
        ) {
            preparedStatement.setString(1,description);


            try (
                    ResultSet resultSet = preparedStatement.executeQuery();
            ) {
                while (resultSet.next()) {
                    Transaction transaction = generateTransactionFromRS(resultSet);
                    transactions.add(transaction);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return transactions;
    }

    public List<Transaction> searchByVendor(String vendor) {
        List <Transaction> transactions = new ArrayList<>();
        try (
                Connection connection = dataSource.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(
                        "SELECT * FROM transactions WHERE vendor = ?"
                );
        ) {
            preparedStatement.setString(1, vendor);


            try (
                    ResultSet resultSet = preparedStatement.executeQuery();
            ) {
                while (resultSet.next()) {
                    Transaction transaction = generateTransactionFromRS(resultSet);
                    transactions.add(transaction);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return transactions;
    }

    public List<Transaction> searchByAmount(String amount) {
        List <Transaction> transactions = new ArrayList<>();
        try (
                Connection connection = dataSource.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(
                        "SELECT * FROM transactions WHERE amount = ?"
                );
        ) {
            preparedStatement.setString(1,amount);


            try (
                    ResultSet resultSet = preparedStatement.executeQuery();
            ) {
                while (resultSet.next()) {
                    Transaction transaction = generateTransactionFromRS(resultSet);
                    transactions.add(transaction);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return transactions;
    }



    public static Transaction generateTransactionFromRS(ResultSet resultSet) throws SQLException {
        int id = resultSet.getInt("transaction_id");
        Date date = resultSet.getDate("date");
        Time transaction = resultSet.getTime("time");
        String description = resultSet.getString("description");
        String transactionType = resultSet.getString("type_of_transaction");
        String vendor = resultSet.getString("vendor");
        Double amount = resultSet.getDouble("amount");


        return new Transaction(amount,date, description, id, transaction, transactionType, vendor);
    }

}













