/**
 * SyncSubLib.java - Library containing methods for updating subtitle timing.
 *
 * @author Matt Mokary ( mxm6060@rit.edu )
 */

import java.util.Scanner;
import java.io.*;
// import java.io.FileNotFoundException;

class SyncSubLib {
    /*
     * Static variables, changed every time update is called.
     * There is no constructor or non-static methods, partly to hold true to
     * the utility/library feel of it, but also because editing more than one
     * file in a session would require creating multiple instances of this
     * class if the methods were non-static.
     */

    /**
     * File with which to work.
     */
    private static File f;

    /**
     * The operation to perform on time data.
     * true: addition, false: subtraction
     * SEE: static final variables PLUS, MINUS
     */
    private static boolean o;

    /**
     * Magnitude of increments for hours.
     */
    private static int h;

    /**
     * Magnitude of increments for minutes.
     */
    private static int m;

    /**
     * Magnitude of increments for seconds.
     */
    private static int s;

    /**
     * Magnitude of increments for milliseconds.
     */
    private static int ms;

    /**
     * The number of subtitle lines whose times were updates.
     */
    private static int numUpdates;

    /**
     * Coder-friendly representation of the addition operation, which is
     * internally represented as the boolean 'true'.
     */
    public static final boolean PLUS = true;

    /**
     * Coder-friendly representation of the subtraction operation, which is
     * internally represented as the boolean 'false'.
     */
    public static final boolean MINUS = false;

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
        String outFile;
        BufferedWriter out;

        try {
            in = new Scanner( f );
            
            outFile = f.getCanonicalPath();
            outFile = outFile.substring( 0, outFile.length() - 4 ) + 
                "(SYNCED).srt";
            FileWriter fstream = new FileWriter( outFile );
            out = new BufferedWriter( fstream );
             
            while ( in.hasNext() ) {
                out.write( updateLine( in.nextLine().split(" --> ") ) + "\n" );
            }
            out.close();
        } catch ( IOException e ) {
            System.err.println( "Problem opening or writing to file." );
            return;
        }
    }
    
    /**
     * Take a line of the current "in" file, alter if necessary, and write to
     * the destination file (current file + "(SYNCED)")
     *
     * @param l Array representing the line split around " --> "
     * @return The updated line to be written to a new file
     */
    protected static String updateLine( String[] l ) {
        if ( l.length == 2 ) {
            String newLine = "";
            numUpdates += 1;
            newLine += sync( l[0] ) + " --> " + sync( l[1] );
            return newLine;
        } else {
            return l[0];
        }
    }

    /**
     * Takes in the 'old' time (format: hh:mm:ss,--- where '-' is ms) and sends
     * it to the correct method for modification so that it reflects the time
     * change held in plusOrMinus, h, m, s, and ms private variables.
     *
     * @param time String of format hh:mm:ss,--- where '-' is ms
     * @return Similarly-formatted String modified to reflect designated change
     */
    private static String sync( String time ) {
        String[] sTime = time.split(":|,");
        String newTime = o ? plusOp( sTime ) : minusOp( sTime );
        return newTime;
    }

    /**
     * Adds the current time increments to the given time, and returns a new,
     * similarly-formatted string.
     *
     * @param time String of format hh:mm:ss,--- where '-' is ms
     * @return Similarly-formatted String with time increments added.
     */
    private static String plusOp( String[] time ) {
        int MS = Integer.parseInt( time[3] );
        int S = Integer.parseInt( time[2] );
        int M = Integer.parseInt( time[1] );
        int H = Integer.parseInt( time[0] );
        int carry = 0;

        // do math
        if ( MS + ms > 999 ) {
            MS = MS + ms - 1000;
            carry = 1;
        } else {
            MS += ms;
            carry = 0;
        }
        if ( S + s + carry > 59 ) {
            S = S + s + carry - 60;
            carry = 1;
        } else {
            S = S + s + carry;
            carry = 0;
        }
        if ( M + m + carry > 59 ) {
            M = M + m + carry - 60;
            carry = 1;
        } else {
            M = M + m + carry;
            carry = 0;
        }
        H = H + h + carry;
        
        if ( H > 99 ) { H = 99; }

        String[] b = bufferNums( H, M, S, MS );
        return b[0] + ":" + b[1] + ":" + b[2] + "," + b[3];
    }

    /**
     * Subtracts the current time increments from the given time and returns a
     * new, similarly-formatted String with the time increments subtracted.
     *
     * @param time String of format hh:mm:ss,-- where '-' is ms
     * @return Similarly-formatted String with time increments subtracted.
     */
    private static String minusOp( String[] time ) {
        int MS = Integer.parseInt( time[3] );
        int S = Integer.parseInt( time[2] );
        int M = Integer.parseInt( time[1] );
        int H = Integer.parseInt( time[0] );
        int carry = 0;

        // do math
        if ( MS - ms < 0 ) {
            MS = ( S == 0 && M == 0 && H == 0 ) ? 0 : MS - ms + 1000;
            carry = 1;
        } else { 
            MS -= ms;
            carry = 0;
        }
        if ( S - s - carry < 0 ) {
            S = ( M == 0 && H == 0 ) ? 0 : S - s - carry + 60;
            carry = 1;
        } else {
            S = S - s - carry;
            carry = 0;
        }
        if ( M - m - carry < 0 ) {
            M = ( H == 0 ) ? 0 : M - m - carry + 60;
            carry = 1;
        } else {
            M = M - m - carry;
            carry = 0;
        }
        H = H - h - carry;
        
        if ( MS < 0 ) { MS = 0; }
        if ( S < 0 ) { S = 0; }
        if ( M < 0 ) { M = 0; }
        if ( H < 0 ) { H = 0; }
                
        String[] b = bufferNums( H, M, S, MS );
        return b[0] + ":" + b[1] + ":" + b[2] + "," + b[3];
    }

    /**
     * Buffers the numbers with zeroes if they aren't two (or three, in the
     * case of milliseconds) characters long already.
     *
     * @param H hours
     * @param M minutes
     * @param S seconds
     * @param MS milliseconds
     * @return Array of values buffered with zeroes.
     */
    private static String[] bufferNums ( int H, int M, int S, int MS ) {
        String newH = H < 10 ? 
            "0" + Integer.toString( H ) : Integer.toString( H );
        String newM = M < 10 ? 
            "0" + Integer.toString( M ) : Integer.toString( M );
        String newS = S < 10 ? 
            "0" + Integer.toString( S ) : Integer.toString( S );
        String newMS;
        if ( MS < 10 ) {
            newMS = "00" + Integer.toString( MS );
        } else if ( MS < 100 ) {
            newMS = "0" + Integer.toString( MS );
        } else {
            newMS = Integer.toString( MS );
        }
        String[] returnStr = { newH, newM, newS, newMS };
        return returnStr;
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


    /* ----------------------- FOR TESTING PURPOSES ----------------------- */

    /**
     * Method for manually setting time increments. Has protected access so
     * that a testing subclass can modify increments to artificial values.
     */
    protected static void setData ( boolean O, int H, int M, int S, int MS ) {
        o = O; h = H; m = M; s = S; ms = MS;
    }

}
