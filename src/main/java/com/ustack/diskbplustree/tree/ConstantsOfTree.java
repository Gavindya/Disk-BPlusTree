/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ustack.diskbplustree.tree;

/**
 *
 * @author AdminPC
 */
public class ConstantsOfTree {

  public static String DB_PATH = "C:\\Users\\AdminPC\\Documents\\gavindya\\bpTree\\db";
  public static int DEGREE = 254;
  public static String TREE_PATH = DB_PATH + "\\tree.ser";
  public static String DB_FILE_PATH = DB_PATH + "\\db.txt";
  public static String NODE_FILE_PATH = DB_PATH + "\\nodes.txt";

  public static int getSplitLocation() {
    if (DEGREE % 2 == 0) {
      return DEGREE / 2;
    } else {
      return ((DEGREE - 1) / 2);
    }
  }

}
