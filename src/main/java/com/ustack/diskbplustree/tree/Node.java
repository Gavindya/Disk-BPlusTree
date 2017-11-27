/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ustack.diskbplustree.tree;

import com.ustack.diskbplustree.helpers.NodeEntityComparator;

import java.io.Serializable;
import java.util.Arrays;

/**
 *
 * @author AdminPC.
 */
public abstract class Node implements Serializable {

  private NodeEntity[] entities;
  private transient int visitCount;
  protected int parent;
  protected transient Tree tree;
  protected transient boolean isDirty;
  private final int position;

  /**
   *
   * @param tree tree.
   */
  public Node(Tree tree) {
    this.tree = tree;
    parent = (-1);
    entities = new NodeEntity[ConstantsOfTree.DEGREE - 1];/* new NodeEntity[this.tree.getDegree() - 1];*/
    visitCount = 0;
    position = tree.getNodeCount() * tree.getNodeSize();
    NodeSerializer.initialSerialization(this, tree);
    isDirty = false;
    tree.increaseNodeCount();
  }

  /**
   *
   * @param entities entities.
   * @param parent parent
   * @param tree tree
   * @param position position
   */
  public Node(NodeEntity[] entities, int parent, Tree tree, int position) {
    this.tree = tree;
    this.parent = parent;
    this.entities = new NodeEntity[ConstantsOfTree.DEGREE - 1];
    for (int i = 0; i < entities.length; i++) {
      if (i < (ConstantsOfTree.DEGREE - 1)) {
        this.entities[i] = entities[i];
      } else {
        break;
      }
    }
    visitCount = 0;
    this.position = position;
    isDirty = false;
  }

  /**
   *
   * @param tree tree.
   */
  public void setTree(Tree tree) {
    this.tree = tree;
  }

  /**
   *
   * @return node.
   */
  public Node increaseVisitCount() {
    visitCount = visitCount + 1;
    return this;
  }

  /**
   *
   * @return position of node.
   */
  public int getPosition() {
    return position;
  }

  /**
   *
   * @return number of visits per node.
   */
  public int getVisitCount() {
    return this.visitCount;
  }

  /**
   *
   * @param tree tree.
   * @return isNodeSerialized
   */
  public boolean serialize(Tree tree) {
    return NodeSerializer.serializeNode(this, tree);
  }

  /**
   * @author AdminPC.
   * @return isNode full
   */
  public boolean isFull() {
    return getEntityCount() >= (ConstantsOfTree.DEGREE - 1);
  }

  /**
   * @return integer count of entities.
   */
  public int getEntityCount() {
    int count = 0;
    for (NodeEntity entity : entities) {
      if (entity != null && /*) {
        if (*/!entity.getKey().equals(0L)) {
          count = count + 1;
//        }
      }
    }
    return count;
  }

  /**
   * @author AdminPC.
   */
  public void sortEntities() {
    Arrays.sort(entities, 0, (int) getEntityCount(), new NodeEntityComparator());
  }

  /**
   * @return node entities list.
   */
  public NodeEntity[] getEntities() {
    return this.entities;
  }
  
  /**
   * @param entity entity.
   * @return whether node added
   */
  public boolean addEntity(NodeEntity entity) {

    boolean isSet = false;
    for (int i = 0; i < entities.length; i++) {
      if (entities[i] == null || entities[i].getKey().equals(0L)) {
        entities[i] = entity;
        isSet = true;
        break;
      }
    }
    sortEntities();
    isDirty = true;
    return isSet;
  }

  /**
   * @author AdminPC.
   */
  private void reOrder() {
    for (int i = 0; i < entities.length; i++) {
      if (entities[i] == null) {
        for (int k = (i + 1); k < entities.length; k++) {
          if (entities[k] != null) {
            entities[i] = entities[k];
            entities[k] = null;
            break;
          }
        }
      }
    }
  }

  /**
   * @param index index.
   * @return  node entity
   */
  public NodeEntity removeEntity(int index) {
    NodeEntity nodeEntity = null;
    if (index < entities.length) {
      nodeEntity = entities[index];
      entities[index] = null;
      reOrder();
    }
    isDirty = true;
    return nodeEntity;
  }

  /**  
   * @author AdminPC.
   */
  public void clearEntities() {
    Arrays.fill(this.entities, null);
    isDirty = true;
  }

  /**  
   * @author AdminPC.
   * @param entitiesArray entitiesArray
   */
  public void addAllEntities(NodeEntity[] entitiesArray) {
    if (entitiesArray.length == (ConstantsOfTree.DEGREE - 1)) {
      this.entities = new LeafNodeEntity[ConstantsOfTree.DEGREE - 1];
      this.entities = entitiesArray;
      sortEntities();
      isDirty = true;
    } else {
      System.out.println("ADD ALL WITH DIFFRENT SIZED ARRAY N NOT COMPATIBLE");
    }
  }
  
  public abstract LeafNode getRange(Object start, Object end);

  public abstract LeafNode getGreaterThan(Object start);

  public abstract LeafNode getLessThan(Object start);

  public abstract void printTree();

  public abstract void checkAvailabilityToInsert(Long key, Object data);

  public abstract boolean isKeyAvailable(Long key);

  public abstract LeafNode searchNode(Long key);

  public abstract Object search(Long key);

  public abstract void append(Long key, Object dataObject);

  public abstract void insert(Long key, Object dataObject);

  public boolean isLeafNode() {
    return this instanceof LeafNode;
  }

  public void setParent(int parentOffset) {
    this.parent = parentOffset;
    isDirty = true;
  }

  public int getParent() {
    return this.parent;
  }
  
  @Override
  public String toString() {
    String entitiesList = "";

    for (int i = 0; i < getEntityCount(); i++) {
      entitiesList = entitiesList + " [" + entities[i].getKey() + "] ";
    }
    return "Node{" + "entities=" + entitiesList + "\n, visitCount=" + visitCount
            + ", parent=" + parent + '}';
  }

}
