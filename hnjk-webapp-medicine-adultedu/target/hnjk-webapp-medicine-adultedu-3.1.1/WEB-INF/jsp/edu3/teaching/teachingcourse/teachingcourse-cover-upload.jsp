<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>上传课程封面</title>
</head>
<body>
	<div class="page">

		<form method="post"
			action="${baseUrl }/edu3/teaching/teachingcourse/saveCover.html"
			class="pageForm" id="courseForm"
			onsubmit="return save_setting(this);">
			<input type="hidden" name="courseId" id="courseId"
				value="${course.resourceid }">
			<table class="form" id="childDict">
				<tr>
					<td style="width: 24%">课程封面:<br />
					</td>
					<td style="width: 66%"><img id="cover"
						src="${rootUrl}covers/default/cover.jpg" /> <br /> <input
						name="uploadify_course_cover" id="uploadify_course_cover"
						type="file" /> <input type="hidden" name="courseCover"
						id="courseCover" value="${course.cover }" />
						说明：小于300KB的gif/jpg/png类型图片,上传的图片的尺寸必须为492*640</td>
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
								<button type="button" class="close"
									onclick="navTab.closeCurrentTab();">取消</button>
							</div>
						</div></li>
				</ul>
			</div>
		</form>
	</div>

	<script type="text/javascript">
$(document).ready(function(){
	 $("#uploadify_course_cover").uploadify({ //初始化图片上传
        'script'         : baseUrl+'/edu3/filemanage/upload.html', //上传URL
        'auto'           : true, //自动上传               
        'multi'          : false, //多文件上传
        'scriptData'	 : {'isStoreToDatabase':'N','storePath':'covers,${storePath}'},
        'fileDesc'       : '支持格式:jpg/gif/png',  //限制文件上传格式描述
        'fileExt'        : '*.JPG;*.GIF;*.PNG;', //限制文件上传的类型,必须有fileDesc这个性质
        'sizeLimit'      : 307200, //限制单个文件上传大小300KB 
        'buttonImg'      : baseUrl+'/jscript/jquery.uploadify/images/icon_upload.png', //按钮图片
        onComplete: function (event, queueID, fileObj, response, data) { //上传成功回调函数
        	var result = response.split("|"); 
        	$('#cover').attr('src','${rootUrl}covers/${storePath}/'+result[1]);   
	
        	$('#cover').bind("load", function(){
        		if(this.width!=492 || this.height!=640){
        			alertMsg.error("图片"+this.width+"*"+this.height+"尺寸错误,上传的图片的尺寸必须为492*640,否则无法保存");
        		}else{
        			$('#courseCover').val('covers/${storePath}/'+result[1]);
        		}
        	});	   
		},  
		onError: function(event, queueID, fileObj) {  //上传失败回调函数
		    //alert("文件" + fileObj.name + "上传失败"); 
		    $('#uploadify').uploadifyClearQueue(); //清空上传队列
		}
	}); 

       var coverSrc = '${course.cover }';
       if(coverSrc != ''){
       		$('#cover').attr('src','${rootUrl}${course.cover}');
       }            
})

//提交校验
function save_setting(form){
	return validateCallback(form, navTabAjaxDone);
}


 </script>
</body>
</html>