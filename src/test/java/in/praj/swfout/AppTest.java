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
    private static final String SIGNATURE = "563412FA";

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
        var options = app.parse(args);
        Assert.assertEquals("game.exe", options.getInputPath());
    }

    @Test
    public void testMissingInputFile() {
        Assert.assertThrows(
                "Should fail when input file is missing",
                RuntimeException.class,
                () -> app.prepareInputFile(new Options("missing.exe", null)));
        Assert.assertNull(app.getInputFile());
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
                () -> app.prepareInputFile(new Options(small.getPath(), null)));
        Assert.assertNull(app.getInputFile());
    }

    @Test
    public void testFileLargeEnough() throws IOException {
        var okay = temp.newFile();
        try (var file = new RandomAccessFile(okay, "rw")) {
            file.writeLong(Long.MAX_VALUE);
            file.writeInt(Integer.MAX_VALUE);
        }

        app.prepareInputFile(new Options(okay.getPath(), null));
        Assert.assertNotNull(app.getInputFile());
    }

    @Test
    public void testValidSignature() {
        var valid = Integer.parseUnsignedInt(SIGNATURE, 16);
        Assert.assertTrue(app.isSignatureValid(valid));
    }

    @Test
    public void testReadingSwfSize() throws IOException {
        var exe = temp.newFile();
        var size   = "2B920500";
        var sizeLE = "0005922B";
        var sizeLong = Long.parseLong(size, 16);

        try (var file = new RandomAccessFile(exe, "rw")) {
            file.writeInt(0);
            file.writeLong(Long.parseUnsignedLong(SIGNATURE + sizeLE, 16));

            Assert.assertEquals(0, Long.compareUnsigned(sizeLong, app.readSwfSize(file)));
        }
    }

    @Test
    public void testExtractingSwf() throws IOException {
        var exe = temp.newFile();
        var swf = temp.newFile();
        var opts = new Options(exe.getPath(), swf.getPath());
        var target = "EMBEDDED SWF";
        swf.delete();

        try (var input = new RandomAccessFile(exe, "rw")) {
            input.writeBytes("PROJECTOR");
            input.writeBytes(target);
            input.writeInt(Integer.parseUnsignedInt(SIGNATURE, 16));
            input.writeInt(Integer.reverseBytes(target.length()));
        }

        app.prepareInputFile(opts);
        app.extractSwf(opts);
        app.exit();

        try (var output = new RandomAccessFile(swf, "r")) {
            Assert.assertEquals(target, output.readLine());
        }
    }

    @Test
    public void testAppExit() throws IOException {
        var file = new RandomAccessFile(temp.newFile(), "r");
        app.setInputFile(file);
        app.exit();
        Assert.assertFalse(file.getChannel().isOpen());
    }
}
