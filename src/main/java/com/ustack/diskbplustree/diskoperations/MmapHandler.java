/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ustack.diskbplustree.diskoperations;

import com.ustack.diskbplustree.tree.ConstantsOfTree;
import com.ustack.diskbplustree.tree.InternalNode;
import com.ustack.diskbplustree.tree.InternalNodeEntity;
import com.ustack.diskbplustree.tree.LeafNode;
import com.ustack.diskbplustree.tree.LeafNodeEntity;
import com.ustack.diskbplustree.tree.Node;
import com.ustack.diskbplustree.tree.Tree;
import java.io.File;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 *
 * @author Gavindya
 */
public class MmapHandler {

  private final File directoryName;
  private final Map<Integer, MmapFile> files = new ConcurrentHashMap<>();
  private MmapFile lastAccessFile = null;
  private final int maxFileSize;

  public MmapHandler(int maxSizeOfaFile) {
    this.directoryName = new File(ConstantsOfTree.DB_PATH);
    this.maxFileSize = maxSizeOfaFile;
    this.directoryName.mkdir();
    if (!directoryName.isDirectory()) {
      System.out.println("NOT A DIRECTORY");
      System.exit(0);
    }
  }

  public void close() {
    for (MmapFile file : files.values()) {
      file.close();
    }
    lastAccessFile = null;
    files.clear();
  }

  private MmapFile getFile(int fileIndex) {
    if (lastAccessFile != null && lastAccessFile.getFileIndex() == fileIndex) {
      return lastAccessFile;
    }
    lastAccessFile = files.computeIfAbsent(fileIndex,
            (fIndex) -> new MmapFile(maxFileSize, fIndex));/*this.directoryName.getAbsolutePath() + "/" + fIndex + ".txt",));*/
    return lastAccessFile;
  }

  public void putLong(long pos, long data) {
    int fileIndex = (int) (pos / (maxFileSize));
    int offset = (int) (pos - (fileIndex * maxFileSize));
    getFile(fileIndex).putLong(offset, data);
  }

  public long getLong(long pos) {
    int fileIndex = (int) (pos / (maxFileSize));
    int offset = (int) (pos - (fileIndex * maxFileSize));
    return getFile(fileIndex).getLong(offset);
  }

  public int getInt(long pos) {
    int fileIndex = (int) (pos / (maxFileSize));
    int offset = (int) (pos - (fileIndex * maxFileSize));
    return getFile(fileIndex).getInt(offset);
  }

  public void putInt(long pos, int value) {
    int fileIndex = (int) (pos / (maxFileSize));
    int offset = (int) (pos - (fileIndex * maxFileSize));
    getFile(fileIndex).putInt(offset, value);
  }

  public void serializeNode(Node node, Tree tree) {
    if (node instanceof LeafNode) {
//            System.out.println("leaf ");
      serializeLeafNode(node, tree);
    } else {
//            System.out.println("internal ");
      serializeInternalNode(node, tree);
    }

  }

  private void serializeLeafNode(Node node, Tree tree) {
    int leafNodeType = 1;
    int pos = node.getPosition();

//        memoryMappedByteBuffer.position(pos);
    if (node.getEntityCount() == 0) {
      return;
    }
    int nullCounter = 0;
    for (int i = 0; i < node.getEntityCount(); i++) {
      if (node.getEntities()[i] != null) {
        putLong(pos + (i * 8), node.getEntities()[i].getKey());
        putLong(pos + tree.getMainSegmentSize() + (i * 8), ((LeafNodeEntity) node.getEntities()[i]).getOffset());
      }
//            else {
//                nullCounter = nullCounter + 1;
//            }
//            if (nullCounter > 2) {
//                return;
//            }
    }

    putInt(pos + tree.getNodeTypeOffset(), leafNodeType);
    putInt(pos + tree.getPreviousNodeOffset(), ((LeafNode) node).getPreviousLeafNodeOffset());
    putInt(pos + tree.getNextNodeOffset(), ((LeafNode) node).getNextLeafNodeOffset());
    putInt(pos + tree.getParentOffset(), node.getParent());
    putInt(pos + tree.getPositionOffset(), node.getPosition());

  }

