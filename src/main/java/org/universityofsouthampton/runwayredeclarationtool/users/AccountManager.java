package org.universityofsouthampton.runwayredeclarationtool.users;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * This class deals with keeping track with status of all account registered in the system
 */
public class AccountManager {

  private List<Account> accounts; // List of accounts
  private Account loggedInAccount; // Account currently logged in

  public AccountManager() {
    accounts = new ArrayList<>();
  }

  public void loadAccountsFromFile() {
    // Specify the file path relative to the project's root directory

    String filePath = "src/main/resources/accounts.txt";

    // Create the accounts file
    File accountsFile = new File(filePath);

    // Create the file if it doesn't exist
    if (!accountsFile.exists()) {
      try {
        boolean created = accountsFile.createNewFile();
        if (!created) {
          System.err.println("Failed to create file: " + accountsFile.getAbsolutePath());
          return;
        }
      } catch (IOException e) {
        System.err.println("Error creating file: " + e.getMessage());
        return;
      }
    }

    // Read the account data from the file
    try (BufferedReader reader = new BufferedReader(new FileReader(accountsFile))) {
      String line;
      while ((line = reader.readLine()) != null) {
        String[] parts = line.split(",");
        if (parts.length == 3) {
          accounts.add(new Account(parts[0], parts[1], parts[2]));
        }
      }
    } catch (IOException e) {
      System.err.println("Error reading from file: " + e.getMessage());
    }

  }

  public void saveAccountsToFile() {
    // Specify the file path relative to the project's root directory
    String filePath = "src/main/resources/accounts.txt";

    // Create the accounts file
    File accountsFile = new File(filePath);

    // Create the file if it doesn't exist
    if (!accountsFile.exists()) {
      try {
        boolean created = accountsFile.createNewFile();
        if (!created) {
          System.err.println("Failed to create file: " + accountsFile.getAbsolutePath());
          return;
        }
      } catch (IOException e) {
        System.err.println("Error creating file: " + e.getMessage());
        return;
      }
    }

    // Write the account data to the file
    try (BufferedWriter writer = new BufferedWriter(new FileWriter(accountsFile))) {
      for (Account account : accounts) {
        writer.write(account.getUsername() + "," + account.getPassword() + "," + account.getRole() + "\n");
      }
    } catch (IOException e) {
      System.err.println("Error writing to file: " + e.getMessage());
    }
  }

  public List<Account> getAccounts() {
    return accounts;
  }

  public void setLoggedInAccount(Account loggedInAccount) {
    this.loggedInAccount = loggedInAccount;
  }

  public Account getLoggedInAccount() {
    return loggedInAccount;
  }
}
