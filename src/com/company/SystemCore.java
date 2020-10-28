package com.company;

public class SystemCore {
    private FileSystem fs;

    public SystemCore() {
        fs = new FileSystem(1024, 4);

    }

    public IFile create(String fileName, String path, FileType type) {

        Directory dir =  fs.openDir(path);

        if (type == FileType.DIRECTORY) {
            return fs.createDir(fileName, dir);
        } else if (type == FileType.FILE) {
            return  fs.createFile(fileName, dir);
        }
         return null;
    }

    public File openFile(String path) {
        return fs.openFile(path);
    }

    public Directory openDirectory(String path) {
        return fs.openDir(path);
    }

    public String readDir(String path) {
        return fs.readDir(fs.openDir(path));
    }
    public boolean fileExist(String path){
        return fs.isFileExist(path);
    }
    public boolean dirExist(String path){
        return fs.isDirExist(path);
    }
    public void closeFile(File file) {
        fs.close(file);
    }
    public boolean append(File file, String str){
        return fs.append(file, str.toCharArray());
    }
    public String read(File file){
        return fs.read(file);
    }
    public boolean copy(String copingFilePath, String pathTo){
       return fs.copy(copingFilePath, pathTo);
    }
    public boolean cut(String cutFilePath, String pathTo){
        return fs.cut(cutFilePath, pathTo);
    }
    public boolean delete(String path){
        return fs.delete(path);
    }

    public boolean[] getClustersTable() {
        return fs.getClustersTable();
    }
}
