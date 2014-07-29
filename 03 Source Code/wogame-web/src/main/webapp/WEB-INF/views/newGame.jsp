<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
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
    <title>最新</title>
    <link href="${ctx}/static/styles/main.css" rel="stylesheet" type="text/css"/>
    <link href="${ctx}/static/styles/paging.css" rel="stylesheet" type="text/css"/>
</head>

<!--top-->
<div class="w-header">
    <div class="w_search"><a href="${ctx}/search/init;jsessionid=${sessionid}">搜索</a></div>
</div>
<div id="info-container"
     style="display:none;position:fixed;top:40%;left:10%;z-index:9999;text-align:center;background-color: white;width:200px;height:100px;border: 1px solid gray">
    <div style="height:40px;line-height: 40px;">温馨提示</div>
    <div style="height:4px;background-color: orange;"></div>
    <div style="height:60px;line-height: 60px;">文件下载中...</div>
</div>
<!--分类筛选-->
<div class="w_paihangtitle" id="w_paihangtitle">
    <!--选中状态-->
    <div class="w_new_011"><a href="${ctx}/main;jsessionid=${sessionid}">首页</a></div>
    <!--没有选中-->
    <div class="w_new_022"><a href="${ctx}/category/list;jsessionid=${sessionid}">分类</a></div>
    <div class="w_new_033"><a href="${ctx}/weeklyHot/list;jsessionid=${sessionid}?pageNum=1">一周热榜</a></div>
    <div class="w_new_04"><a href="#">最新</a></div>
</div>
<!--列表-->
<div id="wrapper">
    <div id="scroller">
        <div id="pullDown">
            <span class="pullDownIcon"></span><span class="pullDownLabel">刷新...</span>
        </div>
        <div id="list">

        </div>
        <div id="pullUp">
            <span class="pullUpIcon"></span><span class="pullUpLabel">更多...</span>
        </div>
    </div>
</div>
<script type="text/javascript" src="http://code.jquery.com/jquery-1.9.1.min.js"></script>
<script type="text/javascript" src="${ctx}/static/js/iscroll.js"></script>
<script type="text/javascript" src="${ctx}/static/js/utils.js?20140715092223"></script>
<script type="text/javascript" src="${ctx}/static/js/jquery.touchwipe.js"></script>
<script type="text/javascript">

    $(function () {
        $("#wrapper").touchwipe({
            wipeLeft: function (e) {
                e.preventDefault();
                location.href = "${ctx}/main;jsessionid=${sessionid}";
            },
            wipeRight: function (e) {
                e.preventDefault();
                location.href = "${ctx}/weeklyHot/list;jsessionid=${sessionid}";
            },
            preventDefaultEvents: false
        });

    });

    var myScroll,
            pullDownEl, pullDownOffset,
            pullUpEl, pullUpOffset,
            generatedCount = 0;
    var categoryId = $("#categoryId").val();
    var isSearching = false;
    pageNum = 1;
    var urlBase = '${ctx}/gameInfo;jsessionid=${sessionid}?productId=';
    var el = $('#list');
    el.empty();

    function ajaxGetData(pPageNum, callback) {
        if (isSearching) {
            return;
        }
        isSearching = true;

        $.getJSON("${ctx}/newGame/ajaxList", {"pageNum": pPageNum}, function (data) {
            isSearching = false;
            if (data.length != 0) {

                if (pPageNum <= 1) {
                    el.empty();
                }

                $.each(data, function (index, entry) {

                    var stringBuffer = [];


                    stringBuffer.push('<div class="w_list">');
                    stringBuffer.push('<div class="w_list_img">');
                    stringBuffer.push('<a href="' + urlBase + entry.id + '">');
                    stringBuffer.push('<img src="' + entry.icon + '" width="48" height="48"/></a></div>');
                    stringBuffer.push('<div class="w_list_title">');
                    stringBuffer.push('<a href="' + urlBase + entry.id + '">' + entry.name + '</a>');
                    stringBuffer.push('</div>');
                    stringBuffer.push('<div class="w_list_numm">' + roundNumber(entry.size / 1024, 2) + 'MB</div>');
                    stringBuffer.push('<div class="w_list_download">');
                    stringBuffer.push('<a href="javascript:download(\'' + entry.id
                            + '\',\'' + entry.name
                            + '\',\'' + entry.icon
                            + '\')">下载</a>');
                    stringBuffer.push('</div>');
                    stringBuffer.push('<div class="w_list_download_txt">');
                    stringBuffer.push('<a href="javascript:download(\'' + entry.id
                            + '\',\'' + entry.name
                            + '\',\'' + entry.icon
                            + '\')">下载</a>');
                    stringBuffer.push('</div>');

                    el.append(stringBuffer.join(""));
                });
            }
            if (callback) {
                callback();
            }

        });
    }

    document.addEventListener('touchmove', function (e) {
        e.preventDefault();
    }, false);

    document.addEventListener('DOMContentLoaded', function () {
        setTimeout(loaded, 200);
    }, false);

    ajaxGetData(1);
</script>

<script type="text/javascript">

    $(function () {
        $("#pageContent").touchwipe({
            wipeLeft: function (e) {
                e.preventDefault();
                location.href = "${ctx}/category/list;jsessionid=${sessionid}";
            },
            wipeRight: function (e) {
                e.preventDefault();
                location.href = "${ctx}/newGame/list;jsessionid=${sessionid}?pageNum=1";
            },
            preventDefaultEvents: false
        });


    });

    function download(id, name, icon) {
        doDownload("${ctx}/download;jsessionid=${sessionid}", id, name, icon);
    }
</script>
<script type="text/javascript">
    logNumber("${ctx}", ['64']);
</script>
</body>
</html>
