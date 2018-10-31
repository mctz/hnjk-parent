<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>设置</title>
</head>
<body>
	<div class="page">

		<form method="post"
			action="${baseUrl }/edu3/framework/user/savesetting.html?act=setting"
			class="pageForm" id="userForm" onsubmit="return save_setting(this);">
			<input type="hidden" name="resourceid" id="resourceid"
				value="${userid }">
			<table class="form" id="childDict">
				<tr>
					<td style="width: 24%">自定义头像:<br /> <font color="green">将出现在论坛、讨论组等交流区</font>
					</td>
					<td style="width: 66%"><img id="person_face"
						src="${baseUrl }/themes/default/images/person.png" width="55"
						height="55" /> <br /> <input name="uploadify_person_face"
						id="uploadify_person_face" type="file" /> <input type="hidden"
						name="userface" id="userface" value="${userface }" />
						说明：小于100KB的gif/jpg类型图片</td>
					</td>
				</tr>
				<tr>
					<td style="width: 24%">所在城市:</td>
					<td style="width: 66%">
						<div class="ChinaArea">
							<select id="province" name="province" style="width: 100px;"></select>
							<select id="city" name="city" style="width: 100px;"></select> <input
								type="hidden" name="usercity" id="usercity" />
						</div>
					</td>
					</td>
				</tr>
				<tr>
					<td style="width: 24%">菜单风格:</td>
					<td style="width: 66%"><input type="radio" name="menustyle"
						size="40" value="tree" />菜单树 <input type="radio" name="menustyle"
						size="40" value="accordion" checked="true" />手风琴</td>
					</td>
				</tr>
			</table>
			<div class="formBar">
				<ul>
					<li><div class="buttonActive">
							<div class="buttonContent">
								<button type="submit">提交</button>
							</div>
						</div></li>
					<li><div class="button">
							<div class="buttonContent">
								<button type="button" class="close" onclick="closeDialog();">取消</button>
							</div>
						</div></li>
				</ul>
			</div>
		</form>
	</div>

	<script type="text/javascript">
$(document).ready(function(){
	$(".ChinaArea").jChinaArea({
		 aspnet:true,
		 s1:"${province}",//默认选中的省名
		 s2:"${city}"//默认选中的市名		 
	})
	
	$("#uploadify_person_face").uploadify({ //初始化图片上传
                'script'         : baseUrl+'/edu3/filemanage/upload.html', //上传URL
                'auto'           : true, //自动上传               
                'multi'          : false, //多文件上传
                'scriptData'	 : {'isStoreToDatabase':'N','storePath':'users,${storeDir}','replaceName':'${storeDir}_face'},
                'fileDesc'       : '支持格式:jpg/gif',  //限制文件上传格式描述
                'fileExt'        : '*.JPG;*.GIF;', //限制文件上传的类型,必须有fileDesc这个性质
                'sizeLimit'      : 102400, //限制单个文件上传大小100KB 
                'buttonImg'      : baseUrl+'/jscript/jquery.uploadify/images/icon_upload.png', //按钮图片
                onComplete: function (event, queueID, fileObj, response, data) { //上传成功回调函数
                	var result = response.split("|");                	             	
                	$('#person_face').attr('src','${rootUrl}users/${storeDir}/'+result[1]);   
                	$('#userface').val(result[1]);             
				},  
				onError: function(event, queueID, fileObj) {  //上传失败回调函数
				    //alert("文件" + fileObj.name + "上传失败"); 
				    $('#uploadify').uploadifyClearQueue(); //清空上传队列
				}
       });
       var faceSrc = '${userface}';
       if(faceSrc != ''){
       		$('#person_face').attr('src','${rootUrl}users/${storeDir}/${userface}');
       }            
})

//提交校验
function save_setting(form){
	jQuery("#usercity").val(jQuery("#province option:selected").text()+","
		                        +jQuery("#city option:selected").text());
	return validateCallback(form, navTabAjaxDone);
}

function closeDialog(){
	$.pdialog.closeCurrent(); 
}
 </script>
</body>
</html>