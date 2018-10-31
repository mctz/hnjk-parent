<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>招生专业设置</title>

</head>
<body>
	<script type="text/javascript">
    $(document).ready(function(){
    	$('#majorSettingForm select[id=major]').multiselect2side({
			selectedPosition: 'right',
			moveOptions: false,
			labelsx: '',
			labeldx: ''
		});
   	});
    function allSelected() {
    	$("#majorms2side__dx option").each(function(){
    		$(this).attr("selected", true);
    	});
    }
</script>
	<h2 class="contentTitle">招生专业设置</h2>
	<div class="page">
		<div class="pageContent">
			<form id="majorSettingForm" method="post"
				action="${baseUrl}/edu3/recruit/recruitmajorsetting/recruitmajorsetting-save.html"
				class="pageForm" id="brachschoolForm"
				onsubmit="return validateCallback(this);">
				<div layoutH="97">
					<table class="form">
						<input type="hidden" name="teachingType" value="${teachingType }" />
						<input type="hidden" name="classic" value="${classic }" />
						<input type="hidden" name="teachingTypeName"
							value="${ghfn:dictCode2Val('CodeTeachingType',teachingType) }" />
						<input type="hidden" name="classicName" value="${classicName }" />
						<tr>
							<td style="width: 10%">学习模式:</td>
							<td style="width: 20%">${ghfn:dictCode2Val('CodeTeachingType',teachingType) }</td>
							<td style="width: 20%"></td>
							<td style="width: 10%">学习层次:</td>
							<td style="width: 20%">${classicName }</td>
							<td style="width: 20%"></td>
						</tr>
						<tr>
							<td style="width: 10%">专业:<font color='red'>* </font></td>
							<td style="width: 90%" colspan="5"><select id="major"
								name="major" size="10" multiple='multiple'>
									<c:forEach items="${majorList }" var="major">
										<option value="${major.resourceid}"
											<c:forEach items="${settingList }" var="setting">
										<c:if test="${setting.major.resourceid eq major.resourceid }">
											 selected="selected"
										</c:if>
									</c:forEach>>${major.majorCode}-${major.majorName}</option>
									</c:forEach>
							</select></td>
						</tr>

					</table>
				</div>
				<div class="formBar">
					<ul>
						<li>
							<div class="buttonActive">
								<div class="buttonContent">
									<button type="submit" onclick="allSelected();">提交</button>
								</div>
							</div>
						</li>
						<li>
							<div class="button">
								<div class="buttonContent">
									<button type="button" class="close"
										onclick="navTab.closeCurrentTab();">取消</button>
								</div>
							</div>
						</li>
					</ul>
				</div>
			</form>
		</div>
	</div>
</body>
</html>