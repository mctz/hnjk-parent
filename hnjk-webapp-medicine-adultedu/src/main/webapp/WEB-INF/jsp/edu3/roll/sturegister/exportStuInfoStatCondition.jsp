<%@page contentType="text/html;charset=UTF-8"%>
<%@include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>学籍信息统计</title>
<style type="text/css">
</style>
<script type="text/javascript">	
$(document).ready(function(){
	var studentStatusSet= '${stuStatusSet}';
	var stuStatusRes ='${stuStatusRes}';
	orgStuStatus("#stuStudentStatus #statStudentStatus",studentStatusSet,stuStatusRes,"12,13,15,18,21,a11,b11,25");
});
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
//导出数据
function exportStatStudentExcel(){
	var statStudentStatus = $("#statStudentStatus").val();
	var statStuBranchSchool   = $("#statStuBranchSchool").val();
	var statStuGrade  = $("#statStuGrade").val();
	var statStuClassic    = $("#statStuClassic").val();
	var statStuMajor    = $("#statStuMajor").val();
	var statStuStudyMode = $("#statStuStudyMode").val();
	var stuStatParam    = $("#stuStatParam").val();
	var url = "${baseUrl}/edu3/register/studentinfo/exportStudentInfoStat.html"
		+"?statStudentStatus="+statStudentStatus
		+"&statStuBranchSchool="+statStuBranchSchool
		+"&statStuGrade="+statStuGrade
		+"&statStuClassic="+statStuClassic
		+"&statStuMajor="+statStuMajor
		+"&statStuStudyMode="+statStuStudyMode+"&stuStatParam="+stuStatParam;
	$('#frame_exportStudentInfos').remove();
	var iframe = document.createElement("iframe");
	iframe.id = "frame_exportStudentInfos";
	iframe.src = url;
	iframe.style.display = "none";
	//创建完成之后，添加到body中
	document.body.appendChild(iframe);
}
function pageChange(){
	$("#statStudentInfoCondition").submit();
}
</script>
</head>
<body>

	<h2 class="contentTitle">学籍信息统计</h2>
	<div class="page">
		<div class="pageHeader">
			<form id="statStudentInfoCondition"
				onsubmit="return navTabSearch(this);"
				action="${baseUrl }/edu3/register/studentinfo/studentInfoStat.html"
				method="post">
				<div id="studentInfoAud" class="searchBar">
					<ul class="searchContent">
						<li style="width: 500px"><select id="stuStatParam"
							name="stuStatParam" onchange="pageChange()">
								<option value="0"
									<c:if test="${condition['stuStatParam']==0}">selected="selected"</c:if>>--请选择--</option>
								<option value="10"
									<c:if test="${condition['stuStatParam']==10}">selected="selected"</c:if>>按教学站进行统计</option>
								<option value="11"
									<c:if test="${condition['stuStatParam']==11}">selected="selected"</c:if>>按教学站层次进行统计</option>
								<option value="1"
									<c:if test="${condition['stuStatParam']==1}">selected="selected"</c:if>>按年级层次进行统计</option>
								<option value="2"
									<c:if test="${condition['stuStatParam']==2}">selected="selected"</c:if>>按教学站年级层次专业进行统计</option>
								<option value="9"
									<c:if test="${condition['stuStatParam']==9}">selected="selected"</c:if>>按学籍状态进行统计</option>
								<option value="3"
									<c:if test="${condition['stuStatParam']==3}">selected="selected"</c:if>>按年级专业层次进行统计</option>
								<option value="4"
									<c:if test="${condition['stuStatParam']==4}">selected="selected"</c:if>>按年级学习模式进行统计</option>
								<option value="5"
									<c:if test="${condition['stuStatParam']==5}">selected="selected"</c:if>>按教学站年级进行统计</option>
								<option value="6"
									<c:if test="${condition['stuStatParam']==6}">selected="selected"</c:if>>按在校生按年龄进行统计</option>
								<option value="7"
									<c:if test="${condition['stuStatParam']==7}">selected="selected"</c:if>>按在校生来源情况进行统计</option>
								<option value="8"
									<c:if test="${condition['stuStatParam']==8}">selected="selected"</c:if>>按政治面貌证件名称民族进行统计</option>
								<option value="13"
									<c:if test="${condition['stuStatParam']==13}">selected="selected"</c:if>>外聘老师统计</option>
								<option value="14"
									<c:if test="${condition['stuStatParam']==14}">selected="selected"</c:if>>外聘老师学位统计</option>
						</select></li>
					</ul>
					<c:choose>
						<c:when test="${condition['stuStatParam'] ne 12}">
							<ul class="searchContent" id="stuStudentStatus">
								<li style="width: 300px"><label>学籍状态:</label> <!-- 	<gh:select id="statStudentStatus" name="statStudentStatus"  value="${condition['statStudentStatus']}"
							dictionaryCode="CodeStudentStatus"  style="width:120px"/> --> <select
									id="statStudentStatus" name="statStudentStatus"
									style="width: 120px">

								</select></li>
								<li style="width: 300px"><label>教学站:</label> <gh:brSchoolAutocomplete
										name="statStuBranchSchool" tabindex="1"
										id="statStuBranchSchool"
										defaultValue="${condition['statStuBranchSchool']}"
										displayType="code" style="width:120px" /></li>
								<li style="width: 300px"><label>年级:</label> <gh:selectModel
										id="statStuGrade" name="statStuGrade" bindValue="resourceid"
										displayValue="gradeName" value="${condition['statStuGrade']}"
										modelClass="com.hnjk.edu.basedata.model.Grade"
										orderBy="gradeName desc" style="width:120px" /></li>
							</ul>
							<ul class="searchContent" id="stuClassic">
								<li style="width: 300px"><label>层次:</label> <gh:selectModel
										id="statStuClassic" name="statStuClassic"
										bindValue="resourceid" displayValue="classicName"
										value="${condition['statStuClassic']}"
										modelClass="com.hnjk.edu.basedata.model.Classic"
										style="width:120px" /></li>
								<li style="width: 300px"><label>专业:</label> <gh:selectModel
										id="statStuMajor" name="statStuMajor" bindValue="resourceid"
										displayValue="majorName" value="${condition['statStuMajor']}"
										modelClass="com.hnjk.edu.basedata.model.Major"
										style="width:120px" /></li>
								<li style="width: 300px"><label>学习模式:</label> <gh:select
										id="statStuStudyMode" name="statStuStudyMode"
										value="${condition['statStuStudyMode']}"
										dictionaryCode="CodeTeachingType" style="width:120px" /></li>
							</ul>
						</c:when>
						<c:otherwise>
							<!-- 在校生统计 -->
							<ul class="searchContent" id="r1">
								<li style="width: 300px"><label>毕业年份:</label> <select
									id="graduateYear" name="graduateYear" style="width: 120px">
								</select></li>
								<li style="width: 300px"><label>省名称:</label> <select
									id="province" name="province" style="width: 120px">
								</select></li>
								<li style="width: 300px"><label>学历层次:</label> <select
									id="classic" name="classic" style="width: 120px">
								</select></li>
							</ul>
							<ul class="searchContent" id="r2">
								<li style="width: 300px"><label>性别:</label> <select
									id="gender" name="gender" style="width: 120px">
								</select>
								<li style="width: 300px"><label>毕结业结论:</label> <select
									id="graduateConclusion" name="graduateConclusion"
									style="width: 120px">
								</select></li>
								<li style="width: 300px"><label>学习形式:</label> <select
									id="studyForm" name="studyForm" style="width: 120px">
								</select></li>
							</ul>
							<ul class="searchContent" id="r3">
								<li style="width: 300px"><label>院校名称:</label> <select
									id="schoolName" name="schoolName" style="width: 120px">
								</select></li>
								<li style="width: 300px"><label>专业名称:</label> <gh:selectModel
										id="majorName" name="majorName" bindValue="resourceid"
										displayValue="majorName" value="${condition['majorName']}"
										modelClass="com.hnjk.edu.basedata.model.Major"
										style="width:120px" /></li>
							</ul>
						</c:otherwise>
					</c:choose>
					<div class="subBar">
						<ul>
							<li><div class="buttonActive">
									<div class="buttonContent">
										<button type="submit">查 询</button>
									</div>
								</div></li>
							<li><div class="buttonActive">
									<div class="buttonContent">
										<button type="button" onclick="exportStatStudentExcel()">导出</button>
									</div>
								</div></li>
						</ul>
					</div>
				</div>
			</form>
		</div>


		<!-- 统计 -->
		<div id="statStudentInfo">
			<div class="pageContent">
				<c:if
					test="${condition['stuStatParam']==1||condition['stuStatParam']==2||condition['stuStatParam']==3||condition['stuStatParam']==4||condition['stuStatParam']==5||condition['stuStatParam']==6||condition['stuStatParam']==7||condition['stuStatParam']==8||condition['stuStatParam']==9||condition['stuStatParam']==10||condition['stuStatParam']==11||condition['stuStatParam']==12}">
					<table class="table" layouth="200">
						<thead>
							<tr>
								<!-- 在校生统计 -->
								<c:if test="${condition['stuStatParam']==12}">
									<th width="10%">毕业年份</th>
									<th width="7%">籍贯省</th>
									<th width="10%">学历层次</th>
									<th>性别</th>
									<th width="10%">毕结业结论</th>
									<th width="8%">学习形式</th>
									<th width="10%">院校名称</th>
									<th width="10%">专业名称</th>
								</c:if>

								<c:if
									test="${condition['stuStatParam']==2||condition['stuStatParam']==5||condition['stuStatParam']==9||condition['stuStatParam']==10||condition['stuStatParam']==11}">
									<th width="10%">教学站</th>
								</c:if>
								<c:if
									test="${condition['stuStatParam']==1||condition['stuStatParam']==2||condition['stuStatParam']==3||condition['stuStatParam']==4||condition['stuStatParam']==5||condition['stuStatParam']==9}">
									<th width="10%">年级</th>
								</c:if>
								<c:if
									test="${condition['stuStatParam']==1||condition['stuStatParam']==2||condition['stuStatParam']==3||condition['stuStatParam']==8||condition['stuStatParam']==11}">
									<th width="10%">层次</th>
								</c:if>
								<c:if
									test="${condition['stuStatParam']==2||condition['stuStatParam']==3}">
									<th width="10%">专业</th>
								</c:if>
								<c:if test="${condition['stuStatParam']==4}">
									<th width="10%">学习模式</th>
								</c:if>
								<c:if
									test="${condition['stuStatParam']==1||condition['stuStatParam']==2||condition['stuStatParam']==3||condition['stuStatParam']==4||condition['stuStatParam']==5||condition['stuStatParam']==10||condition['stuStatParam']==11||condition['stuStatParam']==12}">
									<th width="10%">总计(人)</th>
								</c:if>
								<c:if
									test="${condition['stuStatParam']==1||condition['stuStatParam']==2||condition['stuStatParam']==3||condition['stuStatParam']==4||condition['stuStatParam']==5||condition['stuStatParam']==10||condition['stuStatParam']==11||condition['stuStatParam']==12}">
									<th width="10%">男(人)</th>
								</c:if>
								<c:if
									test="${condition['stuStatParam']==1||condition['stuStatParam']==2||condition['stuStatParam']==3||condition['stuStatParam']==4||condition['stuStatParam']==5||condition['stuStatParam']==10||condition['stuStatParam']==11||condition['stuStatParam']==12}">
									<th width="10%">女(人)</th>
								</c:if>
								<c:if
									test="${condition['stuStatParam']==2||condition['stuStatParam']==3}">
									<th width="10%">纯网络(人)</th>
								</c:if>
								<c:if
									test="${condition['stuStatParam']==2||condition['stuStatParam']==3}">
									<th width="10%">网成班(人)</th>
								</c:if>
								<c:if test="${condition['stuStatParam']==6}">
									<th width="10%">统计项目</th>
								</c:if>
								<c:if test="${condition['stuStatParam']==6}">
									<th>17岁以下</th>
								</c:if>
								<c:if test="${condition['stuStatParam']==6}">
									<th>17岁</th>
								</c:if>
								<c:if test="${condition['stuStatParam']==6}">
									<th>18岁</th>
								</c:if>
								<c:if test="${condition['stuStatParam']==6}">
									<th>19岁</th>
								</c:if>
								<c:if test="${condition['stuStatParam']==6}">
									<th>20岁</th>
								</c:if>
								<c:if test="${condition['stuStatParam']==6}">
									<th>21岁</th>
								</c:if>
								<c:if test="${condition['stuStatParam']==6}">
									<th>22岁</th>
								</c:if>
								<c:if test="${condition['stuStatParam']==6}">
									<th>23岁</th>
								</c:if>
								<c:if test="${condition['stuStatParam']==6}">
									<th>24岁</th>
								</c:if>
								<c:if test="${condition['stuStatParam']==6}">
									<th>25岁</th>
								</c:if>
								<c:if test="${condition['stuStatParam']==6}">
									<th>26岁</th>
								</c:if>
								<c:if test="${condition['stuStatParam']==6}">
									<th>27岁</th>
								</c:if>
								<c:if test="${condition['stuStatParam']==6}">
									<th>28岁</th>
								</c:if>
								<c:if test="${condition['stuStatParam']==6}">
									<th>29岁</th>
								</c:if>
								<c:if test="${condition['stuStatParam']==6}">
									<th>30岁</th>
								</c:if>
								<c:if test="${condition['stuStatParam']==6}">
									<th>30岁以上</th>
								</c:if>
								<c:if test="${condition['stuStatParam']==7}">
									<th width="10%">户口所在地</th>
								</c:if>
								<c:if test="${condition['stuStatParam']==7}">
									<th width="10%">专科生(人)</th>
								</c:if>
								<c:if test="${condition['stuStatParam']==7}">
									<th width="10%">本科生(人)</th>
								</c:if>
								<c:if test="${condition['stuStatParam']==8}">
									<th width="10%">共产党员</th>
								</c:if>
								<c:if test="${condition['stuStatParam']==8}">
									<th width="10%">共青团员</th>
								</c:if>
								<c:if test="${condition['stuStatParam']==8}">
									<th width="10%">民主党派</th>
								</c:if>
								<c:if test="${condition['stuStatParam']==8}">
									<th width="10%">华侨</th>
								</c:if>
								<c:if test="${condition['stuStatParam']==8}">
									<th width="10%">港澳台</th>
								</c:if>
								<c:if test="${condition['stuStatParam']==8}">
									<th width="10%">少数民族</th>
								</c:if>
								<c:if test="${condition['stuStatParam']==8}">
									<th width="10%">残疾人</th>
								</c:if>
								<c:if test="${condition['stuStatParam']==9}">
									<th width="10%">正常注册</th>
								</c:if>
								<c:if test="${condition['stuStatParam']==9}">
									<th width="10%">正常未注册</th>
								</c:if>
								<c:if test="${condition['stuStatParam']==9}">
									<th width="10%">过期</th>
								</c:if>
								<c:if test="${condition['stuStatParam']==9}">
									<th width="10%">延期</th>
								</c:if>
								<c:if test="${condition['stuStatParam']==9}">
									<th width="10%">自动退学</th>
								</c:if>
								<c:if test="${condition['stuStatParam']==9}">
									<th width="10%">开除学籍</th>
								</c:if>
								<c:if test="${condition['stuStatParam']==9}">
									<th width="10%">退学</th>
								</c:if>

							</tr>
						</thead>
						<tbody>
							<c:forEach items="${stuInfos1.result }" var="stInfo1">
								<tr>
									<c:if
										test="${condition['stuStatParam']==2||condition['stuStatParam']==5||condition['stuStatParam']==9||condition['stuStatParam']==10||condition['stuStatParam']==11}">
										<td width="10%">${stInfo1.unit}</td>
									</c:if>
									<c:if
										test="${condition['stuStatParam']==1||condition['stuStatParam']==2||condition['stuStatParam']==3||condition['stuStatParam']==4||condition['stuStatParam']==5||condition['stuStatParam']==9}">
										<td width="10%">${stInfo1.grade}</td>
									</c:if>
									<c:if
										test="${condition['stuStatParam']==1||condition['stuStatParam']==2||condition['stuStatParam']==3||condition['stuStatParam']==8||condition['stuStatParam']==11}">
										<td width="10%">${stInfo1.classic}</td>
									</c:if>
									<c:if
										test="${condition['stuStatParam']==2||condition['stuStatParam']==3}">
										<td width="10%">${stInfo1.major}</td>
									</c:if>
									<c:if test="${condition['stuStatParam']==4}">
										<td width="10%">${ghfn:dictCode2Val('CodeTeachingType',stInfo1.teachingType)}</td>
									</c:if>

									<c:if
										test="${condition['stuStatParam']==1||condition['stuStatParam']==2||condition['stuStatParam']==3||condition['stuStatParam']==4||condition['stuStatParam']==5||condition['stuStatParam']==10||condition['stuStatParam']==11}">
										<td width="10%">${stInfo1.accountStatus}</td>
									</c:if>
									<c:if
										test="${condition['stuStatParam']==1||condition['stuStatParam']==2||condition['stuStatParam']==3||condition['stuStatParam']==4||condition['stuStatParam']==5||condition['stuStatParam']==10||condition['stuStatParam']==11}">
										<td width="10%">${stInfo1.bloodType}</td>
									</c:if>
									<c:if
										test="${condition['stuStatParam']==1||condition['stuStatParam']==2||condition['stuStatParam']==3||condition['stuStatParam']==4||condition['stuStatParam']==5||condition['stuStatParam']==10||condition['stuStatParam']==11}">
										<td width="10%">${stInfo1.bornAddress}</td>
									</c:if>
									<c:if
										test="${condition['stuStatParam']==2||condition['stuStatParam']==3}">
										<td width="10%">${stInfo1.certType}</td>
									</c:if>
									<c:if
										test="${condition['stuStatParam']==2||condition['stuStatParam']==3}">
										<td width="10%">${stInfo1.contactAddress}</td>
									</c:if>
									<c:if test="${condition['stuStatParam']==6}">
										<td width="10%">${stInfo1.accountStatus}</td>
									</c:if>
									<c:if test="${condition['stuStatParam']==6}">
										<td>${stInfo1.bloodType}</td>
									</c:if>
									<c:if test="${condition['stuStatParam']==6}">
										<td>${stInfo1.bornAddress}</td>
									</c:if>
									<c:if test="${condition['stuStatParam']==6}">
										<td>${stInfo1.bornDay}</td>
									</c:if>
									<c:if test="${condition['stuStatParam']==6}">
										<td>${stInfo1.certNum}</td>
									</c:if>
									<c:if test="${condition['stuStatParam']==6}">
										<td>${stInfo1.certType}</td>
									</c:if>
									<c:if test="${condition['stuStatParam']==6}">
										<td>${stInfo1.classic}</td>
									</c:if>
									<c:if test="${condition['stuStatParam']==6}">
										<td>${stInfo1.contactAddress}</td>
									</c:if>
									<c:if test="${condition['stuStatParam']==6}">
										<td>${stInfo1.contactPhone}</td>
									</c:if>
									<c:if test="${condition['stuStatParam']==6}">
										<td>${stInfo1.contactZipcode}</td>
									</c:if>
									<c:if test="${condition['stuStatParam']==6}">
										<td>${stInfo1.country}</td>
									</c:if>
									<c:if test="${condition['stuStatParam']==6}">
										<td>${stInfo1.currenAddress}</td>
									</c:if>
									<c:if test="${condition['stuStatParam']==6}">
										<td>${stInfo1.degree}</td>
									</c:if>
									<c:if test="${condition['stuStatParam']==6}">
										<td>${stInfo1.email}</td>
									</c:if>
									<c:if test="${condition['stuStatParam']==6}">
										<td>${stInfo1.enterAttr}</td>
									</c:if>
									<c:if test="${condition['stuStatParam']==6}">
										<td>${stInfo1.enterSchool}</td>
									</c:if>
									<c:if test="${condition['stuStatParam']==6}">
										<td>${stInfo1.examNo}</td>
									</c:if>
									<c:if test="${condition['stuStatParam']==7}">
										<td width="10%">${stInfo1.homePlace}</td>
									</c:if>
									<c:if test="${condition['stuStatParam']==7}">
										<td width="10%">${stInfo1.certNum}</td>
									</c:if>
									<c:if test="${condition['stuStatParam']==7}">
										<td width="10%">${stInfo1.bornDay}</td>
									</c:if>
									<c:if test="${condition['stuStatParam']==8}">
										<td width="10%">${stInfo1.contactPhone}</td>
									</c:if>
									<c:if test="${condition['stuStatParam']==8}">
										<td width="10%">${stInfo1.contactZipcode}</td>
									</c:if>
									<c:if test="${condition['stuStatParam']==8}">
										<td width="10%">${stInfo1.country}</td>
									</c:if>
									<c:if test="${condition['stuStatParam']==8}">
										<td width="10%">${stInfo1.currenAddress}</td>
									</c:if>
									<c:if test="${condition['stuStatParam']==8}">
										<td width="10%">${stInfo1.degree}</td>
									</c:if>
									<c:if test="${condition['stuStatParam']==8}">
										<td width="10%">${stInfo1.email}</td>
									</c:if>
									<c:if test="${condition['stuStatParam']==8}">
										<td width="10%">${stInfo1.enterAttr}</td>
									</c:if>
									<c:if test="${condition['stuStatParam']==9}">
										<td width="10%">${stInfo1.enterSchool}</td>
									</c:if>
									<c:if test="${condition['stuStatParam']==9}">
										<td width="10%">${stInfo1.examNo}</td>
									</c:if>
									<c:if test="${condition['stuStatParam']==9}">
										<td width="10%">${stInfo1.faith}</td>
									</c:if>
									<c:if test="${condition['stuStatParam']==9}">
										<td width="10%">${stInfo1.gaqCode}</td>
									</c:if>
									<c:if test="${condition['stuStatParam']==9}">
										<td width="10%">${stInfo1.gender}</td>
									</c:if>
									<c:if test="${condition['stuStatParam']==9}">
										<td width="10%">${stInfo1.health}</td>
									</c:if>
									<c:if test="${condition['stuStatParam']==9}">
										<td width="10%">${stInfo1.height}</td>
									</c:if>
								</tr>
							</c:forEach>

						</tbody>

					</table>
				</c:if>

				<c:if test="${condition['stuStatParam']==13}">

					<table class="table" layouth="320">

						<thead>
							<tr>
								<th colspan="16" style="text-align: center;">教职工情况</th>
							</tr>
							<tr>
								<th colspan="16">高基411                                                                                                                                                                             
									单位:人</th>
							</tr>
							<tr>
								<th colspan="2" rowspan="3"></th>
								<th rowspan="3">编号</th>
								<th colspan="9">教职工数</th>
								<th rowspan="3">聘请<br /> 校外教师
								</th>
								<th rowspan="3">离退休人员</th>
								<th rowspan="3">附属中小学<br /> 幼儿园<br /> 教职工
								</th>
								<th rowspan="3">集体所有制<br /> 人员
								</th>
							</tr>
							<tr>
								<th rowspan="2">合计</th>
								<th colspan="5">校本部教职工</th>
								<th rowspan="2">科研机构<br /> 人员
								</th>
								<th rowspan="2">校办企业<br /> 职工
								</th>
								<th rowspan="2">其他附设<br /> 机构人员
								</th>
							</tr>
							<tr>
								<td>计</td>
								<td>专任教师</td>
								<td>行政人员</td>
								<td>教辅人员</td>
								<td>工勤人员</td>
							</tr>
							<tr>
								<th colspan="2">甲</th>
								<th>乙</th>
								<th>1</th>
								<th>2</th>
								<th align="right">3</th>
								<th align="right">4</th>
								<th align="right">5</th>
								<th align="right">6</th>
								<th align="right">7</th>
								<th align="right">8</th>
								<th align="right">9</th>
								<th align="right">10</th>
								<th align="right">11</th>
								<th align="right">12</th>
								<th align="right">13</th>
							</tr>
						</thead>
						<tbody>
							<tr>
								<td width="13.2%" colspan="2">总计</td>
								<td width="6.2%"></td>
								<td width="6.2%"></td>
								<td width="6.2%"></td>
								<td width="6.2%"></td>
								<td width="6.2%"></td>
								<td width="6.2%"></td>
								<td width="6.2%"></td>
								<td width="6.2%"></td>
								<td width="6.2%"></td>
								<td width="6.2%"></td>
								<td width="6.2%">${counsum}</td>
								<td width="6.2%"></td>
								<td width="6.2%"></td>
								<td width="6.2%"></td>
							</tr>

							<c:forEach items="${stuInfos1.result }" var="stInfo1">
								<tr>
									<td colspan="2"><c:if
											test="${not empty ghfn:dictCode2Val('CodeTitleOfTechnicalCode',stInfo1.type) }">${ghfn:dictCode2Val('CodeTitleOfTechnicalCode',stInfo1.type)  }</c:if>
										<c:if
											test="${empty ghfn:dictCode2Val('CodeTitleOfTechnicalCode',stInfo1.type) }">未定级</c:if>
									</td>
									<td></td>
									<td></td>
									<td></td>
									<td></td>
									<td></td>
									<td></td>
									<td></td>
									<td></td>
									<td></td>
									<td></td>
									<td>${stInfo1.coun }</td>
									<td></td>
									<td></td>
									<td></td>
								</tr>
							</c:forEach>
							<tr>
								<td colspan="2">其中女</td>
								<td></td>
								<td></td>
								<td></td>
								<td></td>
								<td></td>
								<td></td>
								<td></td>
								<td></td>
								<td></td>
								<td></td>
								<td>${counwm}</td>
								<td></td>
								<td></td>
								<td></td>
							</tr>

							<tr>
								<td rowspan="7">其中<br /> 聘任制
								</td>
								<td>小  计</td>
								<td></td>
								<td></td>
								<td></td>
								<td></td>
								<td></td>
								<td></td>
								<td></td>
								<td></td>
								<td></td>
								<td></td>
								<td></td>
								<td>*</td>
								<td>*</td>
								<td>*</td>

							</tr>
							<tr>
								<td>其中：女</td>
								<td></td>
								<td></td>
								<td></td>
								<td></td>
								<td></td>
								<td></td>
								<td></td>
								<td></td>
								<td></td>
								<td></td>
								<td>*</td>
								<td>*</td>
								<td>*</td>
								<td>*</td>
							</tr>
							<tr>
								<td>正 高 级</td>
								<td></td>
								<td></td>
								<td></td>
								<td></td>
								<td></td>
								<td></td>
								<td></td>
								<td></td>
								<td></td>
								<td></td>
								<td>*</td>
								<td>*</td>
								<td>*</td>
								<td>*</td>
							</tr>
							<tr>
								<td>副 高 级</td>
								<td></td>
								<td></td>
								<td></td>
								<td></td>
								<td></td>
								<td></td>
								<td></td>
								<td></td>
								<td></td>
								<td></td>
								<td>*</td>
								<td>*</td>
								<td>*</td>
								<td>*</td>
							</tr>
							<tr>
								<td>中    级</td>
								<td></td>
								<td></td>
								<td></td>
								<td></td>
								<td></td>
								<td></td>
								<td></td>
								<td></td>
								<td></td>
								<td></td>
								<td>*</td>
								<td>*</td>
								<td>*</td>
								<td>*</td>
							</tr>
							<tr>
								<td>初    级</td>
								<td></td>
								<td></td>
								<td></td>
								<td></td>
								<td></td>
								<td></td>
								<td></td>
								<td></td>
								<td></td>
								<td></td>
								<td>*</td>
								<td>*</td>
								<td>*</td>
								<td>*</td>
							</tr>
							<tr>
								<td>未定职级</td>
								<td></td>
								<td></td>
								<td></td>
								<td></td>
								<td></td>
								<td></td>
								<td></td>
								<td></td>
								<td></td>
								<td></td>
								<td>*</td>
								<td>*</td>
								<td>*</td>
								<td>*</td>
							</tr>
						</tbody>
					</table>
				</c:if>
				<c:if test="${condition['stuStatParam']==14}">
					<table class="table" layouth="320">
						<thead>
							<tr>
								<th colspan="17">专任教师、聘请校外教师学历（位）情况</th>
							</tr>
							<tr>
								<th colspan="17">高基422                                                                                                                                                                              
									单位:人</th>
							</tr>
							<tr>
								<th rowspan="3"></th>
								<th rowspan="3">编号</th>
								<th colspan="3">合计</th>
								<th colspan="3">博士研究生</th>
								<th colspan="3">硕士研究生</th>
								<th colspan="3">本科</th>
								<th colspan="3">专科及以下</th>
							</tr>
							<tr>
								<th rowspan="2">计</th>
								<th colspan="2">其中：获学位</th>
								<th rowspan="2">计</th>
								<th colspan="2">其中：获学位</th>
								<th rowspan="2">计</th>
								<th colspan="2">其中：获学位</th>
								<th rowspan="2">计</th>
								<th colspan="2">其中：获学位</th>
								<th rowspan="2">计</th>
								<th colspan="2">其中：获学位</th>
							</tr>
							<tr>
								<th>博士</th>
								<th>硕士</th>
								<th>博士</th>
								<th>硕士</th>
								<th>博士</th>
								<th>硕士</th>
								<th>博士</th>
								<th>硕士</th>
								<th>博士</th>
								<th>硕士</th>
							</tr>
							<tr>
								<th>甲</th>
								<th>乙</th>
								<th>1</th>
								<th>2</th>
								<th>3</th>
								<th>4</th>
								<th>5</th>
								<th>6</th>
								<th>7</th>
								<th>8</th>
								<th>9</th>
								<th>10</th>
								<th>11</th>
								<th>12</th>
								<th>13</th>
								<th>14</th>
								<th>15</th>
							</tr>
						<thead>
						<tbody>
							<c:set var="a" value="0"></c:set>
							<c:forEach items="${stuInfos1.result }" var="stInfo1">
								<tr>
									<td width="5.8%">${stInfo1.type }</td>
									<td width="5.8%">${a+1 }<c:set var="a" value="${a+1 }"></c:set></td>
									<td width="5.8%">${stInfo1.c_q }</td>
									<td width="5.8%">${stInfo1.c_b }</td>
									<td width="5.8%">${stInfo1.c_s }</td>
									<td width="5.8%">${stInfo1.b_q }</td>
									<td width="5.8%">${stInfo1.b_b }</td>
									<td width="5.8%">${stInfo1.b_s }</td>
									<td width="5.8%">${stInfo1.s_q }</td>
									<td width="5.8%">${stInfo1.s_b }</td>
									<td width="5.8%">${stInfo1.s_s }</td>
									<td width="5.8%">${stInfo1.bk_q }</td>
									<td width="5.8%">${stInfo1.bk_b }</td>
									<td width="5.8%">${stInfo1.bk_s }</td>
									<td width="5.8%">${stInfo1.q_q }</td>
									<td width="5.8%">${stInfo1.q_b }</td>
									<td width="5.8%">${stInfo1.q_s }</td>
								</tr>
							</c:forEach>
						</tbody>
					</table>

				</c:if>
				<gh:page page="${stuInfos1}"
					goPageUrl="${baseUrl }/edu3/register/studentinfo/studentInfoStat.html"
					condition="${condition }" pageType="sys" />

			</div>
		</div>

	</div>
</body>
</html>