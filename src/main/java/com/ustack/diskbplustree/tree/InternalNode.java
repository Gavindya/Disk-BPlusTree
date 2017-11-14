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

    @Override
    public boolean isKeyAvailable(Integer key) {
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
    public void checkAvailabilityToInsert(Integer key, Object data) {
        LeafNode find = searchNode(key);
//        System.out.println("found node =\n" + find.toString() + "\n");
        find.checkAvailabilityToInsert(key, data);
    }

    @Override
    public LeafNode searchNode(Integer key) {
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
        super.sortEntities();
        for (int k = 0; k < getEntities().length; k++) {
            if (getEntities()[k] != null) {
                comparison = naturalComparator.compare(key, super.getEntities()[k].getKey());
//                comparison = naturalComparator.compare(Integer.valueOf(key.toString()), Integer.valueOf(super.getEntities()[k].getKey().toString()));
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
    public Object search(Integer key) {
        int closest = findClosestIndex(key);
        InternalNodeEntity foundNodeEntity = (InternalNodeEntity) super.getEntities()[closest];
//        System.out.println(foundNodeEntity);
//        log.info("searching key = " + key.toString() + " ");
        if (foundNodeEntity != null) {
            int comparison = new NaturalComparator().compare(key, foundNodeEntity.getKey());
//            int comparison = new NaturalComparator().compare(Integer.valueOf(key.toString()), Integer.valueOf(foundNodeEntity.getKey().toString()));
            if (comparison >= 0) {
//                log.info("right");
                return foundNodeEntity.getRightNode(tree).search(key);
            } else {
//                log.info("left");
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
    public void insert(Integer key, Object dataObject) {
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
    public void append(Integer key, Object dataObject) {
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

    public void put(Integer key, Node left, Node right) throws IOException {

//        System.out.println("\n PUT METH \n");
//        System.out.println("this - (parent most probably) " + this.toString());
        if (left.getNodeOffset() == (-1)) {
            //    serialize and set offset
        }
        if (right.getNodeOffset() == (-1)) {
            //    serialize and set offset  
        }
        if (this.parent == (-1)) {
            tree.setRoot(this);
        }
        InternalNodeEntity internalNodeEntity = new InternalNodeEntity(key, tree);
        internalNodeEntity.setLeftNode(left.getNodeOffset());
        internalNodeEntity.setRightNode(right.getNodeOffset());

        if (this.isFull()) {
            split(internalNodeEntity);
        } else {
            isDirty = true;
            this.addEntity(internalNodeEntity);
//            log.info(" \n \n entity added to internal parent :) \n\n");
            this.sortEntities();
            left.setParent(this.getNodeOffset());
            right.setParent(this.getNodeOffset());
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
                    ((InternalNodeEntity) node.getEntities()[i]).setLeftNode(right.getNodeOffset());
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

        System.out.println("\ntemp Left Map created  - \n");
        for (InternalNodeEntity e : tempLeftMap) {
            if (e != null) {
                System.out.print(e.getKey() + ",");
            } else {
                System.out.print("null,");
            }
        }
        System.out.println();
        System.out.println("\ntemp Right Map created  - \n");
        for (InternalNodeEntity e : tempRightMap) {
            if (e != null) {
                System.out.print(e.getKey() + ",");
            } else {
                System.out.print("null,");
            }
        }
        System.out.println();
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
                ((InternalNodeEntity) en).getLeftNode(tree).setParent(rightInternalNode.getNodeOffset());
                ((InternalNodeEntity) en).getRightNode(tree).setParent(rightInternalNode.getNodeOffset());
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
        tree.getInMemoryNodes().addNode(rightInternalNode);

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
                ((InternalNodeEntity) getEntities()[i]).getLeftNode(tree).setParent(this.getNodeOffset());
                ((InternalNodeEntity) getEntities()[i]).getRightNode(tree).setParent(this.getNodeOffset());
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
            this.setParent(parentNode.getNodeOffset());
            tree.getInMemoryNodes().addNode(parentNode);
        }
        NodeEntity splittingNodeEntity = rightInternalNode.removeEntity(0);
//        System.out.println("\n AFTER REMOVAL OF SPLITTING ENTITY \n");
//        tree.getInMemoryNodes().printNodesInMemory();

//        System.out.println("\n splitting node entity = "+splittingNodeEntity.toString()+"\n");
        if (splittingNodeEntity != null) {
            InternalNodeEntity splittingEntity = (InternalNodeEntity) splittingNodeEntity;
            try {
                ((InternalNode) tree.getInMemoryNodes().getNode(parent)).put(splittingEntity.getKey(), this, rightInternalNode);
//                ((InternalNode) NodeSerializer.deSerializeNode(parent, ConstantsOfTree.NODE_FILE_PATH,tree)).put(splittingEntity.getKey(), this, rightInternalNode);
            } catch (IOException ex) {
//                System.out.println("\n EXCption in  INTERNAL SPLIT METHOD \n");
//                ex.printStackTrace();
                System.out.println(ex.getMessage());
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
