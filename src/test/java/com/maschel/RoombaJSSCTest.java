package com.maschel;

/**
 * Created by geoffrey on 12/10/2016.
 */

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Unit test for RoombaJSSCTest.
 */
public class RoombaJSSCTest extends TestCase
{
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public RoombaJSSCTest( String testName )
    {
        super( testName );
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
        return new TestSuite( RoombaJSSC.class );
    }

    /**
     * Rigourous Test :-)
     */
    public void testRoombaJSSC()
    {
        assertTrue( true );
    }
}
