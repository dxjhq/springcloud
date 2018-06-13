package com.hhly.web.web;

import com.hhly.api.dto.ResultObject;
import com.hhly.api.exception.ValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;


/**
* @author wangxianchen
* @create 2017-09-27
* @desc  处理全局异常的控制类。统一处理controller抛出的异常
*/
public class BaseExceptionController {
    
    private Logger logger = LoggerFactory.getLogger(BaseExceptionController.class);
    
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public ResultObject allException(Exception e, HttpServletResponse response)throws IOException{
        ResultObject ro;
      if(e instanceof ValidationException){
          ValidationException instant = (ValidationException)e;
          ro = new ResultObject();
          ro.fail(instant.getErrorCodeEnum());
          ro.setData(instant.getParam());

      }else if(e instanceof MethodArgumentNotValidException){
          MethodArgumentNotValidException instant = (MethodArgumentNotValidException)e;
          ro = new ResultObject();
          handleMessage(instant.getBindingResult(),ro);

      }else if(e instanceof HttpRequestMethodNotSupportedException){
          HttpRequestMethodNotSupportedException instant = (HttpRequestMethodNotSupportedException)e;
          ro = new ResultObject();
          ro.fail(instant.getMessage());

      }else if(e instanceof HttpMessageNotReadableException){
          //HttpMessageNotReadableException instant = (HttpMessageNotReadableException)e;
          //ro.fail(instant.getMessage());
          ro = new ResultObject();
          ro.fail("参数不合法");
      }else if(e instanceof BindException){
          ro = new ResultObject();
          handleMessage(((BindException) e).getBindingResult(),ro);
      }else{
          ro = new ResultObject();
          ro.doFail();
      }
      logger.error("BaseExceptionController:ResultObject="+ro,e);
      return ro;
    }


    private void handleMessage(BindingResult result,ResultObject ro) throws IOException {
        List<ObjectError> errorList = result.getAllErrors();
        StringBuilder sb = new StringBuilder();
        for(ObjectError objError:errorList){
            FieldError fieldError = (FieldError) objError;
            //String fieldName = fieldError.getField();
            String errorMsg = fieldError.getDefaultMessage();
            //sb.append(fieldName);
            //sb.append("=");
            sb.append(errorMsg);
            sb.append(" ");
        }
        ro.fail(sb.toString());
    }
}
