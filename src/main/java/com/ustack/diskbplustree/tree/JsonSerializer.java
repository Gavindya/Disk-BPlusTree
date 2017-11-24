///*
// * To change this license header, choose License Headers in Project Properties.
// * To change this template file, choose Tools | Templates
// * and open the template in the editor.
// */
//package com.ustack.diskbplustree.tree;
//
//import com.ustack.spi.logger.ConsoleLogWriter;
//import com.ustack.spi.logger.Logger;
//import java.io.File;
//import java.io.FileNotFoundException;
//import java.io.IOException;
//import java.io.RandomAccessFile;
//
///**
// *
// * @author AdminPC
// */
//public class JsonSerializer {
//
//    private File nodeFile;
//    private RandomAccessFile randomAccessFile;
//    private Logger log = new Logger("test", new ConsoleLogWriter());
//    private char record_separator = 0x1e;
////    private MappedByteBuffer mappedByteBuffer;
////    private long bufferSize = 8 * 1000;
////    private int start =0;
////    private  FileChannel fc;
////    private char line_separator = 0x2028;
//
//    public JsonSerializer(String fileName) {
//
//        this.nodeFile = new File(fileName);
//        if (!nodeFile.exists()) {
//            try {
//                nodeFile.createNewFile();
//            } catch (IOException ex) {
//                log.exception(ex, "File not found", ex.getMessage());
//            }
//        }
//        try {
//
//            this.randomAccessFile = new RandomAccessFile(nodeFile, "rw");
////            fc = new RandomAccessFile(nodeFile, "rw").getChannel();
////            mappedByteBuffer = fc.map(FileChannel.MapMode.READ_WRITE, 0, bufferSize);
//
//        } catch (FileNotFoundException ex) {
//            log.exception(ex, "FileNotFoundException {0}", ex.getMessage());
//        }
//    }
//
//    public long getNodeFileLength() {
////        return nextLineFree();
//        return nodeFile.length();
//    }
//
//    public boolean serialize(String json) {
//        json = json + record_separator + "\n";
////        log.info("serializing to file");
//        try /*(RandomAccessFile randomAccessFile = new RandomAccessFile(nodeFile, "rw")) */ {
//            randomAccessFile.seek(nodeFile.length());
//            randomAccessFile.writeBytes(json);
////            BufferedWriter bw = new BufferedWriter(new FileWriter(nodeFile, true));
////            bw.write(json);
////            bw.close();
//
////            randomAccessFile.close();
//            return true;
//        } catch (IOException e) {
////            e.printStackTrace();
//            System.out.println(e.getMessage());
//        }
//        return false;
//    }
//
//    public void updateOffset(String json, long offset) {
////        log.info("at update in jsonSerializer , offset =" + offset);
////        json = json + record_separator + "\n";
//        try /*(RandomAccessFile randomAccessFile = new RandomAccessFile(nodeFile, "rw")) */ {
//            randomAccessFile.seek(offset);
//            long pointer = randomAccessFile.getFilePointer();
//            StringBuilder stringBuilder = new StringBuilder();
//            int character = randomAccessFile.read();
//            while ((character != -1) && (character != 0x1e/*0x2028*/)) {
//                stringBuilder.append((char) character);
//                character = randomAccessFile.read();
//            }
//            String stringFound = stringBuilder.toString();
//            if (!stringFound.contains("$$")) {
//                String length = String.valueOf(getNodeFileLength());
//                int c = length.length();
//                while (c < stringBuilder.toString().length()) {
//                    length = length + "$";
//                    c = c + 1;
//                }
////               log.info("val =" + length + ": " + length.length());
//                String value = length + record_separator + "\n";
//                randomAccessFile.seek(pointer);
//                randomAccessFile.writeBytes(value);
////                log.info("current len = " + getNodeFileLength());
//                serialize(json);
//            } else {
//                StringBuilder nxtOfst = new StringBuilder();
//                for (int i = 0; i < stringFound.length(); i++) {
//                    if (stringFound.charAt(i) != '$') {
//                        nxtOfst.append(stringFound.charAt(i));
//                    } else {
//                        break;
//                    }
//                }
//                updateOffset(json, Long.valueOf(nxtOfst.toString()));
//            }
//
////            BufferedWriter bw = new BufferedWriter(new FileWriter(nodeFile, false));
////            bw.write(json);
//////            bw.flush();
////            bw.close();
////            randomAccessFile.close();
//            System.gc();
//        } catch (IOException e) {
//            log.exception(e, "IOException {0}", e.getMessage());
//        }
//    }
//
//    public String deSerialize(long offset) {
//        if (nodeFile.exists()) {
//            try /*(RandomAccessFile randomAccessFile = new RandomAccessFile(nodeFile, "r"))*/ {
//                randomAccessFile.seek(offset);
//                StringBuilder stringBuilder = new StringBuilder();
//                int character = randomAccessFile.read();
//                while ((character != -1) && (character != 0x1e/*0x2028*/)) {
//                    stringBuilder.append((char) character);
//                    character = randomAccessFile.read();
//                }
//                String stringBuilt = stringBuilder.toString();
//                if (!stringBuilt.contains("$$")) {
//                    return stringBuilt;
//                } else {
//                    StringBuilder nxtOfst = new StringBuilder();
//                    for (int i = 0; i < stringBuilt.length(); i++) {
//                        if (stringBuilt.charAt(i) != '$') {
//                            nxtOfst.append(stringBuilt.charAt(i));
//                        } else {
//                            break;
//                        }
//                    }
//                    return deSerialize(Long.valueOf(nxtOfst.toString()));
////                    return nxtOfst.toString();
//                }
//            } catch (FileNotFoundException ex) {
//                log.exception(ex, "FileNotFoundException {0}", ex.getMessage());
//            } catch (IOException ex) {
//                log.exception(ex, "IOException {0}", ex.getMessage());
//            }
//        }
//        return "";
//    }
//
//    public void closeRandomAccessFile() {
//        try {
//            randomAccessFile.close();
//        } catch (IOException ex) {
//            log.exception(ex, "IOException {0}", ex.getMessage());
//        }
//    }
//}
