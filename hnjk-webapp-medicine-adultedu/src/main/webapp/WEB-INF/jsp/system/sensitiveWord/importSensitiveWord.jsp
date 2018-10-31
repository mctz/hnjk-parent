<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>导入敏感词</title>
</head>
<body>
	<div class="page">
		<div class="pageContent">
			<form id="importSensitiveWordForm" class="pageForm" method="post">
				<div class="pageFormContent" layoutH="60">
					<table class="form" id="importSensitiveWordTable">
						<tr>
							<td width="20%">敏感词文件：</td>
							<td width="80%" id="attach_td"><gh:upload
									hiddenInputName="attachId" uploadType="attach"
									baseStorePath="sensitiveWord" extendStorePath="attachs"
									formType="importSensitiveWord" uploadLimit="1" isMulti="false"
									fileExt="xls|txt" /></td>
						</tr>
						<tr>
							<td width="20%">支持的文件类型：</td>
							<td width="80%"><font style="font-weight: bolder;">xls,txt</font>
							</td>
						</tr>
						<tr>
							<td width="20%">文件要求：</td>
							<td width="80%"><font color="red">1、导入EXCEL文件应为Excel2003及以前版本<br>2、txt要使用UTF-8进行编码
							</font></td>
						</tr>
					</table>
				</div>
				<div class="formBar">
					<ul>
						<li>
							<div class="buttonActive">
								<div class="buttonContent">
									<button id="submit" type="button"
										onclick="return verifiedSensitiveWord()">提交</button>
								</div>
							</div>
						</li>
						<li>
							<div class="button">
								<div class="buttonContent">
									<button type="button" class="close"
										onclick="$.pdialog.closeCurrent();">取消</button>
								</div>
							</div>
						</li>
					</ul>
				</div>
			</form>
		</div>
	</div>
	<script type="text/javascript">	
		function verifiedSensitiveWord() {
			var attid    = new Array();
			var isReupload = $("#attachId").val();
			if(!isReupload){
				alertMsg.warn("请删除当前敏感词文件，重新上传！");
				return false; 
			}
			$("#importSensitiveWordTable input[name='attachId']").each(function(index){
				attid.push($(this).val());
			});
	
			var url 	 = "${baseUrl}/edu3/system/sensitiveWord/handle-sensitiveWord-import.html";
			if(attid.length <= 0){
				alertMsg.warn("请上传一个敏感词文件！");
				return false; 
			}
			
			$.ajax({
				type:'POST',
				url:url,
				data:{"attachId":attid.toString()},
				dataType:"json",
				cache: false,
				error: DWZ.ajaxError,
				success:function(resultData){
					if(resultData.status==200){
						alertMsg.correct(resultData.message);
						navTabPageBreak();
					} else {
						alertMsg.error(resultData.message);
					}
					var i=0;
					$("#attach_td").children("div").each(function(){
						if(i<2){
							$(this).empty();
							i++;
						}
					});
				}
			});
		}
</script>
</body>
</html>
