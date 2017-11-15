/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ustack.diskbplustree.tree;

import com.ustack.diskbplustree.diskoperations.MmapHandler;

/**
 *
 * @author AdminPC.
 */
public class NodeSerializer {

//    static Logger log = new Logger("test", new ConsoleLogWriter());
//    static String NODE_FILE_PATH = ConstantsOfTree.NODE_FILE_PATH;
//    static JsonSerializer jsonSerializer = new JsonSerializer(NODE_FILE_PATH);
//    static JsonConverter jsonConverter = new JsonConverter();
  static MmapHandler mmapHandler = new MmapHandler(1024 * 1024 * 1024);

//    public static long getSerializingOffset(Node node) {
//        long nodeOffset = node.getNodeOffset();
//        if (nodeOffset == (-1)) {
//            nodeOffset = jsonSerializer.getNodeFileLength();
//        }
//        return nodeOffset;
//    }
  /**
   *
   * @param node node.
   * @param tree tree
   */
  public static /*long*/ void initialSerialization(Node node, Tree tree) {
//        mmapHandler.setTree(tree);
    mmapHandler.serializeNode(node, tree);
//        String json = jsonConverter.getJSON(node);
//        long nodeOffset = jsonSerializer.getNodeFileLength();
//        node.setNodeOffset(nodeOffset);
//        jsonSerializer.serialize(json);
//        return nodeOffset;
  }

  /**
   *
   * @param node node.
   * @param tree tree
   */
  public static void serializeNode(Node node, Tree tree) {
//        System.out.println("is dirty ? " + node.isDirty);
//       log.info("serialize node is Dirty? {0}",node.isDirty);
    if (node.isDirty) {
//            mmapHandler.setTree(tree);
      mmapHandler.serializeNode(node, tree);

//            String json = jsonConverter.getJSON(node);
////        log.info("serialize node meth \n");
//            long nodeOffset = node.getNodeOffset();
////       log.info("cur ofst ="+nodeOffset);
//            if (nodeOffset == (-1)) {
//                nodeOffset = jsonSerializer.getNodeFileLength();
//                node.setNodeOffset(nodeOffset);
//                boolean isSerialized = jsonSerializer.serialize(json);
////            log.info("is serialized ? "+ isSerialized);
//            } else {
//
////            log.info("previously serlzd at "+nodeOffset);
//                serializeUpdatedNode(jsonSerializer, json, nodeOffset,tree);
//            }
    }
  }

  /**
   *
   * @author AdminPC.
   */
  public static void closeMemMap() {
    if (mmapHandler != null) {
      mmapHandler.close();
    }
  }

//    private static void serializeUpdatedNode(JsonSerializer jsonSerializer, String json, int nodeOffset, Tree tree) {
//        jsonSerializer.updateOffset(json, nodeOffset);
//    }
  
   /**
   *
   * @param nodeOffset nodeOffset.
   * @param tree tree
   * @return node
   */
  public static Node deSerializeNode(int nodeOffset, Tree tree) {
//        mmapHandler.setTree(tree);
//    System.out.println("desrializing node at " + nodeOffset);
    return mmapHandler.deSerializeNode(nodeOffset, tree);
//        String json = jsonSerializer.deSerialize(nodeOffset);
//        Node node = jsonConverter.getNode(json, tree);
//        node.isDirty = false;
////        if (node instanceof LeafNode) {
////            log.info("leaf node ");
////        }
//        tree.getInMemoryNodes().addNode(node,tree);
//
//        return node;
  }

}
