/**
 * This file is licensed under the MIT license.
 * See the LICENSE file in project root for details.
 */

package in.praj.swfout;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

public class App {
    private static final int SIGNATURE = Integer.parseUnsignedInt("563412FA", 16);
    private RandomAccessFile input;

    Options parse(String[] args) {
        if (args.length == 0) {
            throw new RuntimeException("Missing input file");
        }
        return new Options(args[0], args.length > 1 ? args[1] : "out.swf");
    }

    void prepareInputFile(Options options) {
        File exe = new File(options.getInputPath());
        if (! exe.exists()) {
            throw new RuntimeException("Input file is missing");
        } else if (exe.length() <= 8) {
            throw new RuntimeException("Invalid file format");
        }

        try {
            setInputFile(new RandomAccessFile(exe, "r"));
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    void extractSwf(Options options) {
        try (var output = new RandomAccessFile(options.getOutputPath(), "rw")) {
            var remaining = readSwfSize(input);
            var position = input.length() - remaining - 8;
            var inChan = input.getChannel();
            var outChan = output.getChannel();

            while (remaining > 0) {
                long sent = inChan.transferTo(position, remaining, outChan);
                remaining -= sent;
                position += sent;
            }
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    boolean isSignatureValid(int bytes) {
        return Integer.compareUnsigned(bytes, SIGNATURE) == 0;
    }

    long readSwfSize(RandomAccessFile file) throws IOException {
        file.seek(file.length() - 8);
        if (isSignatureValid(file.readInt())) {
            var bytes = file.readInt();
            file.seek(0);
            return Integer.reverseBytes(bytes);
        }
        throw new RuntimeException("File signature is invalid");
    }

    void exit() {
        try {
            if (input != null)
                input.close();
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public RandomAccessFile getInputFile() {
        return input;
    }

    public void setInputFile(RandomAccessFile input) {
        this.input = input;
    }

    public static void main(String[] args) {
        var app = new App();
        try {
            var opts = app.parse(args);
            app.prepareInputFile(opts);
            app.extractSwf(opts);
        } catch (RuntimeException e) {
            System.out.println("Error: " + e.getMessage());
        } finally {
            app.exit();
        }
    }
}
