package org.lhh.spongehome.manager;


import com.qcloud.cos.COSClient;
import com.qcloud.cos.model.COSObject;
import com.qcloud.cos.model.GetObjectRequest;
import com.qcloud.cos.model.PutObjectRequest;
import com.qcloud.cos.model.PutObjectResult;
import com.qcloud.cos.model.ciModel.persistence.PicOperations;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.lhh.spongehome.config.CosClientConfig;
import org.springframework.stereotype.Component;

import java.io.File;

/**
 * Cos 对象存储操作
 */
@Component
@Slf4j
public class CosManager {

    @Resource
    private CosClientConfig cosClientConfig;

    @Resource
    private COSClient cosClient;

    //设置操作cos的方法
    /**
     * 上传对象
     *
     * @param key 唯一键
     * @param file 本地文件
     * @return
     */
    public PutObjectResult putObject(String key, File file) {
        PutObjectRequest putObjectRequest = new PutObjectRequest(cosClientConfig.getBucket(), key,
                file);
        return cosClient.putObject(putObjectRequest);
    }

    /**
     * 下载对象
     * @param key 文件
     * @return
     */
    public COSObject getObject(String key) {
        GetObjectRequest getObjectRequest = new GetObjectRequest(cosClientConfig.getBucket(), key);
        return cosClient.getObject(getObjectRequest);
    }


    /**
     * 删除文件
     * @param file
     * @param filepath
     * @return
     */
    public void deleteObject(File file,String filepath) {

        boolean delete = file.delete();
        if (!delete) {
            log.error("file delete error, filepath = {}", filepath);
        }
        cosClient.shutdown();
    }
}
