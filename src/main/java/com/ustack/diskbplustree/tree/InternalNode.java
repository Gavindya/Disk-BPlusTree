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
      if (e != null) {
        if (e.getKey().equals(key)) {
          available = true;
          break;
        }
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
//        System.out.println("found node = " + find.toString() +"\n");
    find.checkAvailabilityToInsert(key, data);
  }

  @Override
  public LeafNode searchNode(Long key) {
    int closest = findClosestIndex(key);
    InternalNodeEntity foundNodeEntity = (InternalNodeEntity) super.getEntities()[closest];
    int comparison = new NaturalComparator().compare(key, foundNodeEntity.getKey());
//        int comparison = new NaturalComparator().compare(Integer.valueOf(key.toString()), Integer.valueOf(foundNodeEntity.getKey().toString()));
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
//        super.sortEntities();
    for (int k = 0; k < getEntities().length; k++) {
      if (getEntities()[k] != null && !getEntities()[k].getKey().equals(Long.valueOf(0))) {
        comparison = naturalComparator.compare(key, super.getEntities()[k].getKey());
//                comparison = naturalComparator.compare(Integer.valueOf(key.toString()), Integer.valueOf(super.getEntities()[k].getKey().toString()));
        if (comparison >= 0) {
          appropriateIndex = k;
        } else {
          break;
        }
      }
    }
//        System.out.println("appropriate index found = "+appropriateIndex);
    return appropriateIndex;
  }

  @Override
  public Object search(Long key) {
    int closest = findClosestIndex(key);
//        System.out.println(" 1 - "+(InternalNodeEntity) super.getEntities()[0]);
//        System.out.println(" 2 - "+(InternalNodeEntity) super.getEntities()[1]);
    InternalNodeEntity foundNodeEntity = (InternalNodeEntity) super.getEntities()[closest];
//        System.out.println("found node entity ="+foundNodeEntity.toString());
//        if(foundNodeEntity.toString().equals("0")){
//            foundNodeEntity = (InternalNodeEntity) super.getEntities()[1];
//        }
//        System.out.println("found node entity ="+foundNodeEntity.toString());
//        System.out.println(foundNodeEntity);
//        log.info("searching key = " + key.toString() + " ");
    if (!foundNodeEntity.toString().equals("0")) {
      int comparison = new NaturalComparator().compare(key, foundNodeEntity.getKey());
//            System.out.println("compare"+comparison+" :  key :"+key+" found key: "+foundNodeEntity.getKey());
//            int comparison = new NaturalComparator().compare(Integer.valueOf(key.toString()), Integer.valueOf(foundNodeEntity.getKey().toString()));
      if (comparison >= 0) {
//                System.out.println/*log.info*/("get right");
//                System.out.println("right - "+foundNodeEntity.getRightNode(tree).getClass()+" : "+foundNodeEntity.getRightNode(tree));
        return foundNodeEntity.getRightNode(tree).search(key);
      } else {
//                System.out.println/*log.info*/("get left");
//                System.out.println("right - "+foundNodeEntity.getLeftNode(tree).getClass()+ " : "+foundNodeEntity.getLeftNode(tree));
//            System.out.println("in mem internal node");
//            tree.getInMemoryNodes().printNodesInMemory();
        return foundNodeEntity.getLeftNode(tree).search(key);
      }
    } else {
//            System.out.println("found node entity = null && returning null");
      return null;
    }
  }

  @Override
  public void insert(Long key, Object dataObject) {
//        if (key.toString().equals("20")) {
//            System.out.println("20 -- " + this.toString());
//        }
    int appropriateIndex = findClosestIndex(key);
    InternalNodeEntity foundNodeEntity = (InternalNodeEntity) super.getEntities()[appropriateIndex];
    if (foundNodeEntity != null) {
//            int comparison = new NaturalComparator().compare(Integer.valueOf(key.toString()), Integer.valueOf(foundNodeEntity.getKey().toString()));
      int comparison = new NaturalComparator().compare(key, foundNodeEntity.getKey());
//            System.out.println("key -" + key.toString() + " foundKey-" + foundNodeEntity.getKey().toString() + " comp-" + comparison);
      if (comparison >= 0) {
//                System.out.println("get right node");
        foundNodeEntity.getRightNode(tree).insert(key, dataObject);
      } else {
//                System.out.println("get left node");
        foundNodeEntity.getLeftNode(tree).insert(key, dataObject);
      }
    }
  }

  @Override
  public void append(Long key, Object dataObject) {
    int appropriateIndex = findClosestIndex(key);
    InternalNodeEntity foundNodeEntity = (InternalNodeEntity) super.getEntities()[appropriateIndex];
    if (foundNodeEntity != null) {
//            log.info("key {0}",key.getClass());
//            log.info("foundNodeEntity {0}",foundNodeEntity.getKey().getClass());
      int comparison = new NaturalComparator().compare(key, foundNodeEntity.getKey());
//            int comparison = new NaturalComparator().compare(Integer.valueOf(key.toString()), Integer.valueOf(foundNodeEntity.getKey().toString()));
      if (comparison >= 0) {
        foundNodeEntity.getRightNode(tree).append(key, dataObject);
      } else {
        foundNodeEntity.getLeftNode(tree).append(key, dataObject);
      }
    }
  }

  public void put(Long key, Node left, Node right) throws IOException {

//        System.out.println("\n PUT METH \n");
//        System.out.println("this - (parent most probably) " + this.toString());
//        if (left.getNodeOffset() == (-1)) {
//            //    serialize and set offset
//        }
//        if (right.getNodeOffset() == (-1)) {
//            //    serialize and set offset  
//        }
    if (this.parent == (-1)) {
      tree.setRoot(this);
    }
    InternalNodeEntity internalNodeEntity = new InternalNodeEntity(key, tree);
    internalNodeEntity.setLeftNode(left.getPosition());
//        System.out.println("left pos="+left.getPosition());
    internalNodeEntity.setRightNode(right.getPosition());
//        System.out.println("right pos="+right.getPosition());

    if (this.isFull()) {
      split(internalNodeEntity);
    } else {
      isDirty = true;
      this.addEntity(internalNodeEntity);
//            log.info(" \n \n entity added to internal parent :) \n\n");
      this.sortEntities();
      left.setParent(this.getPosition());
      right.setParent(this.getPosition());
      handleTheOrderOfEntities(this);

//            tree.getInMemoryNodes().addNode(left);
//            tree.getInMemoryNodes().addNode(right);
//            log.info("added to internal node");
    }
  }

  private void handleTheOrderOfEntities(InternalNode node) {
    node.isDirty = true;
//        this.sortEntities();
    if (((InternalNodeEntity) node.getEntities()[0]) != null) {
      Node right = ((InternalNodeEntity) node.getEntities()[0]).getRightNode(tree);
      for (int i = 1; i < node.getEntityCount(); i++) {
        if (((InternalNodeEntity) node.getEntities()[i]) != null) {
//                    System.out.println("ine"+((InternalNodeEntity) node.getEntities()[i]));
//                    System.out.println("r = "+right.getPosition());
          ((InternalNodeEntity) node.getEntities()[i]).setLeftNode(right.getPosition());
          right = ((InternalNodeEntity) node.getEntities()[i]).getRightNode(tree);
        }
      }
    }

  }

  public void split(InternalNodeEntity internalNodeEntity) {

//        System.out.println("\n INTERNAL NODE SPLIT METH \n");
//        System.out.println("this internal node = " + this.toString() + "\n");
    InternalNodeEntity[] tempList = new InternalNodeEntity[ConstantsOfTree.DEGREE];
    for (int i = 0; i < ConstantsOfTree.DEGREE - 1; i++) {
      tempList[i] = (InternalNodeEntity) getEntities()[i];
    }
    tempList[ConstantsOfTree.DEGREE - 1] = internalNodeEntity;

//        System.out.println("\ntemp list created ===> - \n");
//        for (InternalNodeEntity e : tempList) {
//            if (e != null) {
//                System.out.print(e.getKey() + ",");
//            } else {
//                System.out.print("null,");
//            }
//        }
//        System.out.println();
//        Arrays.sort(tempList, 0, (int) getEntityCount(), new NodeEntityComparator());
    List<InternalNodeEntity> list = Arrays.asList(tempList);
    Collections.sort(list, new NodeEntityComparator());
    tempList = new InternalNodeEntity[ConstantsOfTree.DEGREE];
    tempList = (InternalNodeEntity[]) list.toArray();

//        Arrays.sort(tempList);
    int split_location = ConstantsOfTree.getSplitLocation();

    InternalNodeEntity[] tempLeftMap = new InternalNodeEntity[ConstantsOfTree.DEGREE - 1];
    InternalNodeEntity[] tempRightMap = new InternalNodeEntity[ConstantsOfTree.DEGREE - 1];

    System.arraycopy(tempList, 0, tempLeftMap, 0, split_location);
//        System.arraycopy(tempList, split_location, tempRightMap, 0, (tempRightMap.length - split_location));
    int counter = 0;
    for (int i = split_location; i < tempList.length; i++) {
      tempRightMap[counter++] = tempList[i];
    }
//        if (ConstantsOfTree.DEGREE % 2 == 0) {
//            System.arraycopy(tempList, split_location, tempRightMap, 0, (tempRightMap.length - split_location));
//        } else {
//            System.arraycopy(tempList, split_location, tempRightMap, 0, (tempRightMap.length));
//        }

//        System.out.println("\ntemp Left Map created  - \n");
//        for (InternalNodeEntity e : tempLeftMap) {
//            if (e != null) {
//                System.out.print(e.getKey() + ",");
//            } else {
//                System.out.print("null,");
//            }
//        }
//        System.out.println();
//        System.out.println("\ntemp Right Map created  - \n");
//        for (InternalNodeEntity e : tempRightMap) {
//            if (e != null) {
//                System.out.print(e.getKey() + ",");
//            } else {
//                System.out.print("null,");
//            }
//        }
//        System.out.println();
    InternalNode rightInternalNode = new InternalNode(tree);
    rightInternalNode.addAllEntities(tempRightMap);
    rightInternalNode.isDirty = true;

//        System.out.println("\n");
//        for(int i=0;i<rightInternalNode.getEntities().length;i++){
//            System.out.print(rightInternalNode.getEntities()[i]+",");
//        }
//        System.out.println();
//        
    Arrays.asList(rightInternalNode.getEntities()).stream().forEach(en -> {
      if (en != null) {
//                System.out.println("en = "+en.toString());
        if (((InternalNodeEntity) en).getLeftNode(tree) != null) {
          ((InternalNodeEntity) en).getLeftNode(tree).setParent(rightInternalNode.getPosition());
        }
        if (((InternalNodeEntity) en).getRightNode(tree) != null) {
          ((InternalNodeEntity) en).getRightNode(tree).setParent(rightInternalNode.getPosition());
        }

      }
    });

//        System.out.println("\n right internal node created - \n");
//        for (NodeEntity e : rightInternalNode.getEntities()) {
//            if (e != null) {
//                System.out.print(e.getKey() + ",");
//            } else {
//                System.out.print("null,");
//            }
//        }
//        handleTheOrderOfEntities(rightInternalNode);
    tree.getInMemoryNodes().addNode(rightInternalNode, tree);

    //print nodes in memory
//        System.out.println("\n\n NODES IN MEMEORY \n");
//        tree.getInMemoryNodes().printNodesInMemory();
//        System.out.println("\n\n\n");
    this.clearEntities();
    this.addAllEntities(tempLeftMap);
    this.isDirty = true;

//        System.out.println(" this node's entities ");
//        for (NodeEntity e : this.getEntities()) {
//            if (e != null) {
//                System.out.print(e.getKey() + ",");
//            } else {
//                System.out.print("null,");
//            }
//        }
//        System.out.println();
    for (int i = 0; i < ConstantsOfTree.DEGREE - 1; i++) {
      if (getEntities()[i] != null) {
//                System.out.println("in - left->"+((InternalNodeEntity) getEntities()[i]).getLeftNode(tree).toString());
        ((InternalNodeEntity) getEntities()[i]).getLeftNode(tree).setParent(this.getPosition());
//                System.out.println("in - right->"+((InternalNodeEntity) getEntities()[i]).getRightNode(tree).toString());
        ((InternalNodeEntity) getEntities()[i]).getRightNode(tree).setParent(this.getPosition());
      }
    }

//        System.out.println("\n set each of reight n left child's parent THIS \n");
//         for (int i = 0; i < ConstantsOfTree.DEGREE - 1; i++) {
//            if (getEntities()[i] != null) {
//                System.out.println("left Node's PARENT = "+((InternalNodeEntity) getEntities()[i]).getLeftNode(tree).getParent());
//                 System.out.println("right Node'a PARENT = "+((InternalNodeEntity) getEntities()[i]).getRightNode(tree).getParent());
//            }
//        }
//        handleTheOrderOfEntities(this);
    if (this.parent == (-1)) {
//            System.out.println("\n internal node's parent NULL ");
      Node parentNode = new InternalNode(tree);
      parentNode.isDirty = true;
//            long parentNodeOffset = parentNode.serialize();
      this.setParent(parentNode.getPosition());
      tree.getInMemoryNodes().addNode(parentNode, tree);
    }
    NodeEntity splittingNodeEntity = rightInternalNode.removeEntity(0);
//        System.out.println("\n AFTER REMOVAL OF SPLITTING ENTITY \n");
//        tree.getInMemoryNodes().printNodesInMemory();

//        System.out.println("\n splitting node entity = "+splittingNodeEntity.toString()+"\n");
    if (splittingNodeEntity != null) {
      InternalNodeEntity splittingEntity = (InternalNodeEntity) splittingNodeEntity;
      try {
        ((InternalNode) tree.getInMemoryNodes().getNode(parent, tree)).put(splittingEntity.getKey(), this, rightInternalNode);
//                ((InternalNode) NodeSerializer.deSerializeNode(parent, ConstantsOfTree.NODE_FILE_PATH,tree)).put(splittingEntity.getKey(), this, rightInternalNode);
      } catch (IOException ex) {
//                System.out.println("\n EXCption in  INTERNAL SPLIT METHOD \n");
        ex.printStackTrace();
//                log.exception(ex, "IOException {0}", ex.getMessage());
      }
    }
  }

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
