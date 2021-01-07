package com.example.auth.utils;

import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class MyExceptionHandler {

    @ExceptionHandler(BindException.class)
    public R beanPropertyBindingResultHandler(BindException e) {
        BindingResult bindingResult = e.getBindingResult();
        StringBuilder errs = new StringBuilder();
        bindingResult.getFieldErrors().forEach(f -> errs.append(f.getDefaultMessage()).append(";"));
        return R.error(errs.toString());
    }

    //参数校验异常
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public R bindExceptionHandler(MethodArgumentNotValidException e) {
        BindingResult bindingResult = e.getBindingResult();
        StringBuilder errs = new StringBuilder();
        bindingResult.getFieldErrors().forEach(f -> errs.append(f.getDefaultMessage()).append(";"));
        return R.error(errs.toString());
    }


    @ExceptionHandler(Exception.class)
    R handle(Throwable e) {
        e.printStackTrace();
        return R.error("系统故障");
    }

    //自定义异常
    @ExceptionHandler(MyException.class)
    R MyExceptionHandle(MyException e) {
        return R.error(e.getMessage());
    }
}
