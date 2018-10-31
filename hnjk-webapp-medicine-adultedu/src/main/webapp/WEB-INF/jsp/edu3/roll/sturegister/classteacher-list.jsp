<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>学籍信息管理--班主任管理</title>
<script type="text/javascript">
$(document).ready(function(){
	var studentStatusSet= '${stuStatusSet}';
	var statusRes= '${stuStatusRes}';
	orgStuStatus("#stuInfo #stuStatus",studentStatusSet,statusRes,"12,13,15,18,21,19,23,a11,b11,25");
	/**
	$("#eiinfo_brSchoolName").flexselect({
		  specialevent:function(){brschool_Major();}
	});
	
	brschool_Major();
	
	$("#eiinfo_brSchoolName").flexselect();
	$("#classteacher_major").flexselect();
	$("#classteacher_classesid").flexselect();
	**/
	classTeacherQueryBegin();
});

//打开页面或者点击查询（即加载页面执行）
function classTeacherQueryBegin() {
	var defaultValue = "${condition['branchSchool']}";
	var schoolId = "${linkageQuerySchoolId}";
	var gradeId = "${condition['stuGrade']}";
	var classicId = "${condition['classic']}";
	var teachingType = "${condition['name~teachingType']}";
	var majorId = "${condition['major']}";
	var classesId = "${condition['classesid']}";
	var selectIdsJson = "{unitId:'eiinfo_brSchoolName',gradeId:'stuGrade',classicId:'classic',teachingType:'id~teachingType',majorId:'classteacher_major',classesId:'classteacher_classesid'}";
	cascadeQuery("begin", defaultValue, schoolId, gradeId, classicId,teachingType, majorId, classesId, selectIdsJson);
}

// 选择教学点
function classTeacherQueryUnit() {
	var defaultValue = $("#eiinfo_brSchoolName").val();
	var selectIdsJson = "{gradeId:'stuGrade',classicId:'classic',teachingType:'id~teachingType',majorId:'classteacher_major',classesId:'classteacher_classesid'}";
	cascadeQuery("unit", defaultValue, "", "", "", "", "", "",selectIdsJson);
}

// 选择年级
function classTeacherQueryGrade() {
	var defaultValue = $("#eiinfo_brSchoolName").val();
	var gradeId = $("#stuGrade").val();
	var selectIdsJson = "{classicId:'classic',teachingType:'id~teachingType',majorId:'classteacher_major',classesId:'classteacher_classesid'}";
	cascadeQuery("grade", defaultValue, "", gradeId, "", "", "", "",selectIdsJson);
}

// 选择层次
function classTeacherQueryClassic() {
	var defaultValue = $("#eiinfo_brSchoolName").val();
	var gradeId = $("#stuGrade").val();
	var classicId = $("#classic").val();
	var selectIdsJson = "{teachingType:'id~teachingType',majorId:'classteacher_major',classesId:'classteacher_classesid'}";
	cascadeQuery("classic", defaultValue, "", gradeId, classicId, "", "", "", selectIdsJson);
}

// 选择专业
function classTeacherQueryMajor() {
	var defaultValue = $("#eiinfo_brSchoolName").val();
	var gradeId = $("#stuGrade").val();
	var classicId = $("#classic").val();
	var teachingTypeId = $("#id~teachingType").val();
	var majorId = $("#classteacher_major").val();
	var selectIdsJson = "{classesId:'classteacher_classesid'}";
	cascadeQuery("major", defaultValue, "", gradeId, classicId,teachingTypeId, majorId, "", selectIdsJson);
}

