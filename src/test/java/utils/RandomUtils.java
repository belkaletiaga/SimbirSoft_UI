package utils;

import org.openqa.selenium.WebElement;

import java.util.List;
import java.util.Random;

public class RandomUtils {
    private static final Random random = new Random();

    public static WebElement selectRandomElement(List<WebElement> elements)  {
        WebElement randomElement = elements.get(random.nextInt(elements.size()));
        return randomElement;
    }

    public static String selectRandomString(List<String> elements)  {
        return elements.get(random.nextInt(elements.size()));
    }

    public static int getRandomInt(int min, int max) {
        return random.nextInt(max - min + 1) + min;
    }
}
