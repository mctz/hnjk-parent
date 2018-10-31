<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>招生专业表单</title>

</head>
<body>
	<script type="text/javascript">

	function checkLimitNum(obj){
		var limitNum = $(obj).val();
		if(!limitNum.isInteger()){
			$(obj).attr("value","");
		}
	}
	function checkLowerNum(obj){
		var lowerNum = $(obj).val();	
		var limitNum = $(obj).parent().parent().find(" input[name=limitNum]").val();
		
		if(""==limitNum){
			$(obj).parent().parent().find(" input[name=limitNum]").focus();
		}
		
		if(lowerNum.isInteger()){
			if(parseInt(lowerNum) >= parseInt(limitNum)){
				$(obj).attr("value","").focus();
				alertMsg.warn("下限数不能大于指标数!");
			}
		}else{
			$(obj).attr("value","");
		}
		
	}
</script>
	<h2 class="contentTitle">设置招生专业</h2>
	<div class="page">
		<div class="pageContent">
			<form method="post" id="form"
				action="${baseUrl}/edu3/recruit/recruitplan/savemajor.html"
				class="pageForm" onsubmit="return validateCallback(this);">
				<div class="pageFormContent" layoutH="97">
					<table class="form">
						<input type="hidden" name="resourceid"
							value="${recruitmajor.resourceid }" />

						<input type="hidden" id="planid" name="planid" value="${planid}" />
						<tr>
							<td style="width: 12%">招生批次名称:</td>
							<td>${planName}</td>

							<td style="width: 12%">招生专业名称:</td>
							<td style="width: 38%"><input type="text"
								name="recruitMajorName" readonly="true" size="40"
								value="${recruitmajor.recruitMajorName }" class="required" /></td>
						</tr>
						<tr>
							<td style="width: 12%">教学站:</td>
							<td><c:choose>
									<c:when test="${empty recruitmajor.brSchool}">
										<gh:brSchoolAutocomplete name="brSchoolid" tabindex="2"
											id="classes_form_brSchoolid"
											defaultValue="${recruitmajor.brSchool.resourceid }"
											displayType="code" style="width:55%;" />
										<span style="color: red;">*</span>
									</c:when>
									<c:otherwise>
										<input type="hidden" name="brSchoolid"
											value="${recruitmajor.brSchool.resourceid }">						
						${recruitmajor.brSchool.unitName }
						</c:otherwise>
								</c:choose></td>

							<td style="width: 12%">招生专业编码:</td>
							<td style="width: 38%"><input type="text"
								name="recruitMajorCode" size="40"
								value="${recruitmajor.recruitMajorCode }" class="required" /></td>
						</tr>
						<tr>
							<td style="width: 12%">层次:</td>
							<td style="width: 38%"><gh:selectModel name="classicid"
									bindValue="resourceid" displayValue="classicName"
									modelClass="com.hnjk.edu.basedata.model.Classic"
									disabled="disabled" value="${recruitmajor.classic.resourceid}"
									classCss="required" /></td>
							<td style="width: 12%">专业:</td>
							<td style="width: 38%"><gh:selectModel name="majorid"
									bindValue="resourceid" displayValue="majorName"
									modelClass="com.hnjk.edu.basedata.model.Major"
									disabled="disabled" value="${recruitmajor.major.resourceid}"
									classCss="required" /></td>
						</tr>
						<tr>
							<td style="width: 12%">办学模式：</td>
							<td style="width: 38%">
								${ghfn:dictCode2Val('CodeTeachingType',recruitmajor.teachingType)}
								<input type="hidden" name="teachingType"
								value="${recruitmajor.teachingType }" />
							</td>
							<td style="width: 12%">学制:</td>
							<td style="width: 38%"><input type="text" name="studyperiod"
								size="40" value="${recruitmajor.studyperiod }" min="0"
								class="required number" /></td>

						</tr>
						<tr>
							<td style="width: 12%">指标数:</td>
							<td style="width: 38%"><input type="text" id="limitNum"
								name="limitNum" size="40" value="${recruitmajor.limitNum }"
								onblur='checkLimitNum(this)' min="0" class="required number" />
							</td>
							<td style="width: 12%">下限人数:</td>
							<td style="width: 38%"><input type="text" name="lowerNum"
								size="40" value="${recruitmajor.lowerNum }"
								onblur='checkLowerNum(this)' min="0" class="required number" />
							</td>
						</tr>
						<tr>
							<td style="width: 12%">总学费:</td>
							<td style="width: 38%"><input type="text" id="tuitionFee"
								name="tuitionFee" size="40" value="${recruitmajor.tuitionFee }"
								min="0" class="number" /></td>
							<td style="width: 12%"></td>
							<td style="width: 38%"></td>
						</tr>
					</table>
				</div>
				<div class="formBar">
					<ul>
						<li>
							<div class="buttonActive">
								<div class="buttonContent">
									<button type="submit">提交</button>
								</div>
							</div>
						</li>
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