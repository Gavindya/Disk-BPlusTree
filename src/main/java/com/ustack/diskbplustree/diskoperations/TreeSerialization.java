/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ustack.diskbplustree.diskoperations;

import com.ustack.diskbplustree.tree.ConstantsOfTree;
import com.ustack.diskbplustree.tree.Tree;
import com.ustack.spi.logger.ConsoleLogWriter;
import com.ustack.spi.logger.Logger;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

/**
 *
 * @author Gavindya
 */
public class TreeSerialization {

  protected transient static Logger log = new Logger("test", new ConsoleLogWriter());
  
  public static void serializeTree(Tree tree) {
    try {
      try (FileOutputStream fileOut = new FileOutputStream(ConstantsOfTree.TREE_PATH); ObjectOutputStream out = new ObjectOutputStream(fileOut)) {
        out.writeObject(tree);
      }
    } catch (IOException ex) {
      log.exception(ex, "IOException {0}", ex.getMessage());
    }

  }

}
