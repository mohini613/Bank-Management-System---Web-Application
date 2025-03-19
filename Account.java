package com.rbi.model;


import java.sql.*;
import java.util.Scanner;

public class Account {
    private static final String URL = "jdbc:mysql://127.0.0.1:3306/bank_system?useSSL=false";
    private static final String USER = "root";
    private static final String PASSWORD = "root"; 

    Scanner sc = new Scanner(System.in);

    public void insertData() {
        try {
            Class.forName("com.mysql.jdbc.Driver"); 
            Connection con = DriverManager.getConnection(URL, USER, PASSWORD);

            System.out.println("Enter Customer ID:");
            String customerId = sc.nextLine();
            System.out.println("Enter Account Number:");
            String accNumber = sc.nextLine();
            System.out.println("Enter Customer Name:");
            String customerName = sc.nextLine();
            System.out.println("Enter Aadhaar Number:");
            String adharNo = sc.nextLine();
            System.out.println("Enter PAN Card:");
            String panCard = sc.nextLine();
            System.out.println("Enter Account Type (Savings/Current):");
            String accountType = sc.nextLine();
            System.out.println("Enter Address:");
            String address = sc.nextLine();
            System.out.println("Enter Pin Code:");
            String pinCode = sc.nextLine();
            System.out.println("Enter Mobile Number:");
            String mobileNo = sc.nextLine();
            System.out.println("Enter Initial Balance:");
            double balance = sc.nextDouble();
            sc.nextLine(); 
            System.out.println("Enter IFSC Code:");
            String ifscCode = sc.nextLine();
            System.out.println("Enter Bank Name:");
            String bankName = sc.nextLine();

            String query = "INSERT INTO account_details (customer_id, account_no, customer_name, adhar_no, pan_card, account_type, address, pin_code, mobile_no, balance, ifsc_code, bank_name) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement pstmt = con.prepareStatement(query);
            pstmt.setString(1, customerId);
            pstmt.setString(2, accNumber);
            pstmt.setString(3, customerName);
            pstmt.setString(4, adharNo);
            pstmt.setString(5, panCard);
            pstmt.setString(6, accountType);
            pstmt.setString(7, address);
            pstmt.setString(8, pinCode);
            pstmt.setString(9, mobileNo);
            pstmt.setDouble(10, balance);
            pstmt.setString(11, ifscCode);
            pstmt.setString(12, bankName);

            if (pstmt.executeUpdate() > 0) {
                System.out.println("✅ Account Created Successfully!");
            } else {
                System.out.println("⚠️ Failed to Create Account.");
            }

            pstmt.close();
            con.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void showAccountDetails() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection(URL, USER, PASSWORD);

            System.out.print("Enter Account Number: ");
            String accNumber = sc.nextLine();

            String query = "SELECT * FROM account_details WHERE account_no = ?";
            PreparedStatement pstmt = con.prepareStatement(query);
            pstmt.setString(1, accNumber);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                System.out.println("\n============================");
                System.out.println("🏦 Account Details");
                System.out.println("============================");
                System.out.println("Customer ID: " + rs.getString("customer_id"));
                System.out.println("Account Number: " + rs.getString("account_no"));
                System.out.println("Customer Name: " + rs.getString("customer_name"));
                System.out.println("Aadhaar Number: " + rs.getString("adhar_no"));
                System.out.println("PAN Card: " + rs.getString("pan_card"));
                System.out.println("Account Type: " + rs.getString("account_type"));
                System.out.println("Balance: ₹" + rs.getDouble("balance"));
                System.out.println("============================");
            } else {
                System.out.println("❌ Account Not Found!");
            }

            rs.close();
            pstmt.close();
            con.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void performTransaction() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection(URL, USER, PASSWORD);

            System.out.print("Enter Account Number: ");
            String accNumber = sc.nextLine();
            System.out.print("Enter Transaction Type (Deposit/Withdraw): ");
            String type = sc.nextLine().toLowerCase();
            System.out.print("Enter Amount: ");
            double amount = sc.nextDouble();
            sc.nextLine(); 

            String query = "SELECT balance FROM account_details WHERE account_no = ?";
            PreparedStatement pstmt = con.prepareStatement(query);
            pstmt.setString(1, accNumber);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                double currentBalance = rs.getDouble("balance");
                double newBalance = (type.equals("deposit")) ? currentBalance + amount :
                                    (type.equals("withdraw") && currentBalance >= amount) ? currentBalance - amount : -1;

                if (newBalance >= 0) {
                    query = "UPDATE account_details SET balance = ? WHERE account_no = ?";
                    pstmt = con.prepareStatement(query);
                    pstmt.setDouble(1, newBalance);
                    pstmt.setString(2, accNumber);

                    if (pstmt.executeUpdate() > 0) {
                        System.out.println("✅ Transaction Successful! New Balance: ₹" + newBalance);
                    } else {
                        System.out.println("❌ Transaction Failed!");
                    }
                } else {
                    System.out.println("⚠️ Insufficient Balance!");
                }
            } else {
                System.out.println("❌ Account Not Found!");
            }

            rs.close();
            pstmt.close();
            con.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
