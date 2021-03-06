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
    <title>专题详情</title>

    <link href="${ctx}/static/styles/new_main.css" rel="stylesheet" type="text/css"/>
    <link href="${ctx}/static/styles/paging.css" rel="stylesheet" type="text/css"/>
    <link href="${ctx}/static/styles/slides.css" rel="stylesheet" type="text/css"/>
    <script type="text/javascript">
        var contextPath = '${ctx}';
    </script>
    <script type="text/javascript" src="http://code.jquery.com/jquery-1.9.1.min.js"></script>
    <script type="text/javascript" src="${ctx}/static/js/iscroll.js"></script>
    <script type="text/javascript" src="${ctx}/static/js/utils.js?20140715092223"></script>
    <script type="text/javascript" src="${ctx}/static/js/jquery.touchwipe.js"></script>

    <script type="text/javascript" name="baidu-tc-cerfication"
            src="http://apps.bdimg.com/cloudaapi/lightapp.js#21e4cc6e9f6e857f9ba7ac86ababad5a"></script>

</head>

<body class="ibody_bg">
<!--top-->

<div class="head" style="position: fixed;top:0;left:0;width:100%;z-index: 1000;">


    <a   href="${ctx}/subject/list.do;jsessionid=${sessionid}">
        <div class="fanhui absolute pic"></div>

        <div class="fanhui-text absolute" style="color:#FF9C00 ">返回</div>
    </a>


    <div class="title">专题详情</div>

    <div class="sousuo absolute pic"><a href="${ctx}/search/init.do;jsessionid=${sessionid}">搜索</a></div>
</div>
<div style="height: 50px;"></div>


<!--列表-->
<div id="wrapper" style="top:56px;">
    <div id="scroller">
        <div id="pullDown">
            <span class="pullDownIcon"></span><span class="pullDownLabel">刷新...</span>
        </div>

        <dl class="juanti_lr"><a href="#">
            <dt><img id="image" src="" height="312"/></dt>
            <dd id="description">
            </dd>
        </a></dl>

        <div id="list">

        </div>
       <jsp:include page="../footer.jsp"/>
        <div id="pullUp">
            <span class="pullUpIcon"></span><span class="pullUpLabel">更多...</span>
        </div>
    </div>
</div>



<input type="hidden" value="${id}" id="subId"/>

<script type="application/javascript">

    var myScroll,
            pullDownEl, pullDownOffset,
            pullUpEl, pullUpOffset,
            generatedCount = 0;
    var subId = $("#subId").val();
    var isSearching = false;
    pageNum = 1;
    var urlBase = '${ctx}/gamedetail/detaillist.do;jsessionid=${sessionid}?product_id=';
    var el = $('#list');
    el.empty();

    function ajaxGetData(pPageNum, callback) {
        if (isSearching) {
            return;
        }
        isSearching = true;

        $.getJSON("${ctx}/subject/gamelist.do;jsessionid=${sessionid}", {"id": subId, "pageNum": pPageNum, "pageSize": 10}, function (data) {

            if (data.description != null) {
                $("#image").attr("src", data.banner_url);

                var arr = new Array();
                var str = data.description;
                var newStr = "";
                arr = str.split('：');//注split可以用字符或字符串分割
                for (var i = 0; i < arr.length; i++) {
                    newStr += arr[i] + '<br/>';
                }

                $("#description").html(newStr);

            }

            isSearching = false;
            if (data.items != null && data.items.length != 0) {

                if (pPageNum <= 1) {
                    el.empty();
                }


                $.each(data.items, function (index, entry) {

                    var stringBuffer = [];
                    stringBuffer.push('<div class="pro_list">');

                    stringBuffer.push('<div class="jiaobiao_' + entry.corner_mark + '">');
                    stringBuffer.push('</div>');

                    stringBuffer.push('<a href="${ctx}/gamedetail/detaillist.do;jsessionid=${sessionid}?product_id=' + entry.product_id + '">');


                    stringBuffer.push('<div class="pro_cp">');

                    /*图片*/
                    stringBuffer.push('<div class="pro_cp_l">');
                    stringBuffer.push('<a href="' + urlBase + entry.product_id + '">');
                    stringBuffer.push('<img  src="${ctx}/static/images/gameicon.png" data-src="' + entry.icon_url + '" height="86"/>');
                    stringBuffer.push('</div>');

                    /*游戏名称*/

                    stringBuffer.push('<dl class="pro_cp_c">');
                    stringBuffer.push('<dt class=etc>' + entry.game_name + '</dt>');

                    stringBuffer.push('<dd>' + roundNumber(entry.apk_size / 1024, 2) + 'MB</dd>');
                    stringBuffer.push('</dl>');
                    stringBuffer.push('</a>');
                    stringBuffer.push('</div>');

                    stringBuffer.push('<div class="pro_cp_r radius">');

                    stringBuffer.push('<a href="javascript:download(\'' + entry.product_id
                            + '\',\'' + entry.game_name
                            + '\',\'' + entry.icon_url
                            + '\')">下载</a>');
                    stringBuffer.push('</div>');


                    stringBuffer.push('<div class="plist_f etc">');
                    stringBuffer.push('<a href="javascript:download(\'' + entry.id
                            + '\',\'' + entry.name
                            + '\',\'' + entry.icon
                            + '\')">' + entry.intro + '</a></div>');


                    stringBuffer.push('</a>');


                    stringBuffer.push('</div>');


                    /*当程序加载页面自动运行ajaxGetData（1）自动加载好页面就ok*/


                    el.append(stringBuffer.join(""));
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