/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ustack.diskbplustree.tree;

import com.ustack.spi.logger.ConsoleLogWriter;
import com.ustack.spi.logger.Logger;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author AdminPC
 */
public class InMemoryNodes implements Serializable {

  private final Map<Integer, Node> nodes;
  private Tree tree;
  private transient final Logger log = new Logger("test", new ConsoleLogWriter());

  public InMemoryNodes(Tree tree) {
    nodes = new HashMap<>();
    this.tree = tree;
  }

  public void closeMemMap() {
    NodeSerializer.closeMemMap();
  }

  public void commitStagedNode(Integer removing, Tree tree) {
//        nodes.get(removing).serialize(tree);
  }

  public void commit(Tree tree) {
    nodes.entrySet().forEach((e) -> {
//            System.out.println("--------\n"+e.getValue().toString()+"\n----");
//            log.info("-------------------");
//            log.info(e.getValue().toString());
      e.getValue().serialize(tree);
    });
    tree.closeMemMap();
  }

  public Node addNode(Node node, Tree tree) {
    if (nodes.containsKey(node.getPosition())) {
//            System.out.println("nodes contains " + nodes.get(node.getNodeOffset()).toString());
      nodes.replace(node.getPosition(), node);
    } //        else if (nodes.size() < ConstantsOfTree.MEMORY_LIMIT_NODES) {
    ////            System.out.println("nodes does not contain " + node.toString());
    //            nodes.put(node.getPosition(), node);
    //        } 
    else {
//            System.out.println("nodes does not contain " + node.toString() + " & insufficient memory");
//            if (removeNodes(computeRemoveNode(),tree)) {
      nodes.put(node.getPosition(), node);
//            }
    }
    return node;
  }

  public Node addNode(int offset) {
    Node node = loadNode(offset);
    if (node != null) {
//            if (node.isLeafNode()) {
//                //
//            }
      nodes.put(offset, node);
    } else {
      System.out.println("loaded null node");
    }
    return node;
  }

//    public boolean removeNode(int offset,Tree tree) {
////        System.out.println("remove node offset = " + offset);
//        if (isNodeLoaded(offset)) {
////            System.out.println("node is in memory");
//            commitStagedNode(offset,tree);
//            nodes.remove(offset);
//            return true;
//        }
//        return false;
//    }
  public boolean isNodeLoaded(int offset) {
//        System.out.println("offset " + offset + " isLoaded? " + nodes.containsKey(offset));
    return nodes.containsKey(offset);
  }

  public Node getNode(int offset, Tree tree) {
//        System.out.println("getNode at offset " + offset);
    if (isNodeLoaded(offset)) {
      return nodes.get(offset).increaseVisitCount();
    } else {
////            System.out.println("node aint in memory");
//            if (nodes.size() < ConstantsOfTree.MEMORY_LIMIT_NODES) {
////                System.out.println("adding node to mem as space is available");
//                return addNode(offset);
//            } else {
//                /*if (*/
////                System.out.println("no space to load the node.");
//                removeNodes(computeRemoveNode(),tree)/*) {*/;
////                System.out.println("remove");
      return addNode(offset);
////               }
////                return null;
////            }
    }
  }

  public Node loadNode(int offset) {
//        System.out.println("desrializing node at" + offset);
    return NodeSerializer.deSerializeNode(offset, tree);
  }

//    private int getSmallestVisitCount() {
//        int smallestVisitCount = Integer.MAX_VALUE;
//        for (Map.Entry<Integer, Node> n : nodes.entrySet()) {
//            if (n.getValue().getVisitCount() < smallestVisitCount) {
//                smallestVisitCount = n.getValue().getVisitCount();
//            }
//        }
//        return smallestVisitCount;
//    }
//
//    private int getSmallestVisitCount(int lowerBound) {
//        int smallestVisitCount = Integer.MAX_VALUE;
//        for (Map.Entry<Integer, Node> n : nodes.entrySet()) {
//            if ((n.getValue().getVisitCount() < smallestVisitCount) && (smallestVisitCount > lowerBound)) {
//                smallestVisitCount = n.getValue().getVisitCount();
//            }
//        }
//        return smallestVisitCount;
//    }
//    public ArrayList<Integer> computeRemoveNode() {
////        System.out.println(" computing nodes to be removed ");
//        ArrayList<Integer> removing = new ArrayList<>();
//
////        System.out.println("found smallest vsit count = " + getSmallestVisitCount());
//        for (Map.Entry<Integer, Node> n : nodes.entrySet()) {
//            if ((n.getValue().getVisitCount() == getSmallestVisitCount()) && (n.getValue().getParent() != -1)) {
//                removing.add(n.getValue().getPosition());
//            }
//        }
//
//        if (removing.isEmpty()) {
//            for (Map.Entry<Integer, Node> n : nodes.entrySet()) {
//                if ((n.getValue().getVisitCount() == getSmallestVisitCount(getSmallestVisitCount())) && (n.getValue().getParent() != -1)) {
//                    removing.add(n.getValue().getPosition());
//                }
//            }
//        }
//
//        if (removing.isEmpty()) {
//            for (Map.Entry<Integer, Node> n : nodes.entrySet()) {
//                if (n.getValue().getParent() != -1) {
//                    removing.add(n.getValue().getPosition());
//                    break;
//                }
//            }
//        }
//
////        System.out.println("nodes to be removed ");
//        removing.forEach(n -> {
////            System.out.println(nodes.get(n).toString());
//        });
////        System.out.println("\n");
//        return removing;
//    }
  public void removeAllNodes() {
//        System.out.println("cleared all nodes");
    nodes.clear();
  }

//    public boolean removeNodes(ArrayList<Integer> removing,Tree tree) {
////        System.out.println("remove nodes method invoked ");
//        boolean removed = false;
////        commitStagedNodes(removing);
//        if (!removing.isEmpty()) {
//            for (int i = 0; i < removing.size(); i++) {
//                if (removeNode(removing.get(i),tree) && !removed) {
//                    removed = true;
//                }
//            }
//        }
//        return removed;
//    }
  public int getNumberOfNodesInMemory() {
    return nodes.size();
  }

  public void printNodesInMemory() {
    nodes.entrySet().forEach((e) -> {
      System.out.println("key = " + e.getKey() + " : value = " + e.getValue().toString() + " : DIRTY ? " + e.getValue().isDirty);
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
