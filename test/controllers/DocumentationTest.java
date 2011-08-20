package controllers;

import org.junit.Test;
import play.test.UnitTest;


public class DocumentationTest extends UnitTest {

    @Test
    public void humanizeTest() {
        assertEquals("", "Camel Case", Documentation.humanize("camelCase"));
        assertEquals("", "The Long And Winding Road", Documentation.humanize("theLongAndWindingRoad"));
    }
}
