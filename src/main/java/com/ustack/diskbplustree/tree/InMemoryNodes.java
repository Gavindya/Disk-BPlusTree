/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ustack.diskbplustree.tree;

import java.io.Serializable;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author AdminPC
 */
public class InMemoryNodes implements Serializable {

  private final Map<Integer, Node> nodes;
  private final Tree tree;

  public InMemoryNodes(Tree tree) {
    nodes = new HashMap<>();
    this.tree = tree;
  }

  public void closeMemMap() {
    NodeSerializer.closeMemMap();
  }

  public void commitStagedNode(Integer removing, Tree tree) {
//        nodes.get(removing).serialize(tree);
  }

  public void commit(Tree tree) {
    nodes.entrySet().forEach((e) -> {
      e.getValue().serialize(tree);
    });
    tree.closeMemMap();
  }

  public Node addNode(Node node, Tree tree) {
    if (nodes.containsKey(node.getPosition())) {
      nodes.replace(node.getPosition(), node);
    } else {
      nodes.put(node.getPosition(), node);
    }
    return node;
  }

  public Node addNode(int offset) {
    Node node = loadNode(offset);
    if (node != null) {
      nodes.put(offset, node);
    } else {
      System.out.println("loaded null node");
    }
    return node;
  }

  public boolean isNodeLoaded(int offset) {
    return nodes.containsKey(offset);
  }

  public Node getNode(int offset, Tree tree) {
    if (isNodeLoaded(offset)) {
      return nodes.get(offset).increaseVisitCount();
    } else {
      return addNode(offset);
    }
  }

  public Node loadNode(int offset) {
    return NodeSerializer.deSerializeNode(offset, tree);
  }

  public void removeAllNodes() {
    nodes.clear();
  }

  public int getNumberOfNodesInMemory() {
    return nodes.size();
  }

  public void printNodesInMemory() {
    nodes.entrySet().forEach((e) -> {
      System.out.println("key = " + e.getKey() + " : value = " + e.getValue().toString() + " : DIRTY ? " + e.getValue().isDirty);
    });
  }

  public void setTree(Tree tree) {
    nodes.entrySet().forEach((e) -> {
      e.getValue().tree = tree;
    });
  }
}