//替学生学籍异动申请
function modifyStuInfo1(){
	if(isCheckOnlyone('resourceid','#classteacherInfoBody')){
		
		
		var rows = document.getElementById("classteacherInfoBody").rows; 
		var a = document.getElementsByName("resourceid"); 
		for(var i=0;i<a.length;i++) 
		{ 
		if(a[i].checked){ 
		var row = a[i].parentElement.parentElement.parentElement.rowIndex; 
		
		
		  var aa=rows[row].cells[2].children;
			var stuid=aa[0].innerHTML;
		} 
		}


		var url = "${baseUrl}/edu3/register/stuchangeinfo/stuid.html";
		navTab.openTab('RES_TEACHER_SCHOOL_SCHOOLROLL_CHANGE_SELF', url+'?stuchangeNO='+stuid, '申请');
		
	}
} 

//跨学年复学
function strideStudy(){
	if(!isChecked('resourceid',"#classteacherInfoBody")){
		alertMsg.warn('请选择一条记录！');
		return false;
 	}
	var stuid = $("#classteacherInfoBody input[@name='resourceid']:checked").val();
	var status = $("#classteacherInfoBody input[@name='resourceid']:checked").attr("alt");
	if('12' != status){
		alertMsg.warn('学籍状态不为休学！');
		return false;
	}
	
	var url = "${baseUrl}/edu3/register/studentinfo/strideStudy.html?stuid="+stuid;
	navTab.openTab('RES_SCHOOL_SCHOOLROLL_STRIDE_STUDY', url, '跨学年复学');
	//给本页加上重载的设定
	navTab.reloadFlag("RES_SCHOOL_SCHOOLROLL_MANAGER");
}

//组合学籍状态的方法(参数为:空白的select控件,原始的组合学籍状态集合,上次查询选择的值,过滤得到的学籍状态)
function orgStuStatus(selectid,studentStatusSet,statusRes,val){
	var html = "<option value=''>请选择</option>";	
	var status= studentStatusSet.split(",");
	var filter = val.split(",");
	for(var i=0;i<(status.length-1)/2;i++){
		for(var j=0;j<filter.length;j++){
			if(filter[j]==status[2*i]){
				if(statusRes==status[2*i]){
					html += "<option selected='selected' value='"+status[2*i]+"'>"+status[2*i+1]+"</option>";
				}else{
					html += "<option value='"+status[2*i]+"'>"+status[2*i+1]+"</option>";
				}
			}
		}
	}
	$(selectid).html(html);
}
/* function brschool_Major(){
	var unitId = $("#eiinfo_brSchoolName").val();
	var majorId = $("#classteacher_major").val();
	var classicId = $("#classic").val();
	var gradeId   = $("#stuGrade").val();
	var classesId = $("#stuInfo #classteacher_classesid").val();//$("#classesId").val();
	var flag = "master";
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
		    	//$("#major").replaceWith("<select id=\"major\" style=\"width:120px\" name=\"major\" onchange=\"brschool_Major()\">"+data['majorOption']+"</select>");
		    	$("#major").replaceWith("<select  class=\"flexselect textInput\" id=major name=\"major\" tabindex=1 style=width:120px; onchange='brschool_Major()' >"+data['majorOption']+"</select>");
			    $("#major_flexselect").remove();
			    $("#major_flexselect_dropdown").remove();
			    $("#major").flexselect();
		    	$("#stuClasses").replaceWith("<select id=\"stuClasses\" style=\"width: 120px\" name=\"classesid\">"+data['classesOption']+"</select>");
			}
		}
	});
	
} */
//导入入学资格数据
function recruitStatusImport(){
	var url = "${baseUrl}/edu3/register/studentinfo/recruitStatusImport/form.html";
	$.pdialog.open(url,"RES_SCHOOL_SCHOOLROLL_IMPORT_RECRUITSTATUS","导入入学资格",{width:600, height:300});
}

