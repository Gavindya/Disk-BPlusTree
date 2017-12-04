/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ustack.diskbplustree.tree;

//import com.ustack.spi.logger.ConsoleLogWriter;
//import com.ustack.spi.logger.Logger;
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

    System.out.println("MAIN");
    Tree tree = new Tree(1024 * 4);
//        tree.insert(1, 1l);
    tree.insert(1l, 1l);
////        tree.printTree();
//////        System.out.println("\n ------------");
//        tree.insert(6l, 6l);
////        tree.printTree();
//////        System.out.println("\n ------------");
//        tree.insert(4l, 4l);
////        tree.printTree();
////
//////        System.out.println("\n ------------");
//        tree.insert(8l, 8l);
//        tree.insert(1l, 1l);
////        System.out.println("\n\n\n\n");
////        tree.getInMemoryNodes().printNodesInMemory();
//        tree.insert(5l, 5l);
////        System.out.println("\n\n\n\n");
////        tree.getInMemoryNodes().printNodesInMemory();
//        tree.insert(20l, 20l);
////        tree.getInMemoryNodes().printNodesInMemory();
////                tree.commitTree();
//        tree.insert(15l, 15l);
//        tree.insert(9l, 9l);
//////       
//        tree.insert(50l, 50l);
//        tree.insert(25l, 25l);
//
//////
//        tree.insert(80l, 8l);
//////        tree.printTree();
//        tree.insert(104l, 1l);
//
////        tree.printTree();
////        System.out.println("\n\n-----------------------------\n\n");
////        tree.getInMemoryNodes().printNodesInMemory();
////        System.out.println("\n\n-----------------------------\n\n");
////        System.out.println/*log.info*/("\n commiting tree \n");
////        tree.commitTree();
////        tree.getInMemoryNodes().printNodesInMemory();
//////        tree.printTree();
//        tree.insert(550l, 5l);
//////        tree.printTree();
//        tree.insert(200l, 20l);
//////        tree.printTree();
//        tree.insert(150l, 15l);
//////        tree.printTree();
//        tree.insert(90l, 9l);
//////        tree.printTree();
//        tree.insert(500l, 50l);
//////        tree.printTree();
//        tree.insert(250l, 25l);
//        
////        tree.insert(95, 25l);
////        tree.insert(98, 25l);
////        tree.insert(100, 25l);
//        
//        
//////        tree.printTree();
////        tree.insert(10, 7845);
    tree.getInMemoryNodes().printNodesInMemory();
    tree.commitTree();
//
//        System.out.println("\n ------------");
//        tree.printTree();
  }
}
