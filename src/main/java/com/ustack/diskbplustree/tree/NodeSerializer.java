/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ustack.diskbplustree.tree;

import com.ustack.spi.logger.ConsoleLogWriter;
import com.ustack.spi.logger.Logger;

/**
 *
 * @author AdminPC
 */
public class NodeSerializer {

    static Logger log = new Logger("test", new ConsoleLogWriter());
    static String NODE_FILE_PATH = ConstantsOfTree.NODE_FILE_PATH;
    static JsonSerializer jsonSerializer = new JsonSerializer(NODE_FILE_PATH);
    static JsonConverter jsonConverter = new JsonConverter();

//    public static long getSerializingOffset(Node node) {
//        long nodeOffset = node.getNodeOffset();
//        if (nodeOffset == (-1)) {
//            nodeOffset = jsonSerializer.getNodeFileLength();
//        }
//        return nodeOffset;
//    }
    public static long initialSerialization(Node node) {
        String json = jsonConverter.getJSON(node);
        long nodeOffset = jsonSerializer.getNodeFileLength();
        node.setNodeOffset(nodeOffset);
        jsonSerializer.serialize(json);
        return nodeOffset;
    }

    public static void serializeNode(Node node) {
//       log.info("serialize node is Dirty? {0}",node.isDirty);
        if (node.isDirty) {
            
            String json = jsonConverter.getJSON(node);
//        log.info("serialize node meth \n");
            long nodeOffset = node.getNodeOffset();
//       log.info("cur ofst ="+nodeOffset);
            if (nodeOffset == (-1)) {
                nodeOffset = jsonSerializer.getNodeFileLength();
                node.setNodeOffset(nodeOffset);
                boolean isSerialized = jsonSerializer.serialize(json);
//            log.info("is serialized ? "+ isSerialized);
            } else {

//            log.info("previously serlzd at "+nodeOffset);
                serializeUpdatedNode(jsonSerializer, json, nodeOffset);
            }
        }
    }

    private static void serializeUpdatedNode(JsonSerializer jsonSerializer, String json, long nodeOffset) {
        jsonSerializer.updateOffset(json, nodeOffset);
    }

    public static Node deSerializeNode(long nodeOffset, Tree tree) {
        String json = jsonSerializer.deSerialize(nodeOffset);
        Node node = jsonConverter.getNode(json, tree);
        node.isDirty = false;
        if (node instanceof LeafNode) {
//            log.info("leaf node ");
        }
        tree.getInMemoryNodes().addNode(node);

        return node;
    }

}