//导出学信网
function exportxxweb(){
	var stuGrade 	 = $("#stuGrade").val();
	var branchSchool = $("#eiinfo_brSchoolName").val();
	var major 		 = $("#classteacher_major").val();
	var classic 	 = $("#classic").val();
	var stuStatus    = $("#stuStatus").val();
	var stuGrade 	 = $("#stuGrade").val();
	var name 		 = $("#name").val();
	var certNum 	 = $("#certNum").val();
	var studyNo 	 = $("#matriculateNoticeNo").val();
	
	var rollCard     = $("#rollCard").val(); //是否提交学籍卡
	var classes      = $("#classteacher_classesid").val();//班级
	
	//20140102 林辉 提议不限制办学单位
	$('#frame_exportXlsStudentInfoList').remove();
	var iframe = document.createElement("iframe");
	iframe.id = "frame_exportXlsStudentInfoList";
	iframe.src = "${baseUrl }/edu3/register/studentinfo/exportxxweb.html?branchSchool="+branchSchool+"&major="+major+"&classic="+classic+"&stuStatus="+stuStatus+"&stuGrade="+stuGrade+"&name="+encodeURIComponent(encodeURIComponent(name))+"&studyNo="+studyNo+"&certNum="+certNum+"&rollCard="+rollCard+"&classes="+classes;
	iframe.style.display = "none";
	//创建完成之后，添加到body中
	document.body.appendChild(iframe);
}

//打印考生信息
function printExameeInfoInRollPage(){
	var studentId = [];
	//条件 查询
	var unitId 		= $("#stuInfo #eiinfo_brSchoolName").val();
	var classicId 	= $("#stuInfo #classic").val();
	var majorId 	= $("#stuInfo #classteacher_major").val();
	var gradeId  	= $("#stuInfo #stuGrade").val();
	var classesId 	= $("#stuInfo #classteacher_classesid").val();
	var matriculateNoticeNo = $("#stuInfo #matriculateNoticeNo").val();
	var name  		= $("#stuInfo #name").val();
	var rollCard 	= $("#stuInfo #rollCard").val();
	var certNum  	= $("#stuInfo #certNum").val();
	var stuStatus 	= $("#stuInfo #stuStatus").val();
	var entranceFlag  = $("#stuInfo #entranceFlag").val();
	var param = "&unitId="+unitId+"&classicId="+classicId+"&majorId="+majorId+"&gradeId="+gradeId
	+"&classesId="+classesId+"&matriculateNoticeNo="+matriculateNoticeNo+"&name="+name+"&rollCard="+rollCard
	+"&certNum="+certNum+"&stuStatus="+stuStatus+"&entranceFlag="+entranceFlag;
	$("#classteacherInfoBody input[@name='resourceid']:checked").each(function(){
		studentId.push($(this).val());
	});
	
	var url = "${baseUrl}/edu3/register/studentinfo/exameeinfo/calulatePrintNum.html";
	$.ajax({
		type:'POST',
		url:url,
		data:{id:studentId.join(','),unitId:unitId,classicId:classicId,majorId:majorId,gradeId:gradeId,classesId:classesId,matriculateNoticeNo:matriculateNoticeNo,name:name,rollCard:rollCard,certNum:certNum,stuStatus:stuStatus,entranceFlagL:entranceFlag},
		dataType:"json",
		cache: false,
		error: DWZ.ajaxError,
		success: function(data){
			if(data['over1000']=='1'){
				alertMsg.warn(data['question']+" 打印数目超过1000，暂不支持此数量级打印。");
				return false;
			}else{
				alertMsg.confirm(data['question'],{
				    okCall:function(){
				    	$.pdialog.open(baseUrl+"/edu3/register/studentinfo/exameeinfo/printview.html?studentId="+studentId.join(',')+param,'RES_SCHOOL_SCHOOLROLL_MANAGER_PRINTEXAMSTUINFO','打印预览',{height:600, width:800});
				    }
			    });
			}
		}
	});
}
//学籍卡打印 
function studentRollCardPrintInRoll(){
	
	var studentId = [];
	//条件 查询
	var unitId 		= $("#stuInfo #eiinfo_brSchoolName").val();
	var classicId 	= $("#stuInfo #classic").val();
	var majorId 	= $("#stuInfo #classteacher_major").val();
	var gradeId  	= $("#stuInfo #stuGrade").val();
	var classesId 	= $("#stuInfo #classteacher_classesid").val();
	var matriculateNoticeNo = $("#stuInfo #matriculateNoticeNo").val();
	var name  		= $("#stuInfo #name").val();
	var rollCard 	= $("#stuInfo #rollCard").val();
	var certNum  	= $("#stuInfo #certNum").val();
	var stuStatus 	= $("#stuInfo #stuStatus").val();
	var entranceFlag  = $("#stuInfo #entranceFlag").val();
	$("#classteacherInfoBody input[@name='resourceid']:checked").each(function(){
		studentId.push($(this).val());
	});
	var param = "&unitId="+unitId+"&classicId="+classicId+"&majorId="+majorId+"&gradeId="+gradeId
	+"&classesId="+classesId+"&matriculateNoticeNo="+matriculateNoticeNo+"&name="+name+"&rollCard="+rollCard
	+"&certNum="+certNum+"&stuStatus="+stuStatus+"&entranceFlag="+entranceFlag+"&fromStudentRoll=1&flag=print&id="+studentId.join(',');
	var url = "${baseUrl}/edu3/register/studentinfo/calulatePrintNumForStudentCard.html";
	$.ajax({
		type:'POST',
		url:url,
		data:{id:studentId.join(','),unitId:unitId,classicId:classicId,majorId:majorId,gradeId:gradeId,classesId:classesId,matriculateNoticeNo:matriculateNoticeNo,name:name,rollCard:rollCard,certNum:certNum,stuStatus:stuStatus,entranceFlagL:entranceFlag,fromStudentRoll:1},
		dataType:"json",
		cache: false,
		error: DWZ.ajaxError,
		success: function(data){
			if(data['over500']=='1'){
				alertMsg.warn(data['question']+" 打印数目超过500，暂不支持此数量级打印。");
				return false;
			}else{
				alertMsg.confirm(data['question'],{
				    okCall:function(){
				    	$.pdialog.open(baseUrl+"/edu3/register/studentinfo/studentCard/print-view.html?studentId="+studentId.join(',')+param,'RES_SCHOOL_SCHOOLROLL_MANAGER_PRINTSTUDENTCARD','打印预览',{height:600, width:800});
				    }
			    });
			}
		}
	});
}

