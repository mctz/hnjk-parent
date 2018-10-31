<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>高级查询</title>

</head>
<body>
	<script type="text/javascript">
	jQuery(document).ready(function(){
		jQuery("#entranceAuditSearchForm #recruitPlan").change(function(){
		    var url    = "${baseUrl}/edu3/recruit/dynamicmenu/recruitplan-recruitmajor.html";
		    var planid = jQuery(this).val();
		    jQuery.post(url,{recruitPlan:planid},function(myJSON){
				jQuery("#entranceAuditSearchForm #recruitMajor").html("");
				var selectObj="<option value=''>请选择</option>";
				for (var i = 0; i < myJSON.length; i++) {
				  selectObj += '<option value="' + myJSON[i].key + '" title="'+myJSON[i].value+'">' + myJSON[i].name + '</option>';    
				}
				  jQuery("#entranceAuditSearchForm #recruitMajor").html(selectObj);
			},"json");
		});
	});
</script>
	<div class="page">
		<div class="pageContent">
			<form id="enrolleeinfoListSearchForm " method="post"
				action="${baseUrl }/edu3/recruit/enroll/enrolleeinfo-list.html"
				class="pageForm" onsubmit="return navTabSearch(this);">
				<div class="pageFormContent" layoutH="58">
					<c:if test="${empty condition['brshSchool']}">
						<div id="advanceSearch">
							<label>教学站：</label>
							<!-- 
					 <input type="hidden" id="eiinfo_brSchoolId1" name="branchSchool" value="${condition['branchSchool']}"/>	
					<input type="text" id="eiinfo_brSchoolName1" name="branchSchoolName" value="${condition['branchSchoolName']}"/>
					-->
							<gh:brSchoolAutocomplete name="branchSchool" tabindex="1"
								id="eiinfo_brSchoolId1"
								defaultValue="${condition['branchSchool']}" />
							<span class="inputInfo">关键字或全称</span>
						</div>
					</c:if>
					<div>
						<label>招生批次：</label>
						<gh:recruitPlanAutocomplete name="recruitPlan" tabindex="1"
							id="_eiinfo1_recruitPlan" value="${condition['recruitPlan']}"
							style="width:22%" />
					</div>

					<div>
						<label>专 业：</label>

						<gh:selectModel id="major" name="major" bindValue="resourceid"
							displayValue="majorName" isSubOptionText="Y" style="width:23%"
							modelClass="com.hnjk.edu.basedata.model.Major"
							value="${condition['major']}" />
					</div>
					<div>
						<label>层 次：</label>
						<gh:selectModel name="classic" bindValue="resourceid"
							displayValue="classicName" style="width:23%"
							modelClass="com.hnjk.edu.basedata.model.Classic"
							value="${condition['classic']}" />
					</div>
					<div>
						<label>学习形式：</label>
						<gh:select name="teachingType" dictionaryCode="CodeTeachingType"
							value="${condition['teachingType']}" style="width:23%" />
					</div>
					<%--
				<div>
					<label>报名类型：</label>
					<gh:select name="enrollType" dictionaryCode="CodeEnrolleeType" value="${condition['enrollType']}" style="width:23%"/>
				</div>	
				<div>
					<label>学习方式：</label>
					<gh:select name="learningStyle" dictionaryCode="CodeLearningStyle" value="${condition['learningStyle']}" style="width:23%"/>
				</div>	
				<div>
					<label>是否免试：</label>
					<gh:select name="isApplyNoexam" dictionaryCode="CodeIsApplyNoExam" value="${condition['isApplyNoexam']}" style="width:23%"/>
				</div>		 --%>
					<div>
						<label>姓 名：</label> <input type="text" id="name" name="name"
							value="${condition['name']}" /> <span class="inputInfo">请输入学生姓名</span>
					</div>
					<div>
						<label>准考证号：</label> <input type="text" name="examCertificateNo"
							value="${condition['examCertificateNo']}" /> <span
							class="inputInfo">请输入准考证号</span>
					</div>
					<div>
						<label>证件号码：</label> <input type="text" name="certNum"
							value="${condition['certNum']}" /> <span class="inputInfo">请输入身证件号码</span>
					</div>
					<div class="divider">divider</div>
					<div>
						<label>照片条件：</label> <label class="radioButton"><input
							name="isExistsPhoto" type="radio" value="Y" />存在照片</label> <label
							class="radioButton"><input name="isExistsPhoto"
							type="radio" value="N" />缺少照片</label>
					</div>
					<div>
						<label>排序条件：</label> <select name="orderBy">
							<option value="">--请选择--</option>
							<option value="recruitMajor.recruitPlan.recruitPlanname">招生批次</option>
							<option value="branchSchool.unitCode">教学站</option>
							<option value="recruitMajor.major.majorName">专业</option>
							<option value="recruitMajor.classic.classicName">层次</option>
							<option value="studentBaseInfo.name">姓名</option>
							<option value="examCertificateNo">准考证号</option>
						</select> <label class="radioButton"><input name="orderType"
							type="radio" checked="checked" value="ASC" />顺序</label> <label
							class="radioButton"><input name="orderType" type="radio"
							value="DESC" />倒序</label>
					</div>
				</div>
				<div class="formBar">
					<ul>
						<li><div class="buttonActive">
								<div class="buttonContent">
									<button type="submit">开始检索</button>
								</div>
							</div></li>
						<li><div class="button">
								<div class="buttonContent">
									<button type="reset">清空重输</button>
								</div>
							</div></li>
					</ul>
				</div>
			</form>
		</div>
	</div>
</body>
</html>
