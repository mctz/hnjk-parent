<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>专业设置</title>
<script type="text/javascript">
	$(document).ready(function() {
		$("#major_majorNationCode").flexselect();
		$("#majorcourseL").html("${courseList}");
		$("#majorcourseR").html("");
		//将选中的option移到右边
		var leftSel = $("#majorcourseL");
		var rightSel = $("#majorcourseR");
		leftSel.find("option:selected").each(function() {
			$(this).remove().appendTo(rightSel);
		});
	});
	$(function() {
		var leftSel = $("#majorcourseL");
		var rightSel = $("#majorcourseR");
		$("#toright").bind("click", function() {
			leftSel.find("option:selected").each(function() {
				$(this).remove().appendTo(rightSel);
			});
		});
		$("#toleft").bind("click", function() {
			rightSel.find("option:selected").each(function() {
				$(this).remove().appendTo(leftSel);
			});
		});
		leftSel.dblclick(function() {
			$(this).find("option:selected").each(function() {
				$(this).remove().appendTo(rightSel);
			});
		});
		rightSel.dblclick(function() {
			$(this).find("option:selected").each(function() {
				$(this).remove().appendTo(leftSel);
			});
		});
		
		$("#uploadify_plancourse_images").uploadify({ //初始化图片上传
	        'script'         : baseUrl+'/edu3/filemanage/upload.html', //上传URL
	        'auto'           : true, //自动上传               
	        'multi'          : false, //多文件上传
	        'scriptData'	 : {'isStoreToDatabase':'N','storePath':'common,plancourse'},
	        'fileDesc'       : '支持格式:jpg/gif',  //限制文件上传格式描述
	        'fileExt'        : '*.JPG;*.GIF;', //限制文件上传的类型,必须有fileDesc这个性质
	        'sizeLimit'      : 2048576, //限制单个文件上传大小2M 
	        'buttonImg'      : baseUrl+'/jscript/jquery.uploadify/images/icon_upload.png', //按钮图片
	        onComplete: function (event, queueID, fileObj, response, data) { //上传成功回调函数
	        	var result = response.split("|");                	             	
	        	$('#plancourse_images').attr('src','${rootUrl}common/plancourse/'+result[1]);   
	        	$('#plancourse').val(result[1]);
			},  
			onError: function(event, queueID, fileObj) {  //上传失败回调函数
			    alert("文件" + fileObj.name + "上传失败"); 
			    $('#uploadify').uploadifyClearQueue(); //清空上传队列
			}
		});
		var faceSrc = '${major.picture4Course}';
		if(faceSrc != ''){
				$('#plancourse_images').attr('src','${rootUrl}common/plancourse/${major.picture4Course}');
		}
		$("#uploadify_textbook_images").uploadify({ //初始化图片上传
	        'script'         : baseUrl+'/edu3/filemanage/upload.html', //上传URL
	        'auto'           : true, //自动上传               
	        'multi'          : false, //多文件上传
	        'scriptData'	 : {'isStoreToDatabase':'N','storePath':'common,textbook'},
	        'fileDesc'       : '支持格式:jpg/gif',  //限制文件上传格式描述
	        'fileExt'        : '*.JPG;*.GIF;', //限制文件上传的类型,必须有fileDesc这个性质
	        'sizeLimit'      : 2048576, //限制单个文件上传大小2M 
	        'buttonImg'      : baseUrl+'/jscript/jquery.uploadify/images/icon_upload.png', //按钮图片
	        onComplete: function (event, queueID, fileObj, response, data) { //上传成功回调函数
	        	var result = response.split("|");                	             	
	        	$('#textbook_images').attr('src','${rootUrl}common/textbook/'+result[1]);   
	        	$('#textbook').val(result[1]);
			},  
			onError: function(event, queueID, fileObj) {  //上传失败回调函数
			    alert("文件" + fileObj.name + "上传失败"); 
			    $('#uploadify').uploadifyClearQueue(); //清空上传队列
			}
		});
		var faceSrc = '${major.picture4TextBook}';
		if(faceSrc != ''){
				$('#textbook_images').attr('src','${rootUrl}common/textbook/${major.picture4TextBook}');
		}
	});
