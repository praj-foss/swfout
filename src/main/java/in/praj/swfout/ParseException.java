/**
 * This file is licensed under the MIT license.
 * See the LICENSE file in project root for details.
 */

package in.praj.swfout;

/**
 * Thrown on invalid CLI args.
 */
public class ParseException extends Exception {
    ParseException(String message) {
        super(message);
    }
}
