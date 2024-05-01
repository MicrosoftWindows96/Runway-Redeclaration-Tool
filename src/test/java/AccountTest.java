import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.universityofsouthampton.runwayredeclarationtool.users.Account;

public class AccountTest {

  private Account account;

  @BeforeEach
  void setUp() {
    account = new Account("username", "password", "admin");
  }

  @Test
  void testGetUsername() {
    assertEquals(account.getUsername(),"username");
  }

  @Test
  void testGetPassword() {
    assertEquals(account.getPassword(),"password");
  }

  @Test
  void testGetRole() {
    assertEquals(account.getRole(),"admin");
  }

  @Test
  void testSetRole() {
    account.setRole("editor");
    assertEquals(account.getRole(),"editor");

    account.setRole("viewer");
    assertEquals(account.getRole(),"viewer");
  }

}
