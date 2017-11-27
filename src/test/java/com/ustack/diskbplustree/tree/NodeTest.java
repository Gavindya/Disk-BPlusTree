/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ustack.diskbplustree.tree;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author AdminPC
 */
public class NodeTest {

  private Tree tree;

  public NodeTest() {
    tree = new Tree(1024 * 4);
  }

//  @BeforeClass
//  public static void setUpClass() {
////
//  }
//
//  @AfterClass
//  public static void tearDownClass() {
//    //
//  }
//
//  @Before
//  public void setUp() {
//    //
//  }
//
//  @After
//  public void tearDown() {
//  }

  /**
   * Test of setTree method, of class Node.
   */
  @Test
  public void testSetTree() {
    System.out.println("setTree");
    Node instance = new LeafNode(tree);
    instance.setTree(tree);
    assertEquals(tree, instance.tree);
    // TODO review the generated test code and remove the default call to fail.
//    fail("The test case is a prototype.");
  }

  /**
   * Test of increaseVisitCount method, of class Node.
   */
  @Test
  public void testIncreaseVisitCount() {
    System.out.println("increaseVisitCount");
    LeafNode instance = new LeafNode(tree);
    instance.increaseVisitCount();
    assertEquals(1, instance.getVisitCount());
  }

  /**
   * Test of getPosition method, of class Node.
   */
  @Test
  public void testGetPosition() {
    System.out.println("getPosition");
    Node instance = new LeafNode(tree);
    int result = instance.getPosition();
    assertTrue(result >= 0);
  }

  /**
   * Test of getVisitCount method, of class Node.
   */
  @Test
  public void testGetVisitCount() {
    System.out.println("getVisitCount");
    Node instance = new LeafNode(tree);
    int expResult = 0;
    int result = instance.getVisitCount();
    assertEquals(expResult, result);
  }

  /**
   * Test of serialize method, of class Node.
   */
  @Test
  public void testSerialize() {
    System.out.println("serialize");
    Node instance =  new LeafNode(tree);
    boolean serialized = instance.serialize(tree);
//    System.out.println(serialized);
    assertFalse(serialized);
    // TODO review the generated test code and remove the default call to fail.
//    fail("The test case is a prototype.");
  }

  /**
   * Test of isFull method, of class Node.
   */
  @Test
  public void testIsFull() {
    System.out.println("isFull");
    Node instance = new LeafNode(tree);
    boolean expResult = false;
    boolean result = instance.isFull();
    assertEquals(expResult, result);
//    // TODO review the generated test code and remove the default call to fail.
//    fail("The test case is a prototype.");
  }

  /**
   * Test of getEntityCount method, of class Node.
   */
  @Test
  public void testGetEntityCount() {
    System.out.println("getEntityCount");
    Node instance = new LeafNode(tree);
    int expResult = 0;
    int result = instance.getEntityCount();
    assertEquals(expResult, result);
//    // TODO review the generated test code and remove the default call to fail.
//    fail("The test case is a prototype.");
  }

//  /**
//   * Test of sortEntities method, of class Node.
//   */
//  @Test
//  public void testSortEntities() {
//    System.out.println("sortEntities");
//    Node instance = null;
//    instance.sortEntities();
//    // TODO review the generated test code and remove the default call to fail.
//    fail("The test case is a prototype.");
//  }

  /**
   * Test of getEntities method, of class Node.
   */
  @Test
  public void testGetEntities() {
    System.out.println("getEntities");
    Node instance = new LeafNode(tree);
    NodeEntity[] expResult = new NodeEntity[ConstantsOfTree.DEGREE-1];
    NodeEntity[] result = instance.getEntities();
    assertEquals(expResult.length, result.length);
//    TODO review the generated test code and remove the default call to fail.
//    fail("The test case is a prototype.");
  }

