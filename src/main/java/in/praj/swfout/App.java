/**
 * This file is licensed under the MIT license.
 * See the LICENSE file in project root for details.
 */

package in.praj.swfout;

import java.io.FileNotFoundException;
import java.io.RandomAccessFile;

public class App {
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
}