  private void serializeInternalNode(Node node, Tree tree) {
    int internalNodeType = 0;
    int pos = node.getPosition();
//        System.out.println("node pos = " + pos);
//        memoryMappedByteBuffer.position(pos);
    if (node.getEntityCount() == 0) {
      return;
    }
    int nullCounter = 0;
    for (int i = 0; i < node.getEntityCount(); i++) {
      if (node.getEntities()[i] != null) {
//                System.out.println("entity - " + node.getEntities()[i].toString());
        putLong((pos + (i * 8)), node.getEntities()[i].getKey());
//                System.out.println("key =" + (pos + (i * 8) + " : " + node.getEntities()[i].getKey()));
        putInt(pos + tree.getMainSegmentSize() + (i * 8), ((InternalNodeEntity) node.getEntities()[i]).getLeftNodeOffset());
//                System.out.println("left =" + (pos + tree.getMainSegmentSize() + (i * 8)) + " : " + ((InternalNodeEntity) node.getEntities()[i]).getLeftNodeOffset());
        putInt(pos + tree.getMainSegmentSize() + ((i + 1) * 8), ((InternalNodeEntity) node.getEntities()[i]).getRightNodeOffset());
//                System.out.println("right =" + (pos + tree.getMainSegmentSize() + ((i + 1) * 8)) + " : " + ((InternalNodeEntity) node.getEntities()[i]).getRightNodeOffset());

      } else {
//                System.out.println("entity - null");
//                nullCounter = nullCounter + 1;
      }
//            if (nullCounter > 2) {
//                return;
//            }
    }
    putInt(pos + tree.getNodeTypeOffset(), internalNodeType);
//        System.out.println("node type = internal = " + internalNodeType + " : pos =" + (pos + tree.getNodeTypeOffset()));
//        System.out.println("node type = internal = " + internalNodeType + " : pos =" + (pos + tree.getNodeTypeOffset()));

    putInt(pos + tree.getParentOffset(), node.getParent());
//        System.out.println("node parent = " + node.getParent() + " : pos =" + (pos + tree.getParentOffset()));

    putInt(pos + tree.getPositionOffset(), node.getPosition());
//        System.out.println("node poition =  " + node.getPosition() + " : pos =" + (pos + tree.getPositionOffset()));

  }

  public void updateNode() {

  }

