import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selectors;
import com.codeborne.selenide.WebDriverRunner;
import org.json.JSONObject;
import org.openqa.selenium.By;
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
import static com.codeborne.selenide.Selenide.$;
import org.openqa.selenium.chrome.ChromeOptions;

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

            logger.info("Exam is the following date: " + date);

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

    public void logout_kronox(){

        try {
            //Switches to new window and sleeps
            switchTo().window(1);
            sleep(1000);
            logger.info("Kronox window selected");

        } catch (Exception e) {
            logger.error("Error occured while switching window " + e);
        }

        try{
            //Presses logout button
            $(Selectors.byCssSelector("a[class='greenbutton']")).click();
            logger.info("User logged out");
        }
        catch (Exception e){
            logger.error("An error occured while logging out : " + e);
        }


    }

    public void logout_ltu(){


        try {
            //Switches to new window and sleeps
            switchTo().window(0);
            sleep(1000);
            logger.info("LTU window selected");

        } catch (Exception e) {
            logger.error("Error occured while switching window " + e);
        }

        try{
            //Presses avatar button
            $(Selectors.byCssSelector("a[class^='user-avatar-link']")).click();

            //Presses logout button
            $(Selectors.byCssSelector("a[title^='Logga']")).click();

            logger.info("User logged out");
        }
        catch (Exception e){
            logger.error("An error occured while logging out : " + e);
        }
    }
    public void navigate_to_transcript() {
        try{
            //Find "Intyg btn" and click it
            $(Selectors.byCssSelector("html > body > div > div:nth-of-type(1) > div:nth-of-type(4) > div:nth-of-type(1) > div:nth-of-type(2) > div:nth-of-type(2) > ul:nth-of-type(2) > li:nth-of-type(4) > a")).click();
            logger.info("Intyg button clicked");
        }
        catch (Exception e){
            logger.error("Could not find intyg button error : " + e);
        }


        try {
            //Switches to new window and sleeps
            switchTo().window(2);
            sleep(1000);
            logger.info("Ladok window selected");

        } catch (Exception e) {
            logger.error("Error occured while switching window " + e);
        }
        try{
            if ($(Selectors.byCssSelector("button[class$='btn-light']")).exists()){
                //accept cookies
                $(Selectors.byCssSelector("button[class$='btn-light']")).click();
                logger.info("Cookies button clicked");
            }
            else{
                logger.info("No cookies was shown");
            }

        }
        catch (Exception e){
            logger.error("Could not find cookies button error : " + e);

        }

        try{
            //Login through school btn click
            $(Selectors.byCssSelector("a[class$='btn-ladok-inloggning']")).click();
            logger.info("School button clicked");
        }
        catch (Exception e){
            logger.error("Could not find school button error : " + e);

        }

        try{
            //Enter school through send keys
            $(Selectors.byCssSelector("input[id='searchinput']")).sendKeys("LTU");
            logger.info("School name entered");

        }
        catch (Exception e){
            logger.error("could not send keys to school name error : " + e);
        }

        try{
            //Click on LTU
            $(Selectors.byCssSelector("div[class$='institution-text']")).click();
            logger.info("LTU clicked");

        }
        catch (Exception e){
            logger.error("Could not click on LTU error : " + e);
        }


        try{
            //Click on transcripts and certification
            $(Selectors.byCssSelector("html > body > ladok-root > div > ladok-sido-meny > nav > div:nth-of-type(1) > ul:nth-of-type(1) > li:nth-of-type(3) > ladok-behorighetsstyrd-nav-link > a")).click();
            logger.info("Transcripts and certification clicked");
        }
        catch (Exception e){
            logger.error("Could not click on transcripts and certification error : " + e);
        }

    }

    public boolean createsTranscript(){

        try {
            //Create button click
            $(Selectors.byCssSelector("html > body > ladok-root > div > main > div > ladok-intyg > ladok-skapa-intyg-knapp > div > button")).click();
            logger.info("Create button clicked");

        }
        catch (Exception e){
            logger.error("Could not click on create button error : " + e);
        }

        try {
            //Open dropdown menu
            $(Selectors.byCssSelector("select[class^='form-select']")).click();
            logger.info("Dropdown menu opened");
        }
        catch (Exception e){
            logger.error("Could not open dropdown menu error : " + e);
        }
        try {
            sleep(1000);
            //Selects certificate of registration
            $(Selectors.byCssSelector("select[id='intygstyp']")).selectOption("Registreringsintyg");
            logger.info("Certificate of registration selected");
        }
        catch (Exception e){
            logger.error("Could not select certificate of registration error : " + e);
        }

        try {
            sleep(1000);
            //Checks the box "All registrations arranged by programme (or equivalent)"
            $(Selectors.byCssSelector("input[id='allaRegistreringarGrupperdePaProgramRadio']")).click();
            logger.info("Checkbox clicked");
        }
        catch (Exception e){
            logger.error("Could not click checkbox error : " + e);
        }

        try{
            sleep(1000);
            //Creates the transcript button click
            $(Selectors.byCssSelector("button[class*='btn-ladok-brand']")).click();
            logger.info("Transcript created");
            return true;
        }
        catch (Exception e){
            logger.error("Transcript not created error : " + e.getMessage());
            return false;
        }
    }

    public void download_transcript(){

        sleep(1000);
        //Downloads the transcript
        $(Selectors.byCssSelector("a[class='card-link']")).click();
    }

    public static boolean isIntygPdfPresent() {
        // Get the current working directory
        String currentDirectory = Paths.get("").toAbsolutePath().toString();
        String targetPath = currentDirectory + "/target/files/Intyg.pdf";

        File IntygPdf = new File(targetPath);

        // Return true if the file exists, false otherwise
        return IntygPdf.exists();
    }

    public static boolean isSyllabulsPresent() {
        // Get the current working directory
        String currentDirectory = Paths.get("").toAbsolutePath().toString();
        String targetPath = currentDirectory + "/target/files/Kursplan_I0015N.pdf";

        File SyllabulsPdf = new File(targetPath);

        // Return true if the file exists, false otherwise
        return SyllabulsPdf.exists();
    }

    public void Download_course_syllabus() {

        try {
            // Click homepage
            sleep(1000);
            $(Selectors.byCssSelector("img[data-img='/ltu-theme/images/ltu/header/logo.png']")).click();
            logger.info("Homepage clicked");
        } catch (Exception e) {
            logger.error("Could not click homepage error : " + e);
        }

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
            // Click search button and send keys
            sleep(1000);
            $(Selectors.byXpath("//button[contains(@class, 'ltu-search-btn')]")).click();
            //Send keys
            $(Selectors.byCssSelector("input[id='cludo-search-bar-input']")).sendKeys("I0015N");
            logger.info("Sent keys to search button");
        } catch (Exception e) {
            logger.error("Could not send keys to search button error : " + e);
        }

        try {
            // Click search
            sleep(1000);
            $(Selectors.byXpath("//button[@class='button is-medium is-info']")).click();
            logger.info("Search button clicked");
        } catch (Exception e) {
            logger.error("Could not click search button error : " + e);
        }

        try {
            // Click course
            sleep(1000);
            $(Selectors.byCssSelector("a[class='courseTitle'] h2")).click();
            logger.info("Course clicked");
        } catch (Exception e) {
            logger.error("Could not click course error : " + e);
        }
        try {
            //Selects the right year
            $(Selectors.byXpath("/html/body/main/div/div/div/div[2]/div/article/div[1]/section/div[4]/ul/li[2]/a/span[2]")).click();
            logger.info("Right year selected");
        }
        catch (Exception e){
            logger.error("Could not select right year error : " + e);
        }


        try {
            // Scroll and find course syllbuls link and click
            sleep(1000);
            $(Selectors.byXpath("/html/body/main/div/div/div/div[2]/div/article/div[1]/section/div[8]/div")).scrollTo();
            sleep(1000);
            $(Selectors.byCssSelector("#maincontent > article > div.article-container > section > div.more-edu-info > div > a")).click();
            logger.info("Scrolled to course syllabus link and clicked it");
        } catch (Exception e) {
            logger.error("Could not scroll to course syllabus link error : " + e);
        }

        try {
            // Downloads the pdf
            sleep(1000);
            $(Selectors.byXpath("/html/body/main/div/div/div/div[2]/div/article/div[1]/section/form/div[4]/a/div")).click();
            logger.info("PDF downloaded");
        } catch (Exception e) {
            logger.error("Could not download PDF error : " + e);
        }

    }

}
