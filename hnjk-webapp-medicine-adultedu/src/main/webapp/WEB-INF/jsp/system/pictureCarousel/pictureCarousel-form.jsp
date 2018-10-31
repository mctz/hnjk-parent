<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>图片轮播管理</title>
</head>
<body>
	<h2 class="contentTitle">${sensitiveWord.resourceid!=null?'编辑':'添加' }图片轮播</h2>
	<div class="page">
		<div class="pageContent">
			<form method="post"
				action="${baseUrl}/edu3/system/pictureCarousel/save.html"
				class="pageForm" onsubmit="return validateCallback(this);">
				<input type="hidden" name="resourceid"
					value="${pictureCarousel.resourceid }" /> <input type="hidden"
					name="picUrl" id="picUrl" value="${pictureCarousel.picUrl }" />
				<div class="pageFormContent" layoutH="97">
					<table class="form" style="width: 75%">
						<tr>
							<td width="10%">图片名称:</td>
							<td width="40%"><input type="text" name="picName"
								value="${pictureCarousel.picName }" style="width: 90%" /></td>
							<td width="10%">是否显示:</td>
							<td width="10%"><select name="isShow" id="isShow">
									<option value="Y"
										<c:if test="${pictureCarousel.isShow eq 'Y'}">selected='selected'</c:if>>显示</option>
									<option value="N"
										<c:if test="${pictureCarousel.isShow eq 'N'}">selected='selected'</c:if>>不显示</option>
							</select></td>
							<td style="width: 10%">显示顺序:</td>
							<td><input type="text" class="digits" name="showOrder"
								<c:choose><c:when test="${empty pictureCarousel.showOrder }">value="0"</c:when><c:otherwise>value="${pictureCarousel.showOrder }"</c:otherwise></c:choose> /></td>
						</tr>
						<tr>
							<td>描述:</td>
							<td colspan="5"><textarea name="memo" style="width: 90%"
									rows="4">${pictureCarousel.memo }</textarea></td>
						</tr>
					</table>
					<table class="form" id="childDict" style="width: 75%">
						<tr>
							<td width="9%">轮播图片:<br />
							</td>
							<td style="width: 80%"><img id="picture" hidden="true" /> <br />
								<input name="uploadify_course_cover" id="uploadify_course_cover"
								type="file" /> 说明：小于300KB的gif/jpg/png类型图片,<font id="msg">上传的图片尺寸必须为980*500<font></td>
						</tr>
					</table>
				</div>

				<div class="formBar">
					<ul>
						<li><div class="buttonActive">
								<div class="buttonContent">
									<button type="submit">提交</button>
								</div>
							</div></li>
						<li><div class="button">
								<div class="buttonContent">
									<button type="button" class="close"
										onclick="navTab.closeCurrentTab();">取消</button>
								</div>
							</div></li>
					</ul>
				</div>
			</form>
		</div>
	</div>
	<script type="text/javascript">
$(document).ready(function(){
	 $("#uploadify_course_cover").uploadify({ //初始化图片上传
        'script'         : baseUrl+'/edu3/filemanage/upload.html', //上传URL
        'auto'           : true, //自动上传               
        'multi'          : false, //多文件上传
        'scriptData'	 : {'isStoreToDatabase':'N','storePath':'${storePath}'},
        'fileDesc'       : '支持格式:jpg/gif/png',  //限制文件上传格式描述
        'fileExt'        : '*.JPG;*.GIF;*.PNG;', //限制文件上传的类型,必须有fileDesc这个性质
        'sizeLimit'      : 307200, //限制单个文件上传大小300KB 
        'buttonImg'      : baseUrl+'/jscript/jquery.uploadify/images/icon_upload.png', //按钮图片
        onComplete: function (event, queueID, fileObj, response, data) { //上传成功回调函数
        	var result = response.split("|");
        	$('#picture').attr('src','${rootUrl}${storePath}/'+result[1]).show();   
        		
        	$('#picture').bind("load", function(){
        		if(this.width!=980 || this.height!=500){
        			alertMsg.error("图片"+this.width+"*"+this.height+"尺寸错误,上传的图片的尺寸必须为980*500,否则无法保存");
        			$('#msg').attr('color','red');
        		}else{
        			$('#picUrl').val('${storePath}/'+result[1]);
        		}
        	});	             
		},  
		onError: function(event, queueID, fileObj) {  //上传失败回调函数
		    //alert("文件" + fileObj.name + "上传失败"); 
		    $('#uploadify').uploadifyClearQueue(); //清空上传队列
		}
	}); 

       var coverSrc = '${pictureCarousel.picUrl }';
       if(coverSrc != ''){
       		$('#picture').attr('src','${rootUrl}${pictureCarousel.picUrl}').show();
       } 
      
})
 </script>
</body>
</html>