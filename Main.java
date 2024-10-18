package com.music;
import java.util.Scanner;

class MainApp {
   
 public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Initialize database connection
        ConnectionClassForDM.getConnection();

        boolean running = true;
        while (running) {
            System.out.println("Welcome to Spotify Console!");
            System.out.println("1. Admin");
            System.out.println("2. User");
            System.out.println("3. Exit");
            System.out.print("Choose an option: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    Admin admin = new Admin(choice);
                    admin.manageSongs();
                    break;
                case 2:
                    Registration registration = new Registration();
                    boolean isLoggedIn = false;
                    while (!isLoggedIn) {
                        System.out.println("\n1. Register");
                        System.out.println("2. Login");
                        System.out.println("3. Exit");
                        System.out.print("Choose an option: ");
                        int userChoice = scanner.nextInt();
                        scanner.nextLine(); // Consume newline

                        switch (userChoice) {
                            case 1:
                                registration.registerUser();
                                break;
                            case 2:
                                isLoggedIn = registration.loginUser();
                                if (isLoggedIn) {
                                    User loggedInUser = registration.getLoggedInUser();

                                   // DemoMusicMain dm = new DemoMusicMain();
                                    Playlist menu = new Playlist(loggedInUser);
                                    menu.userMenu();
                                }
                                break;
                            case 3:
                                running = false;
                                break;
                            default:
                                System.out.println("Invalid choice. Please try again.");
                        }
                    }
                    break;
                case 3:
                    running = false;
                    System.out.println("Exiting...");
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }

        scanner.close();
    }
}