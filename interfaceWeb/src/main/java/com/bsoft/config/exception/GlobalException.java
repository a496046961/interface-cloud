package com.bsoft.config.exception;

import com.bsoft.constants.InterfaceConstants;
import com.bsoft.exception.CommonException;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author ：hxy
 * @date ：Created in 2019/08/07 15:06
 * @description：需要创建一个能够处理异常的全局异常类
 * @modified By：
 * @version: 1$
 */
@ResponseBody
@ControllerAdvice
public class GlobalException {
    private static Logger logger = LoggerFactory.getLogger(GlobalException.class);

    /**
     * 处理Controller抛出的异常
     *
     * @param e 异常实例
     * @return Controller层的返回值
     */
    @ExceptionHandler
    @ResponseBody
    public Object expHandler(Exception e)   throws Exception {
        logger.error(e.toString());
        JSONObject json = new JSONObject();
        json.put("data", "");
        json.put("code", 99);
        json.put("msg", "系统错误");
        if (e instanceof CommonException) {
            json.put("msg", e.getMessage());
            json.put("code", 97);
        }
        else if(e instanceof MethodArgumentNotValidException) {
            json.put("msg", ((MethodArgumentNotValidException) e).getBindingResult().getFieldError().getDefaultMessage());
            json.put("code", InterfaceConstants.PARAMETER_NOTNULL);
        }
        else if (e.getMessage().contains("ORA-00001")) {
            json.put("msg", "写入失败，有重复数据");
        }else {
            throw e;
        }
        return json.toString();
    }
}
