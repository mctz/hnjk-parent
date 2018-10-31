<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>学籍库管理</title>
<script type="text/javascript">
$(document).ready(function(){
	var studentStatusSet= '${stuStatusSet}';
	var statusRes= '${stuStatusRes}';
	orgStuStatus("#studentInfo #stuStatus",studentStatusSet,statusRes,"12,13,15,18,21,19,23,a11,b11,16,24,25");

	schoolinfoQueryBegin();
});

//打开页面或者点击查询（即加载页面执行）
function schoolinfoQueryBegin() {
	var defaultValue = "${condition['branchSchool']}";
	var isBrschool = "${isBrschool}";
	var schoolId = "";
	if(isBrschool==true || isBrschool=="true"){
		schoolId = defaultValue;
	}
	var gradeId = "${condition['stuGrade']}";
	var classicId = "${condition['classic']}";

	var majorId = "${condition['major']}";
	var classesId = "${condition['classesid']}";
	var selectIdsJson = "{unitId:'eiinfo_brSchoolName',gradeId:'stuGrade',classicId:'classic',majorId:'majorId',classesId:'classesId'}";
	cascadeQuery("begin", defaultValue, schoolId, gradeId, classicId,"", majorId, classesId, selectIdsJson);
}

// 选择教学点
function schoolinfoQueryUnit() {
	var defaultValue = $("#eiinfo_brSchoolName").val();
	var selectIdsJson = "{gradeId:'stuGrade',classicId:'classic',majorId:'majorId',classesId:'classesId'}";
	cascadeQuery("unit", defaultValue, "", "", "", "", "", "",selectIdsJson);
}

// 选择年级
function schoolinfoQueryGrade() {
	var defaultValue = $("#eiinfo_brSchoolName").val();
	var gradeId = $("#stuGrade").val();
	var selectIdsJson = "{classicId:'classic',majorId:'majorId',classesId:'classesId'}";
	cascadeQuery("grade", defaultValue, "", gradeId, "", "", "", "",selectIdsJson);
}

// 选择层次
function schoolinfoQueryClassic() {
	var defaultValue = $("#eiinfo_brSchoolName").val();
	var gradeId = $("#stuGrade").val();
	var classicId = $("#classic").val();
	var selectIdsJson = "{majorId:'majorId',classesId:'classesId'}";
	cascadeQuery("classic", defaultValue, "", gradeId, classicId, "", "", "", selectIdsJson);
}

