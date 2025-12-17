package com.ecommerce.product.controller;

import com.ecommerce.common.result.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

/**
 * 文件上传控制器
 */
@RestController
@RequestMapping("/upload")
@Slf4j
public class FileUploadController {

    @Value("${file.upload.path:./uploads}")
    private String uploadPath;

    @Value("${file.upload.url-prefix:/uploads}")
    private String urlPrefix;

    /**
     * 上传图片
     */
    @PostMapping("/image")
    public Result<String> uploadImage(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return Result.error("请选择要上传的文件");
        }

        // 检查文件类型
        String contentType = file.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            return Result.error("只能上传图片文件");
        }

        // 检查文件大小（最大5MB）
        if (file.getSize() > 5 * 1024 * 1024) {
            return Result.error("图片大小不能超过5MB");
        }

        try {
            // 获取文件扩展名
            String originalFilename = file.getOriginalFilename();
            String extension = "";
            if (originalFilename != null && originalFilename.contains(".")) {
                extension = originalFilename.substring(originalFilename.lastIndexOf("."));
            }

            // 生成唯一文件名
            String newFilename = UUID.randomUUID().toString().replace("-", "") + extension;

            // 确保上传目录存在
            Path uploadDir = Paths.get(uploadPath);
            if (!Files.exists(uploadDir)) {
                Files.createDirectories(uploadDir);
            }

            // 保存文件
            Path filePath = uploadDir.resolve(newFilename);
            file.transferTo(filePath.toFile());

            // 返回访问URL
            String imageUrl = urlPrefix + "/" + newFilename;
            log.info("图片上传成功: {}", imageUrl);
            return Result.success(imageUrl);

        } catch (IOException e) {
            log.error("图片上传失败", e);
            return Result.error("图片上传失败: " + e.getMessage());
        }
    }
}

