package mytest;

import java.io.*;

public class Merge {

    public static void merge(String filepath) throws IOException {
        File dir = new File(filepath);
        File newfile = new File(dir.getParent() + "/合并/merge.json");
        BufferedWriter writer = new BufferedWriter(new FileWriter(newfile));
        writer.write("{");
        writer.write("\"items\":[");
        if (dir.isDirectory()) {
            System.out.println("文件夹");
            String[] filelist = dir.list();
            for (int i = 0; i < filelist.length; i++) {
                File file = new File(filepath + "/" + filelist[i]);
                if (file.isDirectory())
                    continue;

                BufferedReader buf = new BufferedReader(new FileReader(file));
                String str = null;
                while ((str = buf.readLine()) != null) {
                    writer.write(str);
                    writer.flush();
                }
                if (i!=filelist.length-1) {
                    writer.write(",");
                    writer.flush();
                }
                buf.close();

            }
            writer.write("]}");
            writer.close();
        }
    }

    public static void main(String[] args) throws IOException {
        merge("/home/hb/Documents/data");
    }
}
