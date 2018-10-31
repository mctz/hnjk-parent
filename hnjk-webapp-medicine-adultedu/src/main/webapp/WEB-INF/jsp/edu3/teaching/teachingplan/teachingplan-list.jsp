
<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>教学计划</title>
<script type="text/javascript">
//打开页面或者点击查询（即加载页面执行）
function teachingplanQueryBegin() {
	var defaultValue = "${condition['orgUnitId']}";
	var schoolId = "";
	var isBrschool = "${isBrschool}";
	if(isBrschool==true || isBrschool=="true"){
		schoolId = defaultValue;
	}
	var gradeId = "${condition['name~grade']}";
	var classicId = "${condition['classic']}";
	var teachingType = "${condition['schoolType']}";
	var majorId = "${condition['major']}";
	var classesId = "${condition['name~classes']}";
	var selectIdsJson = "{unitId:'teachingplanist_orgUnitId',gradeId:'id~grade',classicId:'teachingplan_classic',teachingType:'schoolType',majorId:'teachingplan_major',classesId:'id~classes'}";
	cascadeQuery("begin", defaultValue, schoolId, gradeId, classicId,teachingType, majorId, classesId, selectIdsJson);
}

// 选择层次
function teachingplanQueryClassic() {
	var defaultValue = $("#teachingplanist_orgUnitId").val();
	var gradeId = $("#id~grade").val();
	var classicId = $("#teachingplan_classic").val();
	var selectIdsJson = "{teachingType:'schoolType',majorId:'teachingplan_major',classesId:'id~classes'}";
	cascadeQuery("classic", defaultValue, "", gradeId, classicId, "", "", "", selectIdsJson);
}

