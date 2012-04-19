/**
 * Class representing the graphic user interface (GUI) for the SyncSub program.
 *
 * @author Matt Mokary ( mxm6060@rit.edu )
 */

import java.io.*;
import java.util.Arrays;
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
     * The currently opened file;
     */
    private File f;

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
     * Label for displaying errors.
     */
    private JLabel errorLabel;

    /**
     * Label for displaying operation status.
     */
    private JLabel statusLabel;

    /**
     * Combo box for choosing direction of time change ( plus or minus ).
     */
    private JComboBox pmComboBox;

    /**
     * Two-character text field for entering the number of hours.
     */
    private JTextField h;

    /**
     * Two-character text field for entering the number of minutes
     */
    private JTextField m;

    /**
     * Two-character text field for entering the number of seconds
     */
    private JTextField s;

    /**
     * Two-character text field for entering the number of milliseconds.
     */
    private JTextField ms;

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
        p.setLayout( new GridLayout( 3, 1 ) );
        
        // Three subpanels so that labels stack vertically and are centered.
        JPanel p1 = new JPanel();
        p1.setLayout( new FlowLayout( FlowLayout.CENTER ) );
        errorLabel = new JLabel();
        p1.add( errorLabel );
        p.add( p1 );

        JPanel p2 = new JPanel();
        p2.setLayout( new FlowLayout( FlowLayout.CENTER ) );
        statusLabel = new JLabel();
        p2.add( statusLabel );
        p.add( p2 );

        JPanel p3 = new JPanel();
        p3.setLayout( new FlowLayout( FlowLayout.CENTER ) );
        fileLabel = new JLabel();
        p3.add( fileLabel );
        p.add( p3 );

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

        h = new JTextField( "hh", 2 );
        p.add( h );
        p.add( new JLabel( " : " ) );
        m = new JTextField( "mm", 2 );
        p.add( m );
        p.add( new JLabel( " : " ) );
        s = new JTextField( "ss", 2 );
        p.add( s );
        p.add( new JLabel( " , " ) );
        ms = new JTextField( "---", 3 );
        p.add( ms );

        return p;
    }

    public void actionPerformed( ActionEvent e ) {
        statusLabel.setText( "" );
        errorLabel.setText( "" );
        if ( e.getSource() == openButton ) {
            openBtnPressed();
        } else if ( e.getSource() == syncButton ) {
            syncBtnPressed();
        } else if ( e.getSource() == pmComboBox ) {
            pmComBoxChanged( (JComboBox)e.getSource() );
        }
    }

    private void syncBtnPressed() {
        String[] t = { h.getText(), m.getText(), s.getText(), ms.getText() };
        for ( String e : t ) {
            if ( ! isInteger( e ) ) {
                displayError( "Invalid time increment: " + e );
                return;
            }
        }
        if ( ! f.getName().contains( ".srt" ) ) {
            displayError( "Not a valid file (not .srt): " + f.getName() );
            return;
        }
        SyncSubLib.update( f, plusOrMinus, Integer.parseInt(t[0]),
                           Integer.parseInt(t[1]), Integer.parseInt(t[2]),
                           Integer.parseInt(t[3]) );
        statusLabel.setText( SyncSubLib.getNumUpdates() + " start/end times" + 
            "  updated." );
    }

    private void openBtnPressed() {
        JFileChooser fc = new JFileChooser();
        int returnVal = fc.showOpenDialog( null );
        if ( returnVal == JFileChooser.APPROVE_OPTION ) {
            f = fc.getSelectedFile();
            fileLabel.setText( f.getName() );
            syncButton.setEnabled( true );
        }
    }

    private void pmComBoxChanged( JComboBox jc ) {
        plusOrMinus = ( jc.getSelectedIndex() == 0 ) ? true : false;
    }

    private boolean isInteger( String s ) {
        try {
            Integer.parseInt( s );
        } catch ( NumberFormatException e ) {
            return false;
        }
        return true;
    }

    private void displayError( String e ) {
        errorLabel.setText( e );
    }
}
