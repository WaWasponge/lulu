package org.lhh.spongehome.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;
import lombok.Data;

/**
 * @TableName picture
 */
@TableName(value ="picture")
@Data
public class Picture {
    private Long id;

    private String url;

    private String name;

    private String introduction;

    private String category;

    private String tags;

    private Long picsize;

    private Integer picwidth;

    private Integer picheight;

    private Double picscale;

    private String picformat;

    private Long userid;

    private Date createtime;

    private Date edittime;

    private Date updatetime;

    private Integer isdelete;
}