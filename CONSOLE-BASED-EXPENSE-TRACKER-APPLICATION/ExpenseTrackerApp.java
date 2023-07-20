import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ExpenseTrackerApp {
    private static ExpenseManager expenseManager = new ExpenseManager();

    public static void main(String[] args) {
        displayWelcomeMessage();
        boolean exit = false;

        while (!exit) {
            displayMainMenu();
            int choice = ConsoleHelper.readIntInput("Enter your choice");

            switch (choice) {
                case 1:
                    recordExpense();
                    break;
                case 2:
                    manageCategories();
                    break;
                case 3:
                    viewExpenses();
                    break;
                case 4:
                    generateReports();
                    break;
                case 5:
                    exit = true;
                    break;
                default:
                    ConsoleHelper.displayError("Invalid choice. Please try again.");
            }
        }

        ConsoleHelper.displayMessage("Thank you for using the Expense Tracker. Goodbye!");
    }

    private static void displayWelcomeMessage() {
        ConsoleHelper.displayMessage("Welcome to the Expense Tracker!");
    }

    private static void displayMainMenu() {
        ConsoleHelper.displayMessage("\nMAIN MENU");
        ConsoleHelper.displayMessage("1. Record Expense");
        ConsoleHelper.displayMessage("2. Manage Categories");
        ConsoleHelper.displayMessage("3. View Expenses");
        ConsoleHelper.displayMessage("4. Generate Reports");
        ConsoleHelper.displayMessage("5. Exit");
    }

    private static void recordExpense() {
        ConsoleHelper.displayMessage("\nRECORD EXPENSE");

        LocalDate date = ConsoleHelper.readDateInput("Enter the date (YYYY-MM-DD)");
        double amount = ConsoleHelper.readDoubleInput("Enter the amount");
        String category = ConsoleHelper.readStringInput("Enter the category");
        String description = ConsoleHelper.readStringInput("Enter the description");

        if (ExpenseValidator.isValidAmount(amount) && ExpenseValidator.isValidDate(date)) {
            Expense expense = new Expense(date, amount, category, description);
            expenseManager.addExpense(expense);
            ConsoleHelper.displayMessage("Expense recorded successfully.");
        } else {
            ConsoleHelper.displayError("Invalid expense details. Please try again.");
        }
    }

    private static void manageCategories() {
        ConsoleHelper.displayMessage("\nMANAGE CATEGORIES");
        ConsoleHelper.displayMessage("1. Create Category");
        ConsoleHelper.displayMessage("2. Delete Category");

        int choice = ConsoleHelper.readIntInput("Enter your choice");

        switch (choice) {
            case 1:
                createCategory();
                break;
            case 2:
                deleteCategory();
                break;
            default:
                ConsoleHelper.displayError("Invalid choice. Please try again.");
        }
    }

    private static void createCategory() {
        ConsoleHelper.displayMessage("\nCREATE CATEGORY");
        String categoryName = ConsoleHelper.readStringInput("Enter the category name");

        ExpenseCategory category = new ExpenseCategory(categoryName);
        expenseManager.addCategory(category);
        ConsoleHelper.displayMessage("Category created successfully.");
    }

    private static void deleteCategory() {
        ConsoleHelper.displayMessage("\nDELETE CATEGORY");

        if (expenseManager.getCategories().isEmpty()) {
            ConsoleHelper.displayError("No categories found.");
            return;
        }

        displayCategories();

        int categoryIndex = ConsoleHelper.readIntInput("Enter the index of the category to delete");

        if (categoryIndex >= 0 && categoryIndex < expenseManager.getCategories().size()) {
            ExpenseCategory category = expenseManager.getCategories().get(categoryIndex);
            expenseManager.removeCategory(category);
            ConsoleHelper.displayMessage("Category deleted successfully.");
        } else {
            ConsoleHelper.displayError("Invalid category index. Please try again.");
        }
    }

    private static void displayCategories() {
        ConsoleHelper.displayMessage("\nCATEGORIES");

        for (int i = 0; i < expenseManager.getCategories().size(); i++) {
            ExpenseCategory category = expenseManager.getCategories().get(i);
            ConsoleHelper.displayMessage(i + ". " + category.getName());
        }
    }

    private static void viewExpenses() {
        ConsoleHelper.displayMessage("\nVIEW EXPENSES");

        if (expenseManager.getExpenses().isEmpty()) {
            ConsoleHelper.displayError("No expenses found.");
            return;
        }

        displayExpenses(expenseManager.getExpenses());
    }

    private static void displayExpenses(List<Expense> expenses) {
        ConsoleHelper.displayMessage("\nEXPENSES");

        for (Expense expense : expenses) {
            ConsoleHelper.displayMessage("Date: " + expense.getDate());
            ConsoleHelper.displayMessage("Amount: " + expense.getAmount());
            ConsoleHelper.displayMessage("Category: " + expense.getCategory());
            ConsoleHelper.displayMessage("Description: " + expense.getDescription());
            ConsoleHelper.displayMessage("-----------------------");
        }
    }

    private static void generateReports() {
        ConsoleHelper.displayMessage("\nGENERATE REPORTS");
        ConsoleHelper.displayMessage("1. Monthly Expense Report");
        ConsoleHelper.displayMessage("2. Category-wise Expense Report");

        int choice = ConsoleHelper.readIntInput("Enter your choice");

        switch (choice) {
            case 1:
                generateMonthlyExpenseReport();
                break;
            case 2:
                generateCategoryExpenseReport();
                break;
            default:
                ConsoleHelper.displayError("Invalid choice. Please try again.");
        }
    }

    private static void generateMonthlyExpenseReport() {
        ConsoleHelper.displayMessage("\nMONTHLY EXPENSE REPORT");
        int year = ConsoleHelper.readIntInput("Enter the year");
        int month = ConsoleHelper.readIntInput("Enter the month (1-12)");

        double totalExpense = 0;

        for (Expense expense : expenseManager.getExpenses()) {
            if (expense.getDate().getYear() == year && expense.getDate().getMonthValue() == month) {
                totalExpense += expense.getAmount();
            }
        }

        ConsoleHelper.displayMessage("Total Expenses for " + month + "/" + year + ": " + totalExpense);
    }

    private static void generateCategoryExpenseReport() {
        ConsoleHelper.displayMessage("\nCATEGORY-WISE EXPENSE REPORT");

        if (expenseManager.getCategories().isEmpty()) {
            ConsoleHelper.displayError("No categories found.");
            return;
        }

        displayCategories();

        int categoryIndex = ConsoleHelper.readIntInput("Enter the index of the category");

        if (categoryIndex >= 0 && categoryIndex < expenseManager.getCategories().size()) {
            ExpenseCategory category = expenseManager.getCategories().get(categoryIndex);

            double totalExpense = 0;

            for (Expense expense : category.getExpenses()) {
                totalExpense += expense.getAmount();
            }

            ConsoleHelper.displayMessage("Total Expenses for " + category.getName() + ": " + totalExpense);
        } else {
            ConsoleHelper.displayError("Invalid category index. Please try again.");
        }
    }
}

