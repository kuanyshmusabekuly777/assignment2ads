import java.util.*;

public class Main {

    // ── Shared data structures ──────────────────────────────────────────────
    static LinkedList<BankAccount>  accounts        = new LinkedList<>();
    static Stack<String>            transactionHistory = new Stack<>();
    static Queue<String>            billQueue       = new LinkedList<>();
    static Queue<BankAccount>       accountRequests = new LinkedList<>();

    static Scanner scanner = new Scanner(System.in);
    static int     nextAccountNumber = 1001;

    // ═══════════════════════════════════════════════════════════════════════
    // MAIN MENU
    // ═══════════════════════════════════════════════════════════════════════
    public static void main(String[] args) {

        // Task 6 – pre-load 3 predefined accounts (Physical / Array)
        BankAccount[] predefined = {
            new BankAccount("1001", "Ali",  150000),
            new BankAccount("1002", "Sara", 220000),
            new BankAccount("1003", "Aru",   80000)
        };
        System.out.println("\n=== Task 6 – Physical Array of Accounts ===");
        for (BankAccount ba : predefined) {
            System.out.println(ba);
            accounts.add(ba);
        }
        nextAccountNumber = 1004;

        // Main loop
        while (true) {
            System.out.println("\n╔══════════════════════════╗");
            System.out.println("║      MAIN MENU           ║");
            System.out.println("╠══════════════════════════╣");
            System.out.println("║  1 -- Enter Bank         ║");
            System.out.println("║  2 -- Enter ATM          ║");
            System.out.println("║  3 -- Admin Area         ║");
            System.out.println("║  4 -- Exit               ║");
            System.out.println("╚══════════════════════════╝");
            System.out.print("Choose: ");

            int choice = readInt();
            switch (choice) {
                case 1 -> bankMenu();
                case 2 -> atmMenu();
                case 3 -> adminMenu();
                case 4 -> { System.out.println("Goodbye!"); return; }
                default -> System.out.println("Invalid option. Try again.");
            }
        }
    }

    // ═══════════════════════════════════════════════════════════════════════
    // BANK MENU  (Tasks 1, 2, 3, 5)
    // ═══════════════════════════════════════════════════════════════════════
    static void bankMenu() {
        while (true) {
            System.out.println("\n--- Bank Menu ---");
            System.out.println("1. Submit account opening request");
            System.out.println("2. Add new account directly");
            System.out.println("3. Display all accounts");
            System.out.println("4. Search account by username");
            System.out.println("5. Deposit money");
            System.out.println("6. Withdraw money");
            System.out.println("7. Add bill payment request");
            System.out.println("8. Show transaction history");
            System.out.println("9. Undo last transaction");
            System.out.println("0. Back");
            System.out.print("Choose: ");

            int choice = readInt();
            switch (choice) {
                case 1 -> submitAccountRequest();   // Task 5
                case 2 -> addAccount();             // Task 1
                case 3 -> displayAccounts();        // Task 1
                case 4 -> searchAccount();          // Task 1
                case 5 -> deposit();                // Task 2
                case 6 -> withdraw();               // Task 2
                case 7 -> addBillPayment();         // Task 4
                case 8 -> showTransactionHistory(); // Task 3
                case 9 -> undoLastTransaction();    // Task 3
                case 0 -> { return; }
                default -> System.out.println("Invalid option.");
            }
        }
    }

    // ═══════════════════════════════════════════════════════════════════════
    // ATM MENU
    // ═══════════════════════════════════════════════════════════════════════
    static void atmMenu() {
        while (true) {
            System.out.println("\n--- ATM Menu ---");
            System.out.println("1. Balance enquiry");
            System.out.println("2. Withdraw");
            System.out.println("0. Back");
            System.out.print("Choose: ");

            int choice = readInt();
            switch (choice) {
                case 1 -> balanceEnquiry();
                case 2 -> withdraw();
                case 0 -> { return; }
                default -> System.out.println("Invalid option.");
            }
        }
    }

