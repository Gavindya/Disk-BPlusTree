/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ustack.diskbplustree.tree;

import com.ustack.diskbplustree.helpers.NaturalComparator;
import com.ustack.diskbplustree.helpers.NodeEntityComparator;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author AdminPC
 */
public class InternalNode extends Node {

  public InternalNode(Tree tree) {
    super(tree);
  }

  public InternalNode(InternalNodeEntity[] entities, int parent, Tree tree, int position) {
    super(entities, parent, tree, position);
  }

  @Override
  public boolean isKeyAvailable(Long key) {
    boolean available = false;
    for (NodeEntity e : getEntities()) {
      if (e != null && e.getKey().equals(key)) {
        available = true;
        break;
      }
    }
    if (!available) {
      Object find = search(key);
      if (find != null) {
        return true;
      }
    }
    return false;
  }

  @Override
  public void checkAvailabilityToInsert(Long key, Object data) {
    LeafNode find = searchNode(key);
    find.checkAvailabilityToInsert(key, data);
  }

  @Override
  public LeafNode searchNode(Long key) {
    int closest = findClosestIndex(key);
    InternalNodeEntity foundNodeEntity = (InternalNodeEntity) super.getEntities()[closest];
    int comparison = new NaturalComparator().compare(key, foundNodeEntity.getKey());
    Node closestNode;
    if (comparison >= 0) {
      closestNode = foundNodeEntity.getRightNode(tree);
      return closestNode.searchNode(key);
    } else {
      closestNode = foundNodeEntity.getLeftNode(tree);
      return closestNode.searchNode(key);
    }
  }

  private Integer findClosestIndex(Object key) {
    Integer appropriateIndex = 0;
    NaturalComparator naturalComparator = new NaturalComparator();
    int comparison;
    for (int k = 0; k < getEntities().length; k++) {
      if (getEntities()[k] != null && !getEntities()[k].getKey().equals(Long.valueOf(0))) {
        comparison = naturalComparator.compare(key, super.getEntities()[k].getKey());
        if (comparison >= 0) {
          appropriateIndex = k;
        } else {
          break;
        }
      }
    }
    return appropriateIndex;
  }

  @Override
  public Object search(Long key) {
    int closest = findClosestIndex(key);
    InternalNodeEntity foundNodeEntity = (InternalNodeEntity) super.getEntities()[closest];
    if (!foundNodeEntity.toString().equals("0")) {
      int comparison = new NaturalComparator().compare(key, foundNodeEntity.getKey());
      if (comparison >= 0) {
        return foundNodeEntity.getRightNode(tree).search(key);
      } else {
        return foundNodeEntity.getLeftNode(tree).search(key);
      }
    } else {
      return null;
    }
  }

  @Override
  public void insert(Long key, Object dataObject) {
    int appropriateIndex = findClosestIndex(key);
    InternalNodeEntity foundNodeEntity = (InternalNodeEntity) super.getEntities()[appropriateIndex];
    if (foundNodeEntity != null) {
      int comparison = new NaturalComparator().compare(key, foundNodeEntity.getKey());
      if (comparison >= 0) {
        foundNodeEntity.getRightNode(tree).insert(key, dataObject);
      } else {
        foundNodeEntity.getLeftNode(tree).insert(key, dataObject);
      }
    }
  }

  @Override
  public void append(Long key, Object dataObject) {
    int appropriateIndex = findClosestIndex(key);
    InternalNodeEntity foundNodeEntity = (InternalNodeEntity) super.getEntities()[appropriateIndex];
    if (foundNodeEntity != null) {
      int comparison = new NaturalComparator().compare(key, foundNodeEntity.getKey());
      if (comparison >= 0) {
        foundNodeEntity.getRightNode(tree).append(key, dataObject);
      } else {
        foundNodeEntity.getLeftNode(tree).append(key, dataObject);
      }
    }
  }

