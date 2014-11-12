<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<c:set var="sessionid" value="${pageContext.request.requestedSessionId}"/>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <Link Rel="ICON NAME" href="${ctx}/favicon.ico">
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <meta name="viewport" content="width=device-width, minimum-scale=1.0, maximum-scale=2.0">
    <meta content="black" name="apple-mobile-web-app-status-bar-style">
    <meta content="telephone=no" name="format-detection">
    <meta content="false" id="twcClient" name="twcClient">
    <title>活动列表</title>

    <link href="${ctx}/static/styles/new_main.css" rel="stylesheet" type="text/css"/>
    <link href="${ctx}/static/styles/paging.css" rel="stylesheet" type="text/css"/>
    <link href="${ctx}/static/styles/slides.css" rel="stylesheet" type="text/css"/>
    <script type="text/javascript">
        var contextPath = '${ctx}';
    </script>

    <script type="text/javascript" src="http://code.jquery.com/jquery-1.9.1.min.js"></script>
    <script type="text/javascript" name="baidu-tc-cerfication"
            src="http://apps.bdimg.com/cloudaapi/lightapp.js#21e4cc6e9f6e857f9ba7ac86ababad5a"></script>
    <script type="text/javascript" src="${ctx}/static/js/iscroll.js"></script>
    <script type="text/javascript" src="${ctx}/static/js/utils.js?20140715092223"></script>
    <script type="text/javascript" src="${ctx}/static/js/jquery.touchwipe.js"></script>

</head>

<body class="ibody_bg">
<!--top-->

<div class="head" style="position: fixed;top:0;left:0;width:100%;z-index: 1000;">

    <a href="${ctx}/main.do;jsessionid=${sessionid}">
        <div class="fanhui absolute pic"></div>

        <div class="fanhui-text absolute" style="color:#FF9C00 ">首页</div>
    </a>

    <div class="title">活动列表</div>
    <div class="sousuo absolute pic"><a href="${ctx}/search/init.do;jsessionid=${sessionid}">搜索</a></div>
</div>

<div style="height: 50px;"></div>
<!--分类筛选-->
<div class="w_paihangtitle" style="position: fixed; width: 100%;z-index: 1001;">
    <!--选中状态-->
    <div class="w_new_01" style=" border-spacing: 1px;">
        <a href="${ctx}/activity/list.do;jsessionid=${sessionid}">活动</a>
    </div>
    <div class="w_new_022">
        <a href="${ctx}/gameInfo/list.do;jsessionid=${sessionid}">资讯</a>
    </div>
</div>


<!--列表-->
<div id="wrapper" style="top: 95px;">
    <div id="scroller">
        <div id="pullDown">
            <span class="pullDownIcon"></span><span class="pullDownLabel">刷新...</span>
        </div>
        <dl class="juanti_lr"><a href="#">
            <dt><img id="image" src="${ctx}/static/images/gameicon.png" data-src="" height="312"/></dt>

            <dd id="title" class="etc" style="line-height: 15px;"></dd>
            <dd id="time" class="etc" style="line-height: 15px;"></dd>
        </a></dl>

        <div id="list">
        </div>
        <jsp:include page="../footer.jsp"/>

        <div id="pullUp">
            <span class="pullUpIcon"></span><span class="pullUpLabel">更多...</span>
        </div>
    </div>
</div>


<script type="application/javascript">

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

        $.getJSON("${ctx}/activity/ajaxList.do;jsessionid=${sessionid}", {"pageNum": pPageNum, "pageSize": 10}, function (data) {

            isSearching = false;
            if (data.items.length != 0) {

                if (pPageNum <= 1) {
                    el.empty();
                }

                if (data.items[0].banner != null) {
                    /*  $("#t1").text(su(data.title,5,0,4));*/
                    $("#image").attr("data-src", data.items[0].banner.banner_url);
                    $("#title").html(data.items[0].title);
                    $("#time").html(getFormatDateByLong(data.items[0].start_time, "yyyy.MM.dd") + ' - ' + getFormatDateByLong(data.items[0].end_time, "yyyy.MM.dd"));
                }


                $.each(data.items, function (index, entry) {

                    if (index != 0) {
                        var stringBuffer = [];


                        stringBuffer.push('<dl class="huodong_list">');

                        stringBuffer.push('<a href="${ctx}/activity/detail.do;jsessionid=${sessionid}?id=' + entry.id + '">');


                        /*图片*/


                        stringBuffer.push('<img  src="${ctx}/static/images/gameicon.png" data-src="' + entry.icon_url + '" height="86";width="86"/>');

                        stringBuffer.push('<dt  class="etc">' + entry.title + '</dt>');

                        stringBuffer.push('<dd>' + su(entry.intro, 35, 0, 35) + '</dd>');
                        stringBuffer.push('<dd class="etc">' + "时间：" + getFormatDateByLong(entry.start_time, "yyyy.MM.dd") + ' - ' + getFormatDateByLong(entry.end_time, "yyyy.MM.dd") + '</dd>');


                        stringBuffer.push('</a>');


                        stringBuffer.push('</dl>');


                        /*当程序加载页面自动运行ajaxGetData（1）自动加载好页面就ok*/


                        el.append(stringBuffer.join(""));
                    }
                });
                $("img[data-src]").scrollLoading();
            } else {

//                $('#pullDown, #pullUp').hide();

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