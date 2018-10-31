<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>新生第一学期课程预约</title>


</head>
<body>
	<div class="page">
		<div class="pageHeader">
			<form id="searchForm" onsubmit="return navTabSearch(this);"
				action="${baseUrl }/edu3/teaching/courseorder/firstTermCourseOrder-list.html"
				method="post">
				<input id="teachingPlanId" name="teachingPlanId" type="hidden" />
				<div class="searchBar">
					<ul class="searchContent">
						<li><label>年级：</label>
						<gh:selectModel id="gradeid" name="gradeid" bindValue="resourceid"
								displayValue="gradeName" style="width:55%"
								modelClass="com.hnjk.edu.basedata.model.Grade"
								value="${condition['gradeid']}" orderBy="gradeName desc" /><font
							color="red">*</font></li>
						<li><label>层次：</label>
						<gh:selectModel id="classic" name="classic" bindValue="resourceid"
								displayValue="classicName"
								modelClass="com.hnjk.edu.basedata.model.Classic"
								value="${condition['classic']}" style="width:55%" /><font
							color="red">*</font></li>
						<li><label>专业：</label>
						<gh:selectModel id="major" name="major" bindValue="resourceid"
								displayValue="majorCodeName" value="${condition['majorid']}"
								modelClass="com.hnjk.edu.basedata.model.Major" style="width:55%"
								orderBy="majorCode" /><font color="red">*</font></li>

					</ul>
					<ul class="searchContent">
						<li><label>办学模式：</label> <gh:select name="schoolType"
								value="${condition['schoolType']}"
								dictionaryCode="CodeTeachingType" style="width:55%" /> <font
							color="red">*</font></li>
						<c:if test="${!isBrschool }">
							<li><label>教学站：</label> <gh:brSchoolAutocomplete
									name="branchSchool" tabindex="1"
									id="firsttermCourseOrder_eiinfo_brSchoolName"
									defaultValue="${condition['branchSchool']}" displayType="code"
									style="width:53%" /></li>
						</c:if>
						<li><label>姓名：</label><input type="text" name="name"
							value="${condition['name']}" style="width: 53%" /></li>
					</ul>
					<ul class="searchContent">
						<li><label>学号：</label><input type="text" name="studyNo"
							value="${condition['studyNo']}" style="width: 53%" /></li>
					</ul>
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
			<gh:resAuth parentCode="RES_TEACHING_BOOKING_FIRSTTERM_COURSE"
				pageType="list"></gh:resAuth>
			<table class="table" layouth="188">
				<thead>
					<tr>
						<th width="5%"><input type="checkbox" name="checkall"
							id="check_all_info"
							onclick="checkboxAll('#check_all_info','resourceid','#firstTermCourseOrderInfoBody')" /></th>
						<th width="15%">姓名</th>
						<th width="20%">学号</th>
						<th width="10%">年级</th>
						<th width="10%">培养层次</th>
						<th width="20%">专业</th>
						<th width="20%">教学站</th>
					</tr>
				</thead>

				<tbody id="firstTermCourseOrderInfoBody" style="display: none;">
					<c:forEach items="${stuList}" var="stu" varStatus="vs">
						<tr>
							<td><input type="checkbox" name="resourceid"
								value="${stu.resourceid }" autocomplete="off" /></td>
							<td><a href="#" onclick="viewStuInfo2('${stu.resourceid}')"
								title="点击查看">${stu.studentBaseInfo.name }</a></td>
							<td>${stu.studyNo}</td>
							<td>${stu.grade.gradeName}</td>
							<td>${stu.classic.classicName }</td>
							<td>${stu.major.majorName }</td>
							<td>${stu.branchSchool}</td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
		</div>

	</div>
	<script type="text/javascript">
	jQuery(document).ready(function(){
		var msg = "${condition['alertMsg']}";
		var teachingPlanId = "${condition['teachingPlanId']}";
		if(""!= msg){
			alertMsg.confirm(msg,{
				okCall:function(){
					jQuery("#firstTermCourseOrderInfoBody").show();
				}
			});
		}
		if(""!= teachingPlanId){
			jQuery("#teachingPlanId").attr("value",teachingPlanId);
		}
	});
	function viewStuInfo2(id){
		var url = "${baseUrl}/edu3/framework/studentinfo/view.html";
		$.pdialog.open(url+'?act=view&resourceid='+id, 'RES_SCHOOL_SCHOOLROLL_MANAGER_VIEW', '查看学籍', {width: 800, height: 600});　
	}
	function firstTermCourseOrder(){
		var teachingPlanId = jQuery("#teachingPlanId").val();
		if(""==teachingPlanId){
			alertMsg.warn("没有加载教学计划数据，请选择年级、专业、层次查询对应的教学计划！");
		}else{
			var ids = new Array();
			jQuery("input[name='resourceid']:checked").each(function(){
				ids.push(jQuery(this).val());
				jQuery(this).remove();
			})
			if(ids.length==0){
				 alertMsg.warn("请选择要预约课程的学生!");
			}else{
				var url="${baseUrl}/edu3/teaching/courseorder/firstTermCourseOrder-save.html"
				var gradeid = jQuery("#gradeid").val();
				var majorid = jQuery("#major").val();
				var classic = jQuery("#classic").val();
				jQuery.ajax({
					type:"post",
					data:"teachingPlanId="+teachingPlanId+"&stuIds="+ids.toString()+"&gradeid="+gradeid+"&majorid="+majorid+"&classic="+classic,
					url:url,
					dataType :"json",
					success:function(resultDate){
						var msgList = resultDate['msg'];
						var msg = "";
						for(i=0;i<msgList.length;i++){
							msg+=(i+1)+"、"+msgList[i]+'\n';
						}
						if(resultDate['setFirstTermCourseStatus']==true){
							var gradeid = resultDate['gradeid'];
							var major =  resultDate['majorid'];
							var classic =  resultDate['classic'];
							navTab.reload("${baseUrl}/edu3/teaching/courseorder/firstTermCourseOrder-list.html?gradeid="+gradeid+"&major="+major+"&classic="+classic);
							alertMsg.correct(msg);
							
						}else{
							alertMsg.warn(msg);
						}
					}
				});
			}	 
		}
	}
</script>
</body>
</html>