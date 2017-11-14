/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ustack.diskbplustree.tree;

import com.ustack.spi.logger.ConsoleLogWriter;
import com.ustack.spi.logger.Logger;
import com.ustack.diskbplustree.helpers.NodeEntityComparator;
import java.io.Serializable;
import java.util.Arrays;

/**
 *
 * @author AdminPC
 */
public abstract class Node implements Serializable {

    private NodeEntity[] entities;
    private transient int visitCount;
    protected long parent;
    protected transient Tree tree;
    protected transient boolean isDirty;
    private long nodeOffset;
    protected transient static Logger log = new Logger("test", new ConsoleLogWriter());
    private final int position;

    public Node(Tree tree) {
        this.tree = tree;
        parent = (-1);
        nodeOffset = (-1);
        entities = new NodeEntity[ConstantsOfTree.DEGREE - 1];/* new NodeEntity[this.tree.getDegree() - 1];*/
        visitCount = 0;
        position = tree.getNodeCount() * tree.getNodeSize();
        nodeOffset = NodeSerializer.initialSerialization(this);
        isDirty = false;
        tree.increaseNodeCount();
//        this.tree.getInMemoryNodes().addNode(this);
    }

    public void setTree(Tree tree) {
        this.tree = tree;
    }

    public Node increaseVisitCount() {
        visitCount = visitCount + 1;
        return this;
    }

    public int getPosition() {
        return position;
    }

    public int getVisitCount() {
        return this.visitCount;
    }

    public void serialize() {
        log.info("serializing");
        NodeSerializer.serializeNode(this);
    }

    public boolean isFull() {
        return getEntityCount() >= (ConstantsOfTree.DEGREE - 1);
    }

    public long getEntityCount() {
        long count = 0;
        for (NodeEntity entity : entities) {
            if (entity != null) {
                count = count + 1;
            }
        }
        return count;
    }

    public void sortEntities() {

        Arrays.sort(entities, 0, (int) getEntityCount(), new NodeEntityComparator());
//        System.out.println(this.toString());
//        if (getEntityCount() > 1) {
//            List<NodeEntity> list = Arrays.asList(this.entities);
//            for (NodeEntity e : list) {
//                if (e != null) {
//                    e.setKey(Double.valueOf(e.getKey().toString()));
//                }
//            }
//            list = list.stream()
//                    .filter(entity -> Objects.nonNull(entity))
//                    .collect(Collectors.toList());
//
//            Collections.sort(list, new NodeEntityComparator());
//            this.entities = new NodeEntity[ConstantsOfTree.DEGREE - 1];
//            NodeEntity[] array = list.toArray(new NodeEntity[list.size()]);
//            if (entities.length == array.length) {
//                list.toArray(this.entities);
//            } else {
//                for (int i = 0; i < array.length; i++) {
//                    this.entities[i] = array[i];
//                }
//            }
//
//        }

    }

    public NodeEntity[] getEntities() {
        return this.entities;
    }

    public boolean addEntity(NodeEntity entity) {

        boolean isSet = false;
//        Integer key;
//        try {
//            Double d = (Double) entity.getKey();
//            key = d.intValue();
//            entity.setKey(key);
        for (int i = 0; i < entities.length; i++) {
            if (entities[i] == null) {
                entities[i] = entity;
                isSet = true;
                break;
            }
        }
        sortEntities();
        isDirty = true;
        return isSet;
//        } catch (ClassCastException e) {
//            System.out.println( entity.getKey().getClass());
//        }
//        return false;
    }

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

    public void clearEntities() {
        Arrays.fill(this.entities, null);
        isDirty = true;
    }

    public void addAllEntities(NodeEntity[] entitiesArray) {
        if (entitiesArray.length == (ConstantsOfTree.DEGREE - 1)) {
            this.entities = new LeafNodeEntity[ConstantsOfTree.DEGREE - 1];
            this.entities = entitiesArray;
            sortEntities();
            isDirty = true;
        } else {
            log.warn("ADD ALL WITH DIFFRENT SIZED ARRAY N NOT COMPATIBLE :( NOT ADDED");
        }
    }

    public long getNodeOffset() {
        return nodeOffset;
    }

    public void setNodeOffset(long offset) {
        this.nodeOffset = offset;
        isDirty = true;
    }

    /*
     *
     */
    public abstract LeafNode getRange(Object start, Object end);

    public abstract LeafNode getGreaterThan(Object start);

    public abstract LeafNode getLessThan(Object start);

    public abstract void printTree();

    public abstract void checkAvailabilityToInsert(Integer key, Object data);

    public abstract boolean isKeyAvailable(Integer key);

    public abstract LeafNode searchNode(Integer key);

    public abstract Object search(Integer key);

    public abstract void append(Integer key, Object dataObject);

    public abstract void insert(Integer key, Object dataObject);

//    public abstract void split(NodeEntity internalNodeEntity);
    public boolean isLeafNode() {
        return this instanceof LeafNode;
    }

    public void setParent(long parentOffset) {
        this.parent = parentOffset;
        isDirty = true;
    }

    public long getParent() {
        return this.parent;
    }

//    public long findParent() {
//        if (this.parent != -1) {
//            return ((InternalNode) this.parent).findParent();
//        } else {
//            return this;
//        }
//    }
    @Override
    public String toString() {
        String entitiesList = "";

        for (int i = 0; i < getEntityCount(); i++) {
            entitiesList = entitiesList + " [" + entities[i].getKey() + "] ";
        }
        return "Node{" + "entities=" + entitiesList + "\n, visitCount=" + visitCount + ", parent=" + parent + ", nodeOffset=" + nodeOffset + '}';
    }

}
