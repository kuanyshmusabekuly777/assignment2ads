public class BankAccount {
    private String accountNumber;
    private String username;
    private double balance;

    public BankAccount(String accountNumber, String username, double balance) {
        this.accountNumber = accountNumber;
        this.username = username;
        this.balance = balance;
    }

    public String getAccountNumber() { return accountNumber; }
    public String getUsername()      { return username; }
    public double getBalance()       { return balance; }
    public void setBalance(double balance) { this.balance = balance; }

    @Override
    public String toString() {
        return "Account[" + accountNumber + "] " + username + " -- Balance: " + (int) balance;
    }
}
