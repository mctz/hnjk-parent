<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>班级管理</title>
<script type="text/javascript">
	$(document).ready(function(){
		//$("#classes_majorid").flexselect();
		classesQueryBegin();
	});
	
	//打开页面或者点击查询（即加载页面执行）
	function classesQueryBegin() {
		var defaultValue = "${condition['brSchoolid']}";
		var schoolId = "";
		var isBrschool = "${isBrschool}";
		if(isBrschool==true || isBrschool=="true"){
			schoolId = defaultValue;
		}
		var gradeId = "${condition['gradeid']}";
		var classicId = "${condition['classicid']}";
		var teachingType = "${condition['teachingType']}";
		var majorId = "${condition['majorid']}";
		var classesId = "${condition['classname']}";
		var selectIdsJson = "{unitId:'classes_brSchoolid',gradeId:'classes_gradeid',classicId:'classes_classicid',teachingType:'classes_teachingType',majorId:'classes_majorid'}";
		cascadeQuery("begin", defaultValue, schoolId, gradeId, classicId,teachingType, majorId, classesId, selectIdsJson);
	}

	// 选择教学点
	function classesQueryUnit() {
		var defaultValue = $("#classes_brSchoolid").val();
		var selectIdsJson = "{gradeId:'classes_gradeid',classicId:'classes_classicid',teachingType:'classes_teachingType',majorId:'classes_majorid'}";
		cascadeQuery("unit", defaultValue, "", "", "", "", "", "",selectIdsJson);
	}

	// 选择年级
	function classesQueryGrade() {
		var defaultValue = $("#classes_brSchoolid").val();
		var gradeId = $("#classes_gradeid").val();
		var selectIdsJson = "{classicId:'classes_classicid',teachingType:'classes_teachingType',majorId:'classes_majorid'}";
		cascadeQuery("grade", defaultValue, "", gradeId, "", "", "", "",selectIdsJson);
	}

	// 选择层次
	function classesQueryClassic() {
		var defaultValue = $("#classes_brSchoolid").val();
		var gradeId = $("#classes_gradeid").val();
		var classicId = $("#classes_classicid").val();
		var selectIdsJson = "{teachingType:'classes_teachingType',majorId:'classes_majorid'}";
		cascadeQuery("classic", defaultValue, "", gradeId, classicId, "", "", "", selectIdsJson);
	}

	// 选择学习形式
	function classesQueryTeachingType() {
		var defaultValue = $("#classes_brSchoolid").val();
		var gradeId = $("#classes_gradeid").val();
		var classicId = $("#classes_classicid").val();
		var teachingTypeId = $("#classes_teachingType").val();
		var selectIdsJson = "{majorId:'classes_majorid'}";
		cascadeQuery("teachingType", defaultValue, "", gradeId, classicId,teachingTypeId, "", "", selectIdsJson);
	}

	/* // 选择专业
	function classesQueryMajor() {
		var defaultValue = $("#classes_brSchoolid").val();
		var gradeId = $("#classes_gradeid").val();
		var classicId = $("#classes_classicid").val();
		var teachingTypeId = $("#classes_teachingType").val();
		var majorId = $("#classes_majorid").val();
		var selectIdsJson = "{classesId:'classes_classname'}";
		cascadeQuery("major", defaultValue, "", gradeId, classicId,teachingTypeId, majorId, "", selectIdsJson);
	} */
	
	//新增
	function addClasses(){
		navTab.openTab('RES_ROLL_CLASSES_INPUT', '${baseUrl}/edu3/roll/classes/input.html', '新增班级');
	}
	
	//修改
	function modifyClasses(){		
		if(isCheckOnlyone('resourceid','#classesBody')){
			var url = "${baseUrl}/edu3/roll/classes/input.html?resourceid="+$("#classesBody input[@name='resourceid']:checked").val();
			navTab.openTab('RES_ROLL_CLASSES_INPUT', url, '编辑班级');
		}			
	}		
	//删除
	function removeClasses(){
		pageBarHandle("您确定要删除这些记录吗？","${baseUrl}/edu3/roll/classes/remove.html","#classesBody");
	}
	//按查询条件导出XLS
	function exportXlsClassesList(){
		var url = "${baseUrl }/edu3/roll/classes/exportXls.html?"
		if(isChecked('resourceid',"#classesBody")){
			url += "exportType=1&";
		}
		$('#exportXlsClassesList').remove();
		var iframe = document.createElement("iframe");
		iframe.id = "exportXlsClassesList";
		iframe.src = url+getUrlParam("selectedid");
		iframe.style.display = "none";
		//创建完成之后，添加到body中
		document.body.appendChild(iframe);
	}
	//导出班级学生信息
	function exportStudentsByClasses(){
		var param = getUrlParam("classesids");
		$('#exportXlsStudentsByClasses').remove();
		var iframe = document.createElement("iframe");
		iframe.id = "exportXlsStudentsByClasses";
		iframe.src = "${baseUrl}/edu3/register/studentinfo/exportXlsDivideClass.html?addSql=Y&includeStuStatus=11&"+param;
		iframe.style.display = "none";
		//创建完成之后，添加到body中
		document.body.appendChild(iframe);
	}
	// 打印成绩册
	function printReportBook(){
		if(!isChecked('resourceid',"#classesBody")){
 			alertMsg.warn('请选择一条要导出的记录。');
			return false;
	 	}
		var classIds = new Array();
		var gradeFirstYear = "";
		var isSameGrade = true;
		$("#classesBody input[@name='resourceid']:checked").each(function(){
			classIds.push($(this).val());
			if(gradeFirstYear != "" && gradeFirstYear != $(this).attr("firstYear")){
				isSameGrade = false;
			}
			gradeFirstYear = $(this).attr("firstYear");
   		 });	
		
		if(!isSameGrade){
			alertMsg.warn('请选择相同年级的班级！');
			return false;
		}
		// 弹出打印成绩册的对话框
		$.pdialog.open("${baseUrl }/edu3/roll/classes/printReportBookCondition.html?classIds="+classIds.toString()+"&gradeFirstYear="+gradeFirstYear,
				"selectCondition","打印成绩册",{mask:true,height:200,width:300});
	}
	
	//批量设置班主任
	function setTeacher(){
		if(!isChecked('resourceid',"#classesBody")){
 			alertMsg.warn('请选择班级！');
			return false;
	 	}
		var url = "${baseUrl }/edu3/roll/classes/select-classesmaster.html";
		$.pdialog.open(url+"?idsN=classeslistMasterId&namesN=classeslistMasterName&type=0&"+getUrlParam("classIds"),"selector","选择班主任",{mask:true,height:600,width:800});
	}

	//设置班长
	function setClassLeader() {
        if(!isCheckOnlyone('resourceid',"#classesBody")){
            alertMsg.warn('请选择一条要操作的记录。');
            return false;
        }
        var classesid = $("#classesBody input[name='resourceid']:checked").val();
        var url = "${baseUrl }/edu3/roll/classes/select-classesLeader.html?classesid="+classesid;
        $.pdialog.open(url,"selectorClassLeader","选择班长",{mask:true,height:600,width:800});
    }

	function queryClassesMemo(){
		if(!isCheckOnlyone('resourceid',"#classesBody")){
 			alertMsg.warn('请选择一条要操作的记录。');
			return false;
	 	}
		var url = "${baseUrl }/edu3/roll/classes/classesMemoInfo.html?classesid="+$("#classesBody input[name='resourceid']:checked").val()
		$.pdialog.open(url,"RES_ROLL_CLASSES_MEMO",{mask:true,height:800,width:800});
		/* navTab.openTab('RES_ROLL_CLASSES_MEMO', url, '班级备注信息'); */
	}
	
	function getUrlParam(residsName){
		var param = "";
		if(isChecked('resourceid',"#classesBody")){
			var res = "";
			var k = 0;
			var num  = $("#classesBody input[name='resourceid']:checked").size();
			$("#classesBody input[name='resourceid']:checked").each(function(){
	                res+=$(this).val();
	                if(k != num -1 ) res += ",";
	                k++;
	        });
			param = residsName+"="+res;
		}else{
			var classname 	 = $("#classes_classname").val();
			var gradeid = $("#classes_gradeid").val();
			var brSchoolid 		 = $("#classes_brSchoolid").val();
			var majorid 	 = $("#classes_majorid").val();
			var classicid    = $("#classes_classicid").val();
			var teachingType 		 = $("#classes_teachingType").val();
			param = "branchSchool="+brSchoolid+"&stuGrade="+gradeid+"&major="+majorid+"&classic="+classicid+"&teachingType="+teachingType+"&classname="+encodeURIComponent(encodeURIComponent(classname));
		}
		
		return param;
	}
	
