/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ustack.diskbplustree.tree;

import java.io.Serializable;
//import com.ustack.spi.logger.Logger;

/**
 *
 * @author AdminPC.
 */
public class NodeEntity implements Serializable {

  protected transient Tree tree;
  private Long key;
//    protected transient static Logger log = new Logger("test", new ConsoleLogWriter());

  public NodeEntity(Long key, Tree tree) {
    this.key = key;
    this.tree = tree;
  }

  public void setKey(Long newKey) {
    key = newKey;
  }

  public Long getKey() {
    return key;
  }

  public String toString() {
    return "[" + key + "]";
  }

}
