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

    public static long MEMORY_LIMIT_NODES = 5;
    public static String DB_PATH = "C:\\Users\\AdminPC\\Documents\\gavindya\\bpTree\\bpTree\\dbPath";
    public static int DEGREE = 5;
    public static String TREE_PATH = "C:\\Users\\AdminPC\\Documents\\gavindya\\bpTree\\bpTree\\dbPath\\tree.ser";
    public static String DB_FILE_PATH = "C:\\Users\\AdminPC\\Documents\\gavindya\\bpTree\\bpTree\\dbPath\\db.txt";
    public static String NODE_FILE_PATH = "C:\\Users\\AdminPC\\Documents\\gavindya\\bpTree\\bpTree\\dbPath\\nodes.txt";

    public static int getSplitLocation() {
        if (DEGREE % 2 == 0) {
            return DEGREE / 2;
        } else {
            return ((DEGREE - 1) / 2);
        }
    }

}
