package com.company;

public interface IFile {
    String getFileName();
    boolean isDirectory();
    IFile getParent();
    void setParent(IFile parent);
}