</script>
</head>
<body>
	<div class="page">
		<div class="pageHeader">
			<form onsubmit="return navTabSearch(this);" action="${baseUrl }/edu3/roll/classes/list.html" method="post" id="classesListForm">
				<input type="hidden" id="classeslistfromPage" name="fromPage" value="${fromPage }"/>
				<input type="hidden" id="classeslistMasterId" name="classeslistMasterId" value=""/>
				<input type="hidden" id="classeslistMasterName" name="classeslistMasterName" value=""/>
				<div class="searchBar">
					<ul class="searchContent">
						<%-- <c:if test="${not isBrschool }"> --%>
							<li class="custom-li"><label>教学站：</label> <span sel-id="classes_brSchoolid"
								sel-name="brSchoolid" sel-onchange="classesQueryUnit()"
								sel-classs="flexselect" ></span></li>
						<%-- </c:if>
						<c:if test="${isBrschool}">
							<input type="hidden" name="brSchoolid" id="classes_brSchoolid"
								value="${condition['brSchoolid']}" />
						</c:if> --%>
						<li><label>年级：</label> <span sel-id="classes_gradeid"
							sel-name="gradeid" sel-onchange="classesQueryGrade()"
							sel-style="width: 120px"></span></li>
						<li><label>层次：</label> <span sel-id="classes_classicid"
							sel-name="classicid" sel-onchange="classesQueryClassic()"
							sel-style="width: 120px"></span></li>
						<li><label>办学模式：</label> <span sel-id="classes_teachingType"
							sel-name="teachingType" sel-onchange="classesQueryTeachingType()"
							sel-style="width: 120px"></span></li>

					</ul>
					<ul class="searchContent">
						
						<li class="custom-li"><label>专业：</label> <span sel-id="classes_majorid"
							sel-name="majorid" sel-onchange="classesQueryMajor()"
							sel-classs="flexselect" ></span></li>
						<li><label>班级名称：</label> <input type="text" name="classname"
							id="classes_classname" value="${condition['classname']}"
							style="width: 140px" /> <!-- <span sel-id="classes_classname" sel-name="classname" sel-classs="flexselect" sel-style="width: 120px"></span> -->
						</li>
						<li><label>班级编号：</label> <input type="text" name="classCode"
							id="classes_classCode" value="${condition['classCode']}"
							style="width: 120px" />
						</li>
						<li><label>有无备注：</label> <select id="classes_hasMemo"
							name="hasMemo" style="width: 120px">
								<option value="">请选择</option>
								<option value="Y"
									<c:if test="${condition['hasMemo'] eq 'Y'}"> selected="selected"</c:if>>有</option>
								<option value="N"
									<c:if test="${condition['hasMemo'] eq 'N'}"> selected="selected"</c:if>>无</option>
							</select>
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
			<c:choose>
				<c:when test="${fromPage eq 'examResult' }">
					<gh:resAuth parentCode="RES_TEACHING_RESULT_CLASSES" pageType="list"></gh:resAuth>
				</c:when>
				<c:otherwise>
					<gh:resAuth parentCode="RES_ROLL_CLASSES" pageType="list"></gh:resAuth>
				</c:otherwise>
			</c:choose>
			
			<table class="table" layouth="161">
				<thead>
					<tr>
						<th width="3%"><input type="checkbox" name="checkall"
							id="check_all_classes"
							onclick="checkboxAll('#check_all_classes','resourceid','#classesBody')" /></th>
						<th width="7%">班级编号</th>
						<th width="20%">班级名称</th>
						<th width="7%">年级</th>
						<th width="13%">教学站</th>
						<th width="12%">专业</th>
						<th width="5%">层次</th>
						<th width="5%">学习形式</th>
						<th width="5%">学生人数</th>
						<th width="8%">班主任姓名</th>
						<th width="10%">班长</th>
						<th width="5%">有无备注</th>
					</tr>
				</thead>
				<tbody id="classesBody">
					<c:forEach items="${classesPage.result}" var="cl" varStatus="vs">
						<tr>
							<td><input type="checkbox" name="resourceid"
								value="${cl.resourceid }" autocomplete="off"
								firstYear="${cl.grade.yearInfo.firstYear }" /></td>
							<td>${cl.classCode }</td>
							<td>${cl.classname }</td>
							<td>${cl.grade.gradeName }</td>
							<td>${cl.brSchool.unitName }</td>
							<td>${cl.major.majorName }</td>
							<td>${cl.classic.classicName }</td>
							<td>${ghfn:dictCode2Val('CodeTeachingType',cl.teachingType) }</td>
							<td>${cl.studentNum }</td>
							<td>${cl.classesMaster }</td>
							<td>${cl.classesLeader }</td>
							<td><c:if test="${ cl.memo ne null}">有</c:if> </td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
			<%-- <gh:page page="${classesPage}"
				goPageUrl="${baseUrl }/edu3/roll/classes/list.html?fromPage=${fromPage }" pageType="sys"
				condition="${condition}" /> --%>
		</div>
		<div  class="pageContent" style="position: absolute;bottom: 0px;width: 100%">
			<gh:page page="${classesPage}"
				goPageUrl="${baseUrl }/edu3/roll/classes/list.html?fromPage=${fromPage }"
				pageType="sys" condition="${condition}" /></div>
	</div>
</body>
</html>