  public void put(Long key, Node left, Node right) throws IOException {
    if (this.parent == (-1)) {
      tree.setRoot(this);
    }
    InternalNodeEntity internalNodeEntity = new InternalNodeEntity(key, tree);
    internalNodeEntity.setLeftNode(left.getPosition());
    internalNodeEntity.setRightNode(right.getPosition());
    if (this.isFull()) {
      split(internalNodeEntity);
    } else {
      isDirty = true;
      this.addEntity(internalNodeEntity);
      this.sortEntities();
      left.setParent(this.getPosition());
      right.setParent(this.getPosition());
      handleTheOrderOfEntities(this);
    }
  }

  private void handleTheOrderOfEntities(InternalNode node) {
    node.isDirty = true;
    if (((InternalNodeEntity) node.getEntities()[0]) != null) {
      Node right = ((InternalNodeEntity) node.getEntities()[0]).getRightNode(tree);
      for (int i = 1; i < node.getEntityCount(); i++) {
        if (((InternalNodeEntity) node.getEntities()[i]) != null) {
          ((InternalNodeEntity) node.getEntities()[i]).setLeftNode(right.getPosition());
          right = ((InternalNodeEntity) node.getEntities()[i]).getRightNode(tree);
        }
      }
    }

  }

  public void split(InternalNodeEntity internalNodeEntity) {
    InternalNodeEntity[] tempList = createTempEntitiesList(internalNodeEntity);
    int split_location = ConstantsOfTree.getSplitLocation();
    InternalNode rightInternalNode = createRightNode(split_location, tempList);
    updateThisNode(split_location, tempList);
    validateParent();
    linkWithParent(rightInternalNode);
  }

  private InternalNodeEntity[] createTempEntitiesList(InternalNodeEntity internalNodeEntity) {
    InternalNodeEntity[] tempList = new InternalNodeEntity[ConstantsOfTree.DEGREE];
    for (int i = 0; i < ConstantsOfTree.DEGREE - 1; i++) {
      tempList[i] = (InternalNodeEntity) getEntities()[i];
    }
    tempList[ConstantsOfTree.DEGREE - 1] = internalNodeEntity;
    List<InternalNodeEntity> list = Arrays.asList(tempList);
    Collections.sort(list, new NodeEntityComparator());
    tempList = new InternalNodeEntity[ConstantsOfTree.DEGREE];
    tempList = (InternalNodeEntity[]) list.toArray();
    return tempList;
  }

  private void updateThisNode(int split_location, InternalNodeEntity[] tempList) {
    InternalNodeEntity[] tempLeftMap = new InternalNodeEntity[ConstantsOfTree.DEGREE - 1];
    System.arraycopy(tempList, 0, tempLeftMap, 0, split_location);
    this.clearEntities();
    this.addAllEntities(tempLeftMap);
    this.isDirty = true;
    setAppropriateParent(this);
  }

  private InternalNode createRightNode(int split_location, InternalNodeEntity[] tempList) {
    InternalNodeEntity[] tempRightMap = new InternalNodeEntity[ConstantsOfTree.DEGREE - 1];
    int counter = 0;
    for (int i = split_location; i < tempList.length; i++) {
      tempRightMap[counter++] = tempList[i];
    }
    InternalNode rightInternalNode = new InternalNode(tree);
    rightInternalNode.addAllEntities(tempRightMap);
    rightInternalNode.isDirty = true;
    setAppropriateParent(rightInternalNode);
    tree.getInMemoryNodes().addNode(rightInternalNode, tree);
    return rightInternalNode;
  }

  private void setAppropriateParent(InternalNode internalNode) {
    Arrays.asList(internalNode.getEntities()).stream().forEach(en -> {
      if (en != null) {
        if (((InternalNodeEntity) en).getLeftNode(tree) != null) {
          ((InternalNodeEntity) en).getLeftNode(tree).setParent(internalNode.getPosition());
        }
        if (((InternalNodeEntity) en).getRightNode(tree) != null) {
          ((InternalNodeEntity) en).getRightNode(tree).setParent(internalNode.getPosition());
        }

      }
    });
  }

