import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;


public class LTUTestClasJuint {
    private SelenideRun web_bot;

    @BeforeEach
    void setUp() {
        web_bot = new SelenideRun();
        web_bot.run_setup();
        web_bot.login();
    }

    @Test
    void testIsExamInformationCorrect() {

        web_bot.navigate_to_exam();
        web_bot.takeScreenshotOfFinalExamInfo();

        ExamInformation examInfo = web_bot.ExtractExamInfo();

        String course = "I0015N";
        String date = "2023-05-30";
        String location = "Luleå internet (INTERNET)";
        String start = "09:00";
        String end = "14:00";

        boolean result = SelenideRun.isExamInformationCorrect(examInfo, course, date, location, start, end);
        assertTrue(result, "Exam information is incorrect.");
    }
}