//选择专业
function schoolinfoQueryMajor() {
	var defaultValue = $("#eiinfo_brSchoolName").val();
	var gradeId = $("#stuGrade").val();
	var classicId = $("#classic").val();
	var teachingTypeId = "";
	var majorId = $("#majorId").val();
	var selectIdsJson = "{classesId:'classesId'}";
	cascadeQuery("major", defaultValue, "", gradeId, classicId,teachingTypeId, majorId, "", selectIdsJson);
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

//打印考生信息
function printExameeInfoInRollPage(){
	var studentId = [];
	//条件 查询
	var unitId 		= $("#studentInfo #eiinfo_brSchoolName").val();
	var classicId 	= $("#studentInfo #classic").val();
	var majorId 	= $("#studentInfo #majorId").val();
	var gradeId  	= $("#studentInfo #stuGrade").val();
	var classesId 	= $("#studentInfo #classesId").val();
	var matriculateNoticeNo = $("#studentInfo #matriculateNoticeNo").val();
	var name  		= $("#studentInfo #name").val();
	var rollCard 	= $("#studentInfo #rollCard").val();
	var certNum  	= $("#studentInfo #certNum").val();
	var stuStatus 	= $("#studentInfo #stuStatus").val();
	var entranceFlag  = $("#studentInfo #entranceFlag").val();
	var param = "&unitId="+unitId+"&classicId="+classicId+"&majorId="+majorId+"&gradeId="+gradeId
	+"&classesId="+classesId+"&matriculateNoticeNo="+matriculateNoticeNo+"&name="+name+"&rollCard="+rollCard
	+"&certNum="+certNum+"&stuStatus="+stuStatus+"&entranceFlag="+entranceFlag+"&from='schoolinfo'";
	$("#infoBody input[name='resourceid']:checked").each(function(){
		studentId.push($(this).val());
	});
	
	var url = "${baseUrl}/edu3/register/studentinfo/exameeinfo/calulatePrintNum.html";
	$.ajax({
		type:'POST',
		url:url,
		data:{id:studentId.join(','),unitId:unitId,classicId:classicId,majorId:majorId,gradeId:gradeId,classesId:classesId,matriculateNoticeNo:matriculateNoticeNo,name:name,rollCard:rollCard,certNum:certNum,stuStatus:stuStatus,entranceFlagL:entranceFlag,from:'schoolinfo'},
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

//学籍卡打印 双面
function studentRollCardPrintInRollTwosided(type){//1正面2反面
	
	var studentId = [];
	//条件 查询
	var unitId 		= $("#studentInfo #eiinfo_brSchoolName").val();
	var classicId 	= $("#studentInfo #classic").val();
	var majorId 	= $("#studentInfo #majorId").val();
	var gradeId  	= $("#studentInfo #stuGrade").val();
	var classesId 	= $("#studentInfo #classesId").val();
	var matriculateNoticeNo = $("#studentInfo #matriculateNoticeNo").val();
	var name  		= $("#studentInfo #name").val();
	var rollCard 	= $("#studentInfo #rollCard").val();
	var certNum  	= $("#studentInfo #certNum").val();
	var stuStatus 	= $("#studentInfo #stuStatus").val();
	var entranceFlag  = $("#studentInfo #entranceFlag").val();
	$("#infoBody input[name='resourceid']:checked").each(function(){
		studentId.push($(this).val());
	});
	var param = "?unitId="+unitId+"&classicId="+classicId+"&majorId="+majorId+"&gradeId="+gradeId+"&type="+type
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
				    	$.pdialog.open(baseUrl+"/edu3/register/studentinfo/studentCardTwosided/print-view.html"+param,'RES_SCHOOL_SCHOOLROLL_MANAGER_PRINTSTUDENTCARDTWOSIDED','打印预览',{height:600, width:800});
				    }
			    });
			}
		}
	});
}

//导出双面学籍卡PDF
function studentRollCardPDFDownload(){
	
	var studentId = [];
	//条件 查询
	var unitId 		= $("#studentInfo #eiinfo_brSchoolName").val();
	var classicId 	= $("#studentInfo #classic").val();
	var majorId 	= $("#studentInfo #majorId").val();
	var gradeId  	= $("#studentInfo #stuGrade").val();
	var classesId 	= $("#studentInfo #classesId").val();
	var matriculateNoticeNo = $("#studentInfo #matriculateNoticeNo").val();
	var name  		= $("#studentInfo #name").val();
	var rollCard 	= $("#studentInfo #rollCard").val();
	var certNum  	= $("#studentInfo #certNum").val();
	var stuStatus 	= $("#studentInfo #stuStatus").val();
	var entranceFlag  = $("#studentInfo #entranceFlag").val();
	$("#infoBody input[name='resourceid']:checked").each(function(){
		studentId.push($(this).val());
	});
	
	var param = "?unitId="+unitId+"&classicId="+classicId+"&majorId="+majorId+"&gradeId="+gradeId
			+"&classesId="+classesId+"&matriculateNoticeNo="+matriculateNoticeNo+"&name="+name+"&rollCard="+rollCard
	+"&certNum="+certNum+"&stuStatus="+stuStatus+"&entranceFlag="+entranceFlag+"&fromStudentRoll=1&flag=print&studentids="+studentId.join(',');

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
				alertMsg.warn("下载学籍卡PDF:"+data['question'].split(":")[1]+" 当前查询数据超过500条，请勾选或细化查询条件再进行下载！");
				return false;
			}else{
				alertMsg.confirm("下载学籍卡PDF:"+data['question'].split(':')[1],{
				    okCall:function(){
				    	url = "${baseUrl}/edu3/roll/studentCardTwosided/downloadPDF.html"+param;
				    	downloadFileByIframe(url,'stucentCard_pdf_downloadIframe');
				    }
			    });
			}
		}
	});
}

