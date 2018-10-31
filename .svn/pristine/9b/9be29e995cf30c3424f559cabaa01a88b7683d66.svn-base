<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
</head>
<script type="text/javascript">
    $(document).ready(function(){
        var department = "${cadreInfo.department}";
        var department_current = "${cadreInfo.department_current}";
        updatePositionCode('cadreInfoForm_position',department);
        updatePositionCode('cadreInfoForm_position_current',department_current);
        var isStudent = "${isStudent}";
        if (isStudent == "false") {
            document.getElementById("cadreInfoForm_yearInfoId").setAttribute('disabled','true');
            document.getElementById("cadreInfoForm_term").setAttribute('disabled','true');
            document.getElementById("cadreInfoForm_department").setAttribute('disabled','true');
            document.getElementById("cadreInfoForm_position1").setAttribute('disabled','true');
            document.getElementById("cadreInfoForm_position2").setAttribute('disabled','true');
        }
    });
    //根据部门获取职位
    function updatePositionCode(positionid,departmentCode) {
        var posid_1 = positionid+"1";
        var posid_2 = positionid+"2";
        if (departmentCode == '1') {
            document.getElementById(posid_1).removeAttribute('hidden');
            document.getElementById(posid_1).removeAttribute('disabled');
            document.getElementById(posid_1).setAttribute('classCss','required');

            document.getElementById(posid_2).setAttribute('hidden','hidden');
            document.getElementById(posid_2).setAttribute('disabled','true');
            document.getElementById(posid_2).removeAttribute('classCss');
        } else {
            document.getElementById(posid_1).setAttribute('hidden','hidden');
            document.getElementById(posid_1).setAttribute('disabled','true');
            document.getElementById(posid_1).removeAttribute('classCss');

            document.getElementById(posid_2).removeAttribute('hidden');
            document.getElementById(posid_2).removeAttribute('disabled');
            document.getElementById(posid_2).setAttribute('classCss','required');
        }
    }
