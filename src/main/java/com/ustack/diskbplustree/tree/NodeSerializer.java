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

  private static final MmapHandler MMAP_HANDLER = new MmapHandler(1024 * 1024 * 1024);

  /**
   *
   * @param node node.
   * @param tree tree
   */
  public static void initialSerialization(Node node, Tree tree) {
    MMAP_HANDLER.serializeNode(node, tree);
  }

  /**
   *
   * @param node node.
   * @param tree tree
   * @return isNodeSerialized
   */
  public static boolean serializeNode(Node node, Tree tree) {
    if (node.isDirty) {
      return MMAP_HANDLER.serializeNode(node, tree);
    }
    return false;
  }

  /**
   *
   * @author AdminPC.
   */
  public static void closeMemMap() {
    if (MMAP_HANDLER != null) {
      MMAP_HANDLER.close();
    }
  }

  /**
   *
   * @param nodeOffset nodeOffset.
   * @param tree tree
   * @return node
   */
  public static Node deSerializeNode(int nodeOffset, Tree tree) {
    return MMAP_HANDLER.deSerializeNode(nodeOffset, tree);
  }

}
