/**
 * This file is licensed under the MIT license.
 * See the LICENSE file in project root for details.
 */

package in.praj.swfout;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.FileNotFoundException;

public class AppTest {
    private App app;

    @Before
    public void resetApp() {
        app = new App();
    }

    @Test
    public void testParseFailureOnZeroArgs() {
        Assert.assertThrows(
                "Should fail on zero CLI arguments",
                ParseException.class,
                () -> app.parse(new String[0]));
    }

    @Test
    public void testParseFailureOnNonexistentFile() {
        Assert.assertThrows(
                "Should fail when given nonexistent file",
                ParseException.class,
                () -> app.parse(new String[] {"missing.exe"}));
    }

    @Test
    public void testParseFailureOnPermissionError() {
        Assert.assertThrows(
                "Should fail when given non-permitted file",
                ParseException.class,
                () -> app.parse(new String[] {"dont-read.exe"}));
    }
}
