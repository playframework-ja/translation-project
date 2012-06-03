package helper;

import helper.*;

import org.junit.Test;
import play.test.UnitTest;


public class StringUtilsTest extends UnitTest {

    @Test
    public void humanizeTest() {
        assertEquals("", "Camel Case", StringUtils.humanize("camelCase"));
        assertEquals("", "The Long And Winding Road", StringUtils.humanize("theLongAndWindingRoad"));
    }
}