</script>
<body>
	<h2 class="contentTitle">${(empty cadreInfo.resourceid)?'新增':'编辑' }学生干部信息</h2>
	<div class="page">
		<div class="pageContent">
		<form method="post" action="${baseUrl}/edu3/work/cadreInfoManage/save.html" class="pageForm" onsubmit="return validateCallback(this);">
			<input type="hidden" name="resourceid" value="${cadreInfo.resourceid }"/>

			<c:if test="${not isStudent}">
				<input type="hidden" name="yearInfoId" value="${cadreInfo.yearInfo.resourceid }"/>
				<input type="hidden" name="term" value="${cadreInfo.term }"/>
				<input type="hidden" name="department" value="${cadreInfo.department }"/>
				<input type="hidden" name="position" value="${cadreInfo.position }"/>
			</c:if>
			<div class="pageFormContent" layoutH="97">
				<table class="form">
					<tr>
						<td width="20%">年度：</td>
						<td width="30%">
						<gh:selectModel id="cadreInfoForm_yearInfoId" name="yearInfoId" bindValue="resourceid" displayValue="yearName" classCss="required"
											 modelClass="com.hnjk.edu.basedata.model.YearInfo" value="${cadreInfo.yearInfo.resourceid}" orderBy="yearName desc" style="width:50%"/>
						</td>
						<td width="20%">学期：</td>
						<td width="30%"><gh:select id="cadreInfoForm_term" name="term" dictionaryCode="CodeTerm" classCss="required" value="${cadreInfo.term}" style="width:50%" /></td>
					</tr>
					<tr>
						<td>学号：</td>
						<td>
							<c:if test="${empty cadreInfo.studyNo}">
								<input type="text" style="width: 40%" id="cadreInfoForm_studyNo" name="studyNo" class="required" value="${cadreInfo.studyNo}">
								<span class="buttonActive" style="margin-left: 20%;">
									<div class="buttonContent"> <button type="button" onclick="queryStuInfo();">查 询</button></div>
								</span>
							</c:if>
							<c:if test="${not empty cadreInfo.studyNo}">
								${cadreInfo.studyNo}<input type="hidden" name="studyNo" value="${cadreInfo.studyNo}">
							</c:if>
						</td>
						<td>姓名：</td>
						<td><input type="text" id="cadreInfoForm_name" class="required" name="studentName" value="${cadreInfo.studentName}" readonly="readonly"></td>
					</tr>

					<tr>
						<td>竞选部门：</td>
						<td>
							<gh:select id="cadreInfoForm_department" name="department" dictionaryCode="Code.WorkManage.department" classCss="required" style="width:50%"
									   value="${cadreInfo.department}" onchange="updatePositionCode(\"cadreInfoForm_position\",this.options[this.options.selectedIndex].value)" />
						</td>
						<td>竞选职位：</td>
						<td>
							<gh:select id="cadreInfoForm_position1" name="position" dictionaryCode="Code.WorkManage.position" value="${cadreInfo.position}" style="width:50%" filtrationStr="1,2" />
							<gh:select id="cadreInfoForm_position2" name="position" dictionaryCode="Code.WorkManage.position" value="${cadreInfo.position}" style="width:50%" filtrationStr="3,4"/>
						</td>
					</tr>
					<tr>
						<td>组织：</td>
						<td><gh:select name="organization" dictionaryCode="Code.WorkManage.organization" value="${cadreInfo.organization}" style="width:50%" /></td>
						<td>调剂职位：</td>
						<td>
							<gh:select name="position_adjust" dictionaryCode="Code.WorkManage.position" value="${cadreInfo.position_adjust}" style="width:50%" />
						</td>
					</tr>
					<c:if test="${isStudent}">
						<tr>
							<td>在校获奖情况：</td>
							<td colspan="3"><textarea name="awards" style="width: 80%" rows="3">${cadreInfo.awards }</textarea></td>
						</tr>
						<tr>
							<td>主要学生工作经历：</td>
							<td colspan="3"><textarea name="workExperience" style="width: 80%" rows="3">${cadreInfo.workExperience }</textarea></td>
						</tr>
						<tr>
							<td>对竞聘职务认识及工作设想：</td>
							<td colspan="3"><textarea name="intention" style="width: 80%" rows="5">${cadreInfo.intention }</textarea></td>
						</tr>
					</c:if>
					<c:if test="${not isStudent}">
						<tr>
							<td>是否候选人：</td>
							<td><gh:select name="isCandidate" dictionaryCode="yesOrNo_default" value="${cadreInfo.isCandidate}" style="width:50%" /></td>
							<td>是否任用：</td>
							<td><gh:select name="isAppoint" dictionaryCode="yesOrNo_default" value="${cadreInfo.isAppoint}" style="width:50%" /></td>
						</tr>
						<tr>
							<td>现任部门：</td>
							<td>
								<gh:select id="cadreInfoForm_department_current" name="department_current" dictionaryCode="Code.WorkManage.department" value="${cadreInfo.department_current}" style="width:50%"
										   onchange="updatePositionCode(\"cadreInfoForm_position_current\",this.options[this.options.selectedIndex].value)" />
							</td>
							<td>现任职位：</td>
							<td>
								<gh:select id="cadreInfoForm_position_current1" name="position_current" dictionaryCode="Code.WorkManage.position" value="${cadreInfo.position_current}" style="width:50%" filtrationStr="1,2" />
								<gh:select id="cadreInfoForm_position_current2" name="position_current" dictionaryCode="Code.WorkManage.position" value="${cadreInfo.position_current}" style="width:50%" filtrationStr="3,4"/>
							</td>
						</tr>
						<tr>
							<td>状态：</td>
							<td><gh:select name="status" dictionaryCode="Code.WorkManage.positionStatus" value="${cadreInfo.status}" style="width:50%" /></td>
							<td>任职时间：</td>
							<td><input type="text"  name="jobTime" size="40" style="width:50%" value="<fmt:formatDate value="${cadreInfo.jobTime }" pattern="yyyy-MM-dd" />"
									   onFocus="WdatePicker({isShowWeek:true})"/></td>
						</tr>
					</c:if>
					<tr>
						<td>备注：</td>
						<td colspan="3"><textarea name="memo" style="width: 50%" rows="3">${cadreInfo.memo }</textarea></td>
					</tr>
				</table>
			</div>
			<div class="formBar">
				<ul>
					<li><div class="buttonActive"><div class="buttonContent">
						<button type="submit">提交</button>
						</div></div></li>
						<li><div class="button"><div class="buttonContent"><button type="button" class="close" onclick="navTab.closeCurrentTab();">取消</button></div></div></li>
				</ul>
			</div>
		</form>
		</div>
	</div>
<script type="text/javascript">
	//验证学号是否存在
	function queryStuInfo() {
		var studyNo = $("#cadreInfoForm_studyNo").val();
        if(studyNo==""){
            alertMsg.warn("请输入学号");
            return false;
        }
        $.ajax({
            type:"post",
            url:"${baseUrl}/edu3/work/cadreInfoManage/validateStudyNo.html",
            data:{studyNo:studyNo},
            dataType:"json",
            cache: false,
            error: DWZ.ajaxError,
            success:function(data){
                if(data.statusCode==200){
					$("#cadreInfoForm_name").val(data.studentName);
                } else {alertMsg.error(data.message);}
            }
        });
    }

</script>
</body>
</html>