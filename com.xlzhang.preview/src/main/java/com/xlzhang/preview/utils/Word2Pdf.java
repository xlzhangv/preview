package com.xlzhang.preview.utils;


import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Word2Pdf {
    private static final Logger LOGGER = LoggerFactory.getLogger(Word2Pdf.class);

    public static int word2Pdf(String severPath, String sourceName, String destName) {

        // 目标路径不存在则建立目标路径
        File dest = new File(severPath + destName);
        if (!dest.getParentFile().exists())
            dest.getParentFile().mkdirs();

        // 源文件不存在则返回 -1
        File source = new File(sourceName);
        if (!source.exists()) {
            LOGGER.info("源文件不存:" + sourceName);
            return -1;
        }
        String wordTools_HOME = severPath + "\\exe\\";// SWFTools的安装路径。在我的项目中，我为了便于拓展接口，没有直接将SWFTools的安装路径写在这里，详见附件
        // 如果从文件中读取的URL地址最后一个字符不是 '\'，则添加'\'
        try {
            LOGGER.info("开始转换..." + System.currentTimeMillis());
            StringBuilder sb = new StringBuilder();
            sb.append("\"").append(wordTools_HOME);
            sb.append("SavePdf.exe").append("\"");
            ProcessBuilder processBuilder = new ProcessBuilder(sb.toString(), sourceName, severPath + destName);
            processBuilder.directory(new File(wordTools_HOME));
            long startTime = System.currentTimeMillis();
            Process pro = processBuilder.start();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(pro.getInputStream()));
            String temp = null;
            List<String> list = new LinkedList<String>();
            while ((temp = bufferedReader.readLine()) != null) {
                list.add(temp);
            }
            pro.waitFor();
            LOGGER.info("返回信息：" + list.toString());
            LOGGER.info("source  file:" + sourceName + ", dest file:" + destName);

            LOGGER.info("word2pdf finish cost:" + (System.currentTimeMillis() - startTime) + "ms");
            int status = pro.exitValue();
            LOGGER.info("转换结束：" + System.currentTimeMillis() + "结束状态：" + status);
            return status;
        } catch (Throwable e) {
            LOGGER.error("", e);
        }
        return 1;
    }
}
