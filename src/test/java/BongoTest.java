import com.applitools.eyes.BatchInfo;
import com.applitools.eyes.StdoutLogHandler;
import com.applitools.eyes.selenium.Eyes;
import com.applitools.eyes.selenium.fluent.Target;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.Test;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

public class BongoTest {

    @Test
    public void test() throws URISyntaxException {
        WebDriver driver = null;
        try {
            Eyes eyes = new Eyes();
            eyes.setBranchName("BongoBranch" + getLimitedIndex(currentBranchIndex, 10));
            eyes.setServerUrl(new URI("https://eyes.applitools.com"));
            eyes.setApiKey("pYhNeRI8AJIwNMX1tSuHc4BkV7JTkB97SZatyfoXTYjE110");
            eyes.setLogHandler(new StdoutLogHandler(false));
            BatchInfo batchInfo = new BatchInfo("Large images batch");
            batchInfo.setId(UUID.randomUUID().toString().substring(0, 10));
            eyes.setBatch(batchInfo);
            driver = new ChromeDriver();
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
        } finally {
            driver.close();
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
