package com.company;

import com.sun.source.tree.IfTree;

public class File implements IFile{

    private IndexNode indexNode;
    private IFile parent;
    public File(String fileName, int clusterSize, IFile parent){
        indexNode = new IndexNode(fileName, clusterSize);
        this.parent = parent;
    }

    public IndexNode getIndexNode() {
        return indexNode;
    }

    public String getFileName() {
        return indexNode.getFileName();
    }

    @Override
    public boolean isDirectory() {
        return false;
    }

    @Override
    public IFile getParent() {
        return parent;
    }

    @Override
    public void setParent(IFile parent) {
        this.parent = parent;
    }
}
