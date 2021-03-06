/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ustack.diskbplustree.tree;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.LongStream;
import org.github.jamm.MemoryMeter;

/**
 *
 * @author Gavindya
 */
public class MainTreeDS {

  private static Tree getTree() {
    try {
      Tree tree;
      try (FileInputStream fileIn = new FileInputStream(ConstantsOfTree.TREE_PATH); ObjectInputStream in = new ObjectInputStream(fileIn)) {
        tree = (Tree) in.readObject();
      }
      return tree;
    } catch (IOException i) {
      System.out.println(i.getMessage());
      return null;
    } catch (ClassNotFoundException c) {
      System.out.println("Tree class not found");
      return null;
    }
  }

  public static void checkNodes(Tree tree) throws NullPointerException {

    Node root = tree.getRoot();
    System.out.println(root.toString());
    Node left1 = ((InternalNodeEntity) root.getEntities()[0]).getLeftNode(tree);
    System.out.println(left1.toString());
    Node right1 = ((InternalNodeEntity) root.getEntities()[0]).getRightNode(tree);
    System.out.println(right1.toString());
//        Node rightlast1 = ((InternalNodeEntity) root.getEntities()[1]).getRightNode(tree);
//        System.out.println(rightlast1.toString());
////
    System.out.println("-----------------------");
//
    Node left2 = ((InternalNodeEntity) left1.getEntities()[0]).getLeftNode(tree);
    System.out.println(left2.toString());
    Node right2 = ((InternalNodeEntity) left1.getEntities()[0]).getRightNode(tree);
    System.out.println(right2.toString());
//
////
    System.out.println("-----------------------");
////        Node right1 = ((InternalNodeEntity) root.getEntities()[0]).getRightNode(tree);
////        System.out.println(right1.toString());
////
    Node left3 = ((InternalNodeEntity) right1.getEntities()[0]).getLeftNode(tree);
    System.out.println(left3.toString());
    Node right3 = ((InternalNodeEntity) right1.getEntities()[0]).getRightNode(tree);
    System.out.println(right3.toString());
//        System.out.println("--------------");
//
////        Node left10 = ((InternalNodeEntity) root.getEntities()[1]).getLeftNode(tree);
////        System.out.println(left10.toString());
////        Node right10 = ((InternalNodeEntity) root.getEntities()[1]).getRightNode(tree);
////        System.out.println(right10.toString());
////        Node rightlast1 = ((InternalNodeEntity) root.getEntities()[1]).getRightNode(tree);
////        System.out.println(rightlast1.toString());
////
//        System.out.println("-----------------------");
//         Node left30 = ((InternalNodeEntity) right1.getEntities()[1]).getLeftNode(tree);
//        System.out.println(left30.toString());
//        Node right30 = ((InternalNodeEntity) right1.getEntities()[1]).getRightNode(tree);
//        System.out.println(right30.toString());

//        Node left20 = ((InternalNodeEntity) right10.getEntities()[0]).getLeftNode(tree);
//        System.out.println(left20.toString());
//        Node right20 = ((InternalNodeEntity) right10.getEntities()[0]).getRightNode(tree);
//        System.out.println(right20.toString());
//
//        System.out.println("-----------------------");
//
//        Node left200 = ((InternalNodeEntity) left10.getEntities()[0]).getLeftNode(tree);
//        System.out.println(left200.toString());
//        Node right200 = ((InternalNodeEntity) left10.getEntities()[0]).getRightNode(tree);
//        System.out.println(right200.toString());
  }

  public static void checkmemory(Tree tree) {

    MemoryMeter meter = new MemoryMeter()/*.enableDebug()*/;
    System.out.println(meter.measure(tree));
//        System.out.println(meter.countChildren(tree));
  }

  public static void checkInsertMillions(Tree tree) {

//        tree.getInMemoryNodes().printNodesInMemory();
//        tree.insert(20, "more more");
    List<Long> intList = LongStream.rangeClosed(2, 1000000).boxed().collect(Collectors.toList());
    Collections.shuffle(intList);
    Iterator listIterator = intList.iterator();
    long stratTime = System.currentTimeMillis();
    long startT = System.currentTimeMillis();
    Long in;
    long counter = 1;
    while (listIterator.hasNext()) {
      in = (Long) listIterator.next();
      tree.insert(in, in);
      counter++;
      if ((System.currentTimeMillis() - startT) >= 1000) {
        System.out.println("count/sec " + counter);
        startT = System.currentTimeMillis();
        counter = 1;
      }
    }
    System.out.println("count/sec " + counter);
    long endTime = System.currentTimeMillis();

    System.out.println("Total time = " + (endTime - stratTime));
//        tree.insert(15, 15l);
//        tree.insert(50, "50 more");
//        tree.insert(9, 25l);
//        tree.insert(80, 8l);
//        tree.insert(104, 1l);
    tree.commitTree();
//        tree.getInMemoryNodes().printNodesInMemory();

  }

  public static void checkInsert(Tree tree) {

//        tree.insert(20, 20l);
//        tree.insert(15, 15l);
//        tree.insert(9, 9l);
//        tree.insert(50, 50l);
//        tree.insert(25, 25l);
//        tree.insert(80, 8l);
//        tree.insert(104, 1l);
    tree.getInMemoryNodes().printNodesInMemory();
    tree.commitTree();
    tree.getInMemoryNodes().printNodesInMemory();

  }

  public static void checkSearch(Tree tree) {
    tree.search(Long.valueOf(1));
    tree.search(Long.valueOf(1000));
//    1000000
    tree.search(1999900l);
    tree.search(Long.valueOf(8000001));
    tree.search(Long.valueOf(9500000));
        tree.search(Long.valueOf(99999));
        tree.search(Long.valueOf(21980));
        tree.search(Long.valueOf(20807));
//        tree.search(Long.valueOf(162));
//        tree.search(Long.valueOf(1759147));
//        tree.search(Long.valueOf(104));
//        tree.search(Long.valueOf(80));
//        tree.search(Long.valueOf(1509874));
//        tree.search(Long.valueOf(25));
//        
//        
//         tree.search(80);
//        tree.search(104);
//        tree.search(550);
//        tree.search(200);
//        tree.search(150);
//        tree.search(90);
//        tree.search(500);
//        
//        tree.search(250);
//
//        
//         tree.search(5000);
//        
//        tree.search(2500);

    tree.getInMemoryNodes().printNodesInMemory();
  }

  public static void main(String[] args) {
    Tree tree = getTree();
    tree.setTree();
//        System.out.println(tree.root.toString());
//        tree.insert(2000009l, 2000009);               
//    checkInsertMillions(tree);
//        checkmemory(tree);
//        checkInsert(tree);
//        System.out.println("\n\n\n\n");
    checkSearch(tree);
//        System.out.println("\n\n\n\n\n");
//        checkNodes(tree);
  }
}
