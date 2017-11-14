/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ustack.diskbplustree.tree;

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
            try (FileOutputStream fileOut = new FileOutputStream(ConstantsOfTree.TREE_PATH); ObjectOutputStream out = new ObjectOutputStream(fileOut)) {
                out.writeObject(tree);
            }
//            System.out.printf("Serialized data is saved in /tmp/employee.ser");
//            System.out.printf("Serialized data is saved in /tmp/employee.ser");
                    } catch (IOException ex) {
            System.out.println(ex.getMessage());
//            ex.printStackTrace();
        }

    }

}
