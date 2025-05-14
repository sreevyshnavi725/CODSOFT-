import java.util.Scanner;

// Class to represent a user's bank account
class BankAccount {
    private double balance;

    // Constructor to initialize account with a starting balance
    public BankAccount(double initialBalance) {
        balance = initialBalance;
    }

    // Deposit money into the account
    public void deposit(double amount) {
        if (amount > 0) {
            balance += amount;
            System.out.println("Deposited: Rs." + amount);
        } else {
            System.out.println("Invalid amount. Deposit must be greater than zero.");
        }
    }

    // Withdraw money from the account
    public void withdraw(double amount) {
        if (amount > 0 && amount <= balance) {
            balance -= amount;
            System.out.println("Withdrawn: Rs." + amount);
        } else if (amount > balance) {
            System.out.println("Insufficient balance. Try a smaller amount.");
        } else {
            System.out.println("Invalid amount. Withdrawal must be greater than zero.");
        }
    }

    // Check the current balance
    public double checkBalance() {
        return balance;
    }
}

// Class to represent the ATM interface
class ATM {
    private BankAccount account;
    private Scanner scanner;

    // Constructor to link ATM to a bank account
    public ATM(BankAccount account) {
        this.account = account;
        scanner = new Scanner(System.in);
    }

    // Start ATM interface
    public void start() {
        int choice;
        do {
            System.out.println("\n==== Welcome to the ATM ====");
            System.out.println("1. Check Balance");
            System.out.println("2. Deposit Money");
            System.out.println("3. Withdraw Money");
            System.out.println("4. Exit");
            System.out.print("Choose an option (1-4): ");
            choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    System.out.println("Your current balance is: Rs." + account.checkBalance());
                    break;
                case 2:
                    System.out.print("Enter amount to deposit: Rs.");
                    double depositAmount = scanner.nextDouble();
                    account.deposit(depositAmount);
                    break;
                case 3:
                    System.out.print("Enter amount to withdraw: Rs.");
                    double withdrawAmount = scanner.nextDouble();
                    account.withdraw(withdrawAmount);
                    break;
                case 4:
                    System.out.println("Thank you for using the ATM. Goodbye!");
                    break;
                default:
                    System.out.println("Invalid option. Please choose between 1 and 4.");
            }
        } while (choice != 4);
    }
}

// Main class to run the ATM program
public class ATMInterface {
    public static void main(String[] args) {
        // You can change the starting balance here
        BankAccount myAccount = new BankAccount(10000); // Rs.10,000 initial balance
        ATM atm = new ATM(myAccount);
        atm.start();
    }
}