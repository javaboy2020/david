package cn.smbms.controller;

import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @ClassName BaseController
 * @Description TODO
 * @Author javaboy
 * @Date 2021/1/19 15:30
 * @Version 1.0
 **/
public class BaseController {

    @InitBinder
    public void initBinder(WebDataBinder dataBinder){
        System.out.println("======init binder=====");
        dataBinder.registerCustomEditor(Date.class,new CustomDateEditor(new SimpleDateFormat("yyyy-MM-dd"),true));
    }

}
