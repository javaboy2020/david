1.SpringMVC_Spring_JDBC
JDBC手写DAO层,封装少,所以性能高
Hibernate
Mybatis
JDBC

步骤:
1.创建web项目并引入依赖
```$xslt
<dependencies>
    <!--DAO-->
    <dependency>
      <groupId>mysql</groupId>
      <artifactId>mysql-connector-java</artifactId>
      <version>5.1.48</version>
    </dependency>

    <dependency>
      <groupId>org.springframework</groupId>
      <artifactId>spring-jdbc</artifactId>
      <version>5.1.12.RELEASE</version>
    </dependency>

    <dependency>
      <groupId>commons-dbcp</groupId>
      <artifactId>commons-dbcp</artifactId>
      <version>1.4</version>
    </dependency>
    <dependency>
      <groupId>commons-pool</groupId>
      <artifactId>commons-pool</artifactId>
      <version>1.6</version>
    </dependency>
    <dependency>
      <groupId>commons-dbutils</groupId>
      <artifactId>commons-dbutils</artifactId>
      <version>1.6</version>
    </dependency>
    <!--Service-->
    <dependency>
      <groupId>org.springframework</groupId>
      <artifactId>spring-context</artifactId>
      <version>5.1.12.RELEASE</version>
    </dependency>

    <!--切面支持-->
    <dependency>
      <groupId>org.springframework</groupId>
      <artifactId>spring-aop</artifactId>
      <version>5.1.12.RELEASE</version>
    </dependency>
    <dependency>
      <groupId>aopalliance</groupId>
      <artifactId>aopalliance</artifactId>
      <version>1.0</version>
    </dependency>
    <dependency>
      <groupId>org.aspectj</groupId>
      <artifactId>aspectjweaver</artifactId>
      <version>1.8.13</version>
    </dependency>


    <!--事务支持-->
    <dependency>
      <groupId>org.springframework</groupId>
      <artifactId>spring-tx</artifactId>
      <version>5.1.12.RELEASE</version>
    </dependency>
    <!--controller-->

    <dependency>
      <groupId>org.springframework</groupId>
      <artifactId>spring-web</artifactId>
      <version>5.1.12.RELEASE</version>
    </dependency>
    <dependency>
      <groupId>org.springframework</groupId>
      <artifactId>spring-webmvc</artifactId>
      <version>5.1.12.RELEASE</version>
    </dependency>
    <dependency>
      <groupId>javax.servlet</groupId>
      <artifactId>javax.servlet-api</artifactId>
      <version>3.1.0</version>
      <scope>provided</scope>
    </dependency>
    <!--jsp
    若不指定scope,默认是在编译,测试及运行时(runtime)都生效
    -->
    <dependency>
      <groupId>javax.servlet.jsp</groupId>
      <artifactId>javax.servlet.jsp-api</artifactId>
      <version>2.3.3</version>
      <scope>provided</scope>
    </dependency>

    <dependency>
      <groupId>jstl</groupId>
      <artifactId>jstl</artifactId>
      <version>1.2</version>
    </dependency>




    <dependency>
      <groupId>junit</groupId>
      <artifactId>junit</artifactId>
      <version>4.12</version>
      <scope>test</scope>
    </dependency>

    <dependency>
      <groupId>log4j</groupId>
      <artifactId>log4j</artifactId>
      <version>1.2.17</version>
    </dependency>

  </dependencies>

```

2.写基本的配置文件
database.properties
log4j.properties
applicationContext.xml
springmvc-servlet.xml

database.properties
```$xslt
driver=com.mysql.jdbc.Driver
url=jdbc:mysql://127.0.0.1:3306/smbms?useUnicode=true&characterEncoding=UTF-8
user=root
password=123456
```

