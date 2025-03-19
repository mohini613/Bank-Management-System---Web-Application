package com.rbi.operation;

import java.sql.*;
import java.util.Scanner;
import com.rbi.model.Account;

public class Operation {
	String url = "jdbc:mysql://127.0.0.1:3306/bank_system?useSSL=false&serverTimezone=UTC";
    static final String USER = "root";
    static final String PASSWORD = "root";
    
    Scanner sc = new Scanner(System.in);

    public void createAccount() {
        try (Connection con = DriverManager.getConnection(url, USER, PASSWORD)) {
            String query = "INSERT INTO account_details (customer_id, account_no, customer_name, adhar_no, pan_card, account_type, address, pin_code, mobile_no, balance, ifsc_code, bank_name) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement pstmt = con.prepareStatement(query);
            
            System.out.println("Enter Customer ID:");
            pstmt.setString(1, sc.next());
            
            System.out.println("Enter Account Number:");
            pstmt.setString(2, sc.next());
            
            System.out.println("Enter Customer Name:");
            sc.nextLine(); 
            pstmt.setString(3, sc.nextLine());
            
            System.out.println("Enter Aadhaar Number:");
            pstmt.setString(4, sc.next());
            
            System.out.println("Enter PAN Card:");
            pstmt.setString(5, sc.next());
            
            System.out.println("Enter Account Type (Savings/Current):");
            sc.nextLine();
            pstmt.setString(6, sc.nextLine());
            
            System.out.println("Enter Address:");
            pstmt.setString(7, sc.nextLine());
            
            System.out.println("Enter Pin Code:");
            pstmt.setString(8, sc.next());
            
            System.out.println("Enter Mobile Number:");
            pstmt.setString(9, sc.next());
            
            System.out.println("Enter Balance (Minimum 500):");
            double balance;
            while (true) {
                balance = sc.nextDouble();
                if (balance >= 500) break;
                System.out.println("Initial deposit must be at least 500. Try again:");
            }
            pstmt.setDouble(10, balance);
            
            System.out.println("Enter IFSC Code:");
            sc.nextLine(); // Consume newline
            pstmt.setString(11, sc.nextLine());
            
            System.out.println("Enter Bank Name:");
            pstmt.setString(12, sc.nextLine());
            
            int rowsInserted = pstmt.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println("Account Created Successfully!");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void showAccountDetails() {
        System.out.println("Enter Customer ID:");
        String cid = sc.next();
        try (Connection con = DriverManager.getConnection(url, USER, PASSWORD)) {
            String query = "SELECT * FROM account_details WHERE customer_id = ?";
            PreparedStatement pstmt = con.prepareStatement(query);
            pstmt.setString(1, cid);
            
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                System.out.println("Customer Name: " + rs.getString("customer_name"));
                System.out.println("Account Number: " + rs.getString("account_no"));
                System.out.println("IFSC Code: " + rs.getString("ifsc_code"));
                System.out.println("Account Type: " + rs.getString("account_type"));
                System.out.println("Balance: " + rs.getDouble("balance"));
                System.out.println("Aadhaar Number: " + rs.getString("adhar_no"));
                System.out.println("PAN Card: " + rs.getString("pan_card"));
                System.out.println("Address: " + rs.getString("address"));
                System.out.println("Pin Code: " + rs.getString("pin_code"));
                System.out.println("Mobile Number: " + rs.getString("mobile_no"));
            } else {
                System.out.println("Customer ID not found.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void transaction() {
        System.out.println("Enter Sender's Customer ID:");
        String senderId = sc.next();
        
        System.out.println("Enter Receiver's Customer ID:");
        String receiverId = sc.next();
        
        System.out.println("Enter Amount to Transfer:");
        double amount = sc.nextDouble();
        
        try (Connection con = DriverManager.getConnection(url, USER, PASSWORD)) {
            con.setAutoCommit(false);
            
            
            PreparedStatement checkSender = con.prepareStatement("SELECT balance FROM account_details WHERE customer_id = ?");
            checkSender.setString(1, senderId);
            ResultSet senderRs = checkSender.executeQuery();
            if (!senderRs.next() || senderRs.getDouble("balance") < amount) {
                System.out.println("Transaction failed: Insufficient funds.");
                return;
            }
            
            PreparedStatement deduct = con.prepareStatement("UPDATE account_details SET balance = balance - ? WHERE customer_id = ?");
            deduct.setDouble(1, amount);
            deduct.setString(2, senderId);
            deduct.executeUpdate();
            
            PreparedStatement add = con.prepareStatement("UPDATE account_details SET balance = balance + ? WHERE customer_id = ?");
            add.setDouble(1, amount);
            add.setString(2, receiverId);
            add.executeUpdate();
            
            con.commit();
            System.out.println("Transaction Successful!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        Operation op = new Operation();
        op.createAccount();
        op.showAccountDetails();
        op.transaction();
    }
}
