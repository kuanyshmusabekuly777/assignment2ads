import java.util.*;

public class Main {

    static LinkedList<BankAccount> accounts = new LinkedList<>();
    static Stack<String> history = new Stack<>();
    static Queue<String> billQueue = new LinkedList<>();
    static Queue<BankAccount> accountRequests = new LinkedList<>();

    static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {

        // Task 6 (array)
        BankAccount[] arr = {
                new BankAccount(1, "Ali", 150000),
                new BankAccount(2, "Sara", 220000),
                new BankAccount(3, "John", 100000)
        };

        System.out.println("Array accounts:");
        for (BankAccount acc : arr) {
            acc.display();
        }

        while (true) {
            System.out.println("\n1 – Enter Bank");
            System.out.println("2 – Enter ATM");
            System.out.println("3 – Admin Area");
            System.out.println("4 – Exit");

            int choice = sc.nextInt();

            switch (choice) {
                case 1 -> bankMenu();
                case 2 -> atmMenu();
                case 3 -> adminMenu();
                case 4 -> {
                    System.out.println("Exit...");
                    return;
                }
            }
        }
    }

    // BANK MENU
    static void bankMenu() {
        System.out.println("\n1. Add account request");
        System.out.println("2. Deposit");
        System.out.println("3. Withdraw");

        int c = sc.nextInt();
        sc.nextLine();

        switch (c) {
            case 1 -> {
                System.out.print("Enter username: ");
                String name = sc.nextLine();
                accountRequests.add(new BankAccount(0, name, 0));
                System.out.println("Request added!");
            }
            case 2 -> {
                BankAccount acc = findAccount();
                if (acc != null) {
                    System.out.print("Deposit: ");
                    double money = sc.nextDouble();
                    acc.deposit(money);
                    history.push("Deposit " + money + " to " + acc.username);
                    System.out.println("New balance: " + acc.balance);
                }
            }
            case 3 -> {
                BankAccount acc = findAccount();
                if (acc != null) {
                    System.out.print("Withdraw: ");
                    double money = sc.nextDouble();
                    acc.withdraw(money);
                    history.push("Withdraw " + money + " from " + acc.username);
                    System.out.println("New balance: " + acc.balance);
                }
            }
        }
    }

    // ATM
    static void atmMenu() {
        BankAccount acc = findAccount();
        if (acc == null) return;

        System.out.println("1. Balance");
        System.out.println("2. Withdraw");

        int c = sc.nextInt();

        if (c == 1) {
            System.out.println("Balance: " + acc.balance);
        } else if (c == 2) {
            System.out.print("Withdraw: ");
            double money = sc.nextDouble();
            acc.withdraw(money);
            history.push("ATM withdraw " + money);
        }
    }

    // ADMIN
    static void adminMenu() {
        System.out.println("\n1. Process account requests");
        System.out.println("2. Bill queue");
        System.out.println("3. Transaction history");

        int c = sc.nextInt();
        sc.nextLine();

        switch (c) {
            case 1 -> {
                if (!accountRequests.isEmpty()) {
                    BankAccount newAcc = accountRequests.poll();
                    newAcc.accountNumber = accounts.size() + 1;
                    accounts.add(newAcc);
                    System.out.println("Account created for " + newAcc.username);
                } else {
                    System.out.println("No requests");
                }
            }

            case 2 -> {
                System.out.println("1. Add bill");
                System.out.println("2. Process bill");

                int x = sc.nextInt();
                sc.nextLine();

                if (x == 1) {
                    System.out.print("Bill name: ");
                    String bill = sc.nextLine();
                    billQueue.add(bill);
                } else if (x == 2) {
                    System.out.println("Processing: " + billQueue.poll());
                }

                System.out.println("Queue: " + billQueue);
            }

            case 3 -> {
                if (!history.isEmpty()) {
                    System.out.println("Last: " + history.peek());

                    System.out.println("Undo last? 1=yes");
                    int u = sc.nextInt();
                    if (u == 1) {
                        history.pop();
                        System.out.println("Removed!");
                    }
                }
            }
        }
    }

    // SEARCH
    static BankAccount findAccount() {
        sc.nextLine();
        System.out.print("Enter username: ");
        String name = sc.nextLine();

        for (BankAccount acc : accounts) {
            if (acc.username.equalsIgnoreCase(name)) {
                return acc;
            }
        }

        System.out.println("Not found!");
        return null;
    }
}
