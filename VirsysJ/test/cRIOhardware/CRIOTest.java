/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package cRIOhardware;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author blake
 */
public class CRIOTest {

    public CRIOTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
        DigitalSidecar.register[0] = true;
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
        DigitalSidecar.register[0] = false;
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    /*
     * Test the ability to write a digital output
     */
    @Test
    public void testWriteDigital() {
        assertEquals(DigitalSidecar.register[0], true);
    }
}