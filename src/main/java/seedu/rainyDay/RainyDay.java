package seedu.rainyDay;

import seedu.rainyDay.command.CommandResult;
import seedu.rainyDay.data.UserData;
import seedu.rainyDay.exceptions.RainyDayException;
import seedu.rainyDay.modules.Storage;
import seedu.rainyDay.modules.Ui;
import seedu.rainyDay.command.Command;
import seedu.rainyDay.data.FinancialReport;
import seedu.rainyDay.modules.Parser;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

public class RainyDay {
    public static String filePath = "./data/rainyDay.json";
    public static UserData userData;

    private static Logger logger = Logger.getLogger(RainyDay.class.getName());

    private final Ui ui;

    private RainyDay(String filePath) {
        ui = new Ui();
        try {
            ui.printLogo();
            userData = Storage.loadFromFile(filePath);
            ui.greetUser(userData.getReportOwner());
            assert userData != null : "Error loading from json file";
            logger.log(Level.INFO, "File loaded successfully.");
        } catch (Exception e) {
            logger.log(Level.INFO, "No valid save file detected. Starting with empty financial data.");
            ui.noFileExist();
            String username = ui.readUserName();
            assert username != null : "Inputted username should not be null";
            FinancialReport financialReport = new FinancialReport(new ArrayList<>(), new HashMap<>());
            userData = new UserData(financialReport);
            financialReport.setReportOwner(username);
        }
    }

    private void run() {
        setUpDate();
        runCommand();
        ui.sayFarewellToUser(userData.getReportOwner());
    }

    private void setUpDate() {
        System.out.println(userData.checkUserBudgetLimit(LocalDate.now()));
        Storage.writeToFile(RainyDay.userData, RainyDay.filePath);
    }

    private void runCommand() {
        Command specificCommand;
        while (true) {
            try {
                String userInput = ui.readUserCommand();
                if (userInput.equalsIgnoreCase("bye")) {
                    break;
                }
                specificCommand = new Parser().parseUserInput(userInput);
                assert specificCommand != null : "Parser returned null";
                executeCommand(specificCommand);
            } catch (Exception e) {
                logger.log(Level.WARNING, e.getMessage());
                System.out.println(e.getMessage());
            }
        }
    }

    private void executeCommand(Command command) throws RainyDayException {
        command.setData(userData);
        CommandResult result = command.execute();
        if (result != null) {
            System.out.println(result.output);
        }
        Storage.writeToFile(userData, filePath);
    }

    private static void setupLogger() {
        LogManager.getLogManager().reset();
        logger.setLevel(Level.INFO);
        try {
            FileHandler fileHandler = new FileHandler("RainyDay.log");
            logger.addHandler(fileHandler);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "File logger not working.", e);
        }
    }

    public static void main(String[] args) {
        setupLogger();
        logger.log(Level.INFO, "Starting RainyDay");
        new RainyDay(filePath).run();
        logger.log(Level.INFO, "Quitting RainyDay");
    }
}
