package com.company;

import java.awt.*;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        // write your code here
        SystemCore core = new SystemCore();
        Scanner scanner = new Scanner(System.in);
        StringBuilder currentPath = new StringBuilder("/");

        FileSystemMonitor fm = new FileSystemMonitor(815, 500, "FM", core.getClustersTable());
        File mfile = null;
        while (true) {

            if(mfile == null){
                fm.render(null);
            }else{
                fm.render(mfile.getIndexNode());
            }

            System.out.print(">");
            System.out.print(currentPath);

            String in1 = scanner.next();
            if (in1.equals("cd")) {
                String in = scanner.next().trim();
                if (in.equals("")) {
                    currentPath = new StringBuilder("/");
                } else if (in.equals("..")) {
                    currentPath.delete(getLastDirStartIndex(currentPath), currentPath.length() - 1);
                } else {
                    if (core.dirExist(currentPath.toString() + in)) {
                        currentPath.append(in).append("/");
                    } else {
                        System.out.println("Директории не существует");
                        System.out.println("currentPath " + currentPath + " in " + in);
                    }
                }
            } else if (in1.equals("createFile")) {
                String in = scanner.next().trim();
                if (in.trim().equals("")) {
                    System.out.println("Недопустимое имя");
                } else if (core.fileExist(currentPath.toString() + in)) {
                    System.out.println("Файл с таким именем уже существвует");
                } else {
                    mfile = (File) core.create(in, currentPath.toString(), FileType.FILE);
                }
            } else if (in1.equals("createDir")) {
                String in = scanner.next().trim();
                if (in.trim().equals("")) {
                    System.out.println("Недопустимое имя");
                } else if (core.dirExist(currentPath.toString() + in)) {
                    System.out.println("Директория с таким именем уже существвует");
                } else {
                    core.create(in, currentPath.toString(), FileType.DIRECTORY);
                    currentPath.append(in).append("/");
                }

            } else if (in1.equals("open")) {
                String in = scanner.next().trim();
                String in2 = "";
                if (core.fileExist(currentPath + in)) {
                    File file = core.openFile(currentPath + in);
                    System.out.print(core.read(file));
                    mfile = file;
                    fm.render(mfile.getIndexNode());
                    StringBuilder sb = new StringBuilder("")  ;
                    while (true) {
                        in2 = scanner.nextLine();
                        if (in2.equals("close")) {
                            if(core.append(file, sb.toString())){
                                System.out.println("Запись прошла успешно.");
                            }else{
                                System.out.println("Недостатосно памяти.");
                            }
                            core.closeFile(file);
                            break;
                        }
                        if (in2.equals("")) {
                            continue;
                        }
                        sb.append(in2).append("\n");
                    }
                } else {
                    System.out.println("Файла " + in2 + " не существует");
                }

            } else if (in1.equals("copy")) {
                String in = scanner.next();
                String in2 = scanner.next();
                if(core.fileExist(currentPath + in)){
                    core.copy(currentPath + in, in2);

                }else{
                    System.out.println("Файла " + in +" не существует.");
                }
            } else if (in1.equals("delete")) {
                String in = scanner.next();
                if(core.fileExist(currentPath + in) || core.dirExist(currentPath + in)){
                    if( core.delete(currentPath + in)){
                        System.out.println("Файл " + in +" удален.");
                        mfile = null;
                    }else{
                        System.out.println("Невозможно удалить директорию, пока в ней находятся файлы.");
                    }

                }else{
                    System.out.println("Файла " + in +" не существует.");
                }

            }
            else if(in1.equals("cut")){
                String in = scanner.next();
                String in2 = scanner.next();
                if(core.fileExist(currentPath + in) || core.dirExist(currentPath + in)){
                    core.cut(currentPath + in, in2);
                    mfile = null;
                }else{
                    System.out.println("Файла " + in +" не существует.");
                }
            }else if (in1.equals("dir")) {
                System.out.println(core.readDir(currentPath.toString()));
            } else if (in1.equals("EXIT")) {
                break;
            } else {
                System.out.println("Команды " + in1 + " не существует.");
            }
        }



        core.create("dir1", "", FileType.DIRECTORY);
        core.create("dir2", "dir1", FileType.DIRECTORY);
        core.create("file1.txt", "dir1", FileType.FILE);
        System.out.println("В dir1" + core.readDir("dir1"));

        core.create("file2.txt", "dir1", FileType.FILE);
        System.out.println("В dir1" + core.readDir("dir1"));

        core.create("file3.txt", "dir1/dir2", FileType.FILE);
        System.out.println("В dir2" + core.readDir("dir1/dir2/"));


    }

    private static int getLastDirStartIndex(StringBuilder path) {
        char[] reversPath = path.reverse().toString().toCharArray();
        path.reverse();
        for (int i = 1; i < reversPath.length; i++) {
            if (reversPath[i] == '/') {
                i++;
                return reversPath.length - i;
            }
        }
        return 0;
    }
}
