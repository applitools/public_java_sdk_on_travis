import com.applitools.eyes.BatchInfo;
import com.applitools.eyes.StdoutLogHandler;
import com.applitools.eyes.selenium.Eyes;
import com.applitools.eyes.selenium.fluent.Target;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.Test;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.concurrent.atomic.AtomicReference;

public class BongoTest {

    @Test
    public void test() throws URISyntaxException {

        System.out.println("Log num 1");
        System.setProperty("webdriver.chrome.driver","/home/travis/build/chromedriver");
        Eyes eyes = new Eyes();
        eyes.setBranchName("BongoBranch" + getLimitedIndex(currentBranchIndex, 10));
        eyes.setServerUrl(new URI("https://test2eyes.applitools.com"));
        eyes.setApiKey(System.getenv("APPLITOOLS_API_KEY"));
        eyes.setLogHandler(new StdoutLogHandler(false));
        BatchInfo batchInfo = new BatchInfo("Large images batch");
        batchInfo.setId("12345");
        eyes.setBatch(batchInfo);
        WebDriver driver = new ChromeDriver();
        driver.get("https://www.smashingmagazine.com/2017/05/long-scrolling/");

        for (int i = 0; i < 100; i++) {
            try {
                int testIndex = getInfiniteIndex(currentTestIndex);
                int appIndex = getLimitedIndex(currentAppIndex, 30);
                eyes.open(driver, "LargePage - " + appIndex, "page - " + testIndex);
                eyes.check(Target.window().fully());
                eyes.close(false);
            } finally {
                eyes.abortIfNotClosed();
            }
        }
    }

    private static AtomicReference<Integer> currentTestIndex = new AtomicReference<Integer>();
    private static AtomicReference<Integer> currentAppIndex = new AtomicReference<Integer>();
    private static AtomicReference<Integer> currentBranchIndex = new AtomicReference<Integer>();

    private static synchronized int getInfiniteIndex(AtomicReference<Integer> index){
        if (index.get() == null) {
            index.set(0);
        }
        index.set(index.get() + 1);
        return index.get();
    }

    private static synchronized int getLimitedIndex(AtomicReference<Integer> index, int limit){
        if (index.get() == null) {
            index.set(0);
        }
        if (index.get() > limit) {
            index.set(1);
        } else {
            index.set(index.get() + 1);
        }
        return index.get();
    }
}
