/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ustack.diskbplustree.tree;

import com.ustack.diskbplustree.helpers.NodeEntityComparator;

import java.io.IOException;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author AdminPC
 */
public class LeafNode extends Node {

  private int nextLeafNode;
  private int previousLeafNode;

  public LeafNode(Tree tree) {
    super(tree);
    nextLeafNode = (-1);
    previousLeafNode = (-1);
  }

  public LeafNode(LeafNodeEntity[] entities, int parent, Tree tree, int position, int nextNode, int previousNode) {
    super(entities, parent, tree, position);
    nextLeafNode = nextNode;
    previousLeafNode = previousNode;
  }

  @Override
  public boolean isFull() {
    return super.getEntityCount() >= (ConstantsOfTree.DEGREE - 1);
  }

  public int getPreviousLeafNodeOffset() {
    return previousLeafNode;
  }

  public int getNextLeafNodeOffset() {
    return nextLeafNode;
  }

  public Node getPreviousLeafNode() {
    if (previousLeafNode != (-1)) {
      return tree.getInMemoryNodes().getNode(previousLeafNode, tree);
    } else {
      return null;
    }
  }

  public Node getNextLeafNode() {
    if (nextLeafNode != (-1)) {
      return tree.getInMemoryNodes().getNode(nextLeafNode, tree);
    } else {
      return null;
    }
  }

  public void setPreviousLeafNode(int preNode) {
    this.previousLeafNode = preNode;
    isDirty = true;
  }

  public void setNextLeafNode(int nextNode) {
    this.nextLeafNode = nextNode;
    isDirty = true;
  }

  @Override
  public void printTree() {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }

  @Override
  public boolean isKeyAvailable(Long key) {
    if (getEntityCount() == 0) {
      return false;
    }
    boolean available = false;
    for (int i = 0; i < getEntityCount(); i++) {
      if (getEntities()[i] != null) {
        if (getEntities()[i].getKey().equals(key)) {
          available = true;
          return available;
        }
      }
    }
    return available;
  }

  @Override
  public void checkAvailabilityToInsert(Long key, Object data) {
    boolean available = false;
    int continuousNullCounter = 0;
    for (int i = 0; i < getEntities().length; i++) {
      if (getEntities()[i] != null) {
        if ((getEntities()[i].getKey().equals(key))) {
          available = true;
          appendToKey(i, key, data);
          break;
        }
        if (!getEntities()[i].getKey().equals(0)) {
          continuousNullCounter = continuousNullCounter + 1;
        }
      } else {
        continuousNullCounter = continuousNullCounter + 1;
      }
      if (continuousNullCounter > 1) {
        break;
      }
    }
    if (!available) {
      insert(key, data);
    }
  }

  public void appendToKey(int index, Object key, Object dataObject) {
    try {
      if (this.getEntities()[index] != null) {
        this.isDirty = true;
        ((LeafNodeEntity) this.getEntities()[index]).setValue(dataObject);
      }
    } catch (Exception ex) {
      System.out.println(ex.getMessage());
    }
  }

  @Override
  public LeafNode searchNode(Long key) {
    return this;
  }

  @Override
  public Object search(Long key) {
    try {
      for (NodeEntity e : super.getEntities()) {
        if (e != null) {
          if (e.getKey().equals(key)) {
            return ((LeafNodeEntity) e).getValue();
          }
        }

      }
    } catch (NumberFormatException ex) {
      for (NodeEntity e : super.getEntities()) {
        if (e != null) {
          if (e.getKey().equals(key)) {
            return ((LeafNodeEntity) e).getValue();
          }
        }
      }
    }
    return null;
  }

  @Override
  public void append(Long key, Object dataObject) {
    try {
      for (NodeEntity e : this.getEntities()) {
        if (e.getKey().equals(key)) {
          this.isDirty = true;
          ((LeafNodeEntity) e).setValue(dataObject);
          break;
        }
      }
    } catch (Exception ex) {
      System.out.println(ex.getMessage());
    }
  }

  @Override
  public void insert(Long key, Object dataObject) {
    LeafNodeEntity entity = new LeafNodeEntity(key, tree);
    entity.setValue(dataObject);
    if (!isFull()) {
      this.isDirty = true;
      if (addEntity(entity)) {
        sortEntities();
      }
    } else {
      split(entity);
    }
  }

  public void split(LeafNodeEntity entity) {
    LeafNodeEntity[] tempList = createTempEntitiesList(entity);
    int split_location = ConstantsOfTree.getSplitLocation();
    LeafNode rightLeafNode = createRightNode(split_location, tempList);
    updateThisNode(split_location, tempList);
    validateParent();
    linkWithParent(rightLeafNode);
  }

  private LeafNodeEntity[] createTempEntitiesList(LeafNodeEntity entity) {
    LeafNodeEntity[] tempList = new LeafNodeEntity[ConstantsOfTree.DEGREE];
    for (int i = 0; i < super.getEntityCount(); i++) {
      tempList[i] = (LeafNodeEntity) super.getEntities()[i];
    }
    tempList[tempList.length - 1] = entity;
    if (getEntityCount() > 1) {
      List<NodeEntity> list = Arrays.asList(tempList);
      for (NodeEntity e : list) {
        if (e != null) {
          e.setKey(e.getKey());
        }
      }
      Collections.sort(list, new NodeEntityComparator());
      tempList = new LeafNodeEntity[ConstantsOfTree.DEGREE];
      tempList = (LeafNodeEntity[]) list.toArray();
    }
    return tempList;
  }

