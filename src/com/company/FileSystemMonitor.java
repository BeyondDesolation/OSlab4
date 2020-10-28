package com.company;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.lang.reflect.GenericArrayType;
import java.util.Arrays;

public class FileSystemMonitor {
    private BufferedImage buffer;
    private int[] bufferData;
    private Graphics bufferGraphics;
    private int clearColor;

    private JFrame window;
    private Canvas content;
    private final int  size = 32;
    private final int cols = 25;
    private boolean[] freeClustersTable;

    public FileSystemMonitor(int width, int height, String title, boolean[] freeClustersTable) {

        this.freeClustersTable = freeClustersTable;
        window = new JFrame(title);
        content = new Canvas();

        window.setPreferredSize(new Dimension(width, height));
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setResizable(false);

        window.setLayout(new BorderLayout());
        window.add(content);
        window.pack();
        window.setVisible(true);
        window.setLocationRelativeTo(null);

        buffer = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        bufferData = ((DataBufferInt) buffer.getRaster().getDataBuffer()).getData();
        bufferGraphics = buffer.getGraphics();
        ((Graphics2D) bufferGraphics).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        clearColor = 0Xffffffff;
    }

    public Graphics2D getGraphics() {
        return (Graphics2D) bufferGraphics;
    }

    public void clear(int color) {
        Arrays.fill(bufferData, color);
    }



    public void render(IndexNode in) {
        clear(0XFFFFFFFF);
        Graphics g = content.getGraphics();
        renderField();
        renderSelectedSectors(in);

        g.drawImage(buffer, 0, 0, null);
    }
    private void renderSelectedSectors(IndexNode in) {
        if(in == null)
            return;
        Graphics2D g2 = getGraphics();
        g2.setPaint(Color.red);
        for (int i = 0; i < in.size(); i++) {
            int row = in.getCluster(i) / cols;
            int col = in.getCluster(i) % cols;
            g2.fillRect(col * size + 2, row * size + 2, size - 4, size - 4 );
        }
    }
    private void renderField() {
        Graphics2D g2 = getGraphics();

        int i = 0, j = 0;
        for (int k = 0; k < freeClustersTable.length; k++) {
            if(freeClustersTable[k]){
                g2.setPaint(Color.lightGray);
            }else{
                g2.setPaint(new Color(100,120,230));
            }
            g2.fillRect(j * size + 2, i * size + 2, size - 4, size - 4);
            j++;
            if(j == cols){
                j = 0;
                i ++;
            }
        }
    }
}
