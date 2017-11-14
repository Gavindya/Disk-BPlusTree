/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ustack.diskbplustree.helpers;

import java.io.Serializable;
import java.util.Comparator;

/**
 *
 * @author AdminPC
 * @param <T>
 */
public class NaturalComparator<T extends Comparable<T>> implements Comparator<T>,Serializable {
  @Override
  public int compare(T a, T b) {
    return a.compareTo(b);
  }
}