  private void updateThisNode(int split_location, LeafNodeEntity[] tempList) {
    LeafNodeEntity[] tempLeftMap = new LeafNodeEntity[ConstantsOfTree.DEGREE - 1];
    System.arraycopy(tempList, 0, tempLeftMap, 0, split_location);
    this.clearEntities();
    this.addAllEntities(tempLeftMap);
    this.isDirty = true;
  }

  private LeafNode createRightNode(int split_location, LeafNodeEntity[] tempList) {
    LeafNodeEntity[] tempRightMap = new LeafNodeEntity[ConstantsOfTree.DEGREE - 1];
    int counter = 0;
    for (int i = split_location; i < tempList.length; i++) {
      tempRightMap[counter++] = tempList[i];
    }
    LeafNode rightLeafNode = new LeafNode(tree);
    rightLeafNode.addAllEntities(tempRightMap);
    rightLeafNode.isDirty = true;
    tree.getInMemoryNodes().addNode(rightLeafNode, tree);
    return rightLeafNode;
  }

  private void setAppropriateNeighbours(LeafNode rightLeafNode) {
    rightLeafNode.setNextLeafNode(this.nextLeafNode);
    if (this.nextLeafNode != (-1)) {
      if (((LeafNode) tree.getInMemoryNodes().getNode(this.nextLeafNode, tree)) != null) {
        ((LeafNode) tree.getInMemoryNodes().getNode(this.nextLeafNode, tree)).setPreviousLeafNode(rightLeafNode.getPosition());
      }
    }
    rightLeafNode.setPreviousLeafNode(this.getPosition());
    this.setNextLeafNode(rightLeafNode.getPosition());
  }

  private void validateParent() {
    if (this.parent == (-1)) {
      Node parentNode = new InternalNode(tree);
      parentNode.isDirty = true;
      this.setParent(parentNode.getPosition());
      tree.getInMemoryNodes().addNode(parentNode, tree);
    }
  }

  private void linkWithParent(LeafNode rightLeafNode) {
    LeafNodeEntity splittingEntity = (LeafNodeEntity) rightLeafNode.getEntities()[0];
    try {
      ((InternalNode) tree.getInMemoryNodes().getNode(parent, tree)).put(splittingEntity.getKey(), this, rightLeafNode);
    } catch (IOException ex) {
      System.out.println(ex.getMessage());
    }
  }
//  public void split(LeafNodeEntity entity) {
//    LeafNodeEntity[] tempList = new LeafNodeEntity[ConstantsOfTree.DEGREE];
//    for (int i = 0; i < super.getEntityCount(); i++) {
//      tempList[i] = (LeafNodeEntity) super.getEntities()[i];
//    }
//    tempList[tempList.length - 1] = entity;
//    if (getEntityCount() > 1) {
//      List<NodeEntity> list = Arrays.asList(tempList);
//      for (NodeEntity e : list) {
//        if (e != null) {
//          e.setKey(e.getKey());
//        }
//      }
//      Collections.sort(list, new NodeEntityComparator());
//      tempList = new LeafNodeEntity[ConstantsOfTree.DEGREE];
//      tempList = (LeafNodeEntity[]) list.toArray();
//    }
//    int split_location = ConstantsOfTree.getSplitLocation();
//    LeafNodeEntity[] tempLeftMap = new LeafNodeEntity[ConstantsOfTree.DEGREE - 1];
//    LeafNodeEntity[] tempRightMap = new LeafNodeEntity[ConstantsOfTree.DEGREE - 1];
//
//    System.arraycopy(tempList, 0, tempLeftMap, 0, split_location);
//    int counter = 0;
//    for (int i = split_location; i < tempList.length; i++) {
//      tempRightMap[counter++] = tempList[i];
//    }
//    LeafNode rightLeafNode = new LeafNode(tree);
//    rightLeafNode.addAllEntities(tempRightMap);
//    rightLeafNode.isDirty = true;
//    tree.getInMemoryNodes().addNode(rightLeafNode, tree);
//    this.clearEntities();
//    this.addAllEntities(tempLeftMap);
//    this.isDirty = true;
//    rightLeafNode.setNextLeafNode(this.nextLeafNode);
//    if (this.nextLeafNode != (-1)) {
//      if (((LeafNode) tree.getInMemoryNodes().getNode(this.nextLeafNode, tree)) != null) {
//        ((LeafNode) tree.getInMemoryNodes().getNode(this.nextLeafNode, tree)).setPreviousLeafNode(rightLeafNode.getPosition());
//      }
//    }
//    rightLeafNode.setPreviousLeafNode(this.getPosition());
//    this.setNextLeafNode(rightLeafNode.getPosition());
//    if (this.parent == (-1)) {
//      Node parentNode = new InternalNode(tree);
//      parentNode.isDirty = true;
//      this.setParent(parentNode.getPosition());
//      tree.getInMemoryNodes().addNode(parentNode, tree);
//    }
//    LeafNodeEntity splittingEntity = (LeafNodeEntity) rightLeafNode.getEntities()[0];
//    try {
//      ((InternalNode) tree.getInMemoryNodes().getNode(parent, tree)).put(splittingEntity.getKey(), this, rightLeafNode);
//    } catch (IOException ex) {
//      System.out.println(ex.getMessage());
//    }
//  }

  @Override
  public LeafNode getRange(Object start, Object end) {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }

  @Override
  public LeafNode getGreaterThan(Object start) {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }

  @Override
  public LeafNode getLessThan(Object start) {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }

}
