package seedu.rainyDay.command;

import seedu.rainyDay.RainyDay;
import seedu.rainyDay.data.FinancialStatement;
import seedu.rainyDay.modules.Storage;
import seedu.rainyDay.modules.Ui;

import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

public class AddCommand extends Command {
    private final Logger logger = Logger.getLogger(AddCommand.class.getName());
    private final String description;
    private final String flowDirection;
    private final int value;

    public AddCommand(String description, String flowDirection, int value) {
        this.description = description;
        this.flowDirection = flowDirection;
        this.value = value;
    }

    private void setupLogger() {
        LogManager.getLogManager().reset();
        logger.setLevel(Level.INFO);
        try {
            FileHandler fileHandler = new FileHandler("AddCommandExecute.log");
            logger.addHandler(fileHandler);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "File logger not working.", e);
        }
    }

    @Override
    public void execute() {
        setupLogger();
        logger.log(Level.INFO, "starting addCommand.execute");

        int totalStatementCount = financialReport.getStatementCount();

        FinancialStatement currentFinancialStatement = new FinancialStatement(description, flowDirection, value);
        financialReport.addStatement(currentFinancialStatement);

        assert totalStatementCount + 1 == financialReport.getStatementCount() : "statement count mismatch";

        logger.log(Level.INFO, " passed assertion");

        Ui.printAddedFinancialStatement(currentFinancialStatement);
        logger.log(Level.INFO, " passed Ui");
        Storage.writeToFile(financialReport, RainyDay.filePath);

        logger.log(Level.INFO, " passed storage");
        logger.log(Level.INFO, " end of addCommand.execute");
    }
}