function backToRegister(){
	pageBarHandle("您确定要驳回这些已经注册了的学生吗？","${baseUrl}/edu3/register/studentinfo/back-to-registering.html","#classteacherInfoBody");
}

function exportDivideClass(flag){
	var studentId = [];
	//条件 查询
	var branchSchool 		= $("#stuInfo #eiinfo_brSchoolName").val();
	var classic        	= $("#stuInfo #classic").val();
	var major 	= $("#stuInfo #classteacher_major").val();
	var stuStatus 	= $("#stuInfo #stuStatus").val();
	
	var stuGrade  	= $("#stuInfo #stuGrade").val();
	var name  		= $("#stuInfo #name").val();
	var rollCard 	= $("#stuInfo #rollCard").val();
	var certNum  	= $("#stuInfo #certNum").val();
	
	var matriculateNoticeNo = $("#stuInfo #matriculateNoticeNo").val();
	var classesid  	= $("#stuInfo #classteacher_classesid").val();
	var entranceFlag  = $("#stuInfo #entranceFlag").val();
	
	
	$("#classteacherInfoBody input[@name='resourceid']:checked").each(function(){
		studentId.push($(this).val());
	});
	
	
	alertMsg.confirm("您确定要根据勾选或查询条件（联系地址不纳入导出条件范围）导出"+("W"==flag?"未":"")+"分班吗?", {
		okCall: function(){	
			var param = "branchSchool="+branchSchool+"&classic="+classic+"&major="+major+"&stuGrade="+stuGrade
			+"&classesid="+classesid+"&matriculateNoticeNo="+matriculateNoticeNo+"&name="+encodeURIComponent(encodeURIComponent(name))+"&rollCard="+rollCard
			+"&certNum="+certNum+"&stuStatus="+stuStatus+"&entranceFlag="+entranceFlag+"&fromStudentRoll=1&flag="+flag+"&studentIds="+studentId.join(',');
			//alert(param);return false;
			$('#frame_exportXlsStudentDivideclass').remove();
			var iframe = document.createElement("iframe");
			iframe.id = "frame_exportXlsStudentDivideclass";
			iframe.src = "${baseUrl }/edu3/register/studentinfo/exportXlsDivideClass.html?"+param;
			iframe.style.display = "none";
			//创建完成之后，添加到body中
			document.body.appendChild(iframe);
		}
	});	
}

