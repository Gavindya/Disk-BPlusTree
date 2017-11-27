/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ustack.diskbplustree.tree;

//import org.junit.After;
//import org.junit.AfterClass;
//import org.junit.Before;
//import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author AdminPC
 */
public class LeafNodeTest {

  private final Tree tree;

  public LeafNodeTest() {
    tree = new Tree(1024 * 4);
  }

//  @BeforeClass
//  public static void setUpClass() {
//  }
//
//  @AfterClass
//  public static void tearDownClass() {
//  }
//
//  @Before
//  public void setUp() {
//  }
//
//  @After
//  public void tearDown() {
//  }

  /**
   * Test of isFull method, of class LeafNode.
   */
  @Test
  public void testIsFull() {
    System.out.println("isFull");
    Node instance = new LeafNode(tree);
    boolean expResult = false;
    boolean result = instance.isFull();
    assertEquals(expResult, result);
  }

//  /**
//   * Test of getPreviousLeafNodeOffset method, of class LeafNode.
//   */
//  @Test
//  public void testGetPreviousLeafNodeOffset() {
//    System.out.println("getPreviousLeafNodeOffset");
//    LeafNode instance = new LeafNode(tree);
//    int expResult = 0;
//    int result = instance.getPreviousLeafNodeOffset();
//    assertEquals(expResult, result);
//    TODO review the generated test code and remove the default call to fail.
//    fail("The test case is a prototype.");
//  }
//  /**
//   * Test of getNextLeafNodeOffset method, of class LeafNode.
//   */
//  @Test
//  public void testGetNextLeafNodeOffset() {
//    System.out.println("getNextLeafNodeOffset");
//    LeafNode instance = null;
//    int expResult = 0;
//    int result = instance.getNextLeafNodeOffset();
//    assertEquals(expResult, result);
//    // TODO review the generated test code and remove the default call to fail.
//    fail("The test case is a prototype.");
//  }
  /**
   * Test of getPreviousLeafNode method, of class LeafNode.
   */
  @Test
  public void testGetPreviousLeafNode() {
    System.out.println("getPreviousLeafNode");
    LeafNode instance = new LeafNode(tree);
    Node expResult = null;
    Node result = instance.getPreviousLeafNode();
    assertEquals(expResult, result);
//    // TODO review the generated test code and remove the default call to fail.
//    fail("The test case is a prototype.");
  }

  /**
   * Test of getNextLeafNode method, of class LeafNode.
   */
  @Test
  public void testGetNextLeafNode() {
    System.out.println("getNextLeafNode");
    LeafNode instance = new LeafNode(tree);
    Node expResult = null;
    Node result = instance.getNextLeafNode();
    assertEquals(expResult, result);
//    // TODO review the generated test code and remove the default call to fail.
//    fail("The test case is a prototype.");
  }

  /**
   * Test of setPreviousLeafNode method, of class LeafNode.
   */
  @Test
  public void testSetPreviousLeafNode() {
    System.out.println("setPreviousLeafNode");
    int preNode = 0;
    LeafNode instance = new LeafNode(tree);
    instance.setPreviousLeafNode(preNode);
    assertEquals(0, instance.getPreviousLeafNodeOffset());
//    // TODO review the generated test code and remove the default call to fail.
//    fail("The test case is a prototype.");
  }

