/**
 * tests.java - Tests for methods in SyncSubLib.java
 *
 * @author Matt Mokary ( mxm6060@rit.edu )
 */

/**
 * Tests for methods in SyncSubLib.java
 */
class Tests extends SyncSubLib {

    // writing my own testing function because JUnit outside of an IDE is hard 
    private static void assertTrue( boolean ex ) {
        if ( ex ) { System.out.print( "." ); }
        else { System.out.print( "X" ); }
    }

// ---------------------------------------------------------------------------

    private static void test_updateLine1() {
        String[] s = { "Some Text" };
        assertTrue( updateLine(s).equals( "Some Text" ) );
    }

    private static void test_updateLine2() {
        setData( PLUS, 0, 0, 1, 0 );
        String[] s = { "00:00:00,000", "00:00:00,000" };
        assertTrue( updateLine(s).equals( "00:00:01,000 --> 00:00:01,000" ) );
    }

    private static void test_updateLine3() {
        setData( MINUS, 0, 0, 1, 0 );
        String[] s = { "00:00:00,000", "00:00:00,000" };
        assertTrue( updateLine(s).equals( "00:00:00,000 --> 00:00:00,000" ) );
    }

    private static void test_updateLine4() {
        setData( MINUS, 0, 0, 0, 500 );
        String[] s = { "00:00:01,000", "00:00:01,000" };
        assertTrue( updateLine(s).equals( "00:00:00,500 --> 00:00:00,500" ) );
    }

    private static void test_updateLine5() {
        setData( PLUS, 0, 0, 1, 0 );
        String[] s = { "00:00:59,000", "00:00:59,000" };
        assertTrue( updateLine(s).equals( "00:01:00,000 --> 00:01:00,000" ) );
    }

    private static void test_updateLine6() {
        setData( PLUS, 0, 1, 0, 0 );
        String[] s = { "00:59:00,000", "00:59:00,000" };
        assertTrue( updateLine(s).equals( "01:00:00,000 --> 01:00:00,000" ) );
    }

    private static void test_updateLine7() {
        setData( MINUS, 0, 1, 0, 0 );
        String[] s = { "01:00:00,000", "01:00:00,000" };
        assertTrue( updateLine(s).equals( "00:59:00,000 --> 00:59:00,000" ) );
    }

    private static void test_updateLine8() {
        setData( PLUS, 0, 1, 0, 0 );
        String[] s = { "99:59:00,000", "99:59:00,000" };
        assertTrue( updateLine(s).equals( "99:00:00,000 --> 99:00:00,000" ) );
    }

    private static void test_updateLine9() {
        setData( MINUS, 0, 0, 0, 500 );
        String[] s = { "01:00:00,000", "01:00:00,000" };
        assertTrue( updateLine(s).equals( "00:59:59,500 --> 00:59:59,500" ) );
    }

    private static void test_updateLine10() {
        setData( MINUS, 0, 0, 1, 0 );
        String[] s = { "00:01:00,000", "00:00:00,000" };
        assertTrue( updateLine(s).equals( "00:00:59,000 --> 00:00:00,000" ) );
    }
// ---------------------------------------------------------------------------

    public static void main( String[] args ) {
        runTests();
    }

    private static void runTests() {
        test_updateLine1();
        test_updateLine2();
        test_updateLine3();
        test_updateLine4();
        test_updateLine5();
        test_updateLine6();
        test_updateLine7();
        test_updateLine8();
        test_updateLine9();
        test_updateLine10();
        System.out.print("\n");
    }

}
