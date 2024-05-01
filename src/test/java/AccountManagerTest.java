import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.universityofsouthampton.runwayredeclarationtool.users.Account;
import org.universityofsouthampton.runwayredeclarationtool.users.AccountManager;

public class AccountManagerTest {

  private AccountManager accountManager;
  private List<Account> accounts;
  private final String ACCOUNT_TEST_FILE_PATH_1 = "src/main/resources/test/accounts/accountsTest1.txt";
  private final String ACCOUNT_TEST_FILE_PATH_2 = "src/main/resources/test/accounts/accountsTest2.txt";


  @BeforeEach
  void setUp() {
    accountManager = new AccountManager();
  }

  @Test
  void testSetGetLoggedInAccount() {
    accountManager.loadAccountsFromFile(ACCOUNT_TEST_FILE_PATH_1);
    accountManager.setLoggedInAccount(accountManager.getAccounts().get(0));

    assertEquals(accountManager.getLoggedInAccount().getUsername(),"anthony");

  }

  @Test
  void testLoadAccountsFromFile() {
    accountManager.loadAccountsFromFile(ACCOUNT_TEST_FILE_PATH_1);
    accounts = accountManager.getAccounts();

    assertEquals(accounts.get(0).getUsername(),"anthony");
    assertEquals(accounts.get(0).getPassword(),"password1");
    assertEquals(accounts.get(0).getRole(),"admin");

    assertEquals(accounts.get(1).getUsername(),"tricia");
    assertEquals(accounts.get(1).getPassword(),"password2");
    assertEquals(accounts.get(1).getRole(),"editor");
  }

  @Test
  void testSaveAccountsToFile() {
    accountManager.loadAccountsFromFile(ACCOUNT_TEST_FILE_PATH_1);
    accounts = accountManager.getAccounts();

    accounts.get(0).setRole("viewer");

    accountManager.saveAccountsToFile(ACCOUNT_TEST_FILE_PATH_2);
    accountManager.loadAccountsFromFile(ACCOUNT_TEST_FILE_PATH_2);
    accounts = accountManager.getAccounts();

    assertEquals(accounts.get(0).getRole(),"viewer");
  }

}