log4j.properties
```$xslt
### set log levels - for more verbose logging change 'info' to 'debug' ###
log4j.rootLogger=debug,stdout,file

### direct log messages to stdout ###
log4j.appender.stdout=org.apache.log4j.ConsoleAppender
log4j.appender.stdout.Target=System.err
log4j.appender.stdout.layout=org.apache.log4j.PatternLayout
log4j.appender.stdout.layout.ConversionPattern=%d{ABSOLUTE} %5p %c{1}:%L - %m%n

### direct messages to file mylog.log ###
log4j.appender.file=org.apache.log4j.FileAppender
log4j.appender.file.File=boslog.log
log4j.appender.file.layout=org.apache.log4j.PatternLayout
log4j.appender.file.layout.ConversionPattern=%d{ABSOLUTE} %5p %c{1}:%L - %m%n

```
applicationContext.xml
```$xslt
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:context="http://www.springframework.org/schema/context"
       xmlns:aop="http://www.springframework.org/schema/aop"
       xmlns:tx="http://www.springframework.org/schema/tx"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xsi:schemaLocation="
       http://www.springframework.org/schema/beans
       http://www.springframework.org/schema/beans/spring-beans.xsd
       http://www.springframework.org/schema/context
       http://www.springframework.org/schema/context/spring-context.xsd
       http://www.springframework.org/schema/aop
       http://www.springframework.org/schema/aop/spring-aop.xsd
       http://www.springframework.org/schema/tx
       http://www.springframework.org/schema/tx/spring-tx.xsd">
    <!--扫描组件-->
    <context:component-scan base-package="cn.smbms.dao,cn.smbms.service"/>


    <!--事务配置,默认在service中手动处理-->

</beans>
```

springmvc-servlet.xml
```$xslt
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:context="http://www.springframework.org/schema/context"
       xmlns:aop="http://www.springframework.org/schema/aop"
       xmlns:mvc="http://www.springframework.org/schema/mvc"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xsi:schemaLocation="
       http://www.springframework.org/schema/beans
       http://www.springframework.org/schema/beans/spring-beans.xsd
       http://www.springframework.org/schema/context
       http://www.springframework.org/schema/context/spring-context.xsd
       http://www.springframework.org/schema/aop
       http://www.springframework.org/schema/aop/spring-aop.xsd
       http://www.springframework.org/schema/mvc
       http://www.springframework.org/schema/mvc/spring-mvc.xsd">
    <!--组件扫描-->
    <context:component-scan base-package="cn.smbms.controller"/>

    <!--开启注解支持-->
    <mvc:annotation-driven/>
    
    <!--视图解析器-->
    <bean class="org.springframework.web.servlet.view.InternalResourceViewResolver">
        <property name="prefix" value="/WEB-INF/jsp/"/>
        <property name="suffix" value=".jsp"/>
    </bean>

</beans>
```
web.xml
````$xslt
<?xml version="1.0" encoding="UTF-8"?>
<web-app xmlns="http://xmlns.jcp.org/xml/ns/javaee"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://xmlns.jcp.org/xml/ns/javaee http://xmlns.jcp.org/xml/ns/javaee/web-app_3_1.xsd"
         version="3.1">

<!--SpringMVC-->
    <servlet>
        <servlet-name>dispatcherServlet</servlet-name>
        <servlet-class>org.springframework.web.servlet.DispatcherServlet</servlet-class>
        <init-param>
            <param-name>contextConfigLocation</param-name>
            <param-value>classpath:springmvc-servlet.xml</param-value>
        </init-param>
        <load-on-startup>1</load-on-startup>
    </servlet>
    <servlet-mapping>
        <servlet-name>dispatcherServlet</servlet-name>
        <url-pattern>/</url-pattern>
    </servlet-mapping>
<!--Spring-->
    <context-param>
        <param-name>contextConfigLocation</param-name>
        <param-value>classpath:applicationContext.xml</param-value>
    </context-param>
    
    <!--监听器监听Spring配置文件-->
    <listener>
        <listener-class>org.springframework.web.context.ContextLoaderListener</listener-class>
    </listener>
    <!--Filter-->
</web-app>

