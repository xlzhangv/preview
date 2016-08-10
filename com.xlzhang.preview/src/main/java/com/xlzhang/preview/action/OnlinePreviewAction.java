package com.xlzhang.preview.action;



import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;

import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.ExceptionMapping;
import org.apache.struts2.convention.annotation.ExceptionMappings;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;

import com.google.gson.Gson;
import com.xlzhang.preview.base.AbstractCommonAction;
import com.xlzhang.preview.pojo.PdfPreview;
import com.xlzhang.preview.service.PdfPreviewSrv;
import com.xlzhang.preview.utils.PDF2ImageEvent;
import com.xlzhang.preview.utils.PDFchangToImage;
import com.xlzhang.preview.utils.Word2Pdf;

/**
 * ClassName: SwfUploadFileAction. <br/>
 * Function: TODO <br/>
 * date: 2016�?2�?19�? 上午9:42:07 <br/>
 * 
 * @author zhang
 * @version
 * @since JDK 1.6
 */
@Scope("prototype")
@SuppressWarnings("unchecked")
@Namespace("/online-preview")
@Results({ @Result(name = "pdf-preview", location = "/WEB-INF/jsp/preview.jsp"),
    @Result(name = "file-home", location = "/WEB-INF/jsp/file-dir.jsp")})
@ExceptionMappings({ @ExceptionMapping(exception = "java.lange.RuntimeException", result = "error") })
public class OnlinePreviewAction extends AbstractCommonAction {
    private static final long serialVersionUID = 572146812454L;
    private static final Logger LOGGER = Logger.getLogger(OnlinePreviewAction.class);
    private static final String FILE_SEPARATOR = "/";
    private static final String ROOT_PATH="/attached";
    private static final String BASE_ATTACH_DIR = "preview";
    
    @Autowired
    private PdfPreviewSrv previewSrv;
    
    @Action("/online-preview/file-home")
    public String index() {

        return "file-home";
    }
    
    @Action("/online-preview/pdf-preview")
    public String pdfPreview() {
        return "pdf-preview";
    }
    
    /**
     * 功能: 文件夹.<br/>
     * date: 2016年8月9日 上午11:52:40 <br/>
     *
     * @author xlzhang@wisdombud.com
     */
    @Action("/online-preview/file-dir")
    public void remoteImages() {
        // 根目录路径，可以指定绝对路径，比如 /var/www/attached/
        final String rpath = this.request.getContextPath();
        final String basePath = this.request.getScheme() + "://" + this.request.getServerName() + ":"
                                + this.request.getServerPort() + rpath + "/";
        String rootPath = ServletActionContext.getServletContext().getRealPath(ROOT_PATH) + "/";

        String baseDir="";
        // 根目录URL，可以指定绝对路径，比如 http://www.yoursite.com/attached/
        String rootUrl = basePath + ROOT_PATH;
        // 根据path参数，设置各路径和URL
        String path = this.request.getParameter("path") != null ? this.request.getParameter("path") : "";
        if(!path.endsWith("/")){
            path+="/";
        }
        baseDir+=path;
        if(!baseDir.startsWith("/")){
            path+="/";
        }
        final String currentPath = rootPath + path;
        final String currentUrl = rootUrl + path;
        final String currentDirPath = path;
        String moveupDirPath = "";
        
        final String str = baseDir.substring(0, baseDir.length() - 1);
        moveupDirPath = str.lastIndexOf("/") >= 0 ? str.substring(0, str.lastIndexOf("/") + 1) : "";
        // 不允许使用..移动到上一级目录
        if (path.indexOf("..") >= 0) {
            this.sendResponseMsg("不允许访问.");
        }
        // 最后一个字符不是/
        if (!"".equals(path) && !path.endsWith("/")) {
            this.sendResponseMsg("参数是无效的.");
        }
        // 目录不存在或不是目录
        final File currentPathFile = new File(currentPath);
        if (!currentPathFile.isDirectory()) {
            this.sendResponseMsg("目录不存在");
        }

        // 遍历目录取的文件信息
        final List<Hashtable> fileList = new ArrayList<Hashtable>();
        if (currentPathFile.listFiles() != null) {
            for (final File file : currentPathFile.listFiles()) {
                final Hashtable<String, Object> hash = new Hashtable<String, Object>();
                final String fileName = file.getName();
                if (file.isDirectory()) {
                    hash.put("is_dir", true);
                    hash.put("has_file", (file.listFiles() != null));
                    hash.put("filesize", 0L);
                    hash.put("is_photo", false);
                    hash.put("filetype", "");
                } else if (file.isFile()) {
                    final String fileExt = fileName.substring(fileName.lastIndexOf('.') + 1).toLowerCase();
                    hash.put("is_dir", false);
                    hash.put("has_file", false);
                    hash.put("filesize", file.length());
                    hash.put("filetype", fileExt);
                }
                hash.put("file_path", baseDir+fileName);
                hash.put("filename", fileName);
                hash.put("datetime", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(file.lastModified()));
                fileList.add(hash);
            }
        }

//        if ("size".equals(order)) {
//            Collections.sort(fileList, new SizeComparator());
//        } else if ("type".equals(order)) {
//            Collections.sort(fileList, new TypeComparator());
//        } else {
//            Collections.sort(fileList, new NameComparator());
//        }
        final Map<String, Object> result = new HashMap<>();
        result.put("parent_dir", moveupDirPath);
        result.put("current_dir_path", currentDirPath);
        result.put("current_url", currentUrl);
        result.put("total_count", fileList.size());
        result.put("file_list", fileList);
        this.sendResponseMsg(new Gson().toJson(result));
    }

