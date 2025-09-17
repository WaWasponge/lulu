package org.lhh.spongehome.model.DTO.picture;

import lombok.Data;

import java.io.Serializable;

/**
 * 对于图片需要重复上传，基础信息不变，图片信息需要更新
 * 图片id参数
 * @author lhh
 */
@Data
public class PictureUploadRequest implements Serializable {

    /**
     * 图片 id （用于修改）
     */
    private Long id;

    private static final long serialVersionUID = 1L;
}
