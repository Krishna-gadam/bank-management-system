import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        while (true) {
            System.out.println("\n== Bank Management System ==");
            System.out.println("1. Register");
            System.out.println("2. Login");
            System.out.println("3. Exit");
            System.out.print("Choose: ");
            int choice = Integer.parseInt(sc.nextLine());

            switch (choice) {
                case 1:
                    Customer.register();
                    break;
                case 2:
                    int id = Customer.login();
                    if (id != -1) {
                        while (true) {
                            System.out.println("\n--- Customer Menu ---");
                            System.out.println("1. View Balance");
                            System.out.println("2. Deposit");
                            System.out.println("3. Withdraw");
                            System.out.println("4. Mini Statement");
                            System.out.println("5. Logout");
                            int opt = Integer.parseInt(sc.nextLine());

                            if (opt == 1) Customer.viewBalance(id);
                            else if (opt == 2) Transaction.deposit(id);
                            else if (opt == 3) Transaction.withdraw(id);
                            else if (opt == 4) Transaction.miniStatement(id);
                            else break;
                        }
                    }
                    break;
                case 3:
                    System.out.println("Goodbye!");
                    System.exit(0);
            }
        }
    }
}
