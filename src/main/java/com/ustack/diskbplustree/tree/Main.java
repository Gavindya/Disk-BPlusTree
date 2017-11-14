/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ustack.diskbplustree.tree;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;

/**
 *
 * @author AdminPC
 */
public class Main {

//    protected transient static Logger log = new Logger("test", new ConsoleLogWriter());
//https://stackoverflow.com/questions/1085812/how-to-override-objectoutputstream-writestreamheader
    public static ObjectOutputStream newInstance(final int myData, final OutputStream out) throws IOException {
        return new ObjectOutputStream(out) {
            @Override
            protected void writeStreamHeader() throws IOException {
                write(myData);
                super.writeStreamHeader();
            }
        };
    }

    public static void main(String[] args) {

        Tree tree = new Tree(1024*4);
//        tree.insert(1, 1l);
        tree.insert(10, 1l);
//        tree.printTree();
////        System.out.println("\n ------------");
        tree.insert(6, 6l);
//        tree.printTree();
////        System.out.println("\n ------------");
        tree.insert(4, 4l);
//        tree.printTree();
//
////        System.out.println("\n ------------");
        tree.insert(8, 8l);
        tree.insert(1, 1l);
//        System.out.println("\n\n\n\n");
//        tree.getInMemoryNodes().printNodesInMemory();
        tree.insert(5, 5l);
//        System.out.println("\n\n\n\n");
//        tree.getInMemoryNodes().printNodesInMemory();
        tree.insert(20, 20l);
//        tree.getInMemoryNodes().printNodesInMemory();
//                tree.commitTree();
        tree.insert(15, 15l);
        tree.insert(9, 9l);
////       
        tree.insert(50, 50l);
        tree.insert(25, 25l);

////
        tree.insert(80, 8l);
////        tree.printTree();
        tree.insert(104, 1l);

//        tree.printTree();
//        System.out.println("\n\n-----------------------------\n\n");
//        tree.getInMemoryNodes().printNodesInMemory();
//        System.out.println("\n\n-----------------------------\n\n");
//        System.out.println/*log.info*/("\n commiting tree \n");
//        tree.commitTree();
//        tree.getInMemoryNodes().printNodesInMemory();
////        tree.printTree();
        tree.insert(550, 5l);
////        tree.printTree();
        tree.insert(200, 20l);
////        tree.printTree();
        tree.insert(150, 15l);
////        tree.printTree();
        tree.insert(90, 9l);
////        tree.printTree();
        tree.insert(500, 50l);
////        tree.printTree();
        tree.insert(250, 25l);
        
//        tree.insert(95, 25l);
//        tree.insert(98, 25l);
//        tree.insert(100, 25l);
        
        
////        tree.printTree();
//        tree.insert(10, 7845);
        tree.getInMemoryNodes().printNodesInMemory();
        tree.commitTree();
//
//        System.out.println("\n ------------");
//        tree.printTree();
    }
}
