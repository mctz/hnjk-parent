<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>毕业导师公告</title>
<style type="text/css">
#nostyle td {
	height: 4px
}
</style>
</head>
<body>
	<h2 class="contentTitle">编辑毕业导师公告</h2>
	<div class="page">
		<div class="pageContent">
			<form method="post"
				action="${baseUrl}/edu3/teaching/graduateNotice/save.html"
				class="pageForm" onsubmit="return validateCallback(this);">
				<input type="hidden" name="resourceid"
					value="${graduate.resourceid }" /> <input type="hidden"
					id="guidTeacherName" name="guidTeacherName"
					value="${graduate.guidTeacherName }" /> <input type="hidden"
					id="guidTeacherId" name="guidTeacherId"
					value="${graduate.guidTeacherId }" /> <input type="hidden"
					id="pubTime" name="pubTime"
					value="<fmt:formatDate value="${graduate.pubTime}" pattern="yyyy-MM-dd HH:mm:ss"/>" />
				<div class="pageFormContent" layoutH="97">
					<table class="form">
						<tr>
							<td width="20%">毕业论文批次:</td>
							<td colspan="3"><select name="examSubId" size="1"
								class="required">
									<option value="">请选择</option>
									<c:forEach items="${examSubs }" var="examsub">
										<option value="${examsub.resourceid }"
											<c:if test="${examsub.resourceid eq  graduate.examSub.resourceid}"> selected</c:if>>${examsub.batchName }</option>
									</c:forEach>
							</select> <font color=red>*</font></td>
						</tr>
						<tr>
							<td>标题:</td>
							<td colspan="3"><input type="text" id="title" name="title"
								style="width: 80%" value="${graduate.title }" class="required" /></td>
						</tr>
						<tr id="nostyle">
							<td>内容:</td>
							<td colspan="3"><textarea id="content" name="content"
									style="width: 80%; height: 300px; visibility: hidden;"
									class="required">${graduate.content }</textarea></td>
						</tr>
						<tr>
							<td>附件:</td>
							<td colspan="3"><input type="file"
								name="uploadify_graduateFile" id="uploadify_graduateFile" />
								<div id="graduateFile">
									<c:forEach items="${filelist}" var="attach" varStatus="vs">
										<li id="${attach.resourceid }"><img
											style="cursor: pointer; height: 10px;"
											src="${baseUrl}/jscript/jquery.uploadify/images/attach.png" />&nbsp;&nbsp;<a
											onclick="downloadAttachFile('${attach.resourceid }')"
											href="#">${attach.attName }&nbsp;</a>&nbsp;&nbsp;<img
											style="cursor: pointer; height: 10px;"
											onclick="deleteAttachFile('${attach.resourceid }');"
											src="${baseUrl}/jscript/jquery.uploadify/images/cancel.png" />
										</li>
									</c:forEach>
								</div>
								<div id="hideGraduateFile" style="display: none"></div></td>
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
<script type="text/javascript">
	
		//function chooseExamSub5(){ //选择批次
		//	var url ="${baseUrl }/edu3/teaching/graduateNotice/chooseExam.html?type=notice&batchType=thesis&guidTeacherId="+$("#guidTeacherId").val();
	    //	$.pdialog.open(url,'chooseExam','选择论文批次',{mask:true,height:500,width:700});
		//}
		
        $(document).ready(function() {
        	KE.init({//初始化编辑器
        	      id : 'content',     
        	      items : [
        					'fontname', 'fontsize', '|','subscript','superscript', 'textcolor', 'bgcolor', 'bold', 'italic', 'underline',
        					'removeformat', '|', 'justifyleft', 'justifycenter', 'justifyright', 'insertorderedlist',
        					'insertunorderedlist', '|', 'emoticons',  'link'],

        		      afterCreate : function(id) {
        					KE.util.focus(id);
        			}	
        	});
        	init('/edu3/filemanage/upload.html');
        });
 		
 		function init(url){
 			$("#uploadify_graduateFile").uploadify({
                'script'         : baseUrl+url+'?storePath=users,${storeDir},gruates', //上传URL
	            'auto'           : true, //自动上传
	            'multi'          : false, //多文件上传
	            'fileDesc'       : '支持格式:zip/rar',  //限制文件上传格式描述
	            'fileExt'        : '*.ZIP;*.RAR;', //限制文件上传的类型,必须有fileDesc这个性质
	            'sizeLimit'      : 10485760, //限制单个文件上传大小10M 
	            'buttonImg'      : baseUrl+'/jscript/jquery.uploadify/images/icon_upload.png', //按钮图片
                onComplete: function (event, queueID, fileObj, response, data) { //上传成功回调函数
	                $("#graduateFile").append("<li id='"+response.split("|")[0]+"'><img src='"+baseUrl+"/jscript/jquery.uploadify/images/attach.png' style='cursor:pointer;height:10px'>&nbsp;&nbsp;<a href='#' onclick='downloadAttachFile(\""+response.split("|")[0]+"\")'>"+fileObj.name+"&nbsp;</a>&nbsp;&nbsp;<img src='"+baseUrl+"/jscript/jquery.uploadify/images/cancel.png' onClick='deleteAttachFile(\""+response.split("|")[0]+"\");' style='cursor:pointer;height:10px'></li>")
					$("#hideGraduateFile").append("<input type='hidden' id='uploadfileid' name='uploadfileid' value='"+response.split("|")[0]+"'>"); //添加隐藏域与业务建立关系
				},
				onError: function(event, queueID, fileObj) {  //上传失败回调函数
					alert("文件:" + fileObj.name + "上传失败"); 
					$('#uploadify_graduateFile').uploadifyClearQueue(); //清空上传队列
				}
            });
 		}
 		
 		//附件下载
		   function downloadAttachFile(attid){
		   		$('#frameForDownload').remove();
		   		var elemIF = document.createElement("iframe");
		   		elemIF.id = "frameForDownload"; //创建id
				elemIF.src = baseUrl+"/edu3/framework/filemanage/download.html?id="+attid;
				elemIF.style.display = "none";  
				document.body.appendChild(elemIF); 
		   }
		   
		   //附件删除
		   function deleteAttachFile(attid){
			  if(confirm("确定要删除这个附件？")){
			  	var url = baseUrl+"/edu3/framework/filemanage/delete.html"; 
				$.get(url,{fileid:attid},function(data){
					$("#graduateFile > li[id='"+attid+"']").remove();
					$("#uploadfileid[value='"+attid+"']").remove();
				});	
			  }
		    }  
 	
			function createKE(){
				KE.create('content');
			}
			setTimeout(createKE,1000);

	</script>
</html>