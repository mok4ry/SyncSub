/**
 * Main source file for the SubSync program. Creates an instance of the GUI
 * object.
 *
 * @author Matt Mokary ( mxm6060@rit.edu )
 */

class SyncSub {

    /**
     * GUI object.
     */
    private static SyncSubGUI g;

    /**
     * Main function. Creates a GUI object, which handles the rest.
     *
     * @param args Command-line arguments.
     */
    public static void main( String[] args ) {
        g = new SyncSubGUI();
    }

}
