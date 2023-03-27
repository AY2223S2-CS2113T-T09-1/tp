package seedu.rainyDay.command;

import org.junit.jupiter.api.Test;
import seedu.rainyDay.data.FinancialReport;
import seedu.rainyDay.data.FinancialStatement;
import seedu.rainyDay.data.UserData;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AddCommandTest {
    ArrayList<FinancialStatement> statements = new ArrayList<>();
    HashMap<Integer, Double> monthlyExpenditures = new HashMap<>();
    FinancialReport financialReport = new FinancialReport(statements, monthlyExpenditures);
    UserData userData = new UserData(financialReport);

    @Test
    public void execute_singleStatement_statementInformation() {
        AddCommand addCommand = new AddCommand("Ipad", "out", 120.50, "Default",
                LocalDate.now());
        addCommand.setData(userData);
        addCommand.execute();
        FinancialStatement expectedStatement = new FinancialStatement("Ipad", "out", 120.5,
                "Default", LocalDate.now());
        assertEquals(expectedStatement.getFullStatement(),
                userData.getStatement(0).getFullStatement());
    }

    @Test
    public void execute_multipleStatements_statementsInformation() {
        AddCommand addCommand = new AddCommand("angpao", "in", 3000.00, "Default",
                LocalDate.now());
        addCommand.setData(userData);
        addCommand.execute();
        FinancialStatement expectedStatement = new FinancialStatement("angpao", "in", 3000.00,
                "Default", LocalDate.now());
        assertEquals(expectedStatement.getFullStatement(),
                financialReport.getFinancialStatement(0).getFullStatement());
        addCommand = new AddCommand("textbook", "out", 50.00, "Default",
                LocalDate.now());
        addCommand.setData(userData);
        addCommand.execute();
        assertEquals(2, userData.getStatementCount());
    }
}
