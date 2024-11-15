package Service;

import Model.Account;
import DAO.AccountDAO;

import java.util.List;

public class AccountService {
    private AccountDAO accountDAO;

    public AccountService(){
        accountDAO = new AccountDAO();
    }

    public AccountService(AccountDAO accountDAO){
        this.accountDAO = accountDAO;
    }

    public List<Account> getAllAccounts() {
        return accountDAO.getAllAccounts();
    }

    public Account getAccountByID(int id){
        return accountDAO.getAccountByID(id);
    }

    public Account getAccountLogin(String username, String password){
        return accountDAO.getAccountLogin(username, password);
    }

    
    public Account addAccount(Account account) {
        Account persistedAccount = accountDAO.addAccount(account);
        return persistedAccount;
    }
}
