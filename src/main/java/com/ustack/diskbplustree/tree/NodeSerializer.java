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

  static MmapHandler mmapHandler = new MmapHandler(1024 * 1024 * 1024);

  /**
   *
   * @param node node.
   * @param tree tree
   */
  public static void initialSerialization(Node node, Tree tree) {
    mmapHandler.serializeNode(node, tree);
  }

  /**
   *
   * @param node node.
   * @param tree tree
   */
  public static void serializeNode(Node node, Tree tree) {
    if (node.isDirty) {
      mmapHandler.serializeNode(node, tree);
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

  /**
   *
   * @param nodeOffset nodeOffset.
   * @param tree tree
   * @return node
   */
  public static Node deSerializeNode(int nodeOffset, Tree tree) {
    return mmapHandler.deSerializeNode(nodeOffset, tree);
  }

}
