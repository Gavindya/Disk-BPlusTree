/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ustack.diskbplustree.tree;

/**
 *
 * @author AdminPC
 */
public class InternalNodeEntity extends NodeEntity{

    private long leftNodeOffset;
    private long rightNodeOffset;

    public InternalNodeEntity(Integer key, Tree tree) {
        super(key, tree);
        leftNodeOffset = (-1);
        rightNodeOffset = (-1);
    }

    public void setLeftNode(long nodeLocation) {
        leftNodeOffset = nodeLocation;
    }

    public void setRightNode(long nodeLocation) {
        rightNodeOffset = nodeLocation;
    }

    public long getLeftNodeOffset() {
        return leftNodeOffset;
    }

    public long getRightNodeOffset() {
        return rightNodeOffset;
    }

    public Node getLeftNode(Tree tree) {
        if (leftNodeOffset != (-1)) {
            Node node= tree.getInMemoryNodes().getNode(leftNodeOffset);
//            System.out.println("left node - "+node.toString());
            return node;
        } else {
            return null;
        }
    }

    public Node getRightNode(Tree tree) {
        if (rightNodeOffset != (-1)) {
            Node node= tree.getInMemoryNodes().getNode(rightNodeOffset);
//            System.out.println("right node - "+node.toString());
            return node;
        } else {
            return null;
        }
    }

    @Override
    public String toString() {
        return getKey().toString();
    }

}
