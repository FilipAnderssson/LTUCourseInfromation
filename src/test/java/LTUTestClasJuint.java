import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.TestInfo;

import static org.junit.jupiter.api.Assertions.assertTrue;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class LTUTestClasJuint {
    private SelenideRun web_bot;

    @BeforeEach
    void setUp() {
        web_bot = new SelenideRun();
        web_bot.run_setup();
        web_bot.login();
    }

    @Test
    @Order(1)
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

        web_bot.logout_kronox();
    }
    @Test
    @Disabled
    @Order(2)
    void CreatesTranscript(){
        web_bot.navigate_to_transcript();
        boolean result = web_bot.createsTranscript();
        assertTrue(result, "Transcript not created");
    }

    @Test
    @Order(3)
    void downloadTranscript(){
        web_bot.navigate_to_transcript();
        web_bot.download_transcript();
        boolean result = web_bot.isIntygPdfPresent();
        assertTrue(result, "Intyg not found");
    }

    @Test
    @Order(4)
    void downloadCourseSyllabuls(){
        web_bot.Download_course_syllabus();
        boolean result = web_bot.isSyllabulsPresent();
        assertTrue(result, "Syllabuls not found");
    }

    @AfterEach
    void exit(TestInfo testInfo){
        if (!testInfo.getTestMethod().orElseThrow().getName().equals("downloadCourseSyllabuls")) {
            web_bot.logout_ltu();
        }
    }
}