  private void validateParent() {
    if (this.parent == (-1)) {
      Node parentNode = new InternalNode(tree);
      parentNode.isDirty = true;
      this.setParent(parentNode.getPosition());
      tree.getInMemoryNodes().addNode(parentNode, tree);
    }
  }

  private void linkWithParent(InternalNode rightInternalNode) {
    NodeEntity splittingNodeEntity = rightInternalNode.removeEntity(0);
    if (splittingNodeEntity != null) {
      InternalNodeEntity splittingEntity = (InternalNodeEntity) splittingNodeEntity;
      try {
        ((InternalNode) tree.getInMemoryNodes().getNode(parent, tree)).put(splittingEntity.getKey(), this, rightInternalNode);
      } catch (IOException ex) {
        System.out.println(ex.getMessage());
      }
    }
  }

//  public void split(InternalNodeEntity internalNodeEntity) {
//    InternalNodeEntity[] tempList = new InternalNodeEntity[ConstantsOfTree.DEGREE];
//    for (int i = 0; i < ConstantsOfTree.DEGREE - 1; i++) {
//      tempList[i] = (InternalNodeEntity) getEntities()[i];
//    }
//    tempList[ConstantsOfTree.DEGREE - 1] = internalNodeEntity;
//    List<InternalNodeEntity> list = Arrays.asList(tempList);
//    Collections.sort(list, new NodeEntityComparator());
//    tempList = new InternalNodeEntity[ConstantsOfTree.DEGREE];
//    tempList = (InternalNodeEntity[]) list.toArray();    
//    InternalNodeEntity[] tempRightMap = new InternalNodeEntity[ConstantsOfTree.DEGREE - 1];
//    int counter = 0;
//    for (int i = split_location; i < tempList.length; i++) {
//      tempRightMap[counter++] = tempList[i];
//    }
//    InternalNode rightInternalNode = new InternalNode(tree);
//    rightInternalNode.addAllEntities(tempRightMap);
//    rightInternalNode.isDirty = true;
//    Arrays.asList(rightInternalNode.getEntities()).stream().forEach(en -> {
//      if (en != null) {
//        if (((InternalNodeEntity) en).getLeftNode(tree) != null) {
//          ((InternalNodeEntity) en).getLeftNode(tree).setParent(rightInternalNode.getPosition());
//        }
//        if (((InternalNodeEntity) en).getRightNode(tree) != null) {
//          ((InternalNodeEntity) en).getRightNode(tree).setParent(rightInternalNode.getPosition());
//        }
//
//      }
//    });
//    InternalNodeEntity[] tempLeftMap = new InternalNodeEntity[ConstantsOfTree.DEGREE - 1];
//    System.arraycopy(tempList, 0, tempLeftMap, 0, split_location);
//    this.clearEntities();
//    this.addAllEntities(tempLeftMap);
//    this.isDirty = true;
//    for (int i = 0; i < ConstantsOfTree.DEGREE - 1; i++) {
//      if (getEntities()[i] != null) {
//        ((InternalNodeEntity) getEntities()[i]).getLeftNode(tree).setParent(this.getPosition());
//        ((InternalNodeEntity) getEntities()[i]).getRightNode(tree).setParent(this.getPosition());
//      }
//    }
//    if (this.parent == (-1)) {
//      Node parentNode = new InternalNode(tree);
//      parentNode.isDirty = true;
//      this.setParent(parentNode.getPosition());
//      tree.getInMemoryNodes().addNode(parentNode, tree);
//    }
//    NodeEntity splittingNodeEntity = rightInternalNode.removeEntity(0);
//    if (splittingNodeEntity != null) {
//      InternalNodeEntity splittingEntity = (InternalNodeEntity) splittingNodeEntity;
//      try {
//        ((InternalNode) tree.getInMemoryNodes().getNode(parent, tree)).put(splittingEntity.getKey(), this, rightInternalNode);
//      } catch (IOException ex) {
//        System.out.println(ex.getMessage());
//      }
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

  @Override
  public void printTree() {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }

}