class Expense {
    private LocalDate date;
    private double amount;
    private String category;
    private String description;

    public Expense(LocalDate date, double amount, String category, String description) {
        this.date = date;
        this.amount = amount;
        this.category = category;
        this.description = description;
    }

    public LocalDate getDate() {
        return date;
    }

    public double getAmount() {
        return amount;
    }

    public String getCategory() {
        return category;
    }

    public String getDescription() {
        return description;
    }
}

class ExpenseCategory {
    private String name;
    private List<Expense> expenses;

    public ExpenseCategory(String name) {
        this.name = name;
        expenses = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public List<Expense> getExpenses() {
        return expenses;
    }

    public void addExpense(Expense expense) {
        expenses.add(expense);
    }

    public void removeExpense(Expense expense) {
        expenses.remove(expense);
    }
}

class ExpenseManager {
    private List<Expense> expenses;
    private List<ExpenseCategory> categories;

    public ExpenseManager() {
        expenses = new ArrayList<>();
        categories = new ArrayList<>();
    }

    public void addExpense(Expense expense) {
        expenses.add(expense);
    }

    public void removeExpense(Expense expense) {
        expenses.remove(expense);
    }

    public void addCategory(ExpenseCategory category) {
        categories.add(category);
    }

    public void removeCategory(ExpenseCategory category) {
        categories.remove(category);
    }

    public List<Expense> getExpenses() {
        return expenses;
    }

    public List<ExpenseCategory> getCategories() {
        return categories;
    }
}

class ExpenseValidator {
    public static boolean isValidAmount(double amount) {
        return amount > 0;
    }

    public static boolean isValidDate(LocalDate date) {
        LocalDate currentDate = LocalDate.now();
        return !date.isAfter(currentDate);
    }
}

class ConsoleHelper {
    private static Scanner scanner = new Scanner(System.in);

    public static void displayMessage(String message) {
        System.out.println(message);
    }

    public static void displayError(String errorMessage) {
        System.err.println(errorMessage);
    }

    public static String readStringInput(String prompt) {
        System.out.print(prompt + ": ");
        return scanner.nextLine();
    }

    public static double readDoubleInput(String prompt) {
        System.out.print(prompt + ": ");
        while (true) {
            try {
                return Double.parseDouble(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.print("Invalid input. " + prompt + ": ");
            }
        }
    }

    public static int readIntInput(String prompt) {
        System.out.print(prompt + ": ");
        while (true) {
            try {
                return Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.print("Invalid input. " + prompt + ": ");
            }
        }
    }

    public static LocalDate readDateInput(String prompt) {
        System.out.print(prompt + ": ");
        while (true) {
            try {
                return LocalDate.parse(scanner.nextLine());
            } catch (Exception e) {
                System.out.print("Invalid input. " + prompt + ": ");
            }
        }
    }
}
