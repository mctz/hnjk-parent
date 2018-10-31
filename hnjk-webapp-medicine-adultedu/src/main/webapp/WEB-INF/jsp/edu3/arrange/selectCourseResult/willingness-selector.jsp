<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/common/common.jsp" %>
<head>
	<script type="text/javascript">
		$(document).ready(function(){
			// 设置老师
			$("#setTeacher").click(function(){
				// 处理参数
				var resId = $("#selectTeacher_resId").val();
				var teachCourseid = $("#selectTeacher_teachCourseid").val();
				var userid = $("#selectTeacher_resId").val();
				var branchSchool = $("#selectTeacher_branchSchool").val();
				var teachtype = $("#selectTeacher_teachtype").val();
				var classroomType = $("#selectTeacher_classroomType").val();
				var applyInfo = encodeURIComponent($("#selectTeacher_applyInfo").val());
				var days = $("#selectTeacher_days").val();
				var timePeriod = $("#selectTeacher_timePeriod").val();
				if(resId == "" && teachCourseid == ""){
					alertMsg.warn("请您关掉弹框，重新设置登分老师！");
					return;
				}
				var teacherIds;
				var setteacherNames;
				if(teachtype=="record"){
					var teacherId = new Array();
					var setteacherName =new Array();
					$("#taskSearchBody input[name='resourceid']:checked").each(function(){
						teacherId.push($(this).val());
						setteacherName.push($(this).attr("rel"));
					});
					teacherIds = teacherId.toString();
					setteacherNames = setteacherName.toString();
				} else {
					teacherIds = $("#teacherPageContent #pagerForm input[name=selectedTeachers]").val();
					setteacherNames = encodeURIComponent($("#teacherPageContent #pagerForm input[name=selectedNames]").val());
				}
				if(!teacherIds){
					alertMsg.warn("请选择老师！");
					return;
				}
				if(!isCheckOnlyone('resourceid','#taskSearchBody')){
					alertMsg.warn("只能选择一个老师！");
					return;
				}
				$.pdialog.closeCurrent();
				var url1 = "${baseUrl}/edu3/arrange/teachingWillingness/edit.html";
				navTab.reload(url1+'?userid='+userid+'&resId='+resId+'&brSchoolid='+branchSchool+'&teachCourseid='+teachCourseid+'&teacherids='+teacherIds+'&teacherNames='+setteacherNames+'&classroomType='+classroomType+'&applyInfo='+applyInfo+"&days="+days+"&timePeriod="+timePeriod, {}, 'RES_ARRANGE_WILLINGNESS_EDIT');
			});
		});
		
		function allSelectedTeacher(obj){
			var selectTeachers = $("#teacherPageContent #pagerForm input[name=selectedTeachers]").val();
			var selectedNames = $("#teacherPageContent #pagerForm input[name=selectedNames]").val();
			var selectedArray = new Array();
			var selectedNamesArray = new Array();
			if(selectTeachers){
				selectedArray = selectTeachers.split(",");
				selectedNamesArray = selectedNames.split(",");
			}
			if($(obj).attr("checked")){
				selectedArray.push($(obj).val());
				selectedNamesArray.push($(obj).attr("rel"));
			}else {
				// 删掉数组中某个字符串
				for(var i=0;i<selectedArray.length;i++){
					if(selectedArray[i]==$(obj).val()){
						selectedArray.splice(i, 1);
						selectedNamesArray.splice(i, 1);
						break;
					}
				}
			}
			$("#teacherPageContent #pagerForm input[name=selectedTeachers]").val(selectedArray.toString());
			$("#teacherPageContent #pagerForm input[name=selectedNames]").val(selectedNamesArray.toString());
		}
	</script>
</head>
<body>	
	<div class="page">
		<div class="pageHeader">
			<div class="pageHeader">
			<form id="taskSearchForm" onsubmit="return dialogSearch(this);" action="${baseUrl}/edu3/arrange/teachingWillingness/selectteacher.html" method="post">
			<input type="hidden" id="selectTeacher_resId" name="resId" value="${resId}">
			<input type="hidden" id="selectTeacher_teachCourseid" name="teachCourseid" value="${teachCourseid}">
			<input type="hidden" id="selectTeacher_branchSchool" name="branchSchool" value="${branchSchool}">
			<input type="hidden" id="selectTeacher_teachtype" name="teachtype" value="${teachtype}">
			<input type="hidden" id="selectTeacher_classroomType" name="classroomType" value="${classroomType}">
			<input type="hidden" id="selectTeacher_applyInfo" name="applyInfo" value="${applyInfo}">
			<input type="hidden" id="selectTeacher_days" name="days" value="${days}">
			<input type="hidden" id="selectTeacher_timePeriod" name="timePeriod" value="${timePeriod}">
			<div class="searchBar">
				<ul class="searchContent">	
					<li>
						<label>姓名：</label><input type="text" name="cnName" value="${condition['cnName']}" />
					</li>
					<li>
						<label>人员编号：</label><input type="text" name="teacherCode" value="${condition['teacherCode']}" />
					</li>										
				</ul>
				<div class="subBar">
					<ul style="margin-top: 20px;margin-right: 30px">
						<li><div class="buttonActive"><div class="buttonContent"><button type="submit"> 查 询 </button></div></div></li>	
						<li><div class="button"><div class="buttonContent"><button type="button"  id="setTeacher"> 确 定 </button></div></div></li>				
					</ul>
				</div>
			</div>
			</form>
		</div>		
		<div class="pageContent" id="teacherPageContent">
			<table class="table" layouth="168">
				<thead>
				    <tr>
				    	<th width="10%">&nbsp;</th>
				    	<th width="15%">人员编号</th>
			            <th width="15%">姓名</th>
			            <th width="10%">性别</th>
				        <th width="15%">职务</th>
				        <th width="10%">办公电话</th>
			            <th width="15%">学习中心</th>
			            <th width="10%">授课模式</th>
				    </tr>
			   	</thead>
			   	<tbody id="taskSearchBody">
		     	<c:forEach items="${teacherlist.result}" var="t" varStatus="vs">
			        <tr>
			            <td><input type="${condition['teachtype']=='record'?'radio':'checkbox' }" name="resourceid" value="${t.resourceid }"  
			            onclick="${condition['teachtype']=='record'?'':'allSelectedTeacher(this)' }" rel="${t.cnName }"  ${ghfn:strContains(t.resourceid,condition['selectedTeachers'])?'checked=checked':''}/></td>
			            <td>${t.teacherCode}</td>
			            <td>${t.cnName }</td>
			        	<td>${ghfn:dictCode2Val('CodeSex',t.gender) }</td>
			            <td>${ghfn:dictCode2Val('CodeDuty',t.duty) }</td>
			            <td>${t.officeTel}</td>
			            <td style="color: ${t.orgUnit.unitType=='brSchool'?'':'red' }">${t.orgUnit.unitName}</td>
			            <td>${ghfn:dictCode2Val('CodeTeachingType',t.teachingType) }</td>		            
			        </tr>
		       	</c:forEach>
		       	</tbody>
			</table>
		 	<gh:page page="${teacherlist}" goPageUrl="${baseUrl }/edu3/arrange/teachingWillingness/selectteacher.html" pageType="sys" targetType="dialog" condition="${condition}"/>
		</div>
	</div>
</div>
</body>