//导入分班信息xls
function imputDivideClass(){
	var url ="${baseUrl}/edu3/register/studentinfo/impDivideXlsView.html";
	$.pdialog.open(url,"RES_SCHOOL_SCHOOLROLL_MANAGER_UPLOAD","导入分班信息", {mask:true,width:450, height:210});
}


//导出班级花名册信息xls
function exportClassesRoster(){
	var studentId = [];
	//条件 查询
	var branchSchool 		= $("#stuInfo #eiinfo_brSchoolName").val();
	var classic        	= $("#stuInfo #classic").val();
	var major 	= $("#stuInfo #classteacher_major").val();
	var grade  	= $("#stuInfo #stuGrade").val();
	var classes  	= $("#stuInfo #classteacher_classesid").val();
	
	var branchSchool_txt 		= $("#stuInfo #eiinfo_brSchoolName_flexselect").val();
	var classic_txt        	= $("#stuInfo select[id='classic'] option[value='"+classic+"']").text();
	var major_txt 	        = $("#stuInfo #classteacher_major_flexselect").val();
	var grade_txt  	= $("#stuInfo select[id='stuGrade'] option[value='"+grade+"']").text();
	var classes_txt = $("#stuInfo #classteacher_classesid_flexselect").val();
	
	if(classic==""||major==""||grade==""||classes==""){
		alertMsg.warn("请选择年级、层次、专业、班级后再导出！");
		return false;
	}
	
	alertMsg.confirm("导出花名册只根据年级、层次、专业、班级导出！您确定要导出花名册吗?", {
		okCall: function(){	
			
			var param = "branchSchool="+branchSchool+"&classic="+classic+"&major="+major+"&grade="+grade+"&classes="+
			             classes+"&branchSchool_txt="+encodeURIComponent(encodeURIComponent(branchSchool_txt))+
			             "&classic_txt="+encodeURIComponent(encodeURIComponent(classic_txt))+"&major_txt="+
			             encodeURIComponent(encodeURIComponent(major_txt))+
			             "&grade_txt="+encodeURIComponent(encodeURIComponent(grade_txt))+
			             "&classes_txt="+encodeURIComponent(encodeURIComponent(classes_txt));
			
			var url = "${baseUrl}/edu3/register/studentinfo/exportClassesRosterDiy.html?"+param;
			$.pdialog.open(url,'RES_SCHOOL_GRADUATESTU_CUSTOMEXPORT','花名册自定义导出信息',{width:500,height:280});
		}
	});
}

