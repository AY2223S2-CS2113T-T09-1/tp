package seedu.rainyDay;

import seedu.rainyDay.exceptions.RainyDayException;
import seedu.rainyDay.modules.Storage;
import seedu.rainyDay.modules.Ui;
import seedu.rainyDay.command.Command;
import seedu.rainyDay.data.FinancialReport;
import seedu.rainyDay.data.Parser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class RainyDay {
    public static String filePath = "rainyDay.txt";
    public static FinancialReport financialReport = new FinancialReport(new ArrayList<>());
    private static Logger logger = Logger.getLogger("RainyDayLog.log");
    private final Ui ui;

    private RainyDay(String filePath) {
        ui = new Ui();
        try {
            financialReport = Storage.loadFromFile(filePath);
            logger.log(Level.INFO, "File loaded successfully.");
        } catch (IOException | ClassNotFoundException | ClassCastException e) {
            logger.log(Level.INFO, "No valid save file detected. Starting with empty financial data.");
            ui.noFileExist();
            String username = ui.readUserName();
            assert username != null : "Inputted username should not be null";
            financialReport.setReportOwner(username);
        }
    }

    private void run() {
        ui.printLogo();
        ui.greetUser(financialReport.getReportOwner());

        while (true) {
            String userInput = ui.readUserCommand();
            if (userInput.equalsIgnoreCase(Command.COMMAND_EXIT)) {
                break;
            }
            try {
                Command specificCommand = Parser.parseUserInput(userInput);
                specificCommand.setData(financialReport);
                assert specificCommand != null : "Parser returned null";
                specificCommand.execute();
                //ui.printEmptyLine();
            } catch (RainyDayException e) {
                logger.log(Level.INFO, "RainyDayException caught");
                System.out.println(e.getMessage());
            } catch (Exception e) {
                logger.log(Level.WARNING, e.getMessage());
            }
        }
        ui.sayFarewellToUser(financialReport.getReportOwner());
    }

    public static void main(String[] args) {
        logger.log(Level.INFO, "Starting RainyDay");
        new RainyDay(filePath).run();
        logger.log(Level.INFO, "Quitting RainyDay");
    }
}
