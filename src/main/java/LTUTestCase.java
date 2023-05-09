public class LTUTestCase {

    public static void main(String args []){

        SelenideRun web_bot = new SelenideRun();

        web_bot.run_setup();
        web_bot.login();
        web_bot.navigate_to_exam();
        web_bot.ExtractExamInfo();
    }
}
