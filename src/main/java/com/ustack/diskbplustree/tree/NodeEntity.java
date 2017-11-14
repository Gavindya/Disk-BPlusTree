/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ustack.diskbplustree.tree;
//
import com.ustack.spi.logger.ConsoleLogWriter;
import java.io.Serializable;
import com.ustack.spi.logger.Logger;

/**
 *
 * @author AdminPC
 */
public class NodeEntity implements Serializable{

    protected transient Tree tree;
    private Integer key;
    protected transient static Logger log = new Logger("test", new ConsoleLogWriter());

    public NodeEntity(Integer key,Tree tree){
        this.key = key;
        this.tree=tree;
    }

    public void setKey(Integer newKey){
        key = newKey;
    }
    public Integer getKey() {
        return key;
    }

    @Override
    public String toString() {
        return "[" + key + "]";
    }

}
