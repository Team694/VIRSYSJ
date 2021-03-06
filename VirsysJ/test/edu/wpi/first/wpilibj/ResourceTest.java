/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.wpi.first.wpilibj;

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
public class ResourceTest {

    public ResourceTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of allocate method, of class Resource.
     */
    @Test
    public void testSuccessfulAllocate() {
        System.out.println("allocate");
        int index = 0;
        Resource instance = new Resource(5);
        int expResult = 0;
        int result = instance.allocate(index);
        assertEquals(expResult, result);
    }

    /**
     * Test of allocate method, of class Resource.
     */
    @Test(expected = Resource.CheckedAllocationException.class)
    public void testFailAllocate() {
        System.out.println("fail allocate");
        Resource instance = new Resource(5);
        int result = instance.allocate(0);
        int result2 = instance.allocate(0); // shouldn't be allowed, since 0
                                            // was already allocated
    }

    /**
     * Test of allocate method, of class Resource.
     */
    @Test(expected = Resource.CheckedAllocationException.class)
    public void testFailAllocateAboveBounds() {
        System.out.println("fail allocate");
        Resource instance = new Resource(5);
        instance.allocate(5); // above bound
    }

     /**
     * Test of allocate method, of class Resource.
     */
    @Test(expected = Resource.CheckedAllocationException.class)
    public void testFailAllocateBelowBounds() {
        System.out.println("fail allocate");
        Resource instance = new Resource(5);
        instance.allocate(-1); // below bound
    }

    /**
     * Test of free method, of class Resource.
     */
    @Test
    public void testFree() {
        System.out.println("free");
        int index = 0;
        Resource instance = new Resource(5);
        instance.allocate(index);
        instance.free(index); // should allow `index' to be allocated again
        instance.allocate(index);
    }

    /**
     * Test of free method, of class Resource.
     */
    @Test(expected = Resource.AllocationException.class)
    public void testFailFree() {
        System.out.println("free");
        int index = 0;
        Resource instance = new Resource(5);
        instance.free(index); // should fail; index not allocated
    }

}