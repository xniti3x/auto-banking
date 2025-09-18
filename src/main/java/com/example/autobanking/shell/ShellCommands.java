package com.example.autobanking.shell;

import com.example.autobanking.service.BankService;
import lombok.RequiredArgsConstructor;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

import java.util.Scanner;

@ShellComponent
@RequiredArgsConstructor
public class ShellCommands {

    private final BankService bankService;

    @ShellMethod(key = "setup", value = "Setup user with secrets and requisitionId")
    public String setup() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter secretKey: ");
        String secretKey = scanner.nextLine();
        System.out.print("Enter secretId: ");
        String secretId = scanner.nextLine();
        System.out.print("Enter requisitionId: ");
        String requisitionId = scanner.nextLine();

        bankService.setupUser(secretKey, secretId, requisitionId);
        return "User setup completed.";
    }

    @ShellMethod(key = "status", value = "Show current status")
    public String status() {
        return bankService.getStatus();
    }

    @ShellMethod(key = "automation-enable", value = "Enable automation")
    public String enableAutomation() {
        
        return bankService.toggleAutomation(true);
    }

    @ShellMethod(key = "automation-disable", value = "Disable automation")
    public String disableAutomation() {
        return bankService.toggleAutomation(false);
    }

    @ShellMethod(key = "metadata", value = "Show metadata placeholder")
    public String showMetaData() {
        return bankService.showMetaData();
    }

    @ShellMethod(key = "exit", value = "Exit the application")
    public void exit() {
        System.out.println("Exiting...");
        System.exit(0);
    }
}
