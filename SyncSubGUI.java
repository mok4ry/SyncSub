/**
 * Class representing the graphic user interface (GUI) for the SyncSub program.
 *
 * @author Matt Mokary ( mxm6060@rit.edu )
 */

import java.io.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.filechooser.*;

class SyncSubGUI implements ActionListener {
    
    /**
     * Version number
     */
    private final String version = "v1.0.0";

    /**
     * The "open" button.
     */
    private JButton openButton;

    /**
     * The "sync" button.
     */
    private JButton syncButton;

    /**
     * Label for displaying the name of the file currently being worked on.
     */
    private JLabel fileLabel;

    /**
     * Combo box for choosing direction of time change ( plus or minus ).
     */
    private JComboBox pmComboBox;

    /**
     * Two-character text field for entering the number of hours.
     */
    private JTextField hours;

    /**
     * Two-character text field for entering the number of minutes.
     */
    private JTextField minutes;

    /**
     * Two-character text field for entering the number of seconds.
     */
    private JTextField seconds;

    /**
     * Two-character text field for entering the number of milliseconds.
     */
    private JTextField mseconds;

    /**
     * Boolean for checking whether time change is positive or negative (plus
     * or minus).
     * true - plus
     * false - minus
     */
    private boolean plusOrMinus = true;

    /**
     * Constructor. Builds and displays the GUI.
     */
    public SyncSubGUI() {
        JFrame f = new JFrame( "SyncSub " + version );
        f.setSize( 300, 170 );
        f.setLayout( new BorderLayout() );

        f.add( buildTopComponents(), BorderLayout.NORTH );
        f.add( buildBottomComponents(), BorderLayout.SOUTH );
        f.add( buildCenterComponents(), BorderLayout.CENTER );

        f.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        f.setVisible( true );
    }

    private JPanel buildTopComponents() {
        JPanel p = new JPanel();
        p.setLayout( new FlowLayout( FlowLayout.CENTER ) );

        openButton = new JButton( "Open" );
        openButton.addActionListener( this );
        p.add( openButton );

        syncButton = new JButton( "Sync" );
        syncButton.addActionListener( this );
        syncButton.setEnabled( false );
        p.add( syncButton );

        return p;
    }

    private JPanel buildBottomComponents() {
        JPanel p = new JPanel();
        p.setLayout( new FlowLayout( FlowLayout.CENTER ) );

        fileLabel = new JLabel();
        p.add( fileLabel );

        return p;
    }

    private JPanel buildCenterComponents() {
        JPanel p = new JPanel();
        p.setLayout( new FlowLayout( FlowLayout.CENTER ) );

        String[] options = { "+", "-" };
        pmComboBox = new JComboBox( options );
        pmComboBox.setSelectedIndex( 0 );
        pmComboBox.addActionListener( this );
        p.add( pmComboBox );

        p.add( new JLabel( "    " ) ); // spacer

        hours = new JTextField( 2 );
        p.add( hours );
        p.add( new JLabel( " : " ) );
        minutes = new JTextField( 2 );
        p.add( minutes );
        p.add( new JLabel( " : " ) );
        seconds = new JTextField( 2 );
        p.add( seconds );
        p.add( new JLabel( " , " ) );
        mseconds = new JTextField( 3 );
        p.add( mseconds );

        return p;
    }

    public void actionPerformed( ActionEvent e ) {
        if ( e.getSource() == openButton ) {
            JFileChooser fc = new JFileChooser();
            int returnVal = fc.showOpenDialog( null );
            if ( returnVal == JFileChooser.APPROVE_OPTION ) {
                File f = fc.getSelectedFile();
                fileLabel.setText( f.getName() );
                syncButton.setEnabled( true );
            }
        } else if ( e.getSource() == syncButton ) {
            System.out.println( "Syncing" );
        } else if ( e.getSource() == pmComboBox ) {
            JComboBox jc = (JComboBox)e.getSource();
            plusOrMinus = ( jc.getSelectedIndex() == 0 )
            ? true : false;
        }
    }

}
