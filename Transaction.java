import java.sql.*;
import java.util.Scanner;

public class Transaction {
    static Scanner sc = new Scanner(System.in);

    public static void deposit(int customerId) {
        System.out.print("Enter amount to deposit: ");
        double amount = Double.parseDouble(sc.nextLine());

        try (Connection con = DBConnection.getConnection()) {
            con.setAutoCommit(false);

            PreparedStatement ps1 = con.prepareStatement("UPDATE customers SET balance = balance + ? WHERE id=?");
            ps1.setDouble(1, amount);
            ps1.setInt(2, customerId);
            ps1.executeUpdate();

            PreparedStatement ps2 = con.prepareStatement("INSERT INTO transactions (customer_id, type, amount) VALUES (?, 'Deposit', ?)");
            ps2.setInt(1, customerId);
            ps2.setDouble(2, amount);
            ps2.executeUpdate();

            con.commit();
            System.out.println("₹" + amount + " deposited successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void withdraw(int customerId) {
        System.out.print("Enter amount to withdraw: ");
        double amount = Double.parseDouble(sc.nextLine());

        try (Connection con = DBConnection.getConnection()) {
            PreparedStatement check = con.prepareStatement("SELECT balance FROM customers WHERE id=?");
            check.setInt(1, customerId);
            ResultSet rs = check.executeQuery();
            if (rs.next() && rs.getDouble("balance") >= amount) {
                con.setAutoCommit(false);

                PreparedStatement ps1 = con.prepareStatement("UPDATE customers SET balance = balance - ? WHERE id=?");
                ps1.setDouble(1, amount);
                ps1.setInt(2, customerId);
                ps1.executeUpdate();

                PreparedStatement ps2 = con.prepareStatement("INSERT INTO transactions (customer_id, type, amount) VALUES (?, 'Withdraw', ?)");
                ps2.setInt(1, customerId);
                ps2.setDouble(2, amount);
                ps2.executeUpdate();

                con.commit();
                System.out.println("₹" + amount + " withdrawn successfully.");
            } else {
                System.out.println("Insufficient balance.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void miniStatement(int customerId) {
        try (Connection con = DBConnection.getConnection()) {
            PreparedStatement ps = con.prepareStatement("SELECT * FROM transactions WHERE customer_id=? ORDER BY date DESC LIMIT 5");
            ps.setInt(1, customerId);
            ResultSet rs = ps.executeQuery();

            System.out.println("Last 5 Transactions:");
            while (rs.next()) {
                System.out.println(rs.getTimestamp("date") + " - " +
                        rs.getString("type") + " ₹" + rs.getDouble("amount"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