// 选择学习形式
function teachingplanQueryTeachingType() {
	var defaultValue = $("#teachingplanist_orgUnitId").val();
	var gradeId = $("#id~grade").val();
	var classicId = $("#teachingplan_classic").val();
	var teachingTypeId = $("#schoolType").val();
	var selectIdsJson = "{majorId:'teachingplan_major',classesId:'id~classes'}";
	cascadeQuery("teachingType", defaultValue, "", gradeId, classicId,teachingTypeId, "", "", selectIdsJson);
}

	//新增
	function addTPlan(){
		navTab.openTab('RES_TEACHING_ESTAB_PLAN_EDIT', '${baseUrl}/edu3/teaching/teachingplan/edit.html', '新增教学计划');
	}
	
	//修改
	function modifyTPlan(){
		var url = "${baseUrl}/edu3/teaching/teachingplan/edit.html";
		var obj = $("#tplanBody input[@name='resourceid']:checked");

		if(obj.attr("title") == 'Y'){
			alertMsg.warn('该教学计划已使用，不能修改！');
			return false;
		}
		if(isCheckOnlyone('resourceid','#tplanBody')){
			navTab.openTab('RES_TEACHING_ESTAB_PLAN_EDIT', url+'?resourceid='+$("#tplanBody input[@name='resourceid']:checked").val(), '编辑教学计划');
		}			
	}
	//设置门数
	function setElectiveNum(){
		var url = "${baseUrl}/edu3/teaching/teachingplan/setElectiveNum.html";
		if(!isChecked('resourceid','#tplanBody')){
 			alertMsg.warn('请选择一条要操作记录！');
			return false;
 		}
		var res = "";
		var k = 0;
		var num  = $("#tplanBody input[name='resourceid']:checked").size();
		$("#tplanBody input[@name='resourceid']:checked").each(function(){
            res+=$(this).val();
            if(k != num -1 ) res += ",";
            k++;
        })
		$.pdialog.open(url+'?teachingPlanIds='+res,'setElectiveNum','设置选修门数',{width:400,height:150});
	}
	//复制
	function copyTeachingPlan(){
		var url = "${baseUrl}/edu3/teaching/teachingplan/edit.html";
		if(isCheckOnlyone('resourceid','#tplanBody')){
			alertMsg.confirm("您确定要复制这份教学计划吗？", {
				okCall: function(){//执行			
					navTab.openTab('RES_TEACHING_ESTAB_PLAN_EDIT', url+'?act=copy&resourceid='+$("#tplanBody input[@name='resourceid']:checked").val(), '复制教学计划');	
				}
			});				
		}
	}
		
	//删除
	function deleteTPlan(){		
			pageBarHandle("您确定要删除这些记录吗？","${baseUrl}/edu3/teaching/teachingplan/delete.html","#tplanBody");
		
	}
		
	//浏览
	function openTeachplanView(pid){
	$.pdialog.open(baseUrl+'/edu3/teaching/teachingplan/edit.html?resourceid='+pid+'&act=view',
					'viewTeachplanInfo',
					'查看教学计划',
					{width:1024,height:600}
					);
	}
	
	function mgrSyllabusind(){
		if(isCheckOnlyone('resourceid','#tplanBody')){
			navTab.openTab('Syllabusind', baseUrl+'/edu3/teaching/teachingplan/edit.html?act=syllabusind&resourceid='+$("#tplanBody input[@name='resourceid']:checked").val(), '指标库管理');
		}
	}
	//打开教学计划课程管理列表
	function openTeachingCourseList(){
		
	}
	/* function brschool_Major(){
		var unitId = $("#teachingplanist_orgUnitId").val();
		var majorId = $("#teachingplan_major").val();
		var classicId = $("#teachingplan_classic").val();
		var gradeId   = $("#stuGrade").val();
		var classesId = $("#stuInfo #stuClasses").val();//$("#classesId").val();
		var flag = "unitor";
		var url = "${baseUrl}/edu3/register/studentinfo/brschool_Major.html";
		$.ajax({
			type:'POST',
			url:url,
			data:{unitId:unitId,majorId:majorId,classicId:classicId,gradeId:gradeId,classesId:classesId,flag:flag},
			dataType:"json",
			cache: false,
			error: DWZ.ajaxError,
			success: function(data){
				if(data['result'] == 300){
					if(undefined!=data['msg']){
						alertMsg.warn(data['msg']);
					}
			    }else{
			    	//$("#teachingplan_major").replaceWith("<select id=\"teachingplan_major\" style=\"width:120px\" name=\"major\" onchange=\"brschool_Major()\">"+data['majorOption']+"</select>");
			    	$("#teachingplan_major").replaceWith("<select  class=\"flexselect textInput\" id=teachingplan_major name=\"major\" tabindex=1 style=width:120px; onchange='brschool_Major()' >"+data['majorOption']+"</select>");
				    $("#major_flexselect").remove();
				    $("#major_flexselect_dropdown").remove();
				    $("#teachingplan_major").flexselect();
			    	$("#stuClasses").replaceWith("<select id=\"stuClasses\" style=\"width: 120px\" name=\"classesid\">"+data['classesOption']+"</select>");
				}
			}
		});
		
	} */
	$(document).ready(function(){
		//brschool_Major();
		$("#teachingplan_major").flexselect();
		$("#planName").flexselect();
	});
	$(document).ready(function(){
		var studentStatusSet= '${stuStatusSet}';
		var statusRes= '${stuStatusRes}';
		//orgStuStatus("#stuInfo #stuStatus",studentStatusSet,statusRes,"12,13,15,18,21,19,23,a11,b11");
		/* $("#teachingplanist_orgUnitId").flexselect({
			  specialevent:function(){brschool_Major();}
		}); 
		
		brschool_Major();
		$("#teachingplan_classesid").flexselect();
		$("#teachingplan_major").flexselect();*/
		teachingplanQueryBegin();
	});
	
