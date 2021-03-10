package cn.smbms.controller;

import cn.smbms.pojo.Role;
import cn.smbms.pojo.User;
import cn.smbms.service.role.RoleService;
import cn.smbms.service.role.RoleServiceImpl;
import cn.smbms.service.user.UserService;
import cn.smbms.service.user.UserServiceImpl;
import cn.smbms.tools.Constants;
import cn.smbms.tools.PageSupport;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.math.RandomUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * @ClassName UserController
 * @Description TODO
 * @Author javaboy
 * @Date 2021/1/7 15:28
 * @Version 1.0
 **/
@Controller
@RequestMapping("user")
public class UserController extends BaseController{
    @Resource
    private UserService userService;
    @Resource
    RoleService roleService;

    /**
     * 异步验证
     * @ResponseBody
     * @param userCode
     * @return
     */
    @RequestMapping("ucexist")
    @ResponseBody
    public Object ucexist(String userCode){
        boolean flag=false;
        User user=userService.selectUserCodeExist(userCode);
        if (user!=null){
            flag=true;
        }
        Map<String,Object> map=new HashMap<String,Object>();
        map.put("result",flag);
        String jsonStr= JSON.toJSONString(map);
        System.out.println("jsonStr:"+jsonStr);
        return jsonStr;
    }

    /**
     * 同步查询
     * @param id
     * @param model
     * @return
     */
 /*   @RequestMapping("view/{uid}")
    public String view(@PathVariable("uid") String id,Model model){
       User user = userService.getUserById(id);
       model.addAttribute("user",user);
       return "userview";
    }*/

    /**
     * 异步查询
     * 当使用json格式将对象传输至页面时,中文发生乱码,
     * 原因是当传输的数据媒体类型MIME为json时,底层传输
     * 默认采用编码格式为ISO8859-1,所以需要将编码格式修正为
     * UTF-8
     *  produces:仅针对当前方法有效
     * @param id
     * @param model
     * @return
     */
    @RequestMapping(value = "view",method = RequestMethod.GET,produces = {"application/json;charset=UTF-8"})
    @ResponseBody
    public Object view(String id,Model model){
        User user = userService.getUserById(id);
        model.addAttribute("user",user);
        String userJson=JSON.toJSONString(user);
        System.out.println("userJson:"+userJson);
        return userJson;
    }

    /**
     * 在功能上add2和add3是一样
     * @param user
     * @return
     */
  /*  @RequestMapping("add2")
    public String add2(User user,Model model){
        model.addAttribute("user",user);
        return "useradd2";
    }*/

    @RequestMapping("add2")
    public String add2(@ModelAttribute("user") User user){

        return "useradd2";
    }

   /* @RequestMapping("add3")
    public String add3(User user,Model model){
        model.addAttribute("user2",user);
        return "useradd2";
    }*/




    @RequestMapping("delete")
    public String delete(@RequestParam(value = "id",required = true) Integer id,Model model,HttpServletRequest request){
        boolean flag = userService.deleteUserById(id);
        if (!flag){
            model.addAttribute("message","删除失败");
            System.out.println("删除失败");
            request.getSession().setAttribute("message","删除失败!");
        }
        //model.addAttribute("message","删除失败");
        return "redirect:/user/query";

    }


    @RequestMapping("add")
    public String add(){
        return "useradd";
    }

