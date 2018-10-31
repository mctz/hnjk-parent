<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>考生信息</title>
<script type="text/javascript">
	$(document).ready(function(){
		$("#predistribution_brSchoolName").flexselect({});
		linkageQueryBegin();
	});
	
	
	
	// 打开页面或者点击查询（即加载页面执行）
   function linkageQueryBegin() {
	   var defaultValue = "${condition['branchSchool']}";
	   var schoolId = "";
	   var isBrschool = "${isBrschool}";
	   if(isBrschool){
		   schoolId = defaultValue;
	   }
	   var gradeId = "";
	   var classicId = "${condition['classic']}";
	   var teachingType = "";
	 
	   var majorId = "${condition['major']}";
	
	   var selectIdsJson = "{unitId:'predistribution_brSchoolName',classicId:'predistribution_classic',majorId:'predistribution_major'}";
	   cascadeQuery("begin", defaultValue, schoolId, gradeId, classicId, teachingType, majorId, "", selectIdsJson);
	   
   }
   
   // 选择教学点
   function linkageQueryUnit() {
	   var defaultValue = $("#predistribution_brSchoolName").val();
	   var selectIdsJson = "{classicId:'predistribution_classic',majorId:'predistribution_major'}";
	   cascadeQuery("unit", defaultValue, "", "", "", "", "", "", selectIdsJson);
   }
	
   
	// 选择层次
   function linkageQueryClassic() {
	   var defaultValue = $("#predistribution_brSchoolName").val();
	   var classicId = $("#predistribution_classic").val();
	   var selectIdsJson = "{majorId:'predistribution_major'}";
	   cascadeQuery("classic", defaultValue, "", "", classicId, "", "", "", selectIdsJson); 
   }

  
	
// 根据年级同步联动信息（招生专业）
   function syncLinkageQuery() {
   	var selectGradeUrl = "${baseUrl}/edu3/teaching/linkageQuery/selectGrade.html";
	   $.pdialog.open(selectGradeUrl, 'RES_TEACHING_LINKAGEQUERY_SELECTGRADE', '选择年级', {mask:true,width: 300, height: 200});
   }
	
	
	//分配教学站
	function updateUnit(){
		var ids = "";
		var isSameMajor = true;
		var major = "";
		$("#exameeInfoBody input[@name='resourceid']:checked").each(function(){
			if(""==ids){
				ids += $(this).val();
	    	}else{
	    		ids += ","+$(this).val();
	    	}
			// 判断是否是同一个录取专业
			if(isSameMajor){
				if(major != "" && major != $(this).attr("major") ){
					isSameMajor = false;
				}
				major = $(this).attr("major");
		    }
        });
		if(ids == ""){
			alertMsg.warn("请您至少选择一条记录进行分配操作.");
			return;
		}
		if(!isSameMajor){
			alertMsg.warn("请您选择同一个录取专业");
			return;
		}
		$.pdialog.open(baseUrl+"/edu3/recruit/exameeinfo/updateinputunit.html?ids="+ids+"&majorName="+encodeURI(major), 'RES_TEACHING_TEACHINGPLANCOURSETIMETABLE_INPUT', '分配教学站', {width: 600, height: 100});
	}
	
	function applydistribute(){
		pageBarHandle("确定要将已勾选的学生申请分配到自己的教学点下吗？","${baseUrl}/edu/recruit/exameeinfo/predistribution-apply.html","#predistributionBody");
	}
	function deleteApply(resourceid){
		var postUrl = "${baseUrl}/edu/recruit/exameeinfo/predistribution-deleteApply.html";		
		$.post(postUrl,{resourceid:resourceid}, navTabAjaxDone, "json");
	}
	
	function auditPass(){
		pageBarHandle("确定审核通过吗？","${baseUrl}/edu/recruit/exameeinfo/predistribution-audtiPass.html","#predistributionBody");
	}
	function auditNopass(){
		pageBarHandle("确定审核不通过吗？","${baseUrl}/edu/recruit/exameeinfo/predistribution-audtiNopass.html","#predistributionBody");
	}
