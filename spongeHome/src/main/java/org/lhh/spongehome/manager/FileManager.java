package org.lhh.spongehome.manager;

import com.qcloud.cos.COSClient;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.lhh.spongehome.config.CosClientConfig;
import org.lhh.spongehome.model.DTO.file.UploadFileRequest;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class FileManager {

    @Resource
    private CosClientConfig cosClientConfig;

    @Resource
    private COSClient cosClient;

    public UploadPictureResult uploadPicture(UploadFileRequest request) {
        // 创建上传文件对象

    }
}
