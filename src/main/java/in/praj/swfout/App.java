/**
 * This file is licensed under the MIT license.
 * See the LICENSE file in project root for details.
 */

package in.praj.swfout;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

public class App {
    private static final int SIGNATURE = Integer.parseUnsignedInt("563412FF", 16);
    private RandomAccessFile input;

    String parse(String[] args) {
        if (args.length == 0) {
            throw new RuntimeException("Missing input file");
        }
        return args[0];
    }

    void prepareInputFile(String path) {
        File exe = new File(path);
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

    boolean isSignatureValid(int bytes) {
        return Integer.compareUnsigned(bytes, SIGNATURE) == 0;
    }

    long readSwfSize(RandomAccessFile file) throws IOException {
        file.seek(file.length() - 8);
        if (isSignatureValid(file.readInt())) {
            return file.readInt();
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
            var exe = app.parse(args);
            app.prepareInputFile(exe);

            // TODO: Fill up
        } catch (RuntimeException e) {
            System.out.println("Error: " + e.getMessage());
        } finally {
            app.exit();
        }
    }
}
