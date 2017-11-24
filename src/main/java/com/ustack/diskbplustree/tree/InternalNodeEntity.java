/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ustack.diskbplustree.tree;

/**
 *
 * @author AdminPC
 */
public class InternalNodeEntity extends NodeEntity {

  private int leftNodeOffset;
  private int rightNodeOffset;

  public InternalNodeEntity(Long key, Tree tree) {
    super(key, tree);
    leftNodeOffset = (-1);
    rightNodeOffset = (-1);
  }

  public void setLeftNode(int nodeLocation) {
    leftNodeOffset = nodeLocation;
  }

  public void setRightNode(int nodeLocation) {
    rightNodeOffset = nodeLocation;
  }

  public int getLeftNodeOffset() {
    return leftNodeOffset;
  }

  public int getRightNodeOffset() {
    return rightNodeOffset;
  }

  public Node getLeftNode(Tree tree) {
    if (leftNodeOffset != (-1)) {
      Node node = tree.getInMemoryNodes().getNode(leftNodeOffset, tree);
//            System.out.println("left node - "+node.toString());
      return node;
    } else {
      return null;
    }
  }

  public Node getRightNode(Tree tree) {
    if (rightNodeOffset != (-1)) {
      Node node = tree.getInMemoryNodes().getNode(rightNodeOffset, tree);
      return node;
    } else {
      return null;
    }
  }

  @Override
  public String toString() {
    return getKey().toString();
  }

}
