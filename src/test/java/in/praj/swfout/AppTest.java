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
    public void testParsingZeroCLIArgs() {
        Assert.assertThrows(
                "Should fail on zero CLI arguments",
                RuntimeException.class,
                () -> app.parse(new String[0]));
    }

    @Test
    public void testParsingValidCLIArgs() {
        var args = new String[] {"game.exe"};
        Assert.assertEquals("game.exe", app.parse(args));
    }

    @Test
    public void testMissingInputFile() {
        Assert.assertThrows(
                "Should fail when input file is missing",
                RuntimeException.class,
                () -> app.prepareInputFile("missing.exe"));
    }

    @Test
    public void testFileTooSmall() throws IOException {
        var small = temp.newFile();
        try (var file = new RandomAccessFile(small, "rw")) {
            file.writeLong(Long.MAX_VALUE);
        }

        Assert.assertThrows(
                "Should fail when input file is not greater than 8 bytes",
                RuntimeException.class,
                () -> app.prepareInputFile(small.getPath()));
    }

    @Test
    public void testValidSignature() {
        var valid = Integer.parseUnsignedInt("563412FF", 16);
        Assert.assertTrue(app.isSignatureValid(valid));
    }

    @Test
    public void testReadingSwfSize() throws IOException {
        var exe = temp.newFile();
        var size = "2B920500";
        var sizeLong = Long.parseLong(size, 16);

        try (var file = new RandomAccessFile(exe, "rw")) {
            file.writeInt(0);
            file.writeLong(Long.parseUnsignedLong("563412FF" + size, 16));

            Assert.assertEquals(0, Long.compareUnsigned(sizeLong, app.readSwfSize(file)));
            Assert.assertEquals(
                    "File pointer should reach EOF after reading SWF size",
                    file.length(),
                    file.getFilePointer());
        }
    }
}
