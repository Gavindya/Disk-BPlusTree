/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ustack.diskbplustree.tree;

import com.ustack.spi.logger.ConsoleLogWriter;
import com.ustack.spi.logger.Logger;
import com.ustack.diskbplustree.helpers.NodeEntityComparator;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 *
 * @author AdminPC
 */
public class LeafNode extends Node {

    private long nextLeafNode;
    private long previousLeafNode;
    private final transient Logger LOG = new Logger("test", new ConsoleLogWriter());

    public LeafNode(Tree tree) {
        super(tree);
        nextLeafNode = (-1);
        previousLeafNode = (-1);
    }

    @Override
    public boolean isFull() {
        return super.getEntityCount() >= (ConstantsOfTree.DEGREE - 1);
    }

    public long getPreviousLeafNodeOffset() {
        return previousLeafNode;
    }

    public long getNextLeafNodeOffset() {
        return nextLeafNode;
    }

    public Node getPreviousLeafNode() {
        if (previousLeafNode != (-1)) {
            return tree.getInMemoryNodes().getNode(previousLeafNode);
        } else {
            return null;
        }
    }

    public Node getNextLeafNode() {
        if (nextLeafNode != (-1)) {
            return tree.getInMemoryNodes().getNode(nextLeafNode);
        } else {
            return null;
        }
    }

    public void setPreviousLeafNode(long preNode) {
        this.previousLeafNode = preNode;
        isDirty = true;
    }

    public void setNextLeafNode(long nextNode) {
        this.nextLeafNode = nextNode;
        isDirty = true;
    }

    @Override
    public void printTree() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean isKeyAvailable(Integer key) {
        if (getEntityCount() == 0) {
            return false;
        }
        boolean available = false;
//        System.out.println("entitiy count = " + getEntityCount());
        for (int i = 0; i < getEntityCount(); i++) {
            if (getEntities()[i] != null) {
                if (Objects.equals(getEntities()[i].getKey(), key)) {
                    available = true;
                    return available;
                }
            }
        }
        return available;
//        return Arrays.asList(super.getEntities()).stream().anyMatch(e -> e.getKey().equals(key));
    }

