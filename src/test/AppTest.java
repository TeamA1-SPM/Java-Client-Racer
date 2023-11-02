package test;

import main.Main;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class AppTest {
    @Test void appHasAGreeting() {
        Main classUnderTest = new Main();
        assertNotNull(classUnderTest.getGreeting(), "app should have a greeting");
    }
}