</script>
</head>
<body>

	<div class="page">
		<div class="pageHeader">
			<form onsubmit="return navTabSearch(this);"
				action="${baseUrl }/edu3/register/studentinfo/classteacher-list.html"
				method="post">
				<input type="hidden" id="isFromPage" name="isFromPage" value="1" />
				<input type="hidden" id="isBrschool" name="isBrschool"
					value="${isBrschool }" />
				<div id="stuInfo" class="searchBar">
					<ul class="searchContent">
						<c:if test="${!isBrschool }">
							<li><label>教学站：</label> <span sel-id="eiinfo_brSchoolName"
								sel-name="branchSchool" sel-onchange="classTeacherQueryUnit()"
								sel-classs="flexselect" sel-style="width: 120px"></span></li>
						</c:if>
						<c:if test="${isBrschool}">
							<input type="hidden" name="branchSchool" id="eiinfo_brSchoolName"
								value="${condition['branchSchool']}" />
						</c:if>
						<li><label>年级：</label> <span sel-id="stuGrade"
							sel-name="stuGrade" sel-onchange="classTeacherQueryGrade()"
							sel-style="width: 120px"></span></li>
						<li><label>层次：</label> <span sel-id="classic"
							sel-name="classic" sel-onchange="classTeacherQueryClassic()"
							sel-style="width: 120px"></span></li>
						<li><label>专业：</label> <span sel-id="classteacher_major"
							sel-name="major" sel-onchange="classTeacherQueryMajor()"
							sel-classs="flexselect" sel-style="width: 120px"></span></li>
						<li><label>学籍状态：</label> <select name="stuStatus"
							id="stuStatus" style="width: 120px">
						</select> <!--<gh:select name="stuStatus" id="stuStatus" value="${condition['stuStatus']}" dictionaryCode="CodeStudentStatus" style="width:125px" />-->
						</li>
					</ul>
					<ul class="searchContent">
						<li><label>邮政编号：</label><input type="text"
							name="contactZipcode" id="contactZipcode"
							value="${condition['contactZipcode']}" style="width: 120px" /></li>

						<li><label>姓名：</label><input type="text" name="name"
							id="name" value="${condition['name']}" style="width: 115px" /></li>
						<li><label>学籍卡状态：</label>
						<gh:select id="rollCard" name="rollCard"
								dictionaryCode="CodeRollCardStatus"
								value="${condition['rollCard']}" choose="Y" style="width: 120px" />
						</li>
						<li><label>身份证号：</label><input type="text" name="certNum"
							id="certNum" value="${condition['certNum']}" style="width: 120px" />
						</li>

					</ul>
					<ul class="searchContent">

						<li><label>学号：</label><input type="text"
							name="matriculateNoticeNo" id="matriculateNoticeNo"
							value="${condition['matriculateNoticeNo']}" style="width: 115px" />
						</li>
						<li><label>班级：</label> <span sel-id="classteacher_classesid"
							sel-name="classesid" sel-classs="flexselect"
							sel-style="width: 120px"></span> <%--<gh:selectModel id="stuClasses" name="classesid" bindValue="resourceid" displayValue="classname" condition="brSchool.resourceid='${condition['branchSchool']}'"
						  	modelClass="com.hnjk.edu.roll.model.Classes" value="${condition['classesid']}" orderBy="grade.gradeName desc,brSchool.unitCode,major.majorCode,classic,classname,resourceid" style="width: 120px" /> --%>
						</li>
						<li><label>入学资格：</label>
						<gh:select id="entranceFlag" name="entranceFlag"
								dictionaryCode="CodeAuditStatus"
								value="${condition['entranceFlag']}" choose="Y"
								style="width: 120px" /></li>
						<li><label>联系地址：</label> <input type="text"
							name="contactAddress" id="contactAddress"
							value="${condition['contactAddress']}" style="width: 120px" /></li>
						<%-- 
				<li>
						<label>审核结果：</label><gh:select name="entranceFlag" dictionaryCode="CodeAuditStatus"  value="${condition['entranceFlag']}" choose="Y" style="width: 120px"/> 
				</li> --%>

					</ul>
					<ul>
						<div class="buttonActive" style="float: right">
							<div class="buttonContent">
								<button type="submit">查 询</button>
							</div>
						</div>
						<div style="float: right">
							<span class="tips">提示：本功能仅限于班主任使用</span>
						</div>
					</ul>
				</div>
			</form>
		</div>
		<div class="pageContent">
			<gh:resAuth parentCode="RES_CLASSESMASTER_LIST" pageType="list"></gh:resAuth>
			<table class="table" layouth="187">
				<thead>
					<tr>
						<th width="3%"><input type="checkbox" name="checkall"
							id="check_all_info"
							onclick="checkboxAll('#check_all_info','resourceid','#classteacherInfoBody')" /></th>
						<th width="4%">姓名</th>
						<th width="8%">学号</th>
						<th width="3%">性别</th>
						<th width="10%">身份证</th>
						<th width="3%">民族</th>
						<th width="4.7%">年级</th>
						<th width="5.8%">联系地址</th>
						<th width="4%">邮编</th>
						<th width="5%">培养层次</th>
						<th width="7.5%">专业</th>
						<th width="6%">办学单位</th>
						<th width="5%">学籍状态</th>
						<th width="3%">账号</th>
						<th width="8%">班级</th>
						<th width="6%">入学日期</th>
						<th width="5.5%">入学资格</th>
						<th width="4%">学籍卡</th>
						<th width="5%">学习形式</th>
						<th width="5%">学籍信息审核结果</th>
					</tr>
				</thead>
				<tbody id="classteacherInfoBody">
					<c:forEach items="${stulist.result}" var="stu" varStatus="vs">
						<tr>
							<td><input type="checkbox" name="resourceid"
								value="${stu.resourceid }" title="${stu.sysUser.username}"
								accept="${stu.studentBaseInfo.name }" alt="${stu.studentStatus}"
								rel="${stu.branchSchool.resourceid}|${stu.grade.resourceid}|${stu.major.resourceid}|${stu.classic.resourceid}|${stu.teachingType}"
								autocomplete="off" /></td>
							<td><a href="#" onclick="viewStuInfo2('${stu.resourceid}')"
								title="点击查看">${stu.studentBaseInfo.name }</a></td>
							<td name="stuNo">${stu.studyNo}</td>
							<td>${ghfn:dictCode2Val('CodeSex',stu.studentBaseInfo.gender) }</td>
							<td>${stu.studentBaseInfo.certNum }</td>
							<td>${ghfn:dictCode2Val('CodeNation',stu.studentBaseInfo.nation) }</td>
							<td>${stu.grade.gradeName}</td>
							<td>${stu.studentBaseInfo.contactAddress}</td>
							<td>${stu.studentBaseInfo.contactZipcode}</td>
							<td>${stu.classic.classicName }</td>
							<td>${stu.major.majorName }</td>
							<td>${stu.branchSchool}</td>
							<td>
								<!-- ${ghfn:dictCode2Val('CodeStudentStatus',stu.studentStatus) } -->
								<c:choose>
									<c:when
										test="${stu.studentStatus == '11' and stu.accountStatus==1}">正常注册</c:when>
									<c:when
										test="${stu.studentStatus == '11' and stu.accountStatus==0}">正常未注册</c:when>
									<c:otherwise>${ghfn:dictCode2Val('CodeStudentStatus',stu.studentStatus)}</c:otherwise>
								</c:choose>

							</td>
							<td>${stu.accountStatus==1?"激活":"<font color='red'>停用</font>"}
							</td>
							<td title="${stu.classes.classname }">
								${stu.classes.classname }</td>
							<td><fmt:formatDate value="${stu.inDate }"
									pattern="yyyy-MM-dd" /></td>
							<td><c:choose>
									<c:when test="${stu.enterAuditStatus=='N'}">
										<font color="red"><b>不通过</b></font>
									</c:when>
									<c:when test="${stu.enterAuditStatus=='Y'}">通过</c:when>
									<c:otherwise>待审核</c:otherwise>
								</c:choose></td>
							<td><c:choose>
									<c:when test="${stu.rollCardStatus eq '2' }">
										<font color="green">已提交</font>
									</c:when>
									<c:when test="${stu.rollCardStatus eq '1' }">
										<font color="blue">已保存</font>
									</c:when>
									<c:otherwise>
										<font color="red">未保存</font>
									</c:otherwise>
								</c:choose></td>
							<td>
								${ghfn:dictCode2Val('CodeTeachingType',stu.teachingType)}</td>

							<td><c:choose>
									<c:when test="${stu.auditResults == '1'}">
										<font color="green">通过</font>
									</c:when>
									<c:when test="${stu.auditResults == '2'}">
										<font color="red">不通过</font>
									</c:when>
									<c:when test="${stu.auditResults == '3'}">
										<font color="#3A5FCD">未提交</font>
									</c:when>
									<c:when test="${stu.auditResults == '0'}">待审核</c:when>
									<c:otherwise>${ghfn:dictCode2Val('CodeAuditResults',stu.auditResults)}</c:otherwise>
								</c:choose></td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
			<gh:page page="${stulist}"
				goPageUrl="${baseUrl }/edu3/register/studentinfo/classteacher-list.html"
				pageType="sys" condition="${condition}" />
		</div>
	</div>
