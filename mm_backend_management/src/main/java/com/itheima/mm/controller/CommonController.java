package com.itheima.mm.controller;

import com.itheima.framework.anno.Controller;
import com.itheima.framework.anno.RequestMapping;
import com.itheima.mm.entity.Result;
import com.itheima.mm.utils.UploadUtils;
import com.itheima.mm.utils.JsonUtils;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.IOUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * @author By--tgl
 * @time 2021/2/1$ 15:16$
 * @Version: 1.0
 * @Description Java类作用描述:
 */
@Controller
public class CommonController {
    @RequestMapping("/common/uploadFile")
    public void uploadFile(HttpServletRequest request, HttpServletResponse response) throws IOException {
        InputStream is = null;
        FileOutputStream os = null;
        try {
            //1. 获取客户端上传的图片
            //创建磁盘工厂对象
            DiskFileItemFactory itemFactory = new DiskFileItemFactory();
            //创建Servlet的上传解析对象,构造方法中,传递磁盘工厂对象
            ServletFileUpload fileUpload = new ServletFileUpload(itemFactory);
            /*
             * fileUpload调用方法 parseRequest,解析request对象
             * 页面可能提交很多内容 文本框,文件,菜单,复选框 是为FileItem对象
             * 返回集合,存储的文件项对象
             */
            List<FileItem> list = fileUpload.parseRequest(request);
            String imgUrl = null;
            for (FileItem fileItem : list) {
                if (!fileItem.isFormField()) {
                    //是上传的文件
                    //获取文件名
                    String fileName = fileItem.getName();
                    //获取上传的文件
                    is = fileItem.getInputStream();

                    //创建一个输出流，输出文件到指定的目录
                    //使用字节输出流输出到那个文件夹(img/upload)
                    String uploadPath = request.getServletContext().getRealPath("img/upload");
                    //创建一个File
                    File file = new File(uploadPath);
                    if (!file.exists()) {
                        //这个目录不存在
                        //将这个目录创建出来
                        file.mkdirs();
                    }
                    //文件名会不会重复呢????最好的办法是生成唯一的文件名
                    String uuidName = UploadUtils.getUUIDName(fileName);
                    os = new FileOutputStream(new File(file, uuidName));

                    //将输入流中的文件，通过输出流写到指定的目录中
                    IOUtils.copy(is, os);

                    //获取文件的存储路径imgUrl
                    imgUrl = "img/upload/" + uuidName;
                }
            }
            JsonUtils.printResult(response, new Result(true, "图片上传成功", imgUrl));
        } catch (Exception e) {
            e.printStackTrace();
            JsonUtils.printResult(response, new Result(false, "图片上传失败"));
        } finally {
            is.close();
            os.close();
        }
    }
}