  public Node deSerializeNode(int pos, Tree tree) {
//    System.out.println("position = " + pos);
    int position = pos + tree.getNodeTypeOffset();
    int type = getInt(position);
//    System.out.println("type of node read = " + type);
    if (type == 0) {
//      System.out.println("internal node");
//            return deSerializeInternalNode(int pos);

      InternalNodeEntity[] entities = new InternalNodeEntity[ConstantsOfTree.DEGREE - 1];
      InternalNodeEntity entity;
      for (int i = 0; i < entities.length; i++) {
//                System.out.println("key = " + getLong(pos + (i * 8)));
        entity = new InternalNodeEntity(getLong(pos + (i * 8)), tree);
        entity.setLeftNode(getInt(pos + tree.getMainSegmentSize() + (i * 8)));
//                System.out.println("left = " + getInt(pos + tree.getMainSegmentSize() + (i * 8)));
        entity.setRightNode(getInt(pos + tree.getMainSegmentSize() + ((i + 1) * 8)));
//                System.out.println("right = " + getInt(pos + tree.getMainSegmentSize() + ((i + 1) * 8)));
        entities[i] = entity;
      }

//      System.out.println("entities read from file");
//      for (int i = 0; i < entities.length; i++) {
//        System.out.print(entities[i] + ",");
//      }
//      System.out.println();
      int parent = getInt(pos + tree.getParentOffset());
//      System.out.println("parent = " + parent);
      int positionInFile = getInt(pos + tree.getPositionOffset());
//      System.out.println("pos in file = " + positionInFile);

      if (pos == positionInFile) {
        InternalNode node = new InternalNode(entities, parent, tree, position);
        return node;
      } else {
        System.out.println("wrong pos + " + pos + ": " + positionInFile);
      }

    } else {
//      System.out.println("leaf node");
//            return deSerializeLeafNode(int pos);
      LeafNodeEntity[] entities = new LeafNodeEntity[ConstantsOfTree.DEGREE - 1];
      LeafNodeEntity entity;
      for (int i = 0; i < entities.length; i++) {
        entity = new LeafNodeEntity(getLong(pos + (i * 8)), tree);
//                System.out.println("key = " + getLong(pos + (i * 8)));
        entity.setDataOffset(getLong(pos + tree.getMainSegmentSize() + (i * 8)));
//                System.out.println("offset of val  = " + getLong(pos + tree.getMainSegmentSize() + (i * 8)));
        entities[i] = entity;
      }

//      System.out.println("entities read from file");
//      for (int i = 0; i < entities.length; i++) {
//        System.out.print(entities[i] + ",");
//      }
//      System.out.println();
      int parent = getInt(pos + tree.getParentOffset());
//      System.out.println("parent = " + parent);
      int positionInFile = getInt(pos + tree.getPositionOffset());
//      System.out.println("positionInFile = " + positionInFile);
      int previousNode = getInt(pos + tree.getPreviousNodeOffset());
//      System.out.println("previousNode = " + previousNode);
      int nextNode = getInt(pos + tree.getNextNodeOffset());
//      System.out.println("nextNode = " + nextNode);

      if (pos == positionInFile) {
        LeafNode node = new LeafNode(entities, parent, tree, position, nextNode, previousNode);
        return node;
      } else {
//        putInt(pos + tree.getPositionOffset(),pos);
        System.out.println("wrong pos + " + pos + ": " + positionInFile);
      }

    }
    return null;
  }
//    public void putLongArray(long pos, long... data) {
//        int fileIndex = (int) (pos / (maxFileSize));
//        int offset = (int) (pos - (fileIndex * maxFileSize));
//        getFile(fileIndex).putLongArray(offset, data);
//    }
//    public short getShort(long pos) {
//        int fileIndex = (int) (pos / (maxFileSize));
//        int offset = (int) (pos - (fileIndex * maxFileSize));
//        return getFile(fileIndex).getShort(offset);
//    }
//    
//    public void putShort(long pos,short value) {
//        int fileIndex = (int) (pos / (maxFileSize));
//        int offset = (int) (pos - (fileIndex * maxFileSize));
//        getFile(fileIndex).putShort(offset,value);
//    }
//    public int getByte(long pos) {
//        int fileIndex = (int) (pos / (maxFileSize));
//        int offset = (int) (pos - (fileIndex * maxFileSize));
//        return getFile(fileIndex).getByte(offset);
//    }
//
//    public void putByte(long pos,byte b) {
//        int fileIndex = (int) (pos / (maxFileSize));
//        int offset = (int) (pos - (fileIndex * maxFileSize));
//        getFile(fileIndex).putByte(offset,b);
//    }
//    public void move(long pos, long newPos, int size) {
//        if(size == 0)
//            return;
//        int fileIndex1 = (int) (pos / (maxFileSize));
//        int offset1 = (int) (pos - (fileIndex1 * maxFileSize));
//
//        int fileIndex2 = (int) (newPos / (maxFileSize));
//        int offset2 = (int) (newPos - (fileIndex2 * maxFileSize));
//
//        if (fileIndex1 != fileIndex2) {
//            throw new UFileException("move only support same block file");
//        }
//
//        getFile(fileIndex1).move(offset1, offset2, size, buffer);
//    }  
}
