/**
 * This file is licensed under the MIT license.
 * See the LICENSE file in project root for details.
 */

package in.praj.swfout;

import java.io.FileNotFoundException;
import java.io.RandomAccessFile;

public class App {
    RandomAccessFile parse(String[] args) throws FileNotFoundException, SecurityException {
        return new RandomAccessFile(args[0], "r");
    }
}