</script>
</head>
<body>
	<div class="page">
		<div class="pageHeader">
			<form onsubmit="return navTabSearch(this);"
				action="${baseUrl }/edu3/recruit/exameeinfo/predistribution-list.html" method="post">
				<div class="searchBar">
					<ul class="searchContent">
							<li class="custom-li"><label>教学站：</label> <span
								sel-id="predistribution_brSchoolName" sel-name="branchSchool"
								sel-onchange="linkageQueryUnit()" sel-classs="flexselect"></span></li>
						
						<li><label>招生批次：</label> <gh:recruitPlanAutocomplete
								name="recruitPlanId" tabindex="1" id="predistribution_recruitPlanId"
								value="${condition['recruitPlanId']}" style="width:140px;" /></li>
						<li><label>层次：</label> <span sel-id="predistribution_classic"
							sel-name="classic" sel-onchange="linkageQueryClassic()"
							sel-style="width: 120px;"></span></li>
						
					</ul>
					<ul class="searchContent">
						<li class="custom-li"><label>专业：</label> <span sel-id="predistribution_major"
							sel-name="major" sel-classs="flexselect"></span>
						</li>
						<li><label>准考准号：</label> <input type="text"
							name="examcertificateno" value="${condition['examcertificateno']}"
							style="width: 140px;" id="predistribution_examcertificateno" /></li>
						<li><label>姓名：</label> <input type="text" name="name"
							value="${condition['name']}" style="width: 115px;"
							id="exameeInfo_name" /></li>
						
						
					</ul>
					<ul class="searchContent">
						<li class="custom-li"><label>证件号码：</label> <input type="text" name="certNum"
							value="${condition['certNum']}" class="custom-inp"
							id="predistribution_certNum" /></li>
						<li><label>分配状态：</label> <gh:select
								dictionaryCode="CodeCheckStatus" id="predistribution_auditStatus"
								name="auditStatus" value="${condition['auditStatus'] }" filtrationStr="0" style="width:120px;" />
						</li>
						
					</ul>
					<div class="subBar">
						<ul>
							<li><div class="buttonActive">
									<div class="buttonContent">
										<button type="submit">查询</button>
									</div>
								</div></li>
						</ul>
					</div>
				</div>
			</form>
		</div>
		<div class="pageContent">
			<gh:resAuth parentCode="RES_RECRUIT_MANAGE_PREDISTRIBUTION" pageType="list" ></gh:resAuth>
				<table class="table" layouth="209">
				<thead>
					<tr>
						<th width="3%"><input type="checkbox" name="checkall"
							id="check_all_predistribution"
							onclick="checkboxAll('#check_all_predistribution','resourceid','#predistributionBody')" /></th>
						<th width="5%">招生批次</th>
						<th width="5%">层次</th>
						<th width="10%">录取专业</th>						
						<th width="8%">准考准号</th>						
						<th width="5%">姓名</th>
						<th width="5%">性别</th>
						<th width="10%">证件号码</th>
						<th width="10%">当前教学站</th>
						<th width="10%">预分配教学站</th>
						<th width="10%">申请用户名</th>
						<th width="5%">状态</th>
					</tr>
				</thead>
				<tbody id="predistributionBody">
					<c:forEach items="${page.result}" var="pd"
						varStatus="vs">
						<tr>
							<td><input type="checkbox" name="resourceid"
								value="${pd.resourceid}" auditstatus="${pd.predistribution.auditStatus }" autocomplete="off"
								 /></td>
							<td>${pd.grade.gradeName }</td>
							<td>${pd.recruitMajor.classic.classicName }</td>
							<td>${pd.recruitMajor.major.majorName }</td>
							<td>${pd.examCertificateNo }</td>
							<td>${pd.studentBaseInfo.name }</td>
							<td>${ghfn:dictCode2Val('CodeSex',pd.studentBaseInfo.gender) }</td>
							<td>${pd.studentBaseInfo.certNum }</td>
							<td>${pd.branchSchool.unitName }</td>
							<td>${pd.predistribution.nextUnit.unitName }</td>
							<td>${pd.predistribution.applyUser.cnName }</td>
							<td>${ghfn:dictCode2Val('CodeCheckStatus', pd.predistribution.auditStatus)}
							<c:if test="${ pd.predistribution.auditStatus eq '0' and isBrschool and pd.predistribution.nextUnit.resourceid eq condition['branchSchool']}">
								(<a href="#" onclick="deleteApply('${pd.resourceid}')"><font color='red'>撤销</font></a>)
							</c:if>
							</td>
<%-- 							<td>${ghfn:dictCode2Val('yesOrNo',exameeInfo['isPrint']) }</td> --%>
						</tr>
					</c:forEach>
				</tbody>
			</table>
		</div>
		<div  class="pageContent" style="position: absolute;bottom: 0px;width: 100%">
		<gh:page page="${page}" targetType="navTab"
				goPageUrl="${baseUrl }/edu3/recruit/exameeinfo/predistribution-list.html"
				pageType="sys" condition="${condition}" /></div>
	</div>
</body>
</html>
