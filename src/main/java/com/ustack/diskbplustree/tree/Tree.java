/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ustack.diskbplustree.tree;

import com.ustack.spi.logger.ConsoleLogWriter;
import java.io.Serializable;
import com.ustack.spi.logger.Logger;

/**
 *
 * @author AdminPC
 */
public class Tree implements Serializable {

    private int allKeysSize;
    private int mainSegmentSize;
    private int keyValueSegementSize;
    private int nodeTypeOffset;
    private int previousNodeOffset;
    private int nextNodeOffset;
    private int parentOffset;
    private int positionOffset;

    private final int nodeSize;
    private int nodeCount;
    private int keyCount;
    private final InMemoryNodes inMemoryNodes;
    public Node root;
    transient Logger log = new Logger("test", new ConsoleLogWriter());

    public Tree(int nodesize) {
        nodeCount = 0;
        nodeSize = nodesize;
        root = new LeafNode(this);
//        root.serialize();
        inMemoryNodes = new InMemoryNodes(this);
        inMemoryNodes.addNode(root);

        calculateSizes(nodeSize);
    }

    private void calculateSizes(int nodeSize) {
        if ((nodeSize % 8) != 0) {
            System.out.println("node Size is inapropriate");
            System.exit(0);
            return;
        }
        int slots = nodeSize / 8;
        if (slots < 12) {
            System.out.println("node Size is inapropriate");
            System.exit(0);
            return;
//            throw new BTreeException("node size is less than miniumum size");
        }
        int usableSlots = slots - 5;
        if (usableSlots % 2 != 1) {
            System.out.println("node Size is inapropriate");
            System.exit(0);
            return;
//            throw new BTreeException("invalid node size");
        }

        keyCount = usableSlots / 2;
        allKeysSize = keyCount * 8;
        mainSegmentSize = allKeysSize + 8;
        keyValueSegementSize = mainSegmentSize + ((keyCount + 1) * 8);
        nodeTypeOffset = allKeysSize + 2;
//        leftSiblingOffset = keyValueSegementSize + 8;
//        rightSibliingOffset = keyValueSegementSize + 16;
    }

    public int getNodeSize() {
        return nodeSize;
    }

    public int getTotalKeysSize() {
        return mainSegmentSize;
    }

    public void increaseNodeCount() {
        nodeCount = nodeCount + 1;
    }

    public int getNodeCount() {
        return nodeCount;
    }

    public InMemoryNodes getInMemoryNodes() {
//        log.info("ïnmem " + inMemoryNodes);
        return inMemoryNodes;
    }

    public void setTree() {
        inMemoryNodes.setTree(this);
    }

    public void setRoot(Node rootNode) {
        this.root = rootNode;
    }

    public Node getRoot() {
        return root;
    }
//    public Tree(int degree, String dbPath,long memoryNodes) {
//        this.degree = degree;
//        root = new LeafNode(this);
//        this.memoryNodes=memoryNodes;
////        counter=1;
//        this.dbPath = dbPath;
//        this.inMemoryNodes = new InMemoryNodes(dbPath + "\\nodes.txt");
////        File db = new File(this.dbPath);
////        db.mkdir();
////        File file = new File(this.dbPath + "\\db.txt");
////        if (!file.exists()) {
////            try {
////                file.createNewFile();
////            } catch (IOException ex) {
////                log.exception(ex, "IOException {0}",ex.getMessage());
////            }
////        }
//    }

//    public String getDbPath() {
//        return this.dbPath;
//    }
//    public long getMemoryNodesLimit(){
//        return memoryNodes;
//    }
//    public int getDegree(){
//        return degree;
//    }
//    public String getNodeFilePath() {
//        return this.dbPath + "\\nodes.txt";
//    }
//    
//    public String getDbFilePath() {
//        return this.dbPath + "\\db.txt";
//    }
    public void unloadTree() {
//        root.unloadDataOfChildren();
    }

    public void insert(Integer key, Object data) {

        root.checkAvailabilityToInsert(key, data);
//        if (root.isKeyAvailable(key,data)) {
////            System.out.println("KEY AVAILABLE ");
//            root.append(key, data);
//        } else {
//            root.insert(key, data);
//        }

    }

    public void printTree() {
        System.out.println(root.toString());
    }

    public void search(Integer /*originalKey*/ key) {
//        int key = originalKey.hashCode();
//        System.out.println("searching = " + key);
        Object found = root.search(key);
        if (found != null) {
            System.out.println("FOUND for " + String.valueOf(key)/*key.toString()*/ + " = " + found.toString());
//            log.info("FOUND "+ found.toString());
        } else {
            System.out.println("NOT FOUND for " + String.valueOf(key)/*key.toString()*/ + "  not found ");
        }
    }

    public void commitTree() {
        this.inMemoryNodes.commit();
        this.inMemoryNodes.removeAllNodes();
        this.inMemoryNodes.addNode(root);
        TreeSerialization.serializeTree(this);
    }

//    public DataIterator getRange(Integer start, Integer end) {
//        return null;
//    }
//
//    public DataIterator getGreaterThan(Object /*originalStart*/ start) {
////        int start = originalStart.hashCode();
//        newpackage.LeafNode ln = root.getGreaterThan(start);
//        if (ln != null) {
//            return new DataIterator(ln);
//        } else {
//            return null;
//        }
//    }
//
//    public DataIterator getLessThan(Object /*originalStart*/ start) {
////         int start = originalStart.hashCode();
//        newpackage.LeafNode ln = root.getLessThan(start);
//        if (ln != null) {
//            return new DataIterator(ln);
//        } else {
//            return null;
//        }
//    }
//    public int getSplitLocation() {
//        if (degree % 2 == 0) {
//            return degree / 2;
//        } else {
//            return ((degree - 1) / 2);
//        }
//    }
//    public void save() {
//        try {
//            String filePath = dbPath + "\\database.ser";
//            FileOutputStream fileOutputStream = new FileOutputStream(filePath);
//            try (ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream)) {
//                objectOutputStream.writeObject(this);
//            }
//        } catch (Exception ex) {
//            log.exception(ex, "Exception {0}", ex.getMessage());
//        }
//    }
}
