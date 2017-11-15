/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ustack.diskbplustree.tree;

import java.io.File;
import java.io.RandomAccessFile;
import java.util.ArrayList;

/**
 *
 * @author AdminPC
 */
public class LeafNodeEntity extends NodeEntity {

  private ArrayList<Long> offset;
  private long longOffset;

  public LeafNodeEntity(Long key, Tree tree) {
    super(key, tree);
    offset = new ArrayList<>();
  }

  private void writeValue(Object data) {
//        try {
//            String filePath = ConstantsOfTree.DB_FILE_PATH;/*tree.getDbFilePath();*/
//            File file = new File(filePath);
//            file.getParentFile().mkdirs();
//            if (!file.exists()) {
////                System.out.println("file does not exist");
//                file.createNewFile();
//                this.offset.add(0l);
//            } else {
////                System.out.println("file exist and appending at : ");
//                this.offset.add(file.length());
////                System.out.println("offsets after appending");
////                printOffsets();
//            }
//            try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePath, true))) {
//                char record_separator = 0x1e;
//                bw.write(data.toString() + record_separator);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//
//        } catch (Exception ex) {
//            ex.printStackTrace();
//        }
  }

  public void setValue(Object data) {
    writeValue(data);
  }

  private String valueFound(long index, String filePath) {
    try (RandomAccessFile randomAccessFile = new RandomAccessFile(new File(filePath), "r")) {
      randomAccessFile.seek(index);
      StringBuilder stringBuilder = new StringBuilder();
      int character = randomAccessFile.read();
      while ((character != -1) && (character != 0x1e)) {
        stringBuilder.append((char) character);
        character = randomAccessFile.read();
      }
      return stringBuilder.toString();
    } catch (Exception ex) {
      return "";
    }
  }

//    private void printOffsets() {
//        System.out.print("offsets = ");
//        for (Long l : offset) {
//            System.out.print(l + ",");
//        }
//        System.out.println();
//    }
  public void setDataOffset(long offset) {
    this.longOffset = offset;
  }

  public long getOffset() {
    return 5l;
  }

  public long getValue() {
    //return offset of DB file
    return 5l;
//        try {
//            String filePath = ConstantsOfTree.DB_FILE_PATH;/*tree.getDbFilePath();*/
//            File file = new File(filePath);
//            if (file.exists()) {
//                String result;
////                printOffsets();
//                if (offset.size() > 1) {
//                    ArrayList<String> values = new ArrayList<>();
//                    for (int i = 0; i < offset.size(); i++) {
//                        result = valueFound(offset.get(i), filePath);
//                        if (!result.equals("")) {
//                            values.add(valueFound(offset.get(i), filePath));
//                        }
//                    }
//                    return values;
//                } else if (offset.size() == 1) {
//                    return valueFound(offset.get(0), filePath);
//                }
//            }
//            log.info("returning null :( ");
//            return null;
//        } catch (Exception exception) {
//            log.exception(exception, "exception - ", exception.getMessage());
//            return null;
//        }
  }

}