    // ═══════════════════════════════════════════════════════════════════════
    // ADMIN MENU  (Tasks 4, 5)
    // ═══════════════════════════════════════════════════════════════════════
    static void adminMenu() {
        while (true) {
            System.out.println("\n--- Admin Menu ---");
            System.out.println("1. View pending account requests");
            System.out.println("2. Process next account request");
            System.out.println("3. View bill payment queue");
            System.out.println("4. Process next bill payment");
            System.out.println("0. Back");
            System.out.print("Choose: ");

            int choice = readInt();
            switch (choice) {
                case 1 -> viewAccountRequests();     // Task 5
                case 2 -> processAccountRequest();   // Task 5
                case 3 -> viewBillQueue();            // Task 4
                case 4 -> processNextBill();          // Task 4
                case 0 -> { return; }
                default -> System.out.println("Invalid option.");
            }
        }
    }

    // ═══════════════════════════════════════════════════════════════════════
    // TASK 1 – LinkedList: add / display / search
    // ═══════════════════════════════════════════════════════════════════════
    static void addAccount() {
        System.out.print("Username: ");
        String name = scanner.nextLine().trim();
        System.out.print("Initial balance: ");
        double bal = readDouble();

        String accNum = String.valueOf(nextAccountNumber++);
        accounts.add(new BankAccount(accNum, name, bal));
        System.out.println("Account added successfully. Account number: " + accNum);
    }

    static void displayAccounts() {
        if (accounts.isEmpty()) { System.out.println("No accounts found."); return; }
        System.out.println("\nAccounts List:");
        int i = 1;
        for (BankAccount ba : accounts) {
            System.out.println(i++ + ". " + ba.getUsername() + " -- Balance: " + (int) ba.getBalance());
        }
    }

    static void searchAccount() {
        System.out.print("Enter username to search: ");
        String name = scanner.nextLine().trim();
        BankAccount found = findByUsername(name);
        if (found != null) System.out.println("Found: " + found);
        else               System.out.println("Account not found.");
    }

    // ═══════════════════════════════════════════════════════════════════════
    // TASK 2 – Deposit & Withdraw
    // ═══════════════════════════════════════════════════════════════════════
    static void deposit() {
        System.out.print("Enter username: ");
        String name = scanner.nextLine().trim();
        BankAccount ba = findByUsername(name);
        if (ba == null) { System.out.println("Account not found."); return; }

        System.out.print("Deposit amount: ");
        double amount = readDouble();
        if (amount <= 0) { System.out.println("Amount must be positive."); return; }

        ba.setBalance(ba.getBalance() + amount);
        String record = "Deposit " + (int) amount + " to " + name;
        transactionHistory.push(record);
        System.out.println(record);
        System.out.println("New balance: " + (int) ba.getBalance());
    }

    static void withdraw() {
        System.out.print("Enter username: ");
        String name = scanner.nextLine().trim();
        BankAccount ba = findByUsername(name);
        if (ba == null) { System.out.println("Account not found."); return; }

        System.out.print("Withdraw amount: ");
        double amount = readDouble();
        if (amount <= 0) { System.out.println("Amount must be positive."); return; }
        if (amount > ba.getBalance()) { System.out.println("Insufficient funds."); return; }

        ba.setBalance(ba.getBalance() - amount);
        String record = "Withdraw " + (int) amount + " from " + name;
        transactionHistory.push(record);
        System.out.println(record);
        System.out.println("New balance: " + (int) ba.getBalance());
    }

    // ═══════════════════════════════════════════════════════════════════════
    // TASK 3 – Stack: transaction history
    // ═══════════════════════════════════════════════════════════════════════
    static void showTransactionHistory() {
        if (transactionHistory.isEmpty()) { System.out.println("No transactions yet."); return; }
        System.out.println("\nTransaction History (most recent first):");
        List<String> copy = new ArrayList<>(transactionHistory);
        Collections.reverse(copy);
        for (String t : copy) System.out.println("  " + t);
        System.out.println("Last transaction: " + transactionHistory.peek());
    }

