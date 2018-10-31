<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>报名信息</title>
</head>
<body>
	<h2 class="contentTitle">报名信息</h2>
	<div class="page">
		<div class="pageContent">
			<form id="editForm" method="post"
				action="${baseUrl}/edu3/recruit/enroll/save.html" class="pageForm">
				<input type="hidden" name="resourceid"
					value="${enrolleeinfo.resourceid }">
				<div class="pageFormContent" layoutH="97">
					<div class="tabs">
						<div class="tabsHeader">
							<div class="tabsHeaderContent">
								<ul>
									<li class="selected"><a href="#"><span>个人信息</span></a></li>
								</ul>
							</div>
						</div>
						<div class="tabsContent" style="height: 100%;">
							<div>
								<table class="form">
									<tr>
										<td style="width: 20%">姓名:</td>
										<td style="width: 30%">${enrolleeinfo.studentBaseInfo.name }</td>
										<td style="width: 20%">性别:</td>
										<td style="width: 30%">${ghfn:dictCode2Val("CodeSex",enrolleeinfo.studentBaseInfo.gender) }</td>
									</tr>
									<tr>
										<td>证件类别:</td>
										<td>${ghfn:dictCode2Val("CodeCertType",enrolleeinfo.studentBaseInfo.certType) }</td>
										<td>证件号码：</td>
										<td>${enrolleeinfo.studentBaseInfo.certNum }</td>
									</tr>
									<tr>
										<td>出生日期:</td>
										<td>${enrolleeinfo.studentBaseInfo.bornDay }</td>
										<td>籍贯:</td>
										<td>${enrolleeinfo.studentBaseInfo.homePlace }</td>
									</tr>
									<tr>
										<td>民族:</td>
										<td>${ghfn:dictCode2Val("CodeNation",enrolleeinfo.studentBaseInfo.nation) }</td>
										<td>政治面目:</td>
										<td>${ghfn:dictCode2Val("CodePolitics",enrolleeinfo.studentBaseInfo.politics) }</td>
									</tr>
									<tr>
										<td>固定电话:</td>
										<td>${enrolleeinfo.studentBaseInfo.contactPhone }</td>
										<td>本人地址：</td>
										<td>${enrolleeinfo.studentBaseInfo.contactAddress }</td>
									</tr>
									<tr>
										<td>教学站:</td>
										<td>${enrolleeinfo.branchSchool }</td>
										<td>招生专业：</td>
										<td>${enrolleeinfo.recruitMajor.recruitMajorName }</td>
									</tr>
									<tr>
										<td>准考证号:</td>
										<td>${enrolleeinfo.examCertificateNo }</td>
										<td>入学前国民教育最高学历层次：</td>
										<td>${ghfn:dictCode2Val("CodeEducationalLevel",enrolleeinfo.educationalLevel) }</td>
									</tr>
									<tr>
										<td>入学前学历学校名称:</td>
										<td>${enrolleeinfo.graduateSchool }</td>
										<td>入学前学历学校代码：</td>
										<td>${enrolleeinfo.graduateSchoolCode }</td>
									</tr>
									<tr>
										<td>入学前学历证书编号:</td>
										<td>${enrolleeinfo.graduateId }</td>
										<td>入学前最高学历毕业日期：</td>
										<td>${enrolleeinfo.graduateDate }</td>
									</tr>
									<tr>
										<td>未报到原因:</td>
										<td colspan="3"><textarea name="noReportReason"
												id="noReportReason" rows="3" style="width: 60%">${enrolleeinfo.noReportReason }</textarea>
										</td>
									</tr>
									<tr>
										<td>特殊学生(是否跟读(Y/N))：</td>
										<td colspan="3"><gh:select name="isStudyFollow"
												id="isStudyFollow" dictionaryCode="yesOrNo"
												value="${enrolleeinfo.isStudyFollow }" style="width:125px" />
										</td>
									</tr>
									<tr>
										<td>备注:</td>
										<td colspan="3"><textarea name="memo" id="memo" rows="3"
												style="width: 60%">${enrolleeinfo.memo }</textarea></td>
									</tr>
								</table>
							</div>
						</div>
						<div class="tabsFooter">
							<div class="tabsFooterContent"></div>
						</div>
					</div>
				</div>
				<div class="formBar">
					<ul>
						<li><div class="buttonActive">
								<div class="buttonContent">
									<button type="button" onclick="submitEdit()">提交</button>
								</div>
							</div></li>
						<li><div class="button">
								<div class="buttonContent">
									<button type="button" class="close" href="#close"
										id="close_tab">取消</button>
								</div>
							</div></li>
					</ul>
				</div>
			</form>
		</div>
	</div>
	<script type="text/javascript">
		// 提交
		function submitEdit() {
			var subForm = $("#editForm");
			$.ajax({
				   type: "POST",
				   url: subForm.attr("action"),	
				   data:subForm.serialize(),
				   dataType: "json",	
				   cache:false,
					error:DWZ.ajaxError,
				   success: function(data){	 
					   	if(data.statusCode == '200'){	
					   		alertMsg.correct(data.message);	
					   		$.pdialog.closeCurrent();
					   		navTabPageBreak();
					   	}else{
					   		alertMsg.error(data.message);
					   		return false;
					   	}
				   }
			 });
		}
	</script>
</body>
</html>