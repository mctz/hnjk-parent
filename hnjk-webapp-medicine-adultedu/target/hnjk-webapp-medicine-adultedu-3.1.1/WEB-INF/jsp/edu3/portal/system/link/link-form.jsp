<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>友情链接管理</title>

<script type="text/javascript">
	$(function() {	
		//更改链接图片尺寸
		$("#linkPosition").change(
			function(){				
				if($("#linkPosition").val()=="imglink"){			
					$("#linkform_piclink").css({display:"block",width:"100%"});		
					$("#facePic").css({width:"180px",height:"140px"});
				}else if($("#linkPosition").val()=="bannerlink"){
					$("#linkform_piclink").css({display:"block",width:"100%"});
					$("#facePic").css({width:"405px",height:"63px"});
				}else{
					$("#linkform_piclink").css({display:"none",width:"100%"});
				}
			}
		);	
		 $("#uploadify_images").uploadify({ //初始化图片上传
                'script'         : baseUrl+'/edu3/filemanage/upload.html', //上传URL
                'auto'           : true, //自动上传               
                'multi'          : false, //多文件上传
                'scriptData'	 : {'isStoreToDatabase':'N','storePath':'portal,link'},
                'fileDesc'       : '支持格式:jpg/gif/png',  //限制文件上传格式描述
                'fileExt'        : '*.JPG;*.GIF;*.PNG;', //限制文件上传的类型,必须有fileDesc这个性质
                'sizeLimit'      : 1048576, //限制单个文件上传大小1M 
                'buttonImg'      : baseUrl+'/jscript/jquery.uploadify/images/icon_upload.png', //按钮图片
                onComplete: function (event, queueID, fileObj, response, data) { //上传成功回调函数
                	var result = response.split("|");                	             	
                	$('#facePic').attr('src','${rootUrl}portal/link/'+result[1]);
                	$('#link-imgPath').val(result[1]);
				},  
				onError: function(event, queueID, fileObj) {  //上传失败回调函数
				    alert("文件" + fileObj.name + "上传失败"); 
				    $('#uploadify').uploadifyClearQueue(); //清空上传队列
				}
       });
       var faceSrc = '${link.imgPath}';
       if(faceSrc != ''){
       		$('#facePic').attr('src','${rootUrl}portal/link/${link.imgPath}');
       }
    });
    
   
</script>
</head>
<body>
	<h2 class="contentTitle">编辑友情链接</h2>
	<div class="page">
		<div class="pageContent">
			<form method="post"
				action="${baseUrl}/edu3/portal/manage/link/save.html"
				class="pageForm" onsubmit="return validateCallback(this);">
				<div class="pageFormContent" layoutH="97">
					<table class="form">
						<input type="hidden" name="resourceid" value="${link.resourceid }" />
						<tr>
							<td style="width: 12%">链接名称:</td>
							<td style="width: 38%"><input type="text" name="linkName"
								size="40" value="${link.linkName }" class="required" /></td>
							<td>链接地址:</td>
							<td><input type="text" name="linkHref" size="40"
								value="${link.linkHref }" class="required url" /></td>
						</tr>
						<tr>
							<td style="width: 12%">打开方式:</td>
							<td style="width: 38%"><select name="openType" size="1">
									<option value="_self"
										<c:if test="${link.openType eq '_self'}">selected</c:if>>本窗口</option>
									<option value="_blank"
										<c:if test="${link.openType eq '_blank'}">selected</c:if>>新窗口</option>
							</select></td>
							<td style="width: 12%">链接类型:</td>
							<td style="width: 38%"><gh:select name="linkPosition"
									value="${link.linkPosition}" dictionaryCode="linkPosition"
									classCss="required" style="width:33%" /></td>
						</tr>
						<tr>
							<td style="width: 12%">链接说明:</td>
							<td style="width: 38%"><input type="text"
								name="linkDescript" size="40" value="${link.linkDescript }" /></td>
							<td style="width: 12%">是否停用:</td>
							<td style="width: 38%"><input type="checkbox" name="status"
								value="1" <c:if test="${ link.status == 1}"> checked</c:if> />
								是</td>
						</tr>

					</table>
					<table class="form" id="linkform_piclink">
						<tr>
							<td style="width: 12%">图片链接:</td>
							<td>
								<!-- 		
						<c:if test="${not empty link.imgPath}">
						<div id="preview"><img src="${baseUrl }/images/cache/portal/link/${link.imgPath }"/></div>
						</c:if>
						<div id="filelist"></div>						
						<input type="file" name="uploadPhoto" id="uploadPhoto" size="40"/>
						<input type="hidden" name="imgPath" value="${link.imgPath }"/>
						 --> <img id="facePic"
								src="${baseUrl }/themes/default/images/img_preview.png"
								width="180" height="140" /> <input name="uploadify_images"
								id="uploadify_images" type="file" /> <input type="hidden"
								id="link-imgPath" name="imgPath" value="${link.imgPath }" /> <input
								type="hidden" name="isImg" value="${link.isImg }" />
							</td>
						</tr>
					</table>
				</div>
				<div class="formBar">
					<ul>
						<li><div class="buttonActive">
								<div class="buttonContent">
									<!-- 隐藏域 -->
									<input type="hidden" name="fillinMan"
										value="${link.fillinMan }" /> <input type="hidden"
										name="fillinManId" value="${link.fillinManId }" />
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
</body>

</html>