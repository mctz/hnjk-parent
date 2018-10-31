<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<title>学习笔记</title>
<style type="text/css">
.list {
	line-height: 22px;
}

.form {
	padding-bottom: 10px;
}
</style>
</head>
<body>
	<div style="padding: 5px;">
		<h2 class="title1">我的学习笔记 >
			${studentLearningNote.syllabus.syllabusName }</h2>
		<form id="note_form" method="post" style="width: 100%;"
			action="${baseUrl}/resource/course/note/save.html"
			onsubmit="return false;">
			<input type="hidden" name="resourceid"
				value="${studentLearningNote.resourceid }" /> <input type="hidden"
				name="syllabusid"
				value="${studentLearningNote.syllabus.resourceid }" />
			<table width="100%" class="form">
				<tr>
					<td class="formtd" align="left" width="15%"
						style="font-weight: bold;">标题</td>
					<td class="formtd" align="left" width="85%"><input type="text"
						name="title" value="${studentLearningNote.title}"
						style="width: 80%;" /></td>
				</tr>
				<tr>
					<td class="formtd" align="left" width="15%"
						style="font-weight: bold;">内容</td>
					<td class="formtd" align="left" width="85%"><textarea
							rows="10" cols=""
							style="width: 98%; overflow: hidden; resize: none;"
							id="learningNote_content" name="content">${studentLearningNote.content}</textarea>
					</td>
				</tr>
				<tr>
					<td class="formtd" align="left" width="15%"></td>
					<td class="formtd" align="left" width="85%"><a class="button"
						style="margin-left: 10px;" onclick="sendLearningNote('saveAs');"><span>另存为...</span></a>
						<a class="button" style="margin-left: 10px;"
						onclick="sendLearningNote('save');"><span>保存</span></a> <a
						class="button" style="margin-left: 10px;"
						onclick="sendLearningNote('close');"><span>关闭</span></a></td>
				</tr>
			</table>
		</form>
		<div id="orgLearnigNoteContent" style="display: none;">${studentLearningNote.content}</div>
		<script type="text/javascript">
		function sendLearningNote(type){
			if(type=="close") {//关闭
				Dialogs.close('resource_course_dialog');
				return false;
			}	
			var $form = $("#note_form");
			if($.trim($form.find("input[name='title']").val())==""){
				alert("标题不能为空");
				return false;
			}
			if($.trim($form.find("textarea[name='content']").val())==""){
				alert("内容不能为空");
				return false;
			}			
			if(type=="saveAs" || type == "save"){//另存为
				$.ajax({
					type:'POST',
					url:$form.attr("action"),
					data:$form.serializeArray(),
					dataType:"json",
					cache: false,
					success: function (json){
						if(json.message && type=='save'){
							alert(json.message);
						}						
						if (json.statusCode && json.statusCode == 200){
							$("#orgLearnigNoteContent").html($("#learningNote_content").val());//保存后备份数据内容
							if(json.noteId && json.noteId != ""){
								$form.find("input[name='resourceid']").val(json.noteId);
							}
							if(type=='saveAs'){
								$('#learningNoteIframe').remove();
								var iframe = document.createElement("iframe");
								iframe.id = 'learningNoteIframe';
								iframe.src = baseUrl+"/resource/course/note/export.html?resourceid="+$form.find("input[name='resourceid']").val();
								iframe.style.display = "none";	
									
								document.body.appendChild(iframe);
							}
						} 
					},							
					error: function (xhr, ajaxOptions, thrownError){
						alert("抱歉，系统出错了！<br/>错误代码："+xhr.status+" - "+ajaxOptions+"<br/>请联系系统管理员。");
					}
				});	
			} 	
		}
		$(function() {	 
			$('#learningNote_content').bind("paste cut keydown keyup focus blur",function(){
				$(this).height(this.scrollHeight);//textarea自适应高度
			}).focus();	
    	}); 
	</script>
	</div>
</body>
</html>