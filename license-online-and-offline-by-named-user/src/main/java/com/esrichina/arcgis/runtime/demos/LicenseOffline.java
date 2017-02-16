package com.esrichina.arcgis.runtime.demos;

import android.support.v7.app.AlertDialog;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by Administrator on 2017/2/16.
 */

public class LicenseOffline {
    public static boolean doesExist(String filePath){
        File localLicenseFile = new File(filePath);
        if (!localLicenseFile.exists()){
            return false;
        }
        return true;
    }
    public static void saveFile(String toSaveString, String filePath) {
        try{
            File localLicenseFile = new File(filePath);
            System.out.println("The file path is: " + filePath);
            if (!localLicenseFile.exists()){
                File dir = new File(localLicenseFile.getParent());
                dir.mkdirs();
                localLicenseFile.createNewFile();
            }
            FileOutputStream outStream = new FileOutputStream(localLicenseFile);
            outStream.write(toSaveString.getBytes());
            outStream.close();
        } catch (FileNotFoundException e){
            e.printStackTrace();
        } catch (IOException e){
            e.printStackTrace();
        }
    }
    /**
     * 读取许可文件内容
     */
    public static String readFile(String filePath) {
        String strCryptoLicenseInfo = "";
        try {
            File localLicenseFile = new File(filePath);
            FileInputStream inStream = new FileInputStream(localLicenseFile);
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int length = -1;
            while ((length = inStream.read(buffer)) != -1) {
                stream.write(buffer, 0, length);
            }
            strCryptoLicenseInfo = stream.toString();
            stream.close();
            inStream.close();
            return strCryptoLicenseInfo;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
