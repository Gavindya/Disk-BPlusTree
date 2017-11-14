/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ustack.diskbplustree.tree;

import com.ustack.spi.logger.ConsoleLogWriter;
import com.ustack.spi.logger.Logger;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author AdminPC
 */
public class InMemoryNodes implements Serializable {

    private final Map<Long, Node> nodes;
    private final Tree tree;
    private final Logger log = new Logger("test", new ConsoleLogWriter());

    public InMemoryNodes(Tree tree) {
        nodes = new HashMap<>();
        this.tree = tree;
    }

    public void commitStagedNode(Long removing) {
//        System.out.println("serializing the node = " + removing);
        nodes.get(removing).serialize();
    }
//    public void commitStagedNodes(ArrayList<Long> removing) {
//        removing.forEach((ofst) -> {
//            nodes.get(ofst).serialize();
//        });
//    }

    public void commit() {
        nodes.entrySet().forEach((e) -> {
//            log.info("-------------------");
//            log.info(e.getValue().toString());
            e.getValue().serialize();
        });
    }

    public Node addNode(Node node) {
        if (nodes.containsKey(node.getNodeOffset())) {
//            System.out.println("nodes contains " + nodes.get(node.getNodeOffset()).toString());
            nodes.replace(node.getNodeOffset(), node);
        } else if (nodes.size() < ConstantsOfTree.MEMORY_LIMIT_NODES) {
//            System.out.println("nodes does not contain " + node.toString());
            nodes.put(node.getNodeOffset(), node);
        } else {
//            System.out.println("nodes does not contain " + node.toString() + " & insufficient memory");
            if (removeNodes(computeRemoveNode())) {
                nodes.put(node.getNodeOffset(), node);
            }
        }
        return node;
    }

    public Node addNode(long offset) {
        Node node = loadNode(offset);
        if (node != null) {
            if (node.isLeafNode()) {
                //
            }
            nodes.put(offset, node);
        }
        return node;
    }

    public boolean removeNode(long offset) {
//        System.out.println("remove node offset = " + offset);
        if (isNodeLoaded(offset)) {
//            System.out.println("node is in memory");
            commitStagedNode(offset);
            nodes.remove(offset);
            return true;
        }
        return false;
    }

    public boolean isNodeLoaded(long offset) {
//        System.out.println("offset " + offset + " isLoaded? " + nodes.containsKey(offset));
        return nodes.containsKey(offset);
    }

    public Node getNode(long offset) {
//        System.out.println("getNode at offset " + offset);
        if (isNodeLoaded(offset)) {
            return nodes.get(offset).increaseVisitCount();
        } else {
//            System.out.println("node aint in memory");
            if (nodes.size() < ConstantsOfTree.MEMORY_LIMIT_NODES) {
//                System.out.println("adding node to mem as space is available");
                return addNode(offset);
            } else {
                /*if (*/
//                System.out.println("no space to load the node.");
                removeNodes(computeRemoveNode())/*) {*/;
//                System.out.println("remove");
                return addNode(offset);
//                }
//                return null;
            }
        }
    }

    public Node loadNode(long offset) {
//        System.out.println("desrializing node " + offset);
        return NodeSerializer.deSerializeNode(offset,tree);
    }

    private long getSmallestVisitCount() {
        int smallestVisitCount = Integer.MAX_VALUE;
        for (Map.Entry<Long, Node> n : nodes.entrySet()) {
            if (n.getValue().getVisitCount() < smallestVisitCount) {
                smallestVisitCount = n.getValue().getVisitCount();
            }
        }
        return smallestVisitCount;
    }

    private long getSmallestVisitCount(long lowerBound) {
        int smallestVisitCount = Integer.MAX_VALUE;
        for (Map.Entry<Long, Node> n : nodes.entrySet()) {
            if ((n.getValue().getVisitCount() < smallestVisitCount) && (smallestVisitCount > lowerBound)) {
                smallestVisitCount = n.getValue().getVisitCount();
            }
        }
        return smallestVisitCount;
    }

    public ArrayList<Long> computeRemoveNode() {
//        System.out.println(" computing nodes to be removed ");
        ArrayList<Long> removing = new ArrayList<>();

//        System.out.println("found smallest vsit count = " + getSmallestVisitCount());
        for (Map.Entry<Long, Node> n : nodes.entrySet()) {
            if ((n.getValue().getVisitCount() == getSmallestVisitCount()) && (n.getValue().getParent() != -1)) {
                removing.add(n.getValue().getNodeOffset());
            }
        }

        if (removing.isEmpty()) {
            for (Map.Entry<Long, Node> n : nodes.entrySet()) {
                if ((n.getValue().getVisitCount() == getSmallestVisitCount(getSmallestVisitCount())) && (n.getValue().getParent() != -1)) {
                    removing.add(n.getValue().getNodeOffset());
                }
            }
        }

        if (removing.isEmpty()) {
            for (Map.Entry<Long, Node> n : nodes.entrySet()) {
                if (n.getValue().getParent() != -1) {
                    removing.add(n.getValue().getNodeOffset());
                    break;
                }
            }
        }

//        System.out.println("nodes to be removed ");
        removing.forEach(n -> {
//            System.out.println(nodes.get(n).toString());
        });
//        System.out.println("\n");
        return removing;
    }

    public void removeAllNodes() {
//        System.out.println("cleared all nodes");
        nodes.clear();
    }

    public boolean removeNodes(ArrayList<Long> removing) {
//        System.out.println("remove nodes method invoked ");
        boolean removed = false;
//        commitStagedNodes(removing);
        if (!removing.isEmpty()) {
            for (int i = 0; i < removing.size(); i++) {
                if (removeNode(removing.get(i)) && !removed) {
                    removed = true;
                }
            }
        }
        return removed;
    }

    public int getNumberOfNodesInMemory() {
        return nodes.size();
    }

    public void printNodesInMemory() {
        nodes.entrySet().forEach((e) -> {
            System.out.println("key = " + e.getKey() + " : value = " + e.getValue().toString()+" : DIRTY ? "+e.getValue().isDirty);
        });
    }

    public void setTree(Tree tree) {
        nodes.entrySet().forEach((e) -> {
            e.getValue().tree = tree;
        });

//        nodes.entrySet().forEach((e) -> {
//            e.getValue().tree.getInMemoryNodes().printNodesInMemory();
//        });
    }
}
