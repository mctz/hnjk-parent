<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>学位审核结果</title>
<script type="text/javascript">
function confirmDegreeAudit(){
	alertMsg.confirm("确定将<font color='blue'>审核通过</font>的学生确认获得学位么？<br>"+
            "确认之后，将无法再次查看此次审核结果， <br>"+
            "也无法再次导出审核结果。", {
		okCall:function(){doConfirmDegreeAudit();}
	}); 
}
function doConfirmDegreeAudit(){//确定获得学位
	var branchSchool = $("#dAuditResult #branchSchool").val();
	var major		       = $("#dAuditResult #major").val();
	var classic			   = $("#dAuditResult #classic").val();
	var grade			   = $("#dAuditResult #grade").val();
	var name			   = $("#dAuditResult #name").val();
	var studyNo	       = $("#dAuditResult #studyNo").val();
	var stus                 = $("#dAuditResult #stus").val();
	var isSelectAll = $("input[name='isSelectAll'] ").val();
	var postUrl="${baseUrl}/edu3/roll/graduateaudit/confirmDegreeAudit.html?stus="+stus+"&branchSchool="+branchSchool+"&major="+major+"&classic="+classic+"&name="+name+"&studyNo="+studyNo+"&grade="+grade+"&isSelectAll="+isSelectAll;
	$.ajax({
		type:"post",
		url:postUrl,
		dataType:"json",
		success:function(data){
			alertMsg.warn(data['message']);
			navTab.closeCurrentTab();
		}
	});
	
}
function exportDegreeAudit(){//导出审核结果(按查询条件导出)
	var branchSchool = $("#dAuditResult #branchSchool").val();
	var major		       = $("#dAuditResult #major").val();
	var classic			   = $("#dAuditResult #classic").val();
	var grade			   = $("#dAuditResult #grade").val();
	var name			   = $("#dAuditResult #name").val();
	var studyNo	       = $("#dAuditResult #studyNo").val();
	var stus  = $("#dAuditResult #stus").val();
	var isSelectAll = $("#dAuditResult #isSelectAll").val();
	$('#frame_exportDegreeAuditList').remove();
	var iframe = document.createElement("iframe");
	iframe.id = "frame_exportDegreeAuditList";
	iframe.src = "${baseUrl }/edu3/schoolroll/degree/student/exportExcel.html?exportType=condition&step='beforeConfirm'&stus="+stus+"&branchSchool="+branchSchool+"&major="+major+"&classic="+classic+"&name="+name+"&studyNo="+studyNo+"&grade="+grade+"&isSelectAll="+isSelectAll;
	iframe.style.display = "none";
	//创建完成之后，添加到body中
	document.body.appendChild(iframe);
}	
</script>
</head>
<body>

	<h2 class="contentTitle">学位审核结果</h2>
	<div class="page">
		<div id="dAuditResult">
			<input type="hidden" id="branchSchool"
				value="${condition['branchSchool']}" /> <input type="hidden"
				id="major" value="${condition['major']}" /> <input type="hidden"
				id="classic" value="${condition['classic']}" /> <input type="hidden"
				id="grade" value="${condition['grade']}" /> <input type="hidden"
				id="name" value="${condition['name']}" /> <input type="hidden"
				id="studyNo" value="${condition['matriculateNoticeNo']}" /> <input
				type="hidden" id="stus" value="${condition['stus']}" /> <input
				type="hidden" id="isSelectAll" value="${condition['isSelectAll']}" />
		</div>
		<div class="pageContent">
			<div class="panelBar">
				<ul class="toolBar">
					<li><a class="icon" onclick="confirmDegreeAudit()" href="#"
						title="确定毕业"> <span>确定获得学位</span>
					</a></li>
					<li><a class="icon" onclick="exportDegreeAudit()" href="#"
						title="导出审核结果"> <span>导出审核结果</span>
					</a></li>
					<li class="line">line</li>
				</ul>
			</div>
			<div class="pageFormContent" layoutH="86">
				<!-- 审核信息 -->
				<div id="main">
					<table class="table" layouth="130" style="width: 100%;">
						<thead>
							<tr>
								<th width="10%">学号</th>
								<th width="5%">学生姓名</th>
								<th width="10%">审核状态</th>
								<th width="35%">原因</th>
								<th width="40%">实际情况</th>
							</tr>
						</thead>
						<tbody id="auditDegreeResults">
							<c:forEach items="${auditDegreeResults.result}" var="auditDegreeResult" varStatus="vs">
								<tr>
									<td>${auditDegreeResult.accountStatus}</td>
									<td>${auditDegreeResult.bloodType}</td>
									<td>&nbsp;<c:if test="${auditDegreeResult.bornAddress=='1'}">
										<font color="blue">审核通过</font>
										</c:if> <c:if test="${auditDegreeResult.bornAddress=='0'}">
											<font color="red">审核不通过</font>
										</c:if>
									</td>
									<td>&nbsp;${auditDegreeResult.bornDay}</td>
									<td>&nbsp;${auditDegreeResult.certNum}</td>
								</tr>
							</c:forEach>
						</tbody>
					</table>
					<div class="panelBar">
						<gh:page page="${auditDegreeResults}" targetType="navTab"
							isShowPageSelector="N"
							goPageUrl="${baseUrl}/edu3/roll/graduateaudit/viaDegree.html"
							pageType="sp_degree" condition="${condition}" />
					</div>
				</div>
			</div>
		</div>
	</div>
</body>

</html>