````
二.实体类
```$xslt
package cn.smbms.pojo;

import java.util.Date;

/**
 * @ClassName User
 * @Description TODO
 * @Author javaboy
 * @Date 2020/12/10 10:36
 * @Version 1.0
 **/
public class User {
    private Long id;
    private String userCode;
    private String userName;
    private String userPassword;
    private Integer gender;
    private Date birthday;
    private String phone;
    private String address;
    private Integer userRole;
    private Long createdBy;
    private Date CreationDate;
    private Long modifyBy;
    private Date modifyDate;
    private String idPicPath;
    private String workPicPath;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserCode() {
        return userCode;
    }

    public void setUserCode(String userCode) {
        this.userCode = userCode;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public Integer getGender() {
        return gender;
    }

    public void setGender(Integer gender) {
        this.gender = gender;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Integer getUserRole() {
        return userRole;
    }

    public void setUserRole(Integer userRole) {
        this.userRole = userRole;
    }

    public Long getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Long createdBy) {
        this.createdBy = createdBy;
    }

    public Date getCreationDate() {
        return CreationDate;
    }

    public void setCreationDate(Date creationDate) {
        CreationDate = creationDate;
    }

    public Long getModifyBy() {
        return modifyBy;
    }

    public void setModifyBy(Long modifyBy) {
        this.modifyBy = modifyBy;
    }

    public Date getModifyDate() {
        return modifyDate;
    }

    public void setModifyDate(Date modifyDate) {
        this.modifyDate = modifyDate;
    }

    public String getIdPicPath() {
        return idPicPath;
    }

    public void setIdPicPath(String idPicPath) {
        this.idPicPath = idPicPath;
    }

    public String getWorkPicPath() {
        return workPicPath;
    }

    public void setWorkPicPath(String workPicPath) {
        this.workPicPath = workPicPath;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", userCode='" + userCode + '\'' +
                ", userName='" + userName + '\'' +
                ", userPassword='" + userPassword + '\'' +
                ", gender=" + gender +
                ", birthday=" + birthday +
                ", phone='" + phone + '\'' +
                ", address='" + address + '\'' +
                ", userRole=" + userRole +
                ", createdBy=" + createdBy +
                ", CreationDate=" + CreationDate +
                ", modifyBy=" + modifyBy +
                ", modifyDate=" + modifyDate +
                ", idPicPath='" + idPicPath + '\'' +
                ", workPicPath='" + workPicPath + '\'' +
                '}';
    }
}

```
Dao层
BaseDao,UserDao,UserDaoImpl

day73 spring_springmvc_jdbc继续整合
````$xslt
    <!--所有静态资源请求,包括js,css,images等默认均会被SpringMVC的DispatcherServlet拦截,需要放行-->
    <mvc:resources mapping="/css/**" location="/css/"/>
    <mvc:resources mapping="/js/**" location="/js/"/>
    <mvc:resources mapping="/images/**" location="/images/"/>
    <mvc:resources mapping="/calendar/**" location="/calendar/"/>
