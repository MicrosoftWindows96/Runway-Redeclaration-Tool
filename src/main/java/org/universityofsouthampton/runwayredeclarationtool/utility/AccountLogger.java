package org.universityofsouthampton.runwayredeclarationtool.utility;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import javafx.stage.FileChooser;
import org.universityofsouthampton.runwayredeclarationtool.users.Account;

public class AccountLogger {

  private final List<String> savedLogs;

  public AccountLogger() {
    savedLogs = new ArrayList<>();
  }

  public void writeLog(Account account, String operation) {
    String log = getCurrentDate() + " " + getCurrentTime() + " [" + account.getUsername()
        + "/" + account.getRole() + "]: " + operation;
    System.out.println(log);
    savedLogs.add(log);
  }

  public void exportReport() { // Function to export airport xml file of current contents to local files
    FileChooser fileChooser = new FileChooser();
    fileChooser.setTitle("Save users log file");
    File file = fileChooser.showSaveDialog(null);
    if (file != null) {
      // Write to the text file
      try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
        for (String log : savedLogs) {
          writer.write(log);
          writer.newLine(); // Write a new line
        }
        System.out.println("Logs written to the file successfully.");
      } catch (IOException e) {
        System.err.println("Error writing to the file: " + e.getMessage());
      }
    }
  }

  private static String getCurrentTime() { // Recorded time of logging
    // Get the current time
    LocalTime currentTime = LocalTime.now();
    // Create a formatter
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
    // Format the current time
    return currentTime.format(formatter);
  }

  private static String getCurrentDate() {
    // Get the current date
    LocalDate currentDate = LocalDate.now();
    // Define a custom date format pattern
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
    // Format the current date using the custom pattern
    return currentDate.format(formatter);
  }

}
