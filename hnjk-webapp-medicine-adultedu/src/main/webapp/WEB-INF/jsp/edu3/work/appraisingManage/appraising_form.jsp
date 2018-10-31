<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<title>学生评优信息</title>
</head>
<script type="text/javascript">

</script>
<body>
	<div class="page">
		<div class="pageContent">
		<form method="post" action="${baseUrl}/edu3/work/appraisingManage/save.html" class="pageForm" onsubmit="return validateCallback(this);">
			<input type="hidden" name="resourceid" value="${appraising.resourceid }"/>
			<input type="hidden" id="appraisingForm_avgScore" name="avgScore" value="${appraising.avgScore }"/>
			<input type="hidden" id="appraisingForm_courseCondition" name="courseCondition" value="${appraising.courseCondition }"/>
			<input type="hidden" id="appraisingForm_isClassLeader" name="isClassLeader" value="${appraising.isClassLeader }"/>
			<input type="hidden" id="appraisingForm_branchSchoolid" name="branchSchoolid" value="${appraising.studentInfo.branchSchool.resourceid}">
			<div class="pageFormContent" layoutH="80">
				<table class="form">
					<tr>
						<td width="20%">年度：</td>
						<td width="30%">
						<gh:selectModel id="appraisingForm_yearInfoId" name="yearInfoId" bindValue="resourceid" displayValue="yearName" classCss="required"
											 modelClass="com.hnjk.edu.basedata.model.YearInfo" value="${appraising.yearInfo.resourceid}" orderBy="yearName desc" style="width:50%"/>
						</td>
						<td width="20%">评优类型：</td>
						<td width="30%"><gh:select name="type" dictionaryCode="Code.WorkManage.appraisingType" classCss="required" value="${appraising.type}" style="width:50%" /></td>
					</tr>
					<tr>
						<td>学号：</td>
						<td>
							<c:if test="${empty appraising.studyNo}">
								<input type="text" style="width: 60%" id="appraisingForm_studyNo" name="studyNo" class="required" value="${appraising.studyNo}">
							</c:if>
							<c:if test="${not empty appraising.studyNo}">
								<input type="text" value="${appraising.studyNo}" disabled="disabled">
								<input type="hidden" id="appraisingForm_studyNo" name="studyNo" value="${appraising.studyNo}">
							</c:if>
							<span class="buttonActive" style="margin-left: 10%;">
									<div class="buttonContent"> <button type="button" onclick="queryStuInfo();">查 询</button></div>
							</span>
						</td>
						<td>姓名：</td>
						<td><input type="text" id="appraisingForm_name" class="required" name="studentName" value="${appraising.studentName}" readonly="readonly"></td>
					</tr>
					<tr>
						<td>成绩平均分：</td>
						<td id="avgScore_td">${appraising.avgScore}</td>
						<td>统考情况：</td>
						<td id="courseCondition_td">${appraising.courseCondition}</td>
					</tr>
					<tr>
						<td>是否班干部：</td>
						<td id="isClassLeader_td">${appraising.isClassLeader}</td>
					</tr>
					<tr>
						<td>自我鉴定：<br>建议不超过800字</td>
						<td colspan="3"><textarea name="selfAssessment" style="width: 80%" rows="8" >${appraising.selfAssessment } </textarea></td>
					</tr>
					<tr>
						<td>备注：</td>
						<td colspan="3"><textarea name="memo" style="width: 50%" rows="3">${appraising.memo }</textarea></td>
					</tr>
				</table>
				<label style="width: 90%">
					<c:if test="${schoolCode eq '11846'}">
						<span style="color: red; ">评选条件：</span>
						<br>1.非统考课程成绩平均75分以上（含75分），统考课程各科成绩70分以上（含70分）；
						<br>2.凡在当年担任班长以上（包括班长）学生干部者，非统考课程成绩平均70分以上（含70分），统考课程各科成绩65分以上（含65分）；
						<br>3.或各科成绩及格，于当年在省级以上刊物公开发表论文、作品或取得具有一定水平的科研成果（均须有关证件原件或证明）。
					</c:if>
				</label>
			</div>
			<div class="formBar">
				<ul>
					<li><div class="buttonActive"><div class="buttonContent">
						<button type="submit">提交</button>
						</div></div></li>
						<li><div class="button"><div class="buttonContent"><button type="button" class="close" onclick="$.pdialog.closeCurrent();">取消</button></div></div></li>
				</ul>
			</div>
		</form>
		</div>
	</div>
<script type="text/javascript">
	//验证学号是否存在
	function queryStuInfo() {
		var studyNo = $("#appraisingForm_studyNo").val();
		var yearInfoid = $("#appraisingForm_yearInfoId").val();
        if(yearInfoid==""){
            alertMsg.warn("请选择年度！");
            return false;
        }
        if(studyNo==""){
            alertMsg.warn("请输入学号！");
            return false;
        }
        $.ajax({
            type:"post",
            url:"${baseUrl}/edu3/work/appraisingManage/validateStudyNo.html",
            data:{studyNo:studyNo,yearInfoid:yearInfoid},
            dataType:"json",
            cache: false,
            error: DWZ.ajaxError,
            success:function(data){
                if(data.statusCode==200){
					$("#appraisingForm_name").val(data.studentName);
                    $("#appraisingForm_branchSchoolid").val(data.branchSchoolid);
                    $("#avgScore_td").html(data.avgScore);
                    $("#appraisingForm_avgScore").val(data.avgScore);
					$("#courseCondition_td").html(data.courseCondition);
                    $("#appraisingForm_courseCondition").val(data.courseCondition);
                    $("#isClassLeader_td").html(data.isClassLeader);
                    $("#appraisingForm_isClassLeader").val(data.isClassLeader);

                } else {alertMsg.error(data.message);}
            }
        });
    }

</script>
</body>
</html>