package com.company;

import java.util.HashMap;
import java.util.Set;

public class Directory implements IFile{
    private String name;
    private IFile parent;
    private HashMap<String, IFile> table;
    private int addressInStore;


    public Directory(String name, IFile parent){
        this.parent = parent;
        this.name = name;
        table = new HashMap<>();
    }
    public void add(IFile file){
        table.put(file.getFileName(), file);
    }
    public void remove(String key){
        table.remove(key);
    }
    public IFile open(String fileName){
        return table.get(fileName);
    }
    public Set<String> getKeySet(){
      return table.keySet();
    }
    @Override
    public String getFileName() {
        return name;
    }

    @Override
    public boolean isDirectory() {
        return true;
    }

    @Override
    public IFile getParent() {
        return parent;
    }

    @Override
    public void setParent(IFile parent) {
        this.parent = parent;
    }

    public void setAddressInStore(int addressInStore) {
        this.addressInStore = addressInStore;
    }

    public int getAddressInStore() {
        return addressInStore;
    }
}
