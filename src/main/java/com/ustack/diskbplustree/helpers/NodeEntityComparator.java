/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ustack.diskbplustree.helpers;

import com.ustack.diskbplustree.tree.NodeEntity;

import java.util.Comparator;

/**
 *
 * @author AdminPC
 */
public class NodeEntityComparator implements Comparator<NodeEntity> {

  @Override
  public int compare(NodeEntity dn1, NodeEntity dn2) {
    NaturalComparator nc = new NaturalComparator();
    return nc.compare(dn1.getKey(), dn2.getKey());
  }

}
