package com.site.permission.security.process;

import com.alibaba.fastjson.JSON;
import com.site.common.web.response.Result;
import com.site.component.utils.servlet.ServletUtil;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Describe: 自定义 Security 用户暂无权限处理类
 * Author: 就 眠 仪 式
 * CreateTime: 2019/10/23
 * */
@Component
public class SecurityAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AccessDeniedException e) throws IOException, ServletException {
        if(ServletUtil.isAjax(httpServletRequest)){
            Result result = Result.failure(403,"暂无权限");
            ServletUtil.write(JSON.toJSONString(result));
        }else{
            httpServletResponse.sendRedirect("/error/403");
        }
    }
}
