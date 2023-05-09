import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selectors;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.io.File;
import java.nio.file.StandardCopyOption;

import com.codeborne.selenide.Screenshots;
import java.io.IOException;
import java.nio.file.Path;


import static com.codeborne.selenide.Selenide.*;

public class SelenideRun {
    static String email = "";
    static String password = "";

    public static boolean isExamInformationCorrect(String course, String date, String location, String start, String end) {
        final String Correct_course = "I0015N";
        final String Correct_date = "2023-05-30";
        final String Correct_location = "Luleå internet (INTERNET)";
        final String Correct_start = "09:00";
        final String Correct_end = "14:00";

        return course.contains(Correct_course) &&
                date.contains(Correct_date) &&
                location.contains(Correct_location) &&
                start.contains(Correct_start) &&
                end.contains(Correct_end);
    }

    //Defining the logger
    private static final Logger logger = LoggerFactory.getLogger(LTUTestCase.class);

    public static void run_setup() {

        System.setProperty("selenide.holdBrowserOpen", "true");

        // Load the JSON file into a String
        String jsonString = null;
        try {
            jsonString = new String(Files.readAllBytes(Paths.get("c:/Temp/credentials.json")));
        } catch (Exception e) {
            logger.error("Error occurred with json: ", e);
        }
        //Sets screenshots path
        Configuration.reportsFolder = "target/screenshots";


        // Create a JSONObject from the JSON String
        JSONObject jsonObject = new JSONObject(jsonString);

        // Get the password value from the JSONObject
        email = jsonObject.getString("email");
        password = jsonObject.getString("password");

        //Opens LTU website
        try {
            Configuration.browser = "chrome";

            Configuration.browserSize = "1920x1080";


            open("https://www.ltu.se/");

            if (title().isEmpty()) {
                logger.error("Failed to open webpage title empty");
            } else {
                logger.info("Successfully opened webpage: " + title());
            }
        } catch (Exception e) {
            logger.error("Error occurred with opening window: ", e);
        }
    }
    public static void login() {

        //Accepts cookies
        try {
            if ($(Selectors.byId("CybotCookiebotDialogBodyLevelButtonLevelOptinAllowAll")).exists()) {
                $(Selectors.byId("CybotCookiebotDialogBodyLevelButtonLevelOptinAllowAll")).click();

                logger.info("Cockies notification has been closed");
            } else {
                logger.info("Cookies notification was not shown");
            }
        } catch (Exception e) {
            logger.error("Error occurred with cookies window: ", e);
        }

        try {
            //Presses StudentBTN
            $(Selectors.byCssSelector("#main-nav > div.ltu-nav-right > div > a:nth-child(1)")).click();

            logger.info("Student button successfully clicked");

        } catch (Exception e) {
            logger.error("Error occurred with Student BTN: ", e);
        }


        try {
            //Presses login btn
            $(Selectors.byCssSelector("#maincontent > div:nth-child(1) > div > div:nth-child(1) > div > div > div.card-footer > a")).click();
            logger.info("Login button successfully clicked");

        } catch (Exception e) {
            logger.error("Error occurred with Lgoin BTN: ", e);
        }

        try {
            //Selects the username field
            $(Selectors.byId("username")).sendKeys(email);

            //Selects the password field
            $(Selectors.byId("password")).sendKeys(password);

            //Presses Login BTN
            $(Selectors.byName("submit")).click();
            logger.info("Credentials successfully entered and login button pressed");
            sleep(1000);
        } catch (Exception e) {
            logger.error("Error occurred with entering credentials and pressing button: ", e);
        }

    }
    public static void navigate_to_exam() {

        try {
            //Presses examinations and then examinations schedule
            $(Selectors.byCssSelector("a[id$='261']")).click();

            $(Selectors.byCssSelector("a[id$='265']")).click();

        } catch (Exception e) {

        }

        try {
            //Switches to new window and sleeps
            switchTo().window(1);
            sleep(1000);
            logger.info("Kronox window selected");

        } catch (Exception e) {
            logger.error("Error occured while switching window " + e);
        }

        try {
            //Presses kronoxLogin
            $(Selectors.byCssSelector("html > body > div:nth-of-type(1) > div:nth-of-type(1) > table > tbody > tr > td:nth-of-type(2) > div > div > a:nth-of-type(2) > span")).click();

            //Logs user in

            //Types in username
            $(Selectors.byCssSelector("input[id$='username']")).sendKeys(email);
            logger.info("Username entered");

            //Types in password
            $(Selectors.byCssSelector("input[id$='password']")).sendKeys(password);
            logger.info("Password entered");

            //press username field to remove autofill
            $(Selectors.byCssSelector("input[id$='username']")).click();

            //presses login button
            $(Selectors.byCssSelector("input[id*='button']")).click();
            logger.info("User logged in");


        } catch (Exception e) {
            logger.info("Error occurred during login: " + e);
        }

        try {
            //Opens the exam activity view
            $(Selectors.byCssSelector("html > body > div:nth-of-type(1) > div:nth-of-type(2) > div > div > ul > li:nth-of-type(9) > a > em > b")).click();

            logger.info("Opened activity view");
        } catch (Exception e) {
            logger.error("Failed to open activity view " + e);
        }
    }
    public ExamInformation ExtractExamInfo(){
        try{

            //Grabs information from website
            final String course = $(Selectors.byCssSelector("html > body > div:nth-of-type(1) > div:nth-of-type(4) > div > div:nth-of-type(1) > div > div:nth-of-type(1) > div:nth-of-type(1) > b")).getText();

            final String date = $(Selectors.byCssSelector("html > body > div:nth-of-type(1) > div:nth-of-type(4) > div > div:nth-of-type(1) > div > div:nth-of-type(1) > div:nth-of-type(2)")).getText();

            final String location = $(Selectors.byCssSelector("html > body > div:nth-of-type(1) > div:nth-of-type(4) > div > div:nth-of-type(1) > div > div:nth-of-type(1) > div:nth-of-type(3)")).getText();

            final String start = $(Selectors.byCssSelector("html > body > div:nth-of-type(1) > div:nth-of-type(4) > div > div:nth-of-type(1) > div > div:nth-of-type(1) > div:nth-of-type(4)")).getText();

            final String end = $(Selectors.byCssSelector("html > body > div:nth-of-type(1) > div:nth-of-type(4) > div > div:nth-of-type(1) > div > div:nth-of-type(1) > div:nth-of-type(5)")).getText();

            logger.info("Information about exam collected");

            return new ExamInformation(course, date, location, start, end);
        }
        catch (Exception e){
            logger.error("Error occurred grabbing info: " + e);
            return null;
        }
    }

    public void takeScreenshotOfFinalExamInfo() {
        String customFileName = "final_examination.jpeg";

        // Take a screenshot using Selenide and get the file
        File screenshotFile = $(Selectors.byCssSelector("html > body > div:nth-of-type(1) > div:nth-of-type(4) > div > div:nth-of-type(1) > div > div:nth-of-type(1)")).screenshot();

        // Move the screenshot to a new file with a custom file name
        try {
            Path sourcePath = screenshotFile.toPath();
            Path targetPath = sourcePath.resolveSibling(customFileName);
            Files.move(sourcePath, targetPath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            System.err.println("Error renaming the screenshot file: " + e.getMessage());
        }
    }

    public static boolean isExamInformationCorrect(ExamInformation examInfo, String correctCourse, String correctDate, String correctLocation, String correctStart, String correctEnd) {
        return examInfo.getCourse().contains(correctCourse) &&
                examInfo.getDate().contains(correctDate) &&
                examInfo.getLocation().contains(correctLocation) &&
                examInfo.getStart().contains(correctStart) &&
                examInfo.getEnd().contains(correctEnd);
    }
}
