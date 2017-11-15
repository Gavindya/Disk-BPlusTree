/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ustack.diskbplustree.diskoperations;

import com.ustack.diskbplustree.tree.ConstantsOfTree;
import com.ustack.diskbplustree.tree.Tree;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

/**
 *
 * @author Yasith Jayawardana
 */
public class TreeSerialization {

  public static void serializeTree(Tree tree) {
    try {
      try (FileOutputStream fileOut = new FileOutputStream(ConstantsOfTree.TREE_PATH)) {
        ObjectOutputStream out = new ObjectOutputStream(fileOut);
        out.writeObject(tree);
        out.close();
      }
//            System.out.printf("Serialized data is saved in /tmp/employee.ser");
    } catch (IOException ex) {
      ex.printStackTrace();
    }

  }

}
