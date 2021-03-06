package com.site.common.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * (BAuthorBasedata)实体类
 *
 * @author lmk
 * @since 2020-12-21 15:52:42
 */
@Data
public class BAuthorBasedata implements Serializable {
    private static final long serialVersionUID = -65264593752892001L;
    /**
     * 作者的UUID（唯一）
     */
    @TableId
    private Long bvUpuuid;

    /**
     * 作者的头像
     */
    private String bvFaceUrl;

    /**
     * 作者的昵称
     */
    private String bvName;



}