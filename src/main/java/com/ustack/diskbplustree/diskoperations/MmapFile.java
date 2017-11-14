/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ustack.diskbplustree.diskoperations;

import com.ustack.diskbplustree.tree.InternalNodeEntity;
import com.ustack.diskbplustree.tree.LeafNode;
import com.ustack.diskbplustree.tree.LeafNodeEntity;
import com.ustack.diskbplustree.tree.Node;
import com.ustack.diskbplustree.tree.Tree;
import com.ustack.spi.logger.ConsoleLogWriter;
import com.ustack.spi.logger.Logger;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

/**
 *
 * @author AdminPC
 */
public class MmapFile {

    private Tree tree;
    private RandomAccessFile randomAccessFile;
    private MappedByteBuffer memoryMappedByteBuffer;
    protected transient static Logger log = new Logger("test", new ConsoleLogWriter());

    public MmapFile(String filename, long filesize, Tree tree) {
        try {
            this.tree = tree;
            randomAccessFile = new RandomAccessFile(filename, "rw");
            if (randomAccessFile.length() == 0) {
                randomAccessFile.setLength(filesize);
            }
            //memMap of file size
            memoryMappedByteBuffer = randomAccessFile.getChannel().map(FileChannel.MapMode.READ_WRITE, 0, filesize);
        } catch (IOException ex) {
            log.exception(ex, " IOException {0} ", ex.getMessage());
        }
    }

    public void putLong(int pos, long data) {
        memoryMappedByteBuffer.putLong(pos, data);
    }

    public void getLong(int pos) {
        memoryMappedByteBuffer.getLong(pos);
    }

    public void serializeNode(Node node) {

        if (node instanceof LeafNode) {
            serializeLeafNode(node);
        } else {
            serializeInternalNode(node);
        }

    }

    private void serializeLeafNode(Node node) {
        int pos = node.getPosition();
//        memoryMappedByteBuffer.position(pos);
        if (node.getEntityCount() == 0) {
            return;
        }
        int nullCounter = 0;
        for (int i = 0; i < node.getEntityCount(); i++) {
            if (node.getEntities()[i] != null) {
                putLong(pos + i, node.getEntities()[i].getKey());
                putLong(pos + tree.getTotalKeysSize() + i, ((LeafNodeEntity) node.getEntities()[i]).getOffset());
            } else {
                nullCounter = nullCounter + 1;
            }
            if (nullCounter > 2) {
                return;
            }
        }
    }

    private void serializeInternalNode(Node node) {
        int pos = node.getPosition();
//        memoryMappedByteBuffer.position(pos);
        if (node.getEntityCount() == 0) {
            return;
        }
        int nullCounter = 0;
        for (int i = 0; i < node.getEntityCount(); i++) {
            if (node.getEntities()[i] != null) {
                putLong(pos + i, node.getEntities()[i].getKey());
                putLong(pos + tree.getTotalKeysSize() + i, ((InternalNodeEntity) node.getEntities()[i]).getLeftNodeOffset());
                putLong(pos + tree.getTotalKeysSize() + i + 1, ((InternalNodeEntity) node.getEntities()[i]).getRightNodeOffset());
            } else {
                nullCounter = nullCounter + 1;
            }
            if (nullCounter > 2) {
                return;
            }
        }
    }

    public void updateNode() {

    }

    public Node deSerializeNode() {
        return null;
    }

}
