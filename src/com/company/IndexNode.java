package com.company;

public class IndexNode {

    private IndexNode next;

    private String fileName;
    private int clusterSize;

    private boolean S = false; // Атрибут, которым помечаются системные файлы.
    private boolean R = false; // Файл только для чтения.
    private boolean A = false; // Для создания бэкапов.
    private boolean H = false; // Скрытый файл.

    private int[] clusters;
    private int headIndex = 0;

    // clusterSize указывается в килобайтах (КБ).
    public IndexNode(String fileName, int clusterSize) {
        this.fileName = fileName;
        int size = (clusterSize - 1) * 256 - 1;
        clusters = new int[size];
        next = null;
        this.clusterSize = clusterSize;
    }
    public int getCluster(int i){
        return clusters[i];
    }
    public int size(){
        return headIndex ;
    }
    public void addClusters(int[] _clusters){
        IndexNode curINode = this;
        while (curINode.next != null){
            curINode = curINode.next;
        }
        for (int i = 0; i < _clusters.length; i++) {
            curINode.clusters[curINode.headIndex] = _clusters[i];
            curINode.headIndex++;
            if(curINode.headIndex == clusters.length){
                curINode.next = new IndexNode(fileName, clusterSize);
                curINode = curINode.next;
            }
        }

    }

    public String getFileName() {
        return fileName;
    }

    public void setAttributes(boolean s, boolean r, boolean a, boolean h) {
        S = s;
        R = r;
        A = a;
        H = h;
    }
}