````
编码错误解决方案,将编码convert到GBK,再Convert到UTF-8,可解决该异常
```$xslt
[ERROR] Failed to execute goal org.apache.maven.plugins:maven-compiler-plugin:3.1:compile (default-compile) on project SpringMVC_Spring_JDBC: Compilation failure: Compilation failure: 
[ERROR] /D:/00_1917JA/day73_SpringMVC04_SpringMVC+Spring+JDBC02/teach-demo/SpringMVC_Spring_JDBC02/src/main/java/cn/smbms/dao/user/UserDaoImpl.java:[1,1] �Ƿ��ַ�: '\ufeff'
[ERROR] /D:/00_1917JA/day73_SpringMVC04_SpringMVC+Spring+JDBC02/teach-demo/SpringMVC_Spring_JDBC02/src/main/java/cn/smbms/dao/user/UserDaoImpl.java:[1,10] ��Ҫclass, interface��enum
[ERROR] /D:/00_1917JA/day73_SpringMVC04_SpringMVC+Spring+JDBC02/teach-demo/SpringMVC_Spring_JDBC02/src/main/java/cn/smbms/dao/user/UserDao.java:[1,1] �Ƿ��ַ�: '\ufeff'
[ERROR] /D:/00_1917JA/day73_SpringMVC04_SpringMVC+Spring+JDBC02/teach-demo/SpringMVC_Spring_JDBC02/src/main/java/cn/smbms/dao/user/UserDao.java:[1,10] ��Ҫclass, interface��enum
[ERROR] /D:/00_1917JA/day73_SpringMVC04_SpringMVC+Spring+JDBC02/teach-demo/SpringMVC_Spring_JDBC02/src/main/java/cn/smbms/service/user/UserServiceImpl.java:[1,1] �Ƿ��ַ�: '\ufeff'
[ERROR] /D:/00_1917JA/day73_SpringMVC04_SpringMVC+Spring+JDBC02/teach-demo/SpringMVC_Spring_JDBC02/src/main/java/cn/smbms/service/user/UserServiceImpl.java:[1,10] ��Ҫclass, interface��enum
[ERROR] /D:/00_1917JA/day73_SpringMVC04_SpringMVC+Spring+JDBC02/teach-demo/SpringMVC_Spring_JDBC02/src/main/java/cn/smbms/service/user/UserService.java:[1,1] �Ƿ��ַ�: '\ufeff'
[ERROR] /D:/00_1917JA/day73_SpringMVC04_SpringMVC+Spring+JDBC02/teach-demo/SpringMVC_Spring_JDBC02/src/main/java/cn/smbms/service/user/UserService.java:[1,10] ��Ҫclass, interface��enum
```
作业:使用SpringMVC+Spring+JDBC改造,完成用户模块的增删改查

day74
局部异常和全局异常是互补的关系
局部异常优先于全局异常,当局部异常触发,全局异常无效;
可以在全局异常中定义通用的异常类,例如:java.lang.Exception,java.lang.RuntimeException
可以在局部异常针对某些特殊的异常进行详细的信息提醒

局部异常的配置方式:
```
  @ExceptionHandler(java.lang.ArithmeticException.class)
    public String handlerException(Model model){
        model.addAttribute("message","触发了算术异常!");
        //1.希望携带信息,又不要经过视图解析器
        return "forward:/error.jsp";
    }
```

全局异常的配置方式:
````
 <!--全局异常处理:对所有的Controller运行时异常生效-->
    <bean id="exceptionResolver" class="org.springframework.web.servlet.handler.SimpleMappingExceptionResolver">
        <property name="exceptionMappings">
            <props>
                <prop key="java.lang.Exception">forward:/error.jsp</prop>
            </props>
        </property>
    </bean>
```

复习redirect和forward:
````
   return "forward:/login.jsp";



day75 SpringMVC06 
1.用户新增
1.1用户新增时发生了异常
```
HTTP Status 400 – Bad Request
Type Status Report

Description The server cannot or will not process the request due to something that is perceived to be a client error (e.g., malformed request syntax, invalid request message framing, or deceptive request routing).

Apache Tomcat/8.5.31
```
```
控制台异常提醒:类型转换错误,不能把Date类型的数据直接从String转成Date
.springframework.web.servlet.handler.AbstractHandlerExceptionResolver.logException Resolved [org.springframework.validation.BindException: org.springframework.validation.BeanPropertyBindingResult: 1 errors
Field error in object 'user' on field 'birthday': rejected value [2017-02-19]; codes [typeMismatch.user.birthday,typeMismatch.birthday,typeMismatch.java.util.Date,typeMismatch]; arguments [org.springframework.context.support.DefaultMessageSourceResolvable: codes [user.birthday,birthday]; arguments []; default message [birthday]]; default message [Failed to convert property value of type 'java.lang.String' to required type 'java.util.Date' for property 'birthday'; nested exception is org.springframework.core.convert.ConversionFailedException: Failed to convert from type [java.lang.String] to type [java.util.Date] for value '2017-02-19'; nested exception is java.lang.IllegalArgumentException]]
```
1.2解决方案
最简单的解决方案:在对应的实体的属性上,给当前日期属性加上@DateTimeFormat注解
```
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date birthday;  //出生日期
```
```
com.mysql.jdbc.MysqlDataTruncation: Data truncation: Data too long for column 'userName' at row 1
	at com.mysql.jdbc.MysqlIO.checkErrorPacket(MysqlIO.java:3931)
	at com.mysql.jdbc.MysqlIO.checkErrorPacket(MysqlIO.java:3869)
	at com.mysql.jdbc.MysqlIO.sendCommand(MysqlIO.java:2524)