    /**
     * 功能: 预览.<br/>
     * date: 2016�?8�?5�? 下午3:59:50 <br/>
     *
     * @author xlzhang@wisdombud.com
     */
    @Action("/online-preview/preview-img")
    public void previewImg() {
        
        final String fileId=request.getParameter("fileId");
        String relativePath=request.getParameter("relativePath");//文件相对地址
        if (!relativePath.startsWith("/")) {
            relativePath = "/" + relativePath;
        }
        relativePath = ROOT_PATH+relativePath;
        String fileName=request.getParameter("fileName");
        
        ServletContext servletContext = ServletActionContext.getServletContext();
        final String serverPathUrl = request.getScheme() + "://" + request.getServerName() + ":"
                                     + request.getServerPort() + request.getContextPath() + "/";
        // 默认保存目录是image
        final String rootPath = servletContext.getRealPath("/");

        List<PdfPreview> pdfPreviews = previewSrv.findByBaseId(fileId);
        if (pdfPreviews.isEmpty()) {
            LOGGER.info(relativePath);
            final String filePath = servletContext.getRealPath(relativePath);
            String fileDir = new Date().getTime() + "";
            String pdfName = "\\" + BASE_ATTACH_DIR + "\\" + fileDir + ".pdf";
            String baseImgDir = BASE_ATTACH_DIR + "\\" + fileDir;
            String pdfPath = rootPath + pdfName;
            // 源文件不存在则返�? -1
            File source = new File(filePath);
            if (!source.exists()) {
                sendFailMsg("", "文件不存在...");
                return;
            }
            // 查扩展名
            final String fileExt = fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();
            if (fileExt.equals("pdf")) {
                PDFchangToImage.changePdfToImg(rootPath, baseImgDir, filePath, new PDF2ImageEvent() {

                    @Override
                    public void after(int i, String fileRelativePath) {
                        // TODO Auto-generated method stub
                        PdfPreview pdfPreview = new PdfPreview(fileId, serverPathUrl + fileRelativePath, i);
                        previewSrv.save(pdfPreview);
                    }
                });
            } else {
                // if (!dest.exists()) {
                LOGGER.info("generate pdf file:source file:" + filePath + ",pdf path:" + pdfPath);
                if (Word2Pdf.word2Pdf(rootPath, filePath, pdfName) == 0) {
                    LOGGER.info("generate image:source file:" + pdfPath + ",imagedir:" + baseImgDir);
                    LOGGER.info("开始pdfToImage...." + System.currentTimeMillis());
                    PDFchangToImage.changePdfToImg(rootPath, baseImgDir, pdfPath, new PDF2ImageEvent() {

                        @Override
                        public void after(int i, String fileRelativePath) {
                            // TODO Auto-generated method stub
                            PdfPreview pdfPreview = new PdfPreview(fileId, serverPathUrl + fileRelativePath, i);
                            previewSrv.save(pdfPreview);
                        }
                    });
                    LOGGER.info("结束pdfToImage...." + System.currentTimeMillis());
                } else {
                    LOGGER.info("转换失败.....");
                }
                // }
            }

            // 目录不存在或不是目录
            File currentPathFile = new File(rootPath + "\\" + baseImgDir);
            LOGGER.debug("check image dir:" + currentPathFile.getAbsolutePath());
            for (int i = 0; i < 600; i++) {
                File testFile = new File(rootPath + "\\" + baseImgDir);
                if (testFile.listFiles() != null && testFile.listFiles().length > 0) {
                    currentPathFile = new File(rootPath + "\\" + baseImgDir);
                    break;
                } else {
                    try {
                        LOGGER.debug(rootPath + "\\" + baseImgDir);
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }
            pdfPreviews =  previewSrv.findByBaseId(fileId);
        }
        sendMsg(true, pdfPreviews, "");

    }
}