    static void undoLastTransaction() {
        if (transactionHistory.isEmpty()) { System.out.println("Nothing to undo."); return; }
        String removed = transactionHistory.pop();
        System.out.println("Undo → " + removed + " removed");
        // Note: actual balance reversal would require storing delta; omitted for simplicity.
    }

    // ═══════════════════════════════════════════════════════════════════════
    // TASK 4 – Queue: bill payments
    // ═══════════════════════════════════════════════════════════════════════
    static void addBillPayment() {
        System.out.print("Bill name (e.g. Electricity Bill): ");
        String bill = scanner.nextLine().trim();
        billQueue.add(bill);
        System.out.println("Added: " + bill);
    }

    static void viewBillQueue() {
        if (billQueue.isEmpty()) { System.out.println("Bill queue is empty."); return; }
        System.out.println("Pending bills:");
        for (String b : billQueue) System.out.println("  - " + b);
    }

    static void processNextBill() {
        if (billQueue.isEmpty()) { System.out.println("No bills to process."); return; }
        System.out.println("Processing: " + billQueue.poll());
        String record = "Bill payment processed";
        transactionHistory.push(record);
        if (!billQueue.isEmpty()) {
            System.out.print("Remaining:");
            for (String b : billQueue) System.out.print(" " + b + ";");
            System.out.println();
        } else {
            System.out.println("Bill queue is now empty.");
        }
    }

    // ═══════════════════════════════════════════════════════════════════════
    // TASK 5 – Queue: account opening requests
    // ═══════════════════════════════════════════════════════════════════════
    static void submitAccountRequest() {
        System.out.print("Your name: ");
        String name = scanner.nextLine().trim();
        System.out.print("Initial deposit: ");
        double bal = readDouble();
        BankAccount request = new BankAccount("PENDING", name, bal);
        accountRequests.add(request);
        System.out.println("Request submitted for " + name + ". Waiting for admin approval.");
    }

    static void viewAccountRequests() {
        if (accountRequests.isEmpty()) { System.out.println("No pending requests."); return; }
        System.out.println("Pending account requests:");
        for (BankAccount r : accountRequests)
            System.out.println("  - " + r.getUsername() + " (deposit: " + (int) r.getBalance() + ")");
    }

    static void processAccountRequest() {
        if (accountRequests.isEmpty()) { System.out.println("No requests to process."); return; }
        BankAccount req = accountRequests.poll();
        String accNum = String.valueOf(nextAccountNumber++);
        BankAccount newAcc = new BankAccount(accNum, req.getUsername(), req.getBalance());
        accounts.add(newAcc);
        System.out.println("Approved! Account created: " + newAcc);
    }

    // ═══════════════════════════════════════════════════════════════════════
    // ATM helpers
    // ═══════════════════════════════════════════════════════════════════════
    static void balanceEnquiry() {
        System.out.print("Enter username: ");
        String name = scanner.nextLine().trim();
        BankAccount ba = findByUsername(name);
        if (ba != null) System.out.println("Balance for " + name + ": " + (int) ba.getBalance());
        else            System.out.println("Account not found.");
    }

    // ═══════════════════════════════════════════════════════════════════════
    // Utility
    // ═══════════════════════════════════════════════════════════════════════
    static BankAccount findByUsername(String name) {
        for (BankAccount ba : accounts)
            if (ba.getUsername().equalsIgnoreCase(name)) return ba;
        return null;
    }

    static int readInt() {
        try {
            String line = scanner.nextLine().trim();
            return Integer.parseInt(line);
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    static double readDouble() {
        try {
            String line = scanner.nextLine().trim();
            return Double.parseDouble(line);
        } catch (NumberFormatException e) {
            System.out.println("Invalid number. Using 0.");
            return 0;
        }
    }
}
