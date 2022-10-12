package com.chestnut.reggie.controller;

import com.chestnut.reggie.common.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.UUID;

/**
 * 文件上传下载
 */
@Slf4j
@RestController
@RequestMapping("/common")
public class    CommonController {

    @Value("${reggie.path}")
    private String path;

    /**
     * 文件下载
     * @param name
     * @param response
     */
    @GetMapping("/download")
    public void download(String name, HttpServletResponse response) {
        response.setContentType("image/jpeg");
        try (FileInputStream fis = new FileInputStream(new File(path, name))) {
            try (ServletOutputStream sos = response.getOutputStream();) {
                int len = 0;
                byte[] bytes = new byte[1024];
                while ((len = fis.read(bytes)) != -1) {
                    sos.write(bytes, 0, len);
                }
                sos.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 文件上传
     * @param file
     * @return
     */
    @PostMapping("/upload")
    public R<String> upload(HttpServletRequest request, MultipartFile file) { // 参数名file需与前端input标签name属性一致
        // 设置上传位置
        File destFile = new File(path);
        if(!destFile.exists()) { // 判断路径是否存在
            destFile.mkdirs(); // 创建文件夹
        }

        // 设置文件名称
        String uploadFilename = file.getOriginalFilename();
        String uuid = UUID.randomUUID().toString();
        uploadFilename = uuid + uploadFilename.substring(uploadFilename.lastIndexOf("."));

        // 开始上传文件
        try {
            file.transferTo(new File(path, uploadFilename));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return R.success(uploadFilename);
    }
}