    @Override
    public void checkAvailabilityToInsert(Integer key, Object data) {
        boolean available = false;
        int continuousNullCounter = 0;
        for (int i = 0; i < getEntities().length; i++) {
            if (getEntities()[i] != null) {
                if ((Objects.equals(getEntities()[i].getKey(), key)) /*|| (Objects.equals(Integer.valueOf(getEntities()[i].getKey().toString()), Integer.valueOf(key.toString())))*/) {
                    available = true;
//                    System.out.println("available");
                    appendToKey(i, key, data);
                    break;
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
//        return Arrays.asList(super.getEntities()).stream().anyMatch(e -> e.getKey().equals(key));
    }

    public void appendToKey(int index, Object key, Object dataObject) {
        try {
            if (this.getEntities()[index] != null) {
                this.isDirty = true;
                ((LeafNodeEntity) this.getEntities()[index]).setValue(dataObject);
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
//            ex.printStackTrace();
        }
    }

    @Override
    public LeafNode searchNode(Integer key) {
        return this;
    }

    @Override
    public Object search(Integer key) {
//        log.info("key = " + key);
//        Integer keyInteger;
        try {
//            keyInteger = Integer.valueOf(key.toString());
//            log.info("key = " + keyDouble);
            for (NodeEntity e : super.getEntities()) {
                if (e != null) {
//                    log.info("key --- " + e.key);
                    if (e.getKey().equals(key)) {
                        return ((LeafNodeEntity) e).getValue();
                    }
                }

            }
        } catch (NumberFormatException ex) {
            LOG.exception(ex, "NumberFormatException {0}", ex.getMessage());
            for (NodeEntity e : super.getEntities()) {
                if (e != null) {
//                    log.info("key --- " + e.getKey());
                    if (Objects.equals(e.getKey(), key)){
                        return ((LeafNodeEntity) e).getValue();
                    }
                }

            }
        }

        return null;
    }

    @Override
    public void append(Integer key, Object dataObject) {
//        System.out.println("appending at leaf node");
        try {
            for (NodeEntity e : this.getEntities()) {
//                System.out.println("NODE ENTITY + + " + e.toString());
                if (/*(Objects.equals(e.getKey(), key)) ||*/e.getKey().equals(key)) {
//                    if ((Objects.equals(Integer.valueOf(e.getKey().toString()), Integer.valueOf(key.toString()))) || e.getKey().equals(key)) {
//                    System.out.println("found key ");
                    this.isDirty = true;
                    ((LeafNodeEntity) e).setValue(dataObject);
                    break;
                } else {
//                    System.out.println("not found");
                }
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
//            ex.printStackTrace();
        }
    }

    @Override
    public void insert(Integer key, Object dataObject) {
//        System.out.println("inserting");
        LeafNodeEntity entity = new LeafNodeEntity(key, tree);
        entity.setValue(dataObject);
        if (!isFull()) {
//            System.out.println("leaf node is not full");
            this.isDirty = true;
            if (addEntity(entity)) {
                sortEntities();
//                System.out.println(this.toString());
            }
        } else {
//            System.out.println("leaf node is full - splitting");
            split(entity);
        }
    }

    public void split(LeafNodeEntity entity) {
//        System.out.println("this - " + this.toString());
//        return;
        LeafNodeEntity[] tempList = new LeafNodeEntity[ConstantsOfTree.DEGREE];
        for (int i = 0; i < super.getEntityCount(); i++) {
            tempList[i] = (LeafNodeEntity) super.getEntities()[i];
        }
        tempList[tempList.length - 1] = entity;
//        Arrays.sort(tempList);
//        System.out.println("templist created ");
//        for (LeafNodeEntity e : tempList) {
//            System.out.print(e.getKey().toString() + ",");
//        }
//        System.out.println();

        if (getEntityCount() > 1) {
            List<NodeEntity> list = Arrays.asList(tempList);
            list.stream().filter((e) -> (e != null)).forEachOrdered((e) -> {
                e.setKey(e.getKey());
            });
            Collections.sort(list, new NodeEntityComparator());
            tempList = new LeafNodeEntity[ConstantsOfTree.DEGREE];
            tempList = (LeafNodeEntity[]) list.toArray();
        }

//        System.out.println("templist sorted ");
        int split_location = ConstantsOfTree.getSplitLocation();

        LeafNodeEntity[] tempLeftMap = new LeafNodeEntity[ConstantsOfTree.DEGREE - 1];
        LeafNodeEntity[] tempRightMap = new LeafNodeEntity[ConstantsOfTree.DEGREE - 1];

//        System.out.println("templist len " + tempList.length);
//        System.out.println("split_location " + split_location);
//        System.out.println("tempLeftMap len " + tempLeftMap.length);
//        System.out.println("tempRightMap len " + tempRightMap.length);
//        System.out.println("diff = " + (tempRightMap.length - split_location));
//        System.out.println("----------");
        System.arraycopy(tempList, 0, tempLeftMap, 0, split_location);
//        Object src,  int  srcPos,Object dest, int destPos,int length
        int counter=0;
        for (int i = split_location; i < tempList.length; i++) {
            tempRightMap[counter++]=tempList[i];
        }
//        if (ConstantsOfTree.DEGREE % 2 == 0) {
////            System.out.println("even");
//            System.arraycopy(tempList, split_location, tempRightMap, 0, (tempRightMap.length - split_location));
//        } else {
////             System.out.println("odd");
//            System.arraycopy(tempList, split_location, tempRightMap, 0, (tempRightMap.length));
//        }

        System.out.println("temp Left Map created ");
        for (LeafNodeEntity e : tempLeftMap) {
            if (e != null) {
                System.out.print(e.getKey() + ",");
            } else {
                System.out.print("null,");
            }
        }
        System.out.println();
        System.out.println("temp Right Map created ");
        for (LeafNodeEntity e : tempRightMap) {
            if (e != null) {
                System.out.print(e.getKey() + ",");
            } else {
                System.out.print("null,");
            }
        }
        System.out.println();
//        System.out.println("new right leaf node creqted");
        // create new LeafNode
        LeafNode rightLeafNode = new LeafNode(tree);
        rightLeafNode.addAllEntities(tempRightMap);
        rightLeafNode.isDirty = true;
//        System.out.println(" Right leaf node's entities ");
//        for (NodeEntity e : rightLeafNode.getEntities()) {
//            if (e != null) {
//                System.out.print(e.getKey() + ",");
//            } else {
//                System.out.print("null,");
//            }
//        }
//        System.out.println();
////        rightLeafNode.serialize();
//        System.out.println("right leaf node added to memory ");
//        System.out.println(rightLeafNode.toString());
        tree.getInMemoryNodes().addNode(rightLeafNode);
        //update this node's data
//        System.out.println("cleard this node's entites");
        this.clearEntities();
//        System.out.println("added left temp map to this node's entities");
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
//        System.out.println(this.toString());

        rightLeafNode.setNextLeafNode(this.nextLeafNode);
        if (this.nextLeafNode != (-1)) {
            ((LeafNode) tree.getInMemoryNodes().getNode(this.nextLeafNode)).setPreviousLeafNode(rightLeafNode.getNodeOffset());
//            ((LeafNode)NodeSerializer.deSerializeNode(this.nextLeafNode, ConstantsOfTree.NODE_FILE_PATH,tree)).setPreviousLeafNode(rightLeafNode.getNodeOffset());
        }
        rightLeafNode.setPreviousLeafNode(this.getNodeOffset());
        this.setNextLeafNode(rightLeafNode.getNodeOffset());
        LeafNodeEntity splittingEntity = (LeafNodeEntity) rightLeafNode.getEntities()[0];

        if (this.parent == (-1)) {
//            System.out.println("this has NO PARENT");
            Node parentNode = new InternalNode(tree);
            parentNode.isDirty = true;
//            long parentOffset = parentNode.serialize();
            this.setParent(parentNode.getNodeOffset());
            tree.getInMemoryNodes().addNode(parentNode);
        }
//        System.out.println("this's parent = " + this.parent.toString());
//        System.out.println/*log.info*/("splitting key = " + splittingEntity.getKey());
//
        try {
//            System.out.println("putting to parent");
            ((InternalNode) tree.getInMemoryNodes().getNode(parent)).put(splittingEntity.getKey(), this, rightLeafNode);
//            ((InternalNode) NodeSerializer.deSerializeNode(this.parent, ConstantsOfTree.NODE_FILE_PATH,tree)).put(splittingEntity.getKey(), this, rightLeafNode);
        } catch (IOException ex) {
            LOG.exception(ex, "IOException {0}", ex.getMessage());
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

}
