import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

// Define Car class
class Car {
    private String carId;
    private String brand;
    private String model;
    private boolean available;
    private double dailyRate;

    public Car(String carId, String brand, String model, double dailyRate) {
        this.carId = carId;
        this.brand = brand;
        this.model = model;
        this.available = true;
        this.dailyRate = dailyRate;
    }

    public String getCarId() {
        return carId;
    }

    public String getBrand() {
        return brand;
    }

    public String getModel() {
        return model;
    }

    public boolean isAvailable() {
        return available;
    }

    public void rent() {
        available = false;
    }

    public void returnCar() {
        available = true;
    }

    public double calculatePrice(int days) {
        return days * dailyRate;
    }
}

// Define Customer class
class Customer {
    private String customerId;
    private String name;

    public Customer(String customerId, String name) {
        this.customerId = customerId;
        this.name = name;
    }

    public String getCustomerId() {
        return customerId;
    }

    public String getName() {
        return name;
    }
}

// Define Rental class
class Rental {
    private Car car;
    private Customer customer;
    private int days;

    public Rental(Car car, Customer customer, int days) {
        this.car = car;
        this.customer = customer;
        this.days = days;
    }

    public Car getCar() {
        return car;
    }

    public Customer getCustomer() {
        return customer;
    }

    public int getDays() {
        return days;
    }
}

// Define CarRentalSystem class
class CarRentalSystem {
    private List<Car> cars;
    private List<Customer> customers;
    private List<Rental> rentals;

    public CarRentalSystem() {
        cars = new ArrayList<>();
        customers = new ArrayList<>();
        rentals = new ArrayList<>();
    }

    public void addCar(Car car) {
        cars.add(car);
    }

    public void addCustomer(Customer customer) {
        customers.add(customer);
    }

    public void rentCar(Car car, Customer customer, int days) {
        if (car.isAvailable()) {
            car.rent();
            rentals.add(new Rental(car, customer, days));
        } else {
            System.out.println("Car is not available for rent.");
        }
    }

    public void returnCar(Car car) {
        car.returnCar();
        Rental rentalToRemove = null;
        for (Rental rental : rentals) {
            if (rental.getCar() == car) {
                rentalToRemove = rental;
                break;
            }
        }
        if (rentalToRemove != null) {
            rentals.remove(rentalToRemove);
        } else {
            System.out.println("Car was not rented.");
        }
    }

    public void showRentals() {
        if (rentals.isEmpty()) {
            System.out.println("\nNo active rentals.");
        } else {
            System.out.println("\n== Active Rentals ==\n");
            for (Rental rental : rentals) {
                System.out.println("Car ID: " + rental.getCar().getCarId());
                System.out.println("Car: " + rental.getCar().getBrand() + " " + rental.getCar().getModel());
                System.out.println("Rented by: " + rental.getCustomer().getName() + " (Customer ID: " + rental.getCustomer().getCustomerId() + ")");
                System.out.println("Rental Days: " + rental.getDays());
                System.out.println("----------------------------------");
            }
        }
    }

    public void menu() {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("===== Car Rental System =====");
            System.out.println("1. Rent a Car");
            System.out.println("2. Return a Car");
            System.out.println("3. Show Active Rentals");
            System.out.println("4. Exit");
            System.out.print("Enter your choice: ");

            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            if (choice == 1) {
                rentCarProcess(scanner);
            } else if (choice == 2) {
                returnCarProcess(scanner);
            } else if (choice == 3) {
                showRentals();
            } else if (choice == 4) {
                break;
            } else {
                System.out.println("Invalid choice. Please enter a valid option.");
            }
        }

        System.out.println("\nThank you for using the Car Rental System!");
    }

    private void rentCarProcess(Scanner scanner) {
        System.out.println("\n== Rent a Car ==\n");
        System.out.print("Enter your name: ");
        String customerName = scanner.nextLine();

        System.out.println("\nAvailable Cars:");
        for (Car car : cars) {
            if (car.isAvailable()) {
                System.out.println(car.getCarId() + " - " + car.getBrand() + " " + car.getModel());
            }
        }

        System.out.print("\nEnter the car ID you want to rent: ");
        String carId = scanner.nextLine();

        System.out.print("Enter the number of days for rental: ");
        int rentalDays = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        Customer newCustomer = new Customer("CUS" + (customers.size() + 1), customerName);
        addCustomer(newCustomer);

        Car selectedCar = null;
        for (Car car : cars) {
            if (car.getCarId().equals(carId) && car.isAvailable()) {
                selectedCar = car;
                break;
            }
        }

        if (selectedCar != null) {
            double totalPrice = selectedCar.calculatePrice(rentalDays);
            System.out.println("\n== Rental Information ==\n");
            System.out.println("Customer ID: " + newCustomer.getCustomerId());
            System.out.println("Customer Name: " + newCustomer.getName());
            System.out.println("Car: " + selectedCar.getBrand() + " " + selectedCar.getModel());
            System.out.println("Rental Days: " + rentalDays);
            System.out.printf("Total Price: $%.2f%n", totalPrice);

            System.out.print("\nConfirm rental (Y/N): ");
            String confirm = scanner.nextLine();

            if (confirm.equalsIgnoreCase("Y")) {
                rentCar(selectedCar, newCustomer, rentalDays);
                System.out.println("\nCar rented successfully.");
            } else {
                System.out.println("\nRental canceled.");
            }
        } else {
            System.out.println("\nInvalid car selection or car not available for rent.");
        }
    }

    private void returnCarProcess(Scanner scanner) {
        System.out.println("\n== Return a Car ==\n");
        System.out.print("Enter the car ID you want to return: ");
        String carId = scanner.nextLine();

        Car carToReturn = null;
        for (Car car : cars) {
            if (car.getCarId().equals(carId) && !car.isAvailable()) {
                carToReturn = car;
                break;
            }
        }

        if (carToReturn != null) {
            Customer customer = null;
            for (Rental rental : rentals) {
                if (rental.getCar() == carToReturn) {
                    customer = rental.getCustomer();
                    break;
                }
            }

            if (customer != null) {
                returnCar(carToReturn);
                System.out.println("Car returned successfully by " + customer.getName());
            } else {
                System.out.println("Car was not rented or rental information is missing.");
            }
        } else {
            System.out.println("Invalid car ID or car is not rented.");
        }
    }
}

// Main class to run the application
public class Main {
    public static void main(String[] args) {
        CarRentalSystem carRentalSystem = new CarRentalSystem();

        // Sample data (optional): Add some cars to the system
        carRentalSystem.addCar(new Car("CAR1", "Toyota", "Camry", 40.0));
        carRentalSystem.addCar(new Car("CAR2", "Honda", "Civic", 35.0));
        carRentalSystem.addCar(new Car("CAR3", "Ford", "Mustang", 50.0));

        // Start the menu-driven system
        carRentalSystem.menu();
    }
}
