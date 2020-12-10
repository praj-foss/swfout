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

    RandomAccessFile parse(String[] args) throws ParseException {
        if (args.length == 0) {
            throw new ParseException("Missing filename");
        }

        try {
            return new RandomAccessFile(args[0], "r");
        } catch (FileNotFoundException e) {
            throw new ParseException("File does not exist");
        } catch (SecurityException e) {
            throw new ParseException("Permission denied for reading file");
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
