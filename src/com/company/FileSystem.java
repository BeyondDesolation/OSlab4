package com.company;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class FileSystem {

    private char[] memory;

    private final int clusterSize;
    private final int memorySize;
    private boolean[] freeClustersTable;
    private Directory rootDir;
    private HashSet<String> openFilesTable;

    public FileSystem(int memorySize, int clusterSize) {
        this.clusterSize = clusterSize;
        this.memorySize = memorySize;
        freeClustersTable = new boolean[memorySize / clusterSize];
        Arrays.fill(freeClustersTable, true);
        rootDir = new Directory("rootDir", null);
        openFilesTable = new HashSet<>();
        memory = new char[memorySize];
    }

    public File createFile(String fileName, Directory dir) {
        File file = new File(fileName, clusterSize, dir);
        allocateMemory(file, 1);
        if (dir == null) {
            rootDir.add(file);
        } else {
            dir.add(file);
        }
        return file;
    }

    public Directory createDir(String dirName, Directory dir) {
        Directory directory = new Directory(dirName, dir);
        allocateMemoryForDir(dir);
        if (dir == null) {
            rootDir.add(directory);
        } else {
            dir.add(directory);
        }
        return dir;
    }

    public Directory openDir(String filePath) {

        IFile dir = findFileOrDir(filePath);
        if (dir != null && dir.isDirectory())
            return (Directory) dir;
        else return null;
    }

    public File openFile(String filePath) {
        IFile file = findFileOrDir(filePath);
        if (file != null && !file.isDirectory()) {
            if(openFilesTable.contains(file.getFileName())){
                return null;
            }
            openFilesTable.add(file.getFileName());
            return (File) file;
        } else return null;
    }

    private IFile findFileOrDir(String filePath) {
        String[] path = filePath.split("/");
        IFile dir = rootDir;
        for (String s : path) {
            if(s.equals("")){
                continue;
            }
            if (((Directory) dir).open(s) != null) {
                dir = ((Directory) dir).open(s);
            } else return null;
        }
        // Возвращает либо файл, либо дерикторию.
        return dir;
    }

    public String readDir(Directory dir) {
        Set<String> keySet = dir.getKeySet();
        return keySet.toString();
    }

    public boolean isFileExist(String filePath) {
        IFile file = findFileOrDir(filePath);
        return file != null && !file.isDirectory();
    }
    public boolean isDirExist(String filePath) {
        IFile file = findFileOrDir(filePath);
        return file != null && file.isDirectory() ;
    }
    public boolean cut(String pathFrom, String pathTo){
        IFile copingFile = findFileOrDir(pathFrom);
        IFile to = findFileOrDir(pathTo);
        if(copingFile != null){
          ((Directory)copingFile.getParent()).remove(copingFile.getFileName());

            if(to == null){
                rootDir.add(copingFile);
                copingFile.setParent(rootDir);
                return true;
            }
            copingFile.setParent(to);
            ((Directory)(to)).add(copingFile);
            return true;
        }
        return false;
    }
    private boolean allocateMemoryForDir(Directory dir){
        for (int i = 0; i < freeClustersTable.length; i++) {
            if(freeClustersTable[i]){
                dir.setAddressInStore(i);
                freeClustersTable[i] = false;
                return true;
            }
        }
        return false;
    }
    private boolean allocateMemory(File file, int countOfClusters){

        int[] clusters = new int[countOfClusters];
        int j = 0;
        for (int i = 0; i < freeClustersTable.length && j < countOfClusters; i++) {
            if(freeClustersTable[i]){
                clusters[j] = i;
                j++;
            }
        }
        if(j == countOfClusters){
            for (int i = 0; i < clusters.length; i++) {
                freeClustersTable[clusters[i]] = false;
            }
            file.getIndexNode().addClusters(clusters);
            return true;
        }
        return false;
    }
    public boolean append(File file, char[] data){

        int countOfClusters = data.length / clusterSize;
        IndexNode in = file.getIndexNode();
        if(data.length % clusterSize != 0) countOfClusters ++;
        if(allocateMemory(file, countOfClusters)){
            int j = 0; // Это номер текущего кластера.
            int k = 0; // Это позиция внутри кластера.
            for (int i = 0; i < data.length; i++) {
                // Это вычисление позиции для записи символа, достаем countOfClusters с конца номер кластера
                // и прибавляем к этому числу, какой по счеты кластер сейчас записываем. Умножаем на размер кластера,
                // чтобы получить нативную позицию и к ней прибавляем сдвиг (k);
                memory[in.getCluster(in.size()  - countOfClusters + j) * clusterSize + k] = data[i];
                k++;
                if(k == clusterSize){
                    k = 0;
                    j++;
                }
            }
            return true;
        }
        return false  ;
    }
    public String read(File file){
        StringBuilder sb = new StringBuilder();

        IndexNode in = file.getIndexNode();
        for (int i = 1; i < in.size(); i++) {
            int c = in.getCluster(i);
            for (int j = 0; j < clusterSize; j++) {
                sb.append(memory[c*clusterSize + j]);
            }
        }
        return sb.toString();
    }
    public void close(File file){
        openFilesTable.remove(file.getFileName());
    }
    public boolean copy(String copingFilePath, String pathTo){
        IFile coping = findFileOrDir(copingFilePath);
        IFile to = findFileOrDir(pathTo);
        if(coping != null){
          File newFile = createFile(coping.getFileName(), (Directory)to);
           append(newFile, read((File)coping).toCharArray());
        }
        return false;
    }
    public boolean delete(String filePath){
        IFile file = findFileOrDir(filePath);
        if(file.isDirectory()){
            if(((Directory)(file)).getKeySet().size() > 0){
                return false;
            }else {
                ((Directory)(file.getParent())).remove(file.getFileName());
                freeClustersTable[((Directory)file).getAddressInStore()] = true;
                return true;
            }
        }
        else {
             IndexNode in = ((File)(file)).getIndexNode();
            for (int i = 0; i < in.size(); i++) {
                freeClustersTable[in.getCluster(i)] = true;
            }
            ((Directory)file.getParent()).remove(file.getFileName());
            return true;
        }
    }
// hkjhygtfrdeswaawsedrftgyhujikpzsxdcfvgbhnjmk,l.;.l,kmjnhbgvfcdxs
    public boolean[] getClustersTable() {
        return freeClustersTable;
    }
}