rollback==================
```
解决方案:新增时userName少些一点

GET乱码:tomcat8已经默认给GET请求做了乱码处理;
POST乱码:字符编码过滤器来解决

```
需求说明
在演示示例2新增用户时，添加JSR 303验证

加入jar文件
hibernate-validator-4.3.2.Final.jar
jboss-logging-3.1.0.CR2.jar
validation-api-1.0.0.GA.jar
POJO
修改User.java
给需要验证的属性增加相应的校验注解

Controller
使用注解所声明的限制规则来进行数据的校验
修改UserController.java
处理方法addSave()的入参
标注@Valid注解
BindingResult参数



View
将验证的错误信息显示在页面中，进行相应的信息提示
修改useradd.jsp
使用<fm:errors/>标签在JSP页面显示错误信息


```


```
Warning:java: 未知的枚举常量 java.lang.annotation.ElementType.TYPE_USE
```


day77_SpringMVC08_文件上传
1. 引入依赖
```
 <!--文件上传开始-->
    <dependency>
      <groupId>commons-fileupload</groupId>
      <artifactId>commons-fileupload</artifactId>
      <version>1.4</version>
    </dependency>
    <dependency>
      <groupId>commons-io</groupId>
      <artifactId>commons-io</artifactId>
      <version>2.2</version>
    </dependency>
    <!--随机数封装-->
    <dependency>
      <groupId>commons-lang</groupId>
      <artifactId>commons-lang</artifactId>
      <version>2.6</version>
    </dependency>
    <!--文件上传结束-->

```

2. 在SpringMVC配置文件中进行文件上传相关的配置
```
```
day79 SpringJSON异步请求:
```
HTTP Status 404 – Not Found
Type Status Report

Message /WEB-INF/jsp/{"result":true}.jsp

Description The origin server did not find a current representation for the target resource or is not willing to disclose that one exists.
```
```
    @RequestMapping("ucexist")
    public String ucexist(String userCode){
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
```
上述写法是同步请求的写法,若发送的请求为异步请求,
则需要加上@ResponseBody这样一个注解,
该注解会将我们的return结果暂存到
Response对象的Body部分,然后被异步引擎捕获

解决方案:
在方法前加上@ResponseBody注解

```
json在页面中文乱码:
 @RequestMapping(value = "view",method = RequestMethod.GET,produces = {"application/json;charset=UTF-8"})
    @ResponseBody
    public Object view(String id,Model model){
        User user = userService.getUserById(id);
        model.addAttribute("user",user);
        String userJson=JSON.toJSONString(user);
        System.out.println("userJson:"+userJson);
        return userJson;
    }

```
解决json显示数据时,日期格式不正常的解决方案,
原因是dao层日期数据显示为毫秒数,
通过在实体类的属性上加@JSONField(format="yyyy-MM-dd");
```
@JSONField(format = "yyyy-MM-dd HH:mm:ss")
	private Date creationDate; //创建时间

```
	异步@ResponseBody,乱码@procedures={"application/json;charset=UTF-8"},
	日期显示:@JSONField(format="yyyy-MM-dd HH:mm:ss")
	
	
	作业1(强制完成):用异步实现用户的删,查,
	作业2:用户角色,下拉列表数据,使用异步显示
	作业3(不强制,兴趣题):用异步实现用户的新增和修改:
	