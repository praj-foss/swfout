/**
 * This file is licensed under the MIT license.
 * See the LICENSE file in project root for details.
 */

package in.praj.swfout;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.IOException;
import java.io.RandomAccessFile;

public class AppTest {
    private App app;

    @Before
    public void resetApp() {
        app = new App();
    }

    @Rule
    public TemporaryFolder temp = new TemporaryFolder();

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
    public void testFileTooSmall() throws IOException {
        try (var empty = new RandomAccessFile(temp.newFile(), "r")) {
            Assert.assertFalse(
                    "Should fail when input file is not greater than 8 bytes",
                    app.isFileLargeEnough(empty));
        }
    }

    @Test
    public void testValidSignature() {
        var valid = Integer.parseUnsignedInt("563412FF", 16);
        Assert.assertTrue(app.isSignatureValid(valid));
    }
}
