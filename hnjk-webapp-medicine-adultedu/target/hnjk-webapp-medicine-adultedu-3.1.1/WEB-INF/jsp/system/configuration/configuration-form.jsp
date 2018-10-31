<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>系统全局参数管理</title>
<script type="text/javascript">
    //验证编码唯一
    function validateOnly(){ 
    	var paramCode = jQuery("#paramCode");
    	if(paramCode.val()==""){ alertMsg.warn("请输入参数编码"); paramCode.focus() ;return false; }
    	var url = "${baseUrl}/edu3/framework/system/sys/configuration/validateCode.html";
    	jQuery.post(url,{paramCode:paramCode.val()},function(data){    		
    		if(data){ alertMsg.warn("系统编码已存在!"); }else{alertMsg.correct("恭喜，此编码可用！") }
    	},"json")
    }   
   
</script>
</head>
<body>
	<h2 class="contentTitle">编辑系统全局参数</h2>
	<div class="page">
		<div class="pageContent">
			<form method="post"
				action="${baseUrl}/edu3/system/configuration/save.html"
				class="pageForm" onsubmit="return validateCallback(this);">
				<input type="hidden" id="resourceid" name="resourceid"
					value="${sysConfiguration.resourceid }" />
				<div class="pageFormContent" layoutH="97">
					<table class="form">
						<tr>
							<td style="height: 80px;">参数编码:</td>
							<td><input type="text" id="paramCode" name="paramCode"
								style="width: 90%" value="${sysConfiguration.paramCode }"
								class="required" /> <br>
								<span class="buttonActive" style="margin-left: 8px;margin-top: 5px;">
									<div class="buttonContent"> <button type="button" onclick="validateOnly()">检查唯一性</button>
									</div>
								</span>
							</td>
							<td>参数名称:</td>
							<td><input type="text" name="paramName" style="width: 90%"
								value="${sysConfiguration.paramName }" class="required" /></td>
						</tr>
						<tr>
							<td>参数值:</td>
							<td><textarea name="paramValue" style="width: 90%;height: 80px">${sysConfiguration.paramValue }</textarea>
							<td>备注：</td>
							<td><textarea name="memo" style="width: 90%; height: 120px">${sysConfiguration.memo }</textarea>
							</td>
						</tr>
					</table>
					<div style="color: green; padding: 8px">
						说明：系统全局参数为系统的配置参数，通常以key-value形式被封装在map中。</div>
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