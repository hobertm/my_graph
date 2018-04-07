package mytest;

import java.io.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UnicodeToJson {

    public static String unicodeToString(String str) {
        Pattern pattern = Pattern.compile("(\\\\u(\\p{XDigit}{4}))");
        Matcher matcher = pattern.matcher(str);
        char ch;
        while (matcher.find()) {
            ch = (char) Integer.parseInt(matcher.group(2), 16);
            str = str.replace(matcher.group(1), ch + "");
        }
        return str;
    }

    public static void readfile(String filepath) throws  IOException {
        try {

            File file = new File(filepath);
            if (!file.isDirectory()) {
                System.out.println("文件");
                System.out.println("path=" + file.getPath());
                System.out.println("absolutepath=" + file.getAbsolutePath());
                System.out.println("name=" + file.getName());
                /*File fileDir = new File("/home/hb/Documents/转换");
                if (!fileDir.exists())
                    fileDir.mkdir();*/
                File newfile = new File("/home/hb/Documents/转换/"+
                        file.getName().replace("txt","json"));
                BufferedReader buf = new BufferedReader(new FileReader(file));
                BufferedWriter writer = new BufferedWriter(new FileWriter(newfile));
                String str = null;
                while ((str = buf.readLine()) != null) {
                    String unicodToString = UnicodeToJson.unicodeToString(str);
                    writer.write(unicodToString);
                    writer.newLine();
                    writer.flush();
/*                  String newLine = System.getProperty("line.separator");
                    Pattern p = Pattern.compile(",");
                    Matcher m = p.matcher(unicodToString);
                    String strResult = m.replaceAll(","+newLine);
                    System.out.println(strResult);*/
                }
                writer.close();
                buf.close();


            } else if (file.isDirectory()) {
                System.out.println("文件夹");
                String[] filelist = file.list();
                for (int i = 0; i < filelist.length; i++) {
                    File readfile = new File(filepath + "/" + filelist[i]);
                    readfile(filepath + "/" + filelist[i]);
                }
            }

        } catch (FileNotFoundException e) {
            System.out.println("readfile()   Exception:" + e.getMessage());
        }

    }

    public static void main(String[] args) throws IOException {
        readfile("/home/hb/Documents/企业数据/");
    }

}