package com.hrbeu5162.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.ResourceUtils;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.spring5.context.SpringContextUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SaveAndGetPicUtils {

    public static String save(MultipartFile srcFile, String userName) {
        StringBuilder filePath = new StringBuilder(userName);
        String dataForm = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        String[] data = dataForm.split("-");
        for (String datum : data)
            filePath.append("/").append(datum);
        filePath.append("/");

        String fileName = srcFile.getOriginalFilename();
        File dest = new File(new File(filePath.toString()).getAbsolutePath() + "/" + fileName);

        if (!dest.getParentFile().exists()) {
            dest.getParentFile().mkdirs();
        }

        try {
            srcFile.transferTo(dest);
        } catch (IOException e) {
            System.out.println("上传失败: " + e);
        }
        String str = dest.toString();
        return "/upload" + str.split("upload")[1];
    }

    public static String saveAvatar(MultipartFile srcFile, String userName){
        String filePath = userName + "/avatar/";  // 绝对路径 。。。upload/李扬
        String fileName = srcFile.getOriginalFilename();
        String dataForm = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss").format(new Date());
        fileName =  dataForm + fileName;
        File dest = new File(new File(filePath).getAbsolutePath() + "/" + fileName);
        if (!dest.getParentFile().exists()) {
            dest.getParentFile().mkdirs();
        }
        try {
            srcFile.transferTo(dest);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String str = dest.toString();
        return "/upload" + str.split("upload")[1];
    }

    public static void main(String[] args) {
        String[] sd = "liyang.jpf".split("\\.");
        System.out.println(sd[0]);
    }
}
