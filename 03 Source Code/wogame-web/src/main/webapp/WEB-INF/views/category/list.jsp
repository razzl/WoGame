<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<c:set var="sessionid" value="${pageContext.request.requestedSessionId}"/>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <meta name="viewport" content="width=device-width, minimum-scale=1.0, maximum-scale=2.0">
    <meta content="black" name="apple-mobile-web-app-status-bar-style">
    <meta content="telephone=no" name="format-detection">
    <meta content="false" id="twcClient" name="twcClient">
    <title>分类</title>

    <link href="${ctx}/static/styles/new_main.css" rel="stylesheet" type="text/css"/>
    <script type="text/javascript">
        var contextPath = '${ctx}';
    </script>

    <script type="text/javascript" name="baidu-tc-cerfication"
            src="http://apps.bdimg.com/cloudaapi/lightapp.js#21e4cc6e9f6e857f9ba7ac86ababad5a"></script>

</head>

<body class="ibody_bg">

<div class="head" style="position: fixed;top:0;left:0;width:100%;z-index: 1000;">
    <div class="fanhui absolute pic"><a href="#">返回</a></div>
    <div class="title">分类</div>
    <div class="fanhui-text absolute"><a href="#">首页</a></div>
    <div class="sousuo absolute pic"><a href="#">搜索</a></div>
</div>
<div class="head-after"></div>
<div style="height: 15px;"></div>


<!--<div class="w_new_077"><a href="#">分类</a></div>-->
</div>
<div id="pageContent" style="margin-bottom: 10px;">
    <!--列表-->

    <c:forEach items="${list}" var="i">

        <div class="hd_l_title">
            <div class="hdjianjie">${i.name}</div>
            <div class="hdmore"><a href="${ctx}/category/detail.do?categoryId=${i.id}&categoryName=${i.name}">查看全部</a>
            </div>
        </div>
        <div class="fenlei">
            <ul>
                <c:forEach var="ie" items="${i.items}">

                    <li><a href="${ctx}/category/detail.do?categoryId=${ie.id}&categoryName=${ie.name}">${ie.name}</a>
                    </li>

                </c:forEach>
            </ul>
        </div>


    </c:forEach>
</div>


<script type="text/javascript">
    function toChangWanPage() {
        location.href = "${ctx}/changWan;jsessionid=${sessionid}";
    }
</script>


</body>
</html>
