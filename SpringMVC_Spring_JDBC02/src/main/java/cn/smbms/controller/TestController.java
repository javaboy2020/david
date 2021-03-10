package cn.smbms.controller;

import cn.smbms.pojo.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @ClassName TestController
 * @Description TODO
 * @Author javaboy
 * @Date 2021/1/11 14:27
 * @Version 1.0
 **/
@Controller
@RequestMapping("test")
public class TestController {
    @RequestMapping("error")
    public void testError(){
        int i=1/0;
    }

    @RequestMapping("error2")
    public void testError2(){
        User user=null;
        System.out.println(user.equals("java"));
    }


    //@ExceptionHandler(java.lang.ArithmeticException.class)
    public String handlerException(Model model){
        model.addAttribute("message","触发了算术异常!");
        //1.希望携带信息,又不要经过视图解析器
        return "forward:/error.jsp";
    }
}
