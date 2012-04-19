/**
 * SyncSubLib.java - Library containing methods for updating subtitle timing.
 *
 * @author Matt Mokary ( mxm6060@rit.edu )
 */

import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;

class SyncSubLib {
    /**
     * Static variables, changed every time update is called.
     * There is no constructor or non-static methods, partly to hold true to
     * the utility/library feel of it, but also because editing more than one
     * file in a session would require creating multiple instances of this
     * class if the methods were non-static.
     */
    private static File f;
    private static boolean o;
    private static int h;
    private static int m;
    private static int s;
    private static int ms;
    private static int numUpdates;

    /**
     * The only method that needs to be called in order to update the times in
     * a .srt file. Gets the ball rolling.
     *
     * @param fi File object (.srt file) to read in.
     * @param op Operation to perform (true = plus, false = minus).
     * @param ho Number of hours to shift.
     * @param mi Number of minutes to shift.
     * @param se Number of seconds to shift.
     * @param mse Number of milliseconds to shift.
     */
    public static void update (File fi, boolean op, int ho, int mi, int se,
                               int mse) {
        f = fi;
        o = op;
        h = ho;
        m = mi;
        s = se;
        ms = mse;
        numUpdates = 0;
        
        traverseFile();
    }

    /**
     * Moves through the file reading individual lines. Calls the updateLine
     * method on each.
     */
    private static void traverseFile() {
        Scanner in;

        try {
            in = new Scanner( f );
        } catch ( FileNotFoundException e ) {
            System.err.println( "File not found! : " + f.getName() );
            return;
        }

        while ( in.hasNext() ) {
            updateLine( in.nextLine().split(" --> ") );
        }
    }
    
    /**
     * Take a line of the current "in" file, alter if necessary, and write to
     * the destination file (current file + "(SYNCED)")
     *
     * @param l Array representing the line split around " --> "
     */
    private static void updateLine( String[] l ) {
        if ( l.length == 2 ) {
            String newLine = "";
            numUpdates += 1;
            newLine += sync( l[0] ) + " --> " + sync( l[1] );
            System.out.println( newLine );
        } else {
            System.out.println( l[0] );
        }
    }

    /**
     * Takes in the 'old' time (format: hh:mm:ss,--- where '-' is ms) and
     * modifies it to reflect the time change held in plusOrMinus, h, m, s, and
     * ms private variables.
     *
     * @param time String of format hh:mm:ss,--- where '-' is ms
     * @return Similarly-formatted String modified to reflect designated change
     */
    private static String sync( String time ) {
        return time;
    }

    /**
     * Return the number of lines that were updated in the file.
     *
     * @return String representing the number of lines that were updated in the
     * file.
     */
    public static String getNumUpdates() {
        return Integer.toString( numUpdates );
    }

}
