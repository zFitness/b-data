package com.site.bdata.controller;

import com.site.bdata.entity.BVideoRank;
import com.site.bdata.service.BVideoRankService;
import org.springframework.web.bind.annotation.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Resource;

/**
 * (BVideoRank)表控制层
 *
 * @author lmk
 * @since 2020-06-03 10:20:01
 */
@Api(tags = "(BVideoRank)") 
@RestController
@RequestMapping("bVideoRank")
public class BVideoRankController {
    /**
     * 服务对象
     */
    @Resource
    private BVideoRankService bVideoRankService;


}