</body>
<script type="text/javascript">
	function viewStuInfo2(id){
		var url = "${baseUrl}/edu3/framework/studentinfo/view.html";
		//navTab.openTab('_blank', url+'?resourceid='+id, '修改学籍');
		$.pdialog.open(url+'?resourceid='+id, 'RES_SCHOOL_SCHOOLROLL_MANAGER_VIEW', '查看学籍', {width: 800, height: 600});
	}
	
	function modifyStuInfo(){
		var url = "${baseUrl}/edu3/register/studentinfo/editstu.html";
		if(isCheckOnlyone('resourceid','#classteacherInfoBody')){
			navTab.openTab('RES_SCHOOL_SCHOOLROLL_MANAGER_EDIT', url+'?resourceid='+$("#classteacherInfoBody input[@name='resourceid']:checked").val(), '信息修改');
		}			
	}
	/*
	//设置学生修改学籍开放时间 
	function changeStudentInfoTime(){
		var url = "${baseUrl}/edu3/register/studentinfo/changeStudentInfoTime.html";
		if(isCheckOnlyone('resourceid','#classteacherInfoBody')){
			navTab.openTab('RES_SCHOOL_SCHOOLROLL_MANAGER_SET', url+'?resourceid='+$("#classteacherInfoBody input[@name='resourceid']:checked").val(), '设置修改学籍开放时间');
		}			
	}
	*/
	function disenabledStudentAccount(){//停用账号			
			pageBarHandle("您确定要停用这些学生账号吗？","${baseUrl}/edu3/register/studentinfo/enableaccount.html?type=disenable","#classteacherInfoBody");		
		
	}
	
	function enabledStudentAccount(){//启用账号		
		pageBarHandle("您确定要启用这些学生账号吗？","${baseUrl}/edu3/register/studentinfo/enableaccount.html?type=enable","#classteacherInfoBody");			
	}
	function resetStudentAccountPassword(){//重置密码	
		pageBarHandle("您确定要重置这些学生账号的密码吗？","${baseUrl}/edu3/register/studentinfo/resetpassword.html","#classteacherInfoBody");			
	}
	
	function switchStudentInfo(){//切换学生账号		
		if(isCheckOnlyone('resourceid','#classteacherInfoBody')){
			var obj = $("#classteacherInfoBody input[@name='resourceid']:checked");
			var username = obj.attr("title");
			switchSecurityTargetUser(username);
		}
		
	}
	
	
	function backStudentCardInfo(){//	退回已提交学籍卡
		if(isCheckOnlyone('resourceid','#classteacherInfoBody')){
			var obj = $("#classteacherInfoBody input[@name='resourceid']:checked");
			var studentid = obj.val();
			var username = obj.attr("accept");
			pageBarHandle("您确定要退回"+username+"的学籍信息吗？","${baseUrl}/edu3/register/studentinfo/studentcardinfoback.html?studentid="+studentid,"#classteacherInfoBody");
		}
		
	}
	
</script>
</html>