package com.example.apple.mybluetootharduino;

import android.content.Context;
import android.content.ContextWrapper;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;


public class MyFile extends ContextWrapper{
    private String file_name;
    private File file;
    private String directory_name;
    private boolean input_is_open;
    private boolean output_is_open;
    private OutputStream os;
    private InputStream is;


    public MyFile(Context context){
        super(context);
        file_name = null;
        directory_name = null;
        file = null;
        input_is_open = false;
        output_is_open = false;
    }

    //构造函数，指定当前上下文，文件名和文件目录名
    public MyFile(Context context, String temp_file_name, String temp_directory_name){
        super(context);
        file_name = temp_file_name;
        directory_name = temp_directory_name;
        file = null;
        input_is_open = false;
        output_is_open = false;
    }

    public void setFileName(String temp_file_name){
        file_name = temp_file_name;
    }

    public void setDirectoryName(String temp_directory_name){
        directory_name = temp_directory_name;
    }

    public void openExternalPrivateFileForWrite(boolean is_append) {
        file = new File(getExternalFilesDir(directory_name), file_name);
        try {
            os = new FileOutputStream(file, is_append);
            if(file.exists())
            {
                output_is_open = true;
            }
            else
            {
                return;
            }
        } catch (FileNotFoundException e) {
            output_is_open = false;
            e.printStackTrace();
        }
    }

    //打开文件来读，文件名已指定
    public void openExternalPrivateFileForRead() {
        file = new File(getExternalFilesDir(directory_name), file_name);
        if(file.exists()) {
            input_is_open = true;
        }
        else
        {
            return;
        }
        try {
            is = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            input_is_open = false;
            e.printStackTrace();
        }
    }

    public boolean writeDataToFile(byte [] data, int offset, int bytecount){
        if(!output_is_open)
        {
            return false;
        }
        try {
            os.write(data, offset, bytecount);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    //从某文件中读取offset位置开始读bytecount个byte到data
    public boolean readDatafromFile(byte [] data, int offset, int bytecount){
        try{
            if(input_is_open) {
                is.read(data, offset, bytecount);
            }
            else
                return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public boolean closeFile(){
        try{
            if(output_is_open) {
                os.close();
                output_is_open = false;
            }
            if(input_is_open) {
                is.close();
                input_is_open = false;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

}
