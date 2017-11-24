/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ustack.diskbplustree.diskoperations;

import com.ustack.spi.logger.ConsoleLogWriter;
import com.ustack.spi.logger.Logger;
import com.ustack.diskbplustree.tree.ConstantsOfTree;
import com.ustack.diskbplustree.tree.Tree;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

/**
 *
 * @author AdminPC
 */
public class MmapFile {

  private RandomAccessFile randomAccessFile;
  private MappedByteBuffer memoryMappedByteBuffer;
  private int fileIndex;
  protected transient static Logger log = new Logger("test", new ConsoleLogWriter());

  public MmapFile(long filesize, int fileIndx) {
    try {
      this.fileIndex = fileIndx;
      randomAccessFile = new RandomAccessFile(ConstantsOfTree.DB_PATH + "\\" + fileIndx + ".txt", "rw");
      if (randomAccessFile.length() == 0) {
        randomAccessFile.setLength(filesize);
      }
      //memMap of file size
      memoryMappedByteBuffer = randomAccessFile.getChannel().map(FileChannel.MapMode.READ_WRITE, 0, filesize);
    } catch (IOException ex) {
      log.exception(ex, " IOException {0} ", ex.getMessage());
    }
  }

  public void close() {
    try {
      randomAccessFile.close();
    } catch (IOException ex) {
      log.exception(ex, " IOException {0} ", ex.getMessage());
    }
  }

  public void putLong(int pos, long data) {
    memoryMappedByteBuffer.position(pos);
    memoryMappedByteBuffer.putLong(pos, data);
  }

  public long getLong(int pos) {
    memoryMappedByteBuffer.position(pos);
    return memoryMappedByteBuffer.getLong(pos);
  }

  public void putInt(int pos, int data) {
    memoryMappedByteBuffer.position(pos);
    memoryMappedByteBuffer.putInt(pos, data);
  }

  public int getInt(int pos) {
    memoryMappedByteBuffer.position(pos);
    return memoryMappedByteBuffer.getInt(pos);
  }

  public int getFileIndex() {
    return fileIndex;
  }

}
