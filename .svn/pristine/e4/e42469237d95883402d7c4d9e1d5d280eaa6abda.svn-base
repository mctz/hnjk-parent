<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>课程论坛版块管理</title>
<script type="text/javascript">
		//检查唯一性
		function validateOnlySectionCode(){
	 		var sectionCode = $("#bbsSectionForm #sectionCode");	
	    	if(sectionCode.val()==""){ alertMsg.warn("请输入论坛版块编码"); sectionCode.focus();return false; }
	    	var url = "${baseUrl}/edu3/metares/bbssection/validateCode.html";
	    	$.post(url,{sectionCode:sectionCode.val()},function(existsCode){
	    		if(existsCode){ alertMsg.warn("编码已存在!"); }else{ alertMsg.correct("恭喜，此编码可用！")}
	    	},"json");
	    }
	    //选择版主
	    function chooseMaster(){
	    	var url ="${baseUrl }/edu3/metares/bbssection/master.html?idsN=bbsSectionMasterId&namesN=bbsSectionMasterName&idsV="+$("#bbsSectionMasterId").val();
	    	$.pdialog.open(url,'chooseMaster','选择版主',{mask:true,height:600,width:800});
	    }
	    $("#bbsSectionForm select[name='parentId']").change(function (){
	    	var url = "${baseUrl }/edu3/metares/bbssection/getShowOrder.html";
	    	var parentId = $(this).val();
	    	$.get(url,{parentId:parentId},function (json){
	    		$("#bbsSectionForm input[name='showOrder']").val(json);
	    	},"json");
	    });
	</script>
</head>
<body>
	<h2 class="contentTitle">编辑课程论坛版块</h2>
	<div class="page">
		<div class="pageContent">
			<form id="bbsSectionForm" method="post"
				action="${baseUrl}/edu3/metares/bbssection/save.html"
				class="pageForm" onsubmit="return validateCallback(this);">
				<input type="hidden" name="resourceid"
					value="${bbsSection.resourceid }" /> <input type="hidden"
					name="clickCount" value="${bbsSection.clickCount }" />
				<div class="pageFormContent" layoutH="97">
					<table class="form">
						<tr>
							<td width="12%">论坛版块编:</td>
							<td width="38%"><input type="text" id="sectionCode"
								name="sectionCode" style="width: 280px"
								value="${bbsSection.sectionCode }" class="required alphanumeric"
								alt="请输入字母下划线或数字的组合" /> <span class="buttonActive"
								style="margin-left: 8px"><div class="buttonContent">
										<button type="button" onclick="validateOnlySectionCode();">检查唯一性</button>
									</div></span></td>
							<td width="12%">是否课程论坛版块:</td>
							<td width="38%"><gh:select name="isCourseSection"
									value="${bbsSection.isCourseSection }" dictionaryCode="yesOrNo"
									style="width:60px;" classCss="required" /><font color="red">*</font>
							</td>
						</tr>
						<tr>
							<td>论坛版块名称:</td>
							<td><input type="text" name="sectionName" style="width: 50%"
								value="${bbsSection.sectionName }" class="required" /></td>
							<td>父版块:</td>
							<%-- 
					<td><gh:selectModel name="parentId" bindValue="resourceid" displayValue="sectionName" style="width:120px"
								modelClass="com.hnjk.edu.learning.model.BbsSection" value="${bbsSection.parent.resourceid }" condition="resourceid<>'${bbsSection.resourceid}'"/></td>
					--%>
							<td><gh:bbsSectionSelect id="parentId" name="parentId"
									style="width: 40%;" value="${bbsSection.parent.resourceid }"
									scope="all" /> <%-- 
						<select name="parentId" id="parentId" style="width: 40%;">
							<option value="" ${(empty bbsSection.parent.resourceid)?'selected':''}>
								选取版块
							</option>											
							<c:forEach items="${parentBbsSections}" var="item">
								<c:forEach items="${item.value}" var="section" varStatus="vs">
									<c:choose>
										<c:when test="${vs.first }">
											<option value="${section.resourceid }" ${(bbsSection.parent.resourceid==section.resourceid)?'selected':''}>
											╋${section.sectionName }
											</option>
										</c:when>
										<c:otherwise>
											<option value="${section.resourceid }" ${(bbsSection.parent.resourceid==section.resourceid)?'selected':''}>
												<c:forEach begin="1" end="${section.sectionLevel }" step="1">&nbsp;&nbsp;</c:forEach>
												├${section.sectionName }
											</option>
										</c:otherwise>
									</c:choose>
								</c:forEach>
							</c:forEach>		
						</select>
						 --%></td>
						</tr>
						<tr>
							<td>论坛版块介绍:</td>
							<td colspan="3"><textarea name="sectionDescript" rows="5"
									cols="" style="width: 80%">${bbsSection.sectionDescript }</textarea>
							</td>
						</tr>
						<tr>
							<td>版主名:</td>
							<td colspan="3"><input type="text" id="bbsSectionMasterName"
								name="masterName" value="${bbsSection.masterName}"
								readonly="readonly" style="width: 80%;" /> <input type="hidden"
								id="bbsSectionMasterId" name="masterId"
								value="${bbsSection.masterId}" /> <span class="buttonActive"
								style="margin-left: 8px"><div class="buttonContent">
										<button type="button" onclick="chooseMaster();">选择版主</button>
									</div></span></td>
						</tr>
						<tr>
							<td>是否可见:</td>
							<td><gh:select dictionaryCode="yesOrNo"
									id="bbsTopic_form_isVisible" name="isVisible"
									value="${bbsSection.isVisible }" /> <span style="color: green;">在外部论坛是否可见</span>
							</td>
							<td>是否只读:</td>
							<td><gh:select dictionaryCode="yesOrNo"
									id="bbsTopic_form_isReadonly" name="isReadonly"
									value="${bbsSection.isReadonly }" /> <span
								style="color: green;">设置只读时帖子不可回复</span></td>
						</tr>
						<tr>
							<td>排序号:</td>
							<td colspan="3"><input type="text" name="showOrder"
								value="${bbsSection.showOrder}" /></td>
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