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
    public void testParseFailureOnNonexistentFile() {
        Assert.assertThrows(FileNotFoundException.class,
                () -> app.parse(new String[] {"nonexistent.exe"}));
    }
}