  /**
   * Test of addEntity method, of class Node.
   */
  @Test
  public void testAddEntity() {
    System.out.println("addEntity");
    NodeEntity entity = new LeafNodeEntity(1L, tree);
    Node instance = new LeafNode(tree);
    boolean expResult = true;
    boolean result = instance.addEntity(entity);
    assertEquals(expResult, result);
//    // TODO review the generated test code and remove the default call to fail.
//    fail("The test case is a prototype.");
  }

  /**
   * Test of removeEntity method, of class Node.
   */
  @Test
  public void testRemoveEntity() {
    System.out.println("removeEntity");
    int index = 0;
    NodeEntity element = new NodeEntity(1L, tree);
    Node instance = new LeafNode(tree);
    instance.addEntity(element);
    NodeEntity expResult = element;
    NodeEntity result = instance.removeEntity(index);
    assertEquals(expResult, result);
//    // TODO review the generated test code and remove the default call to fail.
//    fail("The test case is a prototype.");
  }
//
//  /**
//   * Test of clearEntities method, of class Node.
//   */
//  @Test
//  public void testClearEntities() {
//    System.out.println("clearEntities");
//    Node instance = null;
//    instance.clearEntities();
//    // TODO review the generated test code and remove the default call to fail.
//    fail("The test case is a prototype.");
//  }
//
//  /**
//   * Test of addAllEntities method, of class Node.
//   */
//  @Test
//  public void testAddAllEntities() {
//    System.out.println("addAllEntities");
//    NodeEntity[] entitiesArray = null;
//    Node instance = null;
//    instance.addAllEntities(entitiesArray);
//    // TODO review the generated test code and remove the default call to fail.
//    fail("The test case is a prototype.");
//  }
//
//  /**
//   * Test of getRange method, of class Node.
//   */
//  @Test
//  public void testGetRange() {
//    System.out.println("getRange");
//    Object start = null;
//    Object end = null;
//    Node instance = null;
//    LeafNode expResult = null;
//    LeafNode result = instance.getRange(start, end);
//    assertEquals(expResult, result);
//    // TODO review the generated test code and remove the default call to fail.
//    fail("The test case is a prototype.");
//  }
//
//  /**
//   * Test of getGreaterThan method, of class Node.
//   */
//  @Test
//  public void testGetGreaterThan() {
//    System.out.println("getGreaterThan");
//    Object start = null;
//    Node instance = null;
//    LeafNode expResult = null;
//    LeafNode result = instance.getGreaterThan(start);
//    assertEquals(expResult, result);
//    // TODO review the generated test code and remove the default call to fail.
//    fail("The test case is a prototype.");
//  }
//
//  /**
//   * Test of getLessThan method, of class Node.
//   */
//  @Test
//  public void testGetLessThan() {
//    System.out.println("getLessThan");
//    Object start = null;
//    Node instance = null;
//    LeafNode expResult = null;
//    LeafNode result = instance.getLessThan(start);
//    assertEquals(expResult, result);
//    // TODO review the generated test code and remove the default call to fail.
//    fail("The test case is a prototype.");
//  }
//
//  /**
//   * Test of printTree method, of class Node.
//   */
//  @Test
//  public void testPrintTree() {
//    System.out.println("printTree");
//    Node instance = null;
//    instance.printTree();
//    // TODO review the generated test code and remove the default call to fail.
//    fail("The test case is a prototype.");
//  }
//
//  /**
//   * Test of checkAvailabilityToInsert method, of class Node.
//   */
//  @Test
//  public void testCheckAvailabilityToInsert() {
//    System.out.println("checkAvailabilityToInsert");
//    Long key = null;
//    Object data = null;
//    Node instance = null;
//    instance.checkAvailabilityToInsert(key, data);
//    // TODO review the generated test code and remove the default call to fail.
//    fail("The test case is a prototype.");
//  }
//
//  /**
//   * Test of isKeyAvailable method, of class Node.
//   */
//  @Test
//  public void testIsKeyAvailable() {
//    System.out.println("isKeyAvailable");
//    Long key = null;
//    Node instance = null;
//    boolean expResult = false;
//    boolean result = instance.isKeyAvailable(key);
//    assertEquals(expResult, result);
//    // TODO review the generated test code and remove the default call to fail.
//    fail("The test case is a prototype.");
//  }
//
//  /**
//   * Test of searchNode method, of class Node.
//   */
//  @Test
//  public void testSearchNode() {
//    System.out.println("searchNode");
//    Long key = null;
//    Node instance = null;
//    LeafNode expResult = null;
//    LeafNode result = instance.searchNode(key);
//    assertEquals(expResult, result);
//    // TODO review the generated test code and remove the default call to fail.
//    fail("The test case is a prototype.");
//  }
//
//  /**
//   * Test of search method, of class Node.
//   */
//  @Test
//  public void testSearch() {
//    System.out.println("search");
//    Long key = null;
//    Node instance = null;
//    Object expResult = null;
//    Object result = instance.search(key);
//    assertEquals(expResult, result);
//    // TODO review the generated test code and remove the default call to fail.
//    fail("The test case is a prototype.");
//  }
//
//  /**
//   * Test of append method, of class Node.
//   */
//  @Test
//  public void testAppend() {
//    System.out.println("append");
//    Long key = null;
//    Object dataObject = null;
//    Node instance = null;
//    instance.append(key, dataObject);
//    // TODO review the generated test code and remove the default call to fail.
//    fail("The test case is a prototype.");
//  }
//
//  /**
//   * Test of insert method, of class Node.
//   */
//  @Test
//  public void testInsert() {
//    System.out.println("insert");
//    Long key = null;
//    Object dataObject = null;
//    Node instance = null;
//    instance.insert(key, dataObject);
//    // TODO review the generated test code and remove the default call to fail.
//    fail("The test case is a prototype.");
//  }
//
//  /**
//   * Test of isLeafNode method, of class Node.
//   */
//  @Test
//  public void testIsLeafNode() {
//    System.out.println("isLeafNode");
//    Node instance = null;
//    boolean expResult = false;
//    boolean result = instance.isLeafNode();
//    assertEquals(expResult, result);
//    // TODO review the generated test code and remove the default call to fail.
//    fail("The test case is a prototype.");
//  }
//
//  /**
//   * Test of setParent method, of class Node.
//   */
//  @Test
//  public void testSetParent() {
//    System.out.println("setParent");
//    int parentOffset = 0;
//    Node instance = null;
//    instance.setParent(parentOffset);
//    // TODO review the generated test code and remove the default call to fail.
//    fail("The test case is a prototype.");
//  }
//
//  /**
//   * Test of getParent method, of class Node.
//   */
//  @Test
//  public void testGetParent() {
//    System.out.println("getParent");
//    Node instance = null;
//    int expResult = 0;
//    int result = instance.getParent();
//    assertEquals(expResult, result);
//    // TODO review the generated test code and remove the default call to fail.
//    fail("The test case is a prototype.");
//  }
//
//  /**
//   * Test of toString method, of class Node.
//   */
//  @Test
//  public void testToString() {
//    System.out.println("toString");
//    Node instance = null;
//    String expResult = "";
//    String result = instance.toString();
//    assertEquals(expResult, result);
//    // TODO review the generated test code and remove the default call to fail.
//    fail("The test case is a prototype.");
//  }
//
//  public class NodeImpl extends Node {
//
//    public NodeImpl() {
//      super(null);
//    }
//
//    public LeafNode getRange(Object start, Object end) {
//      return null;
//    }
//
//    public LeafNode getGreaterThan(Object start) {
//      return null;
//    }
//
//    public LeafNode getLessThan(Object start) {
//      return null;
//    }
//
//    public void printTree() {
//    }
//
//    public void checkAvailabilityToInsert(Long key, Object data) {
//    }
//
//    public boolean isKeyAvailable(Long key) {
//      return false;
//    }
//
//    public LeafNode searchNode(Long key) {
//      return null;
//    }
//
//    public Object search(Long key) {
//      return null;
//    }
//
//    public void append(Long key, Object dataObject) {
//    }
//
//    public void insert(Long key, Object dataObject) {
//    }
//  }
}