  /**
   * Test of setNextLeafNode method, of class LeafNode.
   */
  @Test
  public void testSetNextLeafNode() {
    System.out.println("setNextLeafNode");
    int nextNode = 0;
    LeafNode instance = new LeafNode(tree);
    instance.setNextLeafNode(nextNode);
    assertEquals(0, instance.getNextLeafNodeOffset());
//    // TODO review the generated test code and remove the default call to fail.
//    fail("The test case is a prototype.");
  }
//
//  /**
//   * Test of printTree method, of class LeafNode.
//   */
//  @Test
//  public void testPrintTree() {
//    System.out.println("printTree");
//    LeafNode instance = null;
//    instance.printTree();
//    // TODO review the generated test code and remove the default call to fail.
//    fail("The test case is a prototype.");
//  }
//

//  /**
//   * Test of isKeyAvailable method, of class LeafNode.
//   */
//  @Test
//  public void testIsKeyAvailable() {
//    System.out.println("isKeyAvailable");
//    Long key = 0L;
//    LeafNode instance = new LeafNode(tree);
//    NodeEntity entity = new NodeEntity(0L, tree);
//    instance.addEntity(entity);
//    boolean result = instance.isKeyAvailable(key);
//    assertTrue(result);
////    // TODO review the generated test code and remove the default call to fail.
////    fail("The test case is a prototype.");
//  }

//  /**
//   * Test of checkAvailabilityToInsert method, of class LeafNode.
//   */
//  @Test
//  public void testCheckAvailabilityToInsert() {
//    System.out.println("checkAvailabilityToInsert");
//    Long key = null;
//    Object data = null;
//    LeafNode instance = null;
//    instance.checkAvailabilityToInsert(key, data);
//    // TODO review the generated test code and remove the default call to fail.
//    fail("The test case is a prototype.");
//  }
//
//  /**
//   * Test of appendToKey method, of class LeafNode.
//   */
//  @Test
//  public void testAppendToKey() {
//    System.out.println("appendToKey");
//    int index = 0;
//    Object key = null;
//    Object dataObject = null;
//    LeafNode instance = null;
//    instance.appendToKey(index, key, dataObject);
//    // TODO review the generated test code and remove the default call to fail.
//    fail("The test case is a prototype.");
//  }
//
//  /**
//   * Test of searchNode method, of class LeafNode.
//   */
//  @Test
//  public void testSearchNode() {
//    System.out.println("searchNode");
//    Long key = null;
//    LeafNode instance = null;
//    LeafNode expResult = null;
//    LeafNode result = instance.searchNode(key);
//    assertEquals(expResult, result);
//    // TODO review the generated test code and remove the default call to fail.
//    fail("The test case is a prototype.");
//  }
//
//  /**
//   * Test of search method, of class LeafNode.
//   */
//  @Test
//  public void testSearch() {
//    System.out.println("search");
//    Long key = null;
//    LeafNode instance = null;
//    Object expResult = null;
//    Object result = instance.search(key);
//    assertEquals(expResult, result);
//    // TODO review the generated test code and remove the default call to fail.
//    fail("The test case is a prototype.");
//  }
//
//  /**
//   * Test of append method, of class LeafNode.
//   */
//  @Test
//  public void testAppend() {
//    System.out.println("append");
//    Long key = null;
//    Object dataObject = null;
//    LeafNode instance = null;
//    instance.append(key, dataObject);
//    // TODO review the generated test code and remove the default call to fail.
//    fail("The test case is a prototype.");
//  }
//
//  /**
//   * Test of insert method, of class LeafNode.
//   */
//  @Test
//  public void testInsert() {
//    System.out.println("insert");
//    Long key = null;
//    Object dataObject = null;
//    LeafNode instance = null;
//    instance.insert(key, dataObject);
//    // TODO review the generated test code and remove the default call to fail.
//    fail("The test case is a prototype.");
//  }
//
//  /**
//   * Test of split method, of class LeafNode.
//   */
//  @Test
//  public void testSplit() {
//    System.out.println("split");
//    LeafNodeEntity entity = null;
//    LeafNode instance = null;
//    instance.split(entity);
//    // TODO review the generated test code and remove the default call to fail.
//    fail("The test case is a prototype.");
//  }
//
//  /**
//   * Test of getRange method, of class LeafNode.
//   */
//  @Test
//  public void testGetRange() {
//    System.out.println("getRange");
//    Object start = null;
//    Object end = null;
//    LeafNode instance = null;
//    LeafNode expResult = null;
//    LeafNode result = instance.getRange(start, end);
//    assertEquals(expResult, result);
//    // TODO review the generated test code and remove the default call to fail.
//    fail("The test case is a prototype.");
//  }
//
//  /**
//   * Test of getGreaterThan method, of class LeafNode.
//   */
//  @Test
//  public void testGetGreaterThan() {
//    System.out.println("getGreaterThan");
//    Object start = null;
//    LeafNode instance = null;
//    LeafNode expResult = null;
//    LeafNode result = instance.getGreaterThan(start);
//    assertEquals(expResult, result);
//    // TODO review the generated test code and remove the default call to fail.
//    fail("The test case is a prototype.");
//  }
//
//  /**
//   * Test of getLessThan method, of class LeafNode.
//   */
//  @Test
//  public void testGetLessThan() {
//    System.out.println("getLessThan");
//    Object start = null;
//    LeafNode instance = null;
//    LeafNode expResult = null;
//    LeafNode result = instance.getLessThan(start);
//    assertEquals(expResult, result);
//    // TODO review the generated test code and remove the default call to fail.
//    fail("The test case is a prototype.");
//  }

}
