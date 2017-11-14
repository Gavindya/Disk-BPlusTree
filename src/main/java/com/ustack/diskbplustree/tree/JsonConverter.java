/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ustack.diskbplustree.tree;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.ustack.spi.logger.ConsoleLogWriter;
import com.ustack.spi.logger.Logger;
import java.io.IOException;
import java.util.Iterator;

/**
 *
 * @author AdminPC
 */
public class JsonConverter {

    private final transient Logger log = new Logger("test", new ConsoleLogWriter());

    public String getJSON(Node node) {
//        Gson gson = new Gson();
//        Gson gson = new GsonBuilder().setPrettyPrinting().setExclusionStrategies(new CustomExclusionStrategy()).create();
//        Type type = new TypeToken<List<Node>>() { }.getType();
//        List<Node> nodesList = new ArrayList<>();
//        nodesList.add(node);
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(Node.class, new MyTypeAdapter<>())
                //                .setPrettyPrinting()
                //                .setExclusionStrategies(new CustomExclusionStrategy())
                .create();
        String json = gson.toJson(node);
//        Gson prettyGson = new GsonBuilder()
//                .setPrettyPrinting()
//                .create();
//        String prettyJson = prettyGson.toJson(node);
//        log.info("converted to json str"+json);
        return json;
    }

    public Node getNode(String json, Tree tree) {
//        Type collectionType = new TypeToken<Collection<NodeEntity>>() {
//        }.getType();
        Gson gson = new GsonBuilder().create();
        JsonObject Node = gson.fromJson(json, JsonObject.class);
        JsonArray entities = Node.getAsJsonArray("entities");
        Iterator entries = entities.iterator();
        Object entry;
        boolean isLeaf = false;
        if (entries.hasNext()) {
            entry = entries.next();
            if (!entry.toString().equals("null")) {
                if (entry.toString().contains("offset")) {
                    isLeaf = true;
                }
            }
        }
        entries = entities.iterator();
        if (isLeaf) {
//            log.info("\n desrializing leaf node \n");
            LeafNode leafNode = gson.fromJson(json, LeafNode.class);
            LeafNodeEntity[] leafNodeEntities = new LeafNodeEntity[ConstantsOfTree.DEGREE - 1];
            for (int i = 0; i < (ConstantsOfTree.DEGREE - 1); i++) {
                if (entries.hasNext()) {
                    entry = entries.next();
                    if (!entry.toString().equals("null")) {
                        leafNodeEntities[i] = getLeafNodeEntity(entry.toString());
//                        log.info("lne =" + leafNodeEntities[i].toString());
                    }
                }
            }
            leafNode.addAllEntities(leafNodeEntities);
            leafNode.toString();
            leafNode.setTree(tree);
//            leafNode.serialize();
            return leafNode;
        } else {
//            log.info("\n desrializing internal node \n");
            InternalNode internalNode = gson.fromJson(json, InternalNode.class);
            InternalNodeEntity[] internalNodeEntities = new InternalNodeEntity[ConstantsOfTree.DEGREE - 1];
            for (int i = 0; i < (ConstantsOfTree.DEGREE - 1); i++) {
                if (entries.hasNext()) {
                    entry = entries.next();
                    if (!entry.toString().equals("null")) {
                        internalNodeEntities[i] = getInternalNodeEntity(entry.toString());
//                        log.info("ine =" + internalNodeEntities[i].toString());
                    }
                }
            }
            internalNode.addAllEntities(internalNodeEntities);
            internalNode.setTree(tree);
//            internalNode.serialize();
            return internalNode;
        }

    }

    public LeafNodeEntity getLeafNodeEntity(String json) {
        Gson gson = new Gson();
        LeafNodeEntity nodeEntity = gson.fromJson(json, LeafNodeEntity.class);
        return nodeEntity;
    }

    public InternalNodeEntity getInternalNodeEntity(String json) {
        Gson gson = new Gson();
        InternalNodeEntity nodeEntity = gson.fromJson(json, InternalNodeEntity.class);
        return nodeEntity;

    }

    private static class MyTypeAdapter<T> extends TypeAdapter<T> {

        @Override
        public T read(JsonReader reader) throws IOException {
            return null;
        }

        @Override
        public void write(JsonWriter writer, T obj) throws IOException {
            if (obj == null) {
                writer.nullValue();
                return;
            }
            writer.value(obj.toString());
        }

    }

    private static class CustomExclusionStrategy implements ExclusionStrategy {

        @Override
        public boolean shouldSkipClass(Class<?> arg0) {
            return false;
        }

        @Override
        public boolean shouldSkipField(FieldAttributes f) {
            return ((/*f.getDeclaringClass() == Logger.class &&*/f.getName().contains("log")) /*|| (f.getDeclaringClass() == Tree.class && f.getName().contains("tree"))*/);
        }

    }
}