</script>
</head>
<body>

	<div class="page">
		<div class="pageHeader">
			<form onsubmit="return navTabSearch(this);"
				action="${baseUrl }/edu3/teaching/teachingplan/list.html"
				method="post">
				<div class="searchBar">
					<ul class="searchContent">
						<%-- <c:if test="${!isBrschool }">
					<li>
						<label>教学站：</label>
						<span sel-id="teachingplanist_orgUnitId" sel-name="orgUnitId" sel-onchange="teachingplanQueryUnit()" sel-classs="flexselect"  sel-style="width: 120px" ></span>
					</li>
				</c:if>		 --%>
						<c:if test="${isBrschool}">
							<input type="hidden" name="orgUnitId"
								id="teachingplanist_orgUnitId" value="${condition['orgUnitId']}" />
						</c:if>
						<li><label>层次：</label> <span sel-id="teachingplan_classic"
							sel-name="classic" sel-onchange="teachingplanQueryClassic()"
							sel-style="width: 120px"></span></li>
						<c:if test="${empty condition['roleModules'] }">
							<li><label>办学模式：</label> <span sel-id="schoolType"
								sel-name="schoolType"
								sel-onchange="teachingplanQueryTeachingType()"
								dictionaryCode="CodeTeachingType" sel-style="width: 120px"></span>
							</li>
						</c:if>
						<li>专业： <span sel-id="teachingplan_major"
							sel-name="major" sel-onchange="teachingplanQueryMajor()"
							sel-classs="flexselect" sel-style="width: 180px"></span></li>
					</ul>
					<ul class="searchContent">
						
						<li><label>是否使用：</label>
						<gh:select name="isUsed" value="${condition['isUsed']}"
								dictionaryCode="yesOrNo" style="width:120px;" /></li>
						<li style="width: 480px;"><label>计划名称：</label><select id="planName" name="planName"
							class="flexselect" style="width: 365px;">${planOptions }</select>
						</li>
					</ul>
					<div class="subBar">
						<ul>
							<li><div class="buttonActive">
									<div class="buttonContent">
										<button type="submit">查 询</button>
									</div>
								</div></li>
						</ul>
					</div>
				</div>
			</form>
		</div>
		<div class="pageContent">
			<gh:resAuth parentCode="RES_TEACHING_ESTAB_PLAN" pageType="list"></gh:resAuth>
			<table class="table" layouth="161">
				<thead>
					<tr>
						<th width="5%"><input type="checkbox" name="checkall"
							id="check_all_tp"
							onclick="checkboxAll('#check_all_tp','resourceid','#tplanBody')" /></th>
						<th width="20%">计划名称</th>
						<th width="10%">专业</th>
						<th width="8%">层次</th>
						<th width="8%">办学模式</th>
						<th width="8%">学制</th>
						<th width="8%">选修门数</th>
						<th width="8%">学位授予</th>
						<th width="10%">制定时间</th>
					</tr>
				</thead>
				<tbody id="tplanBody">
					<c:forEach items="${planList.result}" var="plan" varStatus="vs">
						<tr>
							<td><input type="checkbox" name="resourceid"
								value="${plan.resourceid }" title="${plan.isUsed }"
								autocomplete="off" /></td>
							<td><a href="#"
								onclick="openTeachplanView('${plan.resourceid }');"> <c:choose>
										<c:when test="${not empty plan.planName }">
			            			${plan.planName }
			            		</c:when>
										<c:when test="${not empty plan.orgUnit }">
			            			${plan.major } - ${plan.classic } (${plan.orgUnit.unitShortName})
			            		</c:when>
										<c:otherwise>
			            			${plan.major } - ${plan.classic } (${plan.versionNum})
			            		</c:otherwise>
									</c:choose>
							</a></td>
							<td>${plan.major }</td>
							<td>${plan.classic }</td>
							<td>${ghfn:dictCode2Val('CodeTeachingType',plan.schoolType) }</td>
							<td>${plan.eduYear }</td>
							<td>${plan.optionalCourseNum }</td>
							<td>${ghfn:dictCode2Val('CodeDegree',plan.degreeName ) }</td>
							<td><fmt:formatDate value="${plan.fillinDate }"
									pattern="yyyy-MM-dd" /></td>
						</tr>
					</c:forEach>
				</tbody>
			</table>

			<gh:page page="${planList}"
				goPageUrl="${baseUrl }/edu3/teaching/teachingplan/list.html"
				pageType="sys" condition="${condition}" />
		</div>
	</div>

</body>
</html>