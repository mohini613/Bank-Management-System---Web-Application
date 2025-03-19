package com.rbi.main;


import com.rbi.model.Account;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        Account account = new Account();

        while (true) {
            // Display menu options
            System.out.println("\n==========================");
            System.out.println("🏦 Bank Management System");
            System.out.println("==========================");
            System.out.println("1. Create Account");
            System.out.println("2. Show Account Details");
            System.out.println("3. Transaction");
            System.out.println("4. Exit");
            System.out.print("Enter your choice: ");

            int choice = sc.nextInt();
            sc.nextLine(); 

            switch (choice) {
                case 1:
                    account.insertData(); 
                    break;
                case 2:
                    account.showAccountDetails();                     break;
                case 3:
                    account.performTransaction(); 
                    break;
                case 4:
                    System.out.println("🚪 Exiting... Thank you for using our bank system!");
                    sc.close();
                    System.exit(0);
                    break;
                default:
                    System.out.println("❌ Invalid choice! Please enter a number between 1 and 4.");
            }
        }
    }
}