    @RequestMapping("addsave")
    public String addsave(User user,
                          HttpServletRequest request,
                          @RequestParam(value = "multipartFile",required = false) MultipartFile[] multipartFiles,
                          Model model){
        /**
         * 企业中若有大量图片,图片服务器,nginx(可以做前端服务器,图片服务器),
         * 存的是绝对路径,图片在服务器中(Linux,没有c,d盘,目录,所有的东西都是文件,目录也是文件,mp4也是文件 )
         * 将图片存进某一个绝对路径中去(realPath),但是要将图片显示在页面上,]
         * 只能通过tomcat服务器去拿,不能直接通过绝对路径去拿到该图片,所以我们通过localhost:8080/uploads/javaboy.jpg
         * 1.判断是否有文件上传操作
         * 2.1 若没有文件上传,则按之前用户新增的逻辑走就可以了
         * 2.2 若有文件,则先对文件进行判断,若符合要求,则进行文件上传操作后,再进行用户新增
         */
        String path="";//是我图片将要存储的路径
        /**
         * 若文件不为空,
         * 则先对文件进行判断,
         * 若符合要求
         * 则进行文件上传相关的操作
         */
        //1.若文件不为空
        int i=0;
        System.out.println("multipartFiles.length:"+multipartFiles.length);
        for (MultipartFile multipartFile:multipartFiles) {
            i++;
            System.out.println("multipartFile:"+i+":"+multipartFile);
            if (!multipartFile.isEmpty()){
                /**
                 * 1,判断 大小,在java代码上也可以控制
                 * 2.后缀名判断 作用? .jpg,png jpeg,pneg 不符合证件照规范,则不予通过
                 */
                String tempPath=request.getSession().getServletContext().getRealPath("uploads"+ File.separator+"images");
                System.out.println("tempPath:"+tempPath);
                String fileName=multipartFile.getOriginalFilename();
                System.out.println("fileName:"+fileName);//javaboy.jpg
                String suffix= FilenameUtils.getExtension(fileName);
                System.out.println("suffix:"+suffix);
                String[] pictureFormat={"jpg","jpeg","png","pneg"};

                if (Arrays.asList(pictureFormat).contains(suffix)){
                    //若为真则证明文件符合格式,则继续进行大小的判断
                    //获取上传文件的大小
                    long fileSize=multipartFile.getSize();
                    long limitSize=524288;//500kb
                    if(fileSize<limitSize){
                        //上传文件大小满足限定  前面搞随机数_fileName.png
                        String newFileName= RandomUtils.nextInt(1000000)+"_idPicPath_"+fileName;
                        //因为兼容其他操作系统,Linux,mac
                        String picServerPath=tempPath+File.separator+newFileName;
                        File file=new File(picServerPath);
                        if (!file.getParentFile().exists()){
                            file.getParentFile().mkdirs();//递归创建
                        }
                        //上传
                        try {
                            multipartFile.transferTo(file);
                            System.out.println("文件上传到服务器成功!");
                            //用户数据库中要记录我们路径信息
                            if (i==1){
                                user.setIdPicPath(newFileName);
                            }
                            if (i==2){
                                user.setWorkPicPath(newFileName);
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                            model.addAttribute("fileUploadMessage","文件上传失败!");
                            return "useradd";
                        }

                    }else{
                        System.out.println("上传文件大小超过指定值500kb");
                        model.addAttribute("fileUploadMessage","上传文件大小超过指定值500kb");
                        return "useradd";
                    }
                }else{
                    System.out.println("文件类型异常,请重新上传!");
                    model.addAttribute("fileUploadMessage","文件类型异常,请重新上传!");
                    return "useradd";
                }

            }

        }


        /**
         * 1.创建人:当前用户被谁创建
         * 2.创建时间
         */
        //将当前对象被谁(谁的id)创建
        user.setCreatedBy(((User)request.getSession().getAttribute(Constants.USER_SESSION)).getId());
        user.setCreationDate(new Date());
        boolean flag= userService.add(user);
       if (!flag){
           model.addAttribute(Constants.SYS_MESSAGE,"新增失败!");
           return "useradd";
       }
        return "redirect:/user/query";
    }


 /*   @RequestMapping("addsave")
    public String addsave(@Valid User user, BindingResult bindingResult, Model model){
        if (bindingResult.hasErrors()){
            System.out.println("是否进入了异常判断!!!");
            return "useradd2";
        }

        boolean flag= userService.add(user);
            if (!flag){
                model.addAttribute(Constants.SYS_MESSAGE,"新增失败!");
                return "useradd2";
            }
            return "redirect:/user/query";


    }*/

    /**
     * 跳转到登录页面
     * @return
     */
    @RequestMapping("login")
    public String login(){
        //会经过视图解析器 /WEB-INF/jsp
        return "redirect:/login.jsp";
    }

    @RequestMapping("dologin")
    public String doLogin(String userCode,
                          String userPassword,
                          Model model,
                          HttpServletRequest request){
                User user=userService.login(userCode,userPassword);
        System.out.println("user:"+user);
        System.out.println("userCode:"+userCode);
        System.out.println("userPassword:"+userPassword);
            //比对用户名和密码是否一致
            if (user!=null){
                //登录成功,在session保存user
                request.getSession().setAttribute(Constants.USER_SESSION,user);
                return "frame";
            } else{
                System.out.println("用户名或密码错误!");
                //request.getSession().setAttribute(Constants.SYS_MESSAGE,"用户名或密码错误!");
                model.addAttribute(Constants.SYS_MESSAGE,"用户名或密码错误!");
                //throw  new RuntimeException("用户名或密码错误");
                return "forward:/login.jsp";
            }
    }


    /**
     * 注销
     */
    @RequestMapping("logout")
    public String logout(HttpServletRequest request){
        request.getSession().invalidate();
        //希望是重定向,若加上redirect,则做重定向,不再经过视图解析器
        return "redirect:/user/login";
    }


    @RequestMapping("query")
    public String query(String queryname,
                        String queryUserRole,
                        String pageIndex,
                        Model model){
        //查询用户列表
        String queryUserName = queryname;
        String temp = queryUserRole;
        int queryUserRole2 = 0;

        List<User> userList = null;
        //设置页面容量
        int pageSize = Constants.pageSize;
        //当前页码
        int currentPageNo = 1;

        System.out.println("queryUserName servlet--------"+queryUserName);
        System.out.println("queryUserRole servlet--------"+queryUserRole);
        System.out.println("query pageIndex--------- > " + pageIndex);
        if(queryUserName == null){
            queryUserName = "";
        }
        if(temp != null && !temp.equals("")){
            queryUserRole2 = Integer.parseInt(temp);
        }

        if(pageIndex != null){
            try{
                currentPageNo = Integer.valueOf(pageIndex);
            }catch(NumberFormatException e){
                return "redirect:/error.jsp";
            }
        }
        //总数量（表）
        int totalCount	= userService.getUserCount(queryUserName,queryUserRole2);
        //总页数
        PageSupport pages=new PageSupport();
        pages.setCurrentPageNo(currentPageNo);
        pages.setPageSize(pageSize);
        pages.setTotalCount(totalCount);

        int totalPageCount = pages.getTotalPageCount();

        //控制首页和尾页
        if(currentPageNo < 1){
            currentPageNo = 1;
        }else if(currentPageNo > totalPageCount){
            currentPageNo = totalPageCount;
        }


        userList = userService.getUserList(queryUserName,queryUserRole2,currentPageNo, pageSize);
        model.addAttribute("userList", userList);
        List<Role> roleList = null;
        roleList = roleService.getRoleList();
        model.addAttribute("roleList", roleList);
        model.addAttribute("queryUserName", queryUserName);
        model.addAttribute("queryUserRole", queryUserRole);
        model.addAttribute("totalPageCount", totalPageCount);
        model.addAttribute("totalCount", totalCount);
        model.addAttribute("currentPageNo", currentPageNo);
        return "userlist";
    }


    //局部异常处理:针对本Controller出现的异常有效
   /* @ExceptionHandler(java.lang.RuntimeException.class)
    public String handlerException(Model model){
        model.addAttribute("message","网络繁忙,请稍后再试!");
        //1.希望携带信息,又不要经过视图解析器
        return "forward:/error.jsp";
    }*/

    @RequestMapping("/test")
    public void test(){
        //throw new ArithmeticException("算数异常");
        int i=1/0;
    }
}
