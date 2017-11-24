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
            (fIndex) -> new MmapFile(maxFileSize, fIndex));
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
      serializeLeafNode(node, tree);
    } else {
      serializeInternalNode(node, tree);
    }

  }

  private void serializeLeafNode(Node node, Tree tree) {
    int leafNodeType = 1;
    int pos = node.getPosition();

    if (node.getEntityCount() == 0) {
      return;
    }
    for (int i = 0; i < node.getEntityCount(); i++) {
      if (node.getEntities()[i] != null) {
        putLong(pos + (i * 8), node.getEntities()[i].getKey());
        putLong(pos + tree.getMainSegmentSize() + (i * 8), ((LeafNodeEntity) node.getEntities()[i]).getOffset());
      }
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
    if (node.getEntityCount() == 0) {
      return;
    }
    for (int i = 0; i < node.getEntityCount(); i++) {
      if (node.getEntities()[i] != null) {
        putLong((pos + (i * 8)), node.getEntities()[i].getKey());
        putInt(pos + tree.getMainSegmentSize() + (i * 8), ((InternalNodeEntity) node.getEntities()[i]).getLeftNodeOffset());
        putInt(pos + tree.getMainSegmentSize() + ((i + 1) * 8), ((InternalNodeEntity) node.getEntities()[i]).getRightNodeOffset());
      }
    }
    putInt(pos + tree.getNodeTypeOffset(), internalNodeType);
    putInt(pos + tree.getParentOffset(), node.getParent());
    putInt(pos + tree.getPositionOffset(), node.getPosition());
  }

  public Node deSerializeNode(int pos, Tree tree) {
    int position = pos + tree.getNodeTypeOffset();
    int type = getInt(position);
    if (type == 0) {
      InternalNodeEntity[] entities = new InternalNodeEntity[ConstantsOfTree.DEGREE - 1];
      InternalNodeEntity entity;
      for (int i = 0; i < entities.length; i++) {
        entity = new InternalNodeEntity(getLong(pos + (i * 8)), tree);
        entity.setLeftNode(getInt(pos + tree.getMainSegmentSize() + (i * 8)));
        entity.setRightNode(getInt(pos + tree.getMainSegmentSize() + ((i + 1) * 8)));
        entities[i] = entity;
      }

      int parent = getInt(pos + tree.getParentOffset());
      int positionInFile = getInt(pos + tree.getPositionOffset());
      if (pos == positionInFile) {
        InternalNode node = new InternalNode(entities, parent, tree, position);
        return node;
      } else {
        System.out.println("wrong pos + " + pos + ": " + positionInFile);
      }

    } else {
      LeafNodeEntity[] entities = new LeafNodeEntity[ConstantsOfTree.DEGREE - 1];
      LeafNodeEntity entity;
      for (int i = 0; i < entities.length; i++) {
        entity = new LeafNodeEntity(getLong(pos + (i * 8)), tree);
        entity.setDataOffset(getLong(pos + tree.getMainSegmentSize() + (i * 8)));
        entities[i] = entity;
      }

      int parent = getInt(pos + tree.getParentOffset());
      int positionInFile = getInt(pos + tree.getPositionOffset());
      int previousNode = getInt(pos + tree.getPreviousNodeOffset());
      int nextNode = getInt(pos + tree.getNextNodeOffset());

      if (pos == positionInFile) {
        LeafNode node = new LeafNode(entities, parent, tree, position, nextNode, previousNode);
        return node;
      } else {
        System.out.println("wrong pos + " + pos + ": " + positionInFile);
      }

    }
    return null;
  }
}
