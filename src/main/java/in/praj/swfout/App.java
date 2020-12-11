/**
 * This file is licensed under the MIT license.
 * See the LICENSE file in project root for details.
 */

package in.praj.swfout;

import java.io.FileNotFoundException;
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
        try {
            input = new RandomAccessFile(path, "r");
        } catch (FileNotFoundException e) {
            throw new RuntimeException("Input file is missing");
        }
    }

    boolean isFileLargeEnough(RandomAccessFile file) throws IOException {
        return file.length() > 8;
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
}