//导出双面学籍卡PDF(安徽医)
function studentRollCardPDFDownload1(){
	
	var studentId = [];
	//条件 查询
	var unitId 		= $("#studentInfo #eiinfo_brSchoolName").val();
	var classicId 	= $("#studentInfo #classic").val();
	var majorId 	= $("#studentInfo #majorId").val();
	var gradeId  	= $("#studentInfo #stuGrade").val();
	var classesId 	= $("#studentInfo #classesId").val();
	var matriculateNoticeNo = $("#studentInfo #matriculateNoticeNo").val();
	var name  		= $("#studentInfo #name").val();
	var rollCard 	= $("#studentInfo #rollCard").val();
	var certNum  	= $("#studentInfo #certNum").val();
	var stuStatus 	= $("#studentInfo #stuStatus").val();
	var entranceFlag  = $("#studentInfo #entranceFlag").val();
	$("#infoBody input[name='resourceid']:checked").each(function(){
		studentId.push($(this).val());
	});
	
	var param = "?unitId="+unitId+"&classicId="+classicId+"&majorId="+majorId+"&gradeId="+gradeId
			+"&classesId="+classesId+"&matriculateNoticeNo="+matriculateNoticeNo+"&name="+name+"&rollCard="+rollCard
	+"&certNum="+certNum+"&stuStatus="+stuStatus+"&entranceFlag="+entranceFlag+"&fromStudentRoll=1&flag=print&studentids="+studentId.join(',');

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
				alertMsg.warn("下载学籍卡PDF:"+data['question'].split(":")[1]+" 当前查询数据超过500条，请勾选或细化查询条件再进行下载！");
				return false;
			}else{
				alertMsg.confirm("下载学籍卡PDF:"+data['question'].split(':')[1],{
				    okCall:function(){
				    	url = "${baseUrl}/edu3/roll/studentCardTwosided/downloadPDF1.html"+param;
				    	downloadFileByIframe(url,'stucentCard_pdf_downloadIframe');
				    }
			    });
			}
		}
	});
}
//下载毕业实习材料模版
function downloadGraduationPracticeMaterialsModel() {
    window.location.href="${baseUrl }/edu3/roll/sturegister/graduationPracticeMaterials/download.html"
}
//导入状态
function importGraduationPracticeMaterialsModel() {
    $.pdialog.open(baseUrl+"/edu3/roll/sturegister/graduationPracticeMaterials/import.html", '导入毕业实习材料提交状态', {width: 600, height: 400});
}
</script>
</head>
<body>

	<div class="page">
		<div class="pageHeader" style="height: 100px;">
			<form onsubmit="return navTabSearch(this);"
				action="${baseUrl }/edu3/register/studentinfo/schoolinfo-list.html"
				method="post">
				<input type="hidden" id="isFromPage" name="isFromPage" value="1" />
				<input type="hidden" id="isBrschool" name="isBrschool"
					value="${isBrschool }" />
				<div id="studentInfo" class="searchBar">
					<ul class="searchContent">

						<li class="custom-li"><label>教学站：</label> <span sel-id="eiinfo_brSchoolName"
							sel-name="branchSchool" sel-onchange="schoolinfoQueryUnit()"
							sel-classs="flexselect" ></span></li>

						<li><label>年级：</label> <span sel-id="stuGrade"
							sel-name="stuGrade" sel-onchange="schoolinfoQueryGrade()"
							sel-classs="flexselect" sel-style="width: 120px"></span></li>

						<li><label>层次：</label> <span sel-id="classic"
							sel-name="classic" sel-onchange="schoolinfoQueryClassic()"
							sel-style="width: 120px"></span></li>

					</ul>
					<ul class="searchContent">
						<li class="custom-li"><label>专业：</label> <span sel-id="majorId"
							sel-name="major" sel-onchange="schoolinfoQueryMajor()"
							sel-classs="flexselect" ></span></li>
						
						<li><label>姓名：</label><input type="text" name="name"
							id="name" value="${condition['name']}" style="width: 120px" /></li>
						<li><label>学号：</label><input type="text"
							name="matriculateNoticeNo" id="matriculateNoticeNo"
							value="${condition['matriculateNoticeNo']}" style="width: 120px" />
						</li>
						
						<li><label>身份证号：</label><input type="text" name="certNum"
							id="certNum" value="${condition['certNum']}" style="width: 120px" />
						</li>

					</ul>
					<ul class="searchContent">

						<li class="custom-li"><label>班级：</label> <span sel-id="classesId"
							sel-name="classesid" sel-classs="flexselect"></span></li>
						<li><label>学籍状态：</label> <select name="stuStatus"
							id="stuStatus" style="width: 120px">
						</select> <%--<gh:select name="stuStatus" id="stuStatus" value="${condition['stuStatus']}" dictionaryCode="CodeStudentStatus" style="width:125px" /> --%>
						</li>
						<li><label>学籍卡状态：</label>
						<gh:select id="rollCard" name="rollCard"
								dictionaryCode="CodeRollCardStatus"
								value="${condition['rollCard']}" choose="Y" style="width: 120px" />
						</li>
						<li><label>入学资格：</label> <gh:select id="entranceFlag"
								name="entranceFlag" dictionaryCode="CodeAuditStatus"
								value="${condition['entranceFlag']}" choose="Y"
								style="width: 120px" /></li>
					</ul>
					<ul class="searchContent">
						<li class="custom-li"><label>联系地址：</label> <input type="text"
							name="contactAddress" id="contactAddress"
							value="${condition['contactAddress']}" class="custom-inp" /></li>
						<%-- 
				<li>
						<label>审核结果：</label><gh:select name="entranceFlag" dictionaryCode="CodeAuditStatus"  value="${condition['entranceFlag']}" choose="Y" style="width: 120px"/> 
				</li> --%>
						<li><label>是否有相片</label> <gh:select id="havePhoto"
								name="havePhoto" dictionaryCode="yesOrNo"
								value="${condition['havePhoto']}" choose="Y"
								style="width: 120px" /> <!-- 				<select name="havePhoto" style="width:55%"> -->
							<!-- 				<option value = "">请选择</option> --> <!-- 				<option value = "Y">是</option> -->
							<!-- 				<option value = "N">否</option> --> <!-- 				</select> -->
						</li>
						<%--<li><label>入学日期：</label>
							<input type="text" id="inDate" name="inDate"
								style="text-align: center;" class="date1"
								onFocus="WdatePicker({isShowWeek:true})" />
						</li>--%>
						<div class="buttonActive">
							<div class="buttonContent">
								<button type="submit">查 询</button>
							</div>
						</div>
					</ul>
				</div>
			</form>
		</div>
		<div class="pageContent">
			<gh:resAuth parentCode="RES_SCHOOL_SCHOOLINFO_MANAGER" pageType="list"></gh:resAuth>
			<table class="table" layouth="190">
				<thead>
					<tr>
						<th width="3%"><input type="checkbox" name="checkall"
							id="check_all_info"
							onclick="checkboxAll('#check_all_info','resourceid','#infoBody')" /></th>
						<th width="3.5%">姓名</th>
						<th width="5.5%">学号</th>
						<th width="2%">性别</th>
						<th width="10.5%">身份证</th>
						<th width="3%">民族</th>
						<th width="4.7%">年级</th>
						<th width="7.8%">联系地址</th>
						<th width="4%">培养层次</th>
						<th width="7.5%">专业</th>
						<th width="6%">办学单位</th>
						<th width="4%">学籍状态</th>
						<th width="3%">账号</th>
						<th width="9%">班级</th>
						<th width="4%">入学日期</th>
						<th width="3.5%">入学资格</th>
						<th width="4%">学籍卡</th>
						<th width="3%">学习形式</th>
						<th width="5.5%">准考证号</th>
						<th width="3.5%">入学总分</th>
						<th width="4%">是否有相片</th>
					</tr>
				</thead>
				<tbody id="infoBody">
					<c:forEach items="${stulist.result}" var="stu" varStatus="vs">
						<tr>
							<td><input type="checkbox" name="resourceid"
								value="${stu.resourceid }" title="${stu.sysUser.username}"
								alt="${stu.studentStatus}"
								rel="${stu.branchSchool.resourceid}|${stu.grade.resourceid}|${stu.major.resourceid}|${stu.classic.resourceid}|${stu.teachingType}"
								autocomplete="off" accept="${stu.studentBaseInfo.name }" /></td>
							<td><a href="#" onclick="viewStuInfo2('${stu.resourceid}')"
								title="点击查看">${stu.studentBaseInfo.name }</a></td>
							<td>${stu.studyNo}</td>
							<td>${ghfn:dictCode2Val('CodeSex',stu.studentBaseInfo.gender) }</td>
							<td>${stu.studentBaseInfo.certNum }</td>
							<td>${ghfn:dictCode2Val('CodeNation',stu.studentBaseInfo.nation) }</td>
							<td>${stu.grade.gradeName}</td>
							<td>${stu.studentBaseInfo.contactAddress}</td>
							<td>${stu.classic.classicName }</td>
							<td>${stu.major.majorName }</td>
							<td>${stu.branchSchool}</td>
							<td <c:if test="${stu.studentStatus == '11'}">style='color: green'</c:if>
								<c:if test="${stu.studentStatus == '13'}">style='color: red'</c:if>
								><c:choose>
									<c:when
										test="${stu.studentStatus == '11' and stu.accountStatus==1}">正常注册</c:when>
									<c:when
										test="${stu.studentStatus == '11' and stu.accountStatus==0}">正常未注册</c:when>
									<c:otherwise>${ghfn:dictCode2Val('CodeStudentStatus',stu.studentStatus)}</c:otherwise>
								</c:choose>
							</td>
							<td <c:if test="${stu.accountStatus ne 1}">style='color: red'</c:if>
								>${stu.accountStatus==1?"激活":"停用"}
							</td>
							<td title="${stu.classes.classname }">
								${stu.classes.classname }</td>
							<td><fmt:formatDate value="${stu.inDate }"
									pattern="yyyy-MM-dd" /></td>
							<td <c:if test="${stu.enterAuditStatus=='N'}">style='color:red'</c:if>
								<c:if test="${stu.enterAuditStatus=='Y'}">style='color:blue'</c:if>
								><c:choose>
									<c:when test="${stu.enterAuditStatus=='N'}">
										不通过
									</c:when>
									<c:when test="${stu.enterAuditStatus=='Y'}">通过</c:when>
									<c:otherwise>待审核</c:otherwise>
								</c:choose></td>
							<td <c:if test="${stu.rollCardStatus eq '2'}">style='color: blue'</c:if>
								<c:if test="${stu.rollCardStatus eq '1'}">style='color: green'</c:if>
								<c:if test="${stu.rollCardStatus eq '0'}">style='color: red'</c:if>
								><c:choose>
									<c:when test="${stu.rollCardStatus eq '2'}">
										已提交
									</c:when>
									<c:when test="${stu.rollCardStatus eq '1'}">
										已保存
									</c:when>
									<c:otherwise>
										未保存
									</c:otherwise>
								</c:choose></td>
							<td>
								${ghfn:dictCode2Val('CodeTeachingType',stu.teachingType)} <input
								type="hidden" value="${stu.auditResults}" id="aduitImgStatus">
								<input type="hidden" value="${stu.studentBaseInfo.name}"
								id="aduitImgUserName">

							</td>
							<td>${stu.examCertificateNo }</td>
							<td>${stu.totalPoint }</td>
							<td><c:choose>
									<c:when test="${not empty stu.studentBaseInfo.photoPath}">
										<font color="green">有</font>
									</c:when>
									<c:otherwise>无</c:otherwise>
								</c:choose></td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
		</div>
		<div  class="pageContent" style="position: absolute;bottom: 0px;width: 100%">
			<gh:page page="${stulist}"
				goPageUrl="${baseUrl }/edu3/register/studentinfo/schoolinfo-list.html"
				pageType="sys" condition="${condition}" /></div>
	</div>
</body>
<script type="text/javascript">
	function viewStuInfo2(id){
		var url = "${baseUrl}/edu3/framework/studentinfo/view.html";
		//navTab.openTab('_blank', url+'?resourceid='+id, '修改学籍');
		$.pdialog.open(url+'?resourceid='+id, 'RES_SCHOOL_SCHOOLROLL_MANAGER_VIEW', '查看学籍', {width: 800, height: 600});
	}

	function updateEnrollDate() {
        var studentId = [];
        $("#infoBody input[name='resourceid']:checked").each(function(){
            studentId.push($(this).val());
        });

        var url = "${baseUrl}/edu3/register/studentinfo/inputEnrollDate.html";
        if (studentId.length > 0) {
            url += "?studentids="+studentId.join(',')
        }
        $.pdialog.open(url, 'RES_SCHOOL_SCHOOLROLL_UPDATEDATE', '修改入学日期', {width: 500, height: 400});
    }
</script>
</html>