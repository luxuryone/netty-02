package com.lux.aio;

import java.io.*;

public class tihuan {
    public static void main(String [] args) {
        File file = new File("D:\\desktop\\Mer_tran_develop_Mon_Final_201806\\新建文件夹\\Mer_tran_develop_Mon_Final_201806\\Mer_tran_develop_Mon_Final_201806.csv");
        BufferedReader reader = null;
        File file0 = new File("D:\\desktop\\Mer_tran_develop_Mon_Final_201806\\temp.csv");
        BufferedWriter writer = null;
        byte[] space = new byte[]{(byte)0x03};
        OutputStream stream = null;

        try {
            String UTFSpace = new String(space,"GB2312");
            reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), "GB2312"));
            stream = new FileOutputStream(file0);
            writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file0), "GB2312"));
            String temp = null;
            writer.write(reader.readLine());
            writer.flush();
            System.out.println("WTF");
            while ((temp = reader.readLine()) != null) {
                System.out.println(temp);
                byte[] aaa = (temp.replace(UTFSpace, ",")).getBytes();
                System.out.println(temp);
                writer.write(reader.readLine());
                writer.flush();
                System.exit(0);
            }


        } catch (Exception e){
            e.printStackTrace();
            try {
                stream.close();
                writer.close();
                reader.close();
            } catch (Exception ex){
                e.printStackTrace();
            }

        }
    }
}