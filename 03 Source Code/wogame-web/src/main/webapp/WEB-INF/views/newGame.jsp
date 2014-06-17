<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <meta name="viewport" content="width=device-width, minimum-scale=1.0, maximum-scale=2.0">
    <meta content="black" name="apple-mobile-web-app-status-bar-style">
    <meta content="telephone=no" name="format-detection">
    <meta content="false" id="twcClient" name="twcClient">
    <title>最新</title>
    <link href="${ctx}/static/styles/main.css" rel="stylesheet" type="text/css"/>
</head>

<body class="ibody_bg">
<!--top-->
<div class="w-header">
    <div class="w_search"><a href="#">搜索</a></div>
</div>
<!--分类筛选-->
<div class="w_paihangtitle">
    <!--选中状态-->
    <div class="w_new_011"><a href="#">首页</a></div>
    <!--没有选中-->
    <div class="w_new_022"><a href="${ctx}/category/list">分类</a></div>
    <div class="w_new_033"><a href="#">一周热榜</a></div>
    <div class="w_new_04"><a href="#">最新</a></div>
</div>
<!--列表-->
<c:forEach items="${list}" var="item">
    <div class="w_list">

        <div class="w_list_img"><img src="${item.icon}" width="48" height="48"/></div>
        <div class="w_list_title"><a href="#">${item.name}</a></div>
        <div class="w_list_numm">${item.sizeinfo}</div>
        <div class="w_list_download"><a href="#">下载</a></div>
        <div class="w_list_download_txt"><a href="#">下载</a></div>


    </div>
</c:forEach>
</body>
</html>