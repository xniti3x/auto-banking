package com.example.autobanking.shell;

import com.example.autobanking.entity.User;
import com.example.autobanking.service.BankService;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

import java.util.Scanner;


@ShellComponent
@RequiredArgsConstructor
public class ShellCommands {

    private final BankService bankService;

    @PostConstruct
    public void runAtStartup() {
        System.out.println(welcome());
        User user = bankService.findUser();
        if(user!=null && user.isAutomationEnabled()){
            bankService.toggleAutomation(true);
        }
    }

    @ShellMethod(key = {"setup","3"}, value = "Setup user with secrets and requisitionId")
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

    @ShellMethod(key = {"status","s","4"}, value = "Show current status")
    public String status() {
        return bankService.getStatus();
    }

    @ShellMethod(key = {"automation-enable","ae","1"}, value = "Enable automation")
    public String enableAutomation() {
        
        return bankService.toggleAutomation(true);
    }

    @ShellMethod(key = {"automation-disable","ad","2"}, value = "Disable automation")
    public String disableAutomation() {
        return bankService.toggleAutomation(false);
    }

    @ShellMethod(key = {"exit","q","6"}, value = "Exit the application")
    public void exit() {
        System.out.println("Exiting...");
        System.exit(0);
    }

    @ShellMethod(key = {"import-json", "ij","5"}, value = "Import data from a JSON file")
    public void importJson(String filePath) {
        bankService.importJson(filePath);    
    }

    @ShellMethod(value = "Show welcome message", key = {"welcome", "w"})
    public String welcome() {
        return """
                
                ██████╗  █████╗ ███╗   ██╗██╗  ██╗██╗███╗   ██╗ ██████╗ 
                ██╔══██╗██╔══██╗████╗  ██║██║ ██╔╝██║████╗  ██║██╔════╝ 
                ██████╔╝███████║██╔██╗ ██║█████╔╝ ██║██╔██╗ ██║██║  ███╗
                ██╔══██╗██╔══██║██║╚██╗██║██╔═██╗ ██║██║╚██╗██║██║   ██║
                ██████╔╝██║  ██║██║ ╚████║██║  ██╗██║██║ ╚████║╚██████╔╝
                ╚═════╝ ╚═╝  ╚═╝╚═╝  ╚═══╝╚═╝  ╚═╝╚═╝╚═╝  ╚═══╝ ╚═════╝ 
                
                🏦 auto - banking - GoCardless Integration
                
                Welcome to the Banking Shell! 
                Type 'menu' or 'm' to see available commands.
                Type 'help' to see all shell commands.
                Type 'setup' to see/follow the setup flow.
                """;
    }

    @ShellMethod(value = "Display the main menu", key = {"menu", "m"})
    public String showMenu() {
        return """
                
                🏦 auto - banking 
                ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
                
                Available Commands:
                   
                1️⃣  automation-on       - turn ON automation
                2️⃣  automation-off      - turn OFF automation
                3️⃣  setup               - setup credentials
                4️⃣  status              - Show current status
                5️⃣  import json         - import transactions
                6️⃣  exit                - Exit the application
                
                💡 You can also use short commands: ae, ad, setup, s, ji, q
                ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
                Type 'help' to see all available commands
                Type 'setup' to see/follow the setup flow.

                import example 'ij filepath/transaction.json 
                 - only supports GoCardless json format
                 - do not import transactions without transactionId!
                """;
    }

}
