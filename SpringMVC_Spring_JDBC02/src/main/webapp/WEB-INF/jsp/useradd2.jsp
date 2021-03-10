<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/jsp/common/head.jsp"%>

<div class="right">
        <div class="location">
            <strong>你现在所在的位置是:</strong>
            <span>用户管理页面 >> 用户添加页面</span>
        </div>
        <div><h2>${message}</h2></div>
        <div class="providerAdd">
            <fm:form action="${pageContext.request.contextPath }/user/addsave" method="post" modelAttribute="user" >
                <fm:errors path="userCode"/>
                用户编码:<fm:input path="userCode" /><br>
                <fm:errors path="userName"/>
                用户名:<fm:input path="userName"/><br>
                <fm:errors path="userPassword"/>
                用户密码:<fm:password path="userPassword"/><br>
                性别:<fm:radiobutton path="gender" value="1"/>男
                <fm:radiobutton path="gender" value="2"/>女<br/>
                <fm:errors path="birthday"/>
                出生日期:<fm:input path="birthday"/><br>
                <fm:errors path="phone"/>
                用户电话:<fm:input path="phone"/><br>
                用户地址:<fm:input path="address"/><br>
                用户角色:<fm:select path="userRole">
                        <fm:option value="1">系统管理员</fm:option>
                        <fm:option value="2" >经理</fm:option>
                        <fm:option value="3">普通员工</fm:option>
            </fm:select><br>

                <input type="submit" value="新增">
            </fm:form>

           <%-- <form id="userForm" name="userForm" method="post" action="${pageContext.request.contextPath }/user/addsave">
				&lt;%&ndash;<input type="hidden" name="method" value="add">&ndash;%&gt;
                <!--div的class 为error是验证错误，ok是验证成功-->
                <div>
                    <label for="userCode">用户编码：</label>
                    <input type="text" name="userCode" id="userCode" value=""> 
					<!-- 放置提示信息 -->
					<font color="red"></font>
                </div>
                <div>
                    <label for="userName">用户名称：</label>
                    <input type="text" name="userName" id="userName" value=""> 
					<font color="red"></font>
                </div>
                <div>
                    <label for="userPassword">用户密码：</label>
                    <input type="password" name="userPassword" id="userPassword" value=""> 
					<font color="red"></font>
                </div>
                <div>
                    <label for="ruserPassword">确认密码：</label>
                    <input type="password" name="ruserPassword" id="ruserPassword" value=""> 
					<font color="red"></font>
                </div>
                <div>
                    <label >用户性别：</label>
					<select name="gender" id="gender">
					    <option value="1" selected="selected">男</option>
					    <option value="2">女</option>
					 </select>
                </div>
                <div>
                    <label for="birthday">出生日期：</label>
                    <input type="text"  id="birthday" name="birthday" 
				Class="Wdate"	readonly="readonly" onclick="WdatePicker();">
					<font color="red"></font>
                </div>
                <div>
                    <label for="phone">用户电话：</label>
                    <input type="text" name="phone" id="phone" value=""> 
					<font color="red"></font>
                </div>
                <div>
                    <label for="address">用户地址：</label>
                   <input name="address" id="address"  value="">
                </div>
                <div>
                    <label >用户角色：</label>
                    <!-- 列出所有的角色分类 -->
					<select name="userRole" id="userRole">
                        <option value="0" selected>--请选择--</option>
                        <option value="1">系统管理员</option>
                        <option value="2">经理</option>
                        <option value="3">普通员工</option>
                    </select>
	        		<font color="red"></font>
                </div>
                <div class="providerAddBtn">
                    <input type="button" name="add" id="add" value="保存" >
					<input type="button" id="back" name="back" value="返回" >
                </div>
            </form>--%>
        </div>
</div>
</section>
<%@include file="/WEB-INF/jsp/common/foot.jsp" %>
<script type="text/javascript" src="${pageContext.request.contextPath }/js/useradd.js"></script>