</script>
</head>
<body>
	<h2 class="contentTitle">编辑专业</h2>
	<div class="page">
		<div class="pageContent">
			<form method="post"
				action="${baseUrl}/edu3/sysmanager/major/save.html" class="pageForm"
				onsubmit="return validateCallback(this);">
				<input type="hidden" name="resourceid" value="${major.resourceid }" />
				<div class="pageFormContent" layoutH="60">
					<div>
						<table class="form">
							<tr>
								<td width="20%">专业编号：</td>
								<td width="30%"><input type="text" name="majorCode"
									style="width: 50%" value="${major.majorCode }" class="required" /></td>
								<td width="20%">学校专业编码：</td>
								<td><input type="text" name="majorSchoolCode"
									style="width: 50%" value="${major.majorSchoolCode }" /></td>
								
							</tr>
							<tr>
								<td width="20%">专业名称：</td>
								<td width="30%"><input type="text" name="majorName"
									style="width: 50%" value="${major.majorName }" class="required" /></td>
								<td>英文名称：</td>
								<td><input type="text" name="majorEnName"
									style="width: 50%" value="${major.majorEnName }"
									class="required" /></td>
								
							</tr>
							<tr>
								<td>专业类别：</td>
								<td><gh:select name="majorClass"
										value="${major.majorClass}" dictionaryCode="CodeMajorClass"
										style="width:52%" classCss="required" /></td>
								<td>国家专业名称：</td>
								<td>
									<%-- 
					<gh:select name="majorNationCode" value="${major.majorNationCode}" dictionaryCode="CodeMajorOfContry" style="width:52%" classCss="required"/>
					 --%> <gh:selectModel id="major_majorNationCode"
										classCss="required flexselect" name="nationMajorId"
										bindValue="resourceid" displayValue="nationMajorName"
										modelClass="com.hnjk.edu.basedata.model.NationMajor"
										style="width:52%" value="${major.nationMajor.resourceid}"
										defaultOptionText="" />
								</td>
							</tr>
							<tr>
								<td>所属层次：</td>
								<td>
									<gh:selectModel id="major_classicCode" name="classicCode"
										bindValue="classicCode" displayValue="classicName"
										modelClass="com.hnjk.edu.basedata.model.Classic"
										style="width:52%" value="${major.classicCode}"
									 />
								</td>
								<td>专业编码(用于编学号)：</td>
								<td><input type="text" name="majorCode4StudyNo"
									style="width: 50%" value="${major.majorCode4StudyNo }" /></td>
							</tr>
							<tr>
								<td>是否成人直属班：</td>
								<td><select name="isAdult" style="width: 52%">
										<option value="Y"
											<c:if test="${major.isAdult eq 'Y'}">selected='selected'</c:if>>是</option>
										<option value="N"
											<c:if test="${major.isAdult eq 'N'}">selected='selected'</c:if>>否</option>
								</select></td>
								<td>是否外语类：</td>
								<td>
									<gh:select name="isForeignLng" value="${major.isForeignLng}" dictionaryCode="yesOrNo" style="width:52%" />
								</td>
							</tr>
							<tr>
								<td>专业介绍：</td>
								<td colspan="3"><textarea name="majorIntroduce" rows="5"
										cols="" style="width: 52%">${major.majorIntroduce }</textarea>
								</td>
							</tr>
							<tr colspan="3">
								<td>专业选修课：</td>
								<td style="width: 30%"><select id="majorcourseL"
									name="majorcourseL" size="18" multiple='multiple'
									style="width: 100%; float: left;"></select></td>
								<td style="width: 10%">
									<ul style="text-align: center;">
										<li>
											<input id="toright" type="button" title="添加" value="》" />
										</li>
									</ul>
									<ul style="text-align: center;">
										<li>
											<input id="toleft" type="button" title="移除" value="《" />
										</li>
									</ul>
									</td>
									<td style="width:30%" >
									<select id="majorcourseR" name="majorcourse" size="18"
										multiple="multiple"
										style="width: 100%; float: right; margin-left: 10px"></select>
							</td>
						</tr>
						
					</table>
				</div>
				<div>
					<table class="form" >
						<tr height="600px">
							<td width="10%">教学计划课程图片<br>
							<input name="uploadify_plancourse_images"
											id="uploadify_plancourse_images" type="file" /></td>
							<td width="40%">
							<img id="plancourse_images"
											src="${baseUrl }/themes/default/images/img_preview.png"
											width="100%" height="600px" />
							<input type="hidden" name="picture4Course" id="plancourse"
											value="${major.picture4Course }" /></td>
							<td width="10%">教材目录图片<br>
							<input name="uploadify_textbook_images"
											id="uploadify_textbook_images" type="file" />
							</td>
							<td width="40%">
							<img id="textbook_images"
											src="${baseUrl }/themes/default/images/img_preview.png"
											width="100%" height="600px" />
							<input type="hidden" name="picture4TextBook" id="textbook"
											value="${major.picture4TextBook }" /></td>
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
</body>
</html>