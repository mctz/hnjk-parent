<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>国家专业设置</title>
<script type="text/javascript">
		function validateOnlyNationMajorCode(){
	 		var majorCode = $("#nationMajorCodeId");	    	  	
	    	if($.trim(majorCode.val())==""){ 
	    		alertMsg.warn("请输入专业代码!");
	    		majorCode.focus();
	    		return false; 
	    	}
	    	var url = "${baseUrl}/edu3/framework/nationmajor/validateCode.html";
	    	jQuery.post(url,{majorCode:majorCode.val()},function(data){
	    		if(data == "exist"){ 
	    			alertMsg.warn("专业代码已存在!"); 
	    		}else{ 
	    			alertMsg.correct("恭喜，此专业代码可用！");
	    		}
	    	});
	   }
	</script>
</head>
<body>
	<h2 class="contentTitle">国家专业</h2>
	<div class="page">
		<div class="pageContent">
			<form method="post"
				action="${baseUrl}/edu3/sysmanager/nationmajor/save.html"
				class="pageForm" onsubmit="return validateCallback(this);">
				<input type="hidden" name="resourceid"
					value="${nationMajor.resourceid }" />
				<div class="pageFormContent" layoutH="97">
					<table class="form">
						<tr>
							<td width="15%">专业大类:</td>
							<td width="35%"><gh:select
									dictionaryCode="nationmajorParentCatolog" name="parentCatalog"
									value="${nationMajor.parentCatalog }" /></td>
							<td width="15%">专业类别:</td>
							<td width="35%"><gh:select
									dictionaryCode="nationmajorChildCatolog" name="childCatalog"
									value="${nationMajor.childCatalog }" /></td>
						</tr>
						<tr>
							<td width="15%">高职高专代码:</td>
							<td width="35%"><input type="text" name="nationMajorCode"
								style="width: 50%" value="${nationMajor.nationMajorCode }"
								class="required" /></td>
							<td width="15%">专业代码:</td>
							<td width="35%"><input type="text" id="nationMajorCodeId"
								name="majorCode" style="width: 50%"
								value="${nationMajor.majorCode }" class="required" /> <a
								class="button" href="javascript:;"
								onclick="validateOnlyNationMajorCode();"><span>检查唯一性</span></a></td>
						</tr>
						<tr>
							<td width="15%">专业名称:</td>
							<td width="35%"><input type="text" name="nationMajorName"
								style="width: 50%" value="${nationMajor.nationMajorName }"
								class="required" /></td>
							<td>科类:</td>
							<td><gh:select name="nationMajorType"
									value="${nationMajor.nationMajorType }"
									dictionaryCode="CodeMajorClass" classCss="required" /><span
								style="color: red;">*</span></td>
						</tr>
						<tr>
							<td>层次:</td>
							<td><gh:selectModel name="classicid" bindValue="resourceid"
									displayValue="classicName"
									modelClass="com.hnjk.edu.basedata.model.Classic"
									value="${nationMajor.classic.resourceid }" /></td>
							<td>专业方向:</td>
							<td><input type="text" name="nationMajorDict"
								style="width: 90%;" value="${nationMajor.nationMajorDict }" /></td>
						</tr>
						<tr>
							<td>备注:</td>
							<td colspan="3"><textarea name="memo" rows="5" cols=""
									style="width: 70%;">${nationMajor.memo }</textarea></td>
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