$(function(){
    var f;
    var files = new Array();
    var alertVal = new Array();
    var basepath = getBasePath();
    $('#cancel_all').click(function(){

    });
    $('#fileField').change(function(){
        $('.add_info').remove();
        var html = '';
        f = document.getElementById("fileField").files;
        for(var i = 0;i<f.length;i++){
            var name,appid,channelId,updateType;
            files[i] = f[i];
            var fileName = f[i].name;
            if(fileName.indexOf("@") != -1){
                var array = fileName.split("@")
                name = array[1];
                var dataArray = array[0].split("&");
                appid = dataArray[0];
                channelId = dataArray[1];
                updateType = dataArray[2];
            }else{
                name = fileName;
                appid = "";
                channelId = "";
                updateType = "";
            }
            html += '<tr class="add_info"><td>'+name+'</td><td><input class="upload_appid" type="text" value="'+appid+'"/></td>' +
                '<td><input class="channelId_upload" type="text" value="'+channelId+'" /></td>' +
                '<td><a class="upload_file" href="javascript:void(0);" style="white-space: nowrap;padding:2px 24px;' +
                'background: url('+basepath+'/static/images/state_yellow.png) no-repeat;color:#f7f7f7;font-weight: bold;">上传文件</a>' +
                '<a class="cancel_upload" href="javascript:void(0);" style="white-space: nowrap;padding:2px 24px;' +
                'background: url('+basepath+'/static/images/state_gray.png) no-repeat;color:#f7f7f7;font-weight: bold;">取消上传</a></td>' +
                '<td><input class="upload_hid" type="hidden" value="'+i+'" /></td></tr>';
        }
        $('#upload_tbody').append(html);
        $('#upload_tb').show();
        $('#upload_tb .upload_file').each(function(){
            $(this).click(function(event,type){
                var input = $(this).parent().siblings().children().filter("input");
                var index = input[2].value;
                var fileVal = files[index];
                var fileName = fileVal.name;
                if(fileName.indexOf("@") != -1){
                    var array = fileName.split("@")
                    fileName = array[1];
                }

                var appidVal = input[0].value;
                if(isEmpty(appidVal.trim())){
                    alert(fileName+" appid不能为空！");
                    return false;
                }

                var channelVal = input[1].value;
                if(isEmpty(channelVal.trim())){
                    alert(fileName+" channelId不能为空！");
                    return false;
                }else if(channelVal.trim().length != 5 && channelVal.trim().length != 8){
                    alert(fileName+" channelID必须是5字符或8字符！");
                    return false;
                }
                alertVal[index] = sendForm(fileVal,appidVal,channelVal);
                if(type != 'allFile' && alertVal[index]){
                    alert("上传成功！");
                }else if(type != 'allFile' && !alertVal[index]){
                    alert("上传失败！");
                }
            });
        });

        $('#upload_tb .cancel_upload').each(function(){
            $(this).click(function(){
                $(this).parent().parent().remove();
            });
        });
    });
    $('#start_upload').click(function(){
        var flag = $('#upload_tb .upload_file').trigger('click',['allFile']);
        if(alertVal != null && alertVal.length>0){
            var alertStr = '';
            for(var i = 0;i<alertVal.length;i++){
                if(!alertVal[i]){
                    var failFile = files[i];
                    getFileName(failFile);
                    var fileName = failFile.name;
                    if(fileName.indexOf("@") != -1){
                        var array = fileName.split("@")
                        alertStr += array[1]+" ";
                    }else{
                        alertStr += fileName+" ";
                    }
                }
            }
            if(!isEmpty(alertStr)){
                alert(alertStr+"上传失败！");
            }else{
                alert("上传成功！");
            }
        }else{
            return;
        }
    });
});

function sendForm(file,appid,channelid) {
    var returnVal;
    var oData = new FormData(document.getElementById("upload_form"));
    oData.append("file", file);
    oData.append("appid", appid);
    oData.append("channelid", channelid);
    var oReq = new XMLHttpRequest();
    oReq.open("POST", getBasePath()+"/uploadFileHandel", true);
    oReq.onload = function(oEvent) {
        if (oReq.status == 200 && oReq.readyState==4) {
            if(oReq.responseText == "true"){
                returnVal = true;
            }else if(oReq.responseText == "false"){
                returnVal = false;
            }

        }
    };
    oReq.send(oData);

    return returnVal;
}

