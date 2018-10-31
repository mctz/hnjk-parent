<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<style>
* {
	font-size: 12px;
}

.type {
	text-align: center;
	line-height: 28px;
}
</style>
<script type="text/javascript">
		$(document).ready(function(){
			// 修改高度
			$("#cd_left").css("height",parseInt($(".dialogContent").css("height")));
			$("#cd_right").css("height",parseInt($(".dialogContent").css("height")));
			
			window.setTimeout(function(){
				var arr = document.getElementsByName("resourceid");
				var existIds = $("#${param.idsN}").val();
				for(i=0;i<arr.length;i++){
					if(existIds.indexOf(arr[i].value)>=0){
						arr[i].checked = true;
					}
				}
			},500);
			
		});
		function dir(type){
			$.pdialog.reload("${baseUrl}/edu3/framework/system/selector/student.html?idsN=${param.idsN}&namesN=${param.namesN}&branchSchool="+type);
		}
		
		function ctx(){
			$.pdialog.reload("${baseUrl}/edu3/framework/system/selector/student.html?idsN=${param.idsN}&namesN=${param.namesN}&branchSchool="
			+$('#branchSchool').val()+
			"&major="
			+$('#major').val()+
			"&classic="
			+$('#classic').val()+
			"&stuStatus="
			+$('#stuStatus').val()+
			"&name="
			+$('#name').val()+
			"&matriculateNoticeNo="
			+$('#matriculateNoticeNo').val());
		}
		
		function clickThis(obj){
			if(obj.checked){
				if($("#${param.idsN}").val().indexOf(obj.value)<0){
					$("#${param.idsN}").val($("#${param.idsN}").val()+obj.value+",");
					$("#${param.namesN}").val($("#${param.namesN}").val()+$(obj).attr('rel')+",");
				}
			}else{
				if($("#${param.idsN}").val().indexOf(obj.value)>=0){
					var ids = $("#${param.idsN}").val().replace((obj.value+","),"");
					var names = $("#${param.namesN}").val().replace(($(obj).attr('rel')+","),"");
					$("#${param.idsN}").val(ids);
					$("#${param.namesN}").val(names);
				}
			}
		}
		
		function clickAll(obj){
			var arr = document.getElementsByName("resourceid");
			for(i=0;i<arr.length;i++){
				arr[i].checked = obj.checked;
				clickThis(arr[i]);
			}
		}
	</script>
</head>

<body>
	<!-- left -->
	<table width="100%">
		<tr>
			<td width="20%">
				<div id="cd_left"
					style="float: left; width: 100%; overflow: auto; border-right: #ccc solid 1px;">
					<c:forEach items="${leftList }" var="v" varStatus="s">
						<a class="panelBar type" href="javascript:dir('${v.resourceid }')">${v.unitShortName }</a>
					</c:forEach>
				</div>
			</td>
			<td>
				<!-- right -->
				<div id="cd_right" style="width: 100%; border-left: #ccc solid 1px;">
					<button onclick="ctx()">查 询</button>
					<table>
						<tr>
							<td>教学站：</td>
							<td><gh:selectModel name="branchSchool"
									bindValue="resourceid" displayValue="unitName"
									style="width:120px"
									modelClass="com.hnjk.security.model.OrgUnit"
									value="${condition['branchSchool']}"
									condition="unitType='brSchool'" /></td>
							<td>专业：</td>
							<td><gh:selectModel name="major" bindValue="resourceid"
									displayValue="majorName" value="${condition['major']}"
									modelClass="com.hnjk.edu.basedata.model.Major"
									style="width:120px" /></td>
						</tr>
						<tr>
							<td>学层次：</td>
							<td><gh:selectModel name="classic" bindValue="resourceid"
									displayValue="classicName" value="${condition['classic']}"
									modelClass="com.hnjk.edu.basedata.model.Classic"
									style="width:120px" /></td>
							<td>学籍状态：</td>
							<td><gh:select name="stuStatus"
									value="${condition['stuStatus']}"
									dictionaryCode="CodeStudentStatus" style="width:120px" /></td>
						</tr>
						<tr>
							<td>姓名：</td>
							<td><input type="text" id="name" name="name"
								value="${condition['name']}" style="width: 115px" /></td>
							<td>学号：</td>
							<td><input type="text" id="matriculateNoticeNo"
								name="matriculateNoticeNo"
								value="${condition['matriculateNoticeNo']}" style="width: 115px" /></td>
						</tr>
					</table>
					<table class="table" layouth="142">
						<thead>
							<tr>
								<th width="10%"><input type="checkbox" name="checkall"
									id="check_all_info" onclick="clickAll(this)" /></th>
								<th width="16%">姓名</th>
								<th width="14%">学号</th>
								<th width="10%">年级</th>
								<th width="15%">培养层次</th>
								<th width="20%">专业</th>
								<th width="15%">教学站</th>
							</tr>
						</thead>
						<tbody id="infoBody">
							<c:forEach items="${stulist.result}" var="stu" varStatus="vs">
								<tr>
									<td><input type="checkbox" name="resourceid"
										value="${stu.resourceid }" rel="${stu.studentBaseInfo.name }"
										onclick="clickThis(this)" /></td>
									<td><a href="#">${stu.studentBaseInfo.name }</a></td>
									<td>${stu.studyNo}</td>
									<td>${stu.grade.gradeName}</td>
									<td>${stu.classic.classicName }</td>
									<td>${stu.major.majorName }</td>
									<td>${stu.branchSchool}</td>
								</tr>
							</c:forEach>
						</tbody>
					</table>
					<gh:page page="${stulist}"
						goPageUrl="${baseUrl }/edu3/framework/system/selector/student.html"
						pageType="sys" targetType="dialog" condition="${condition}" />
				</div>
			</td>
		</tr>
	</table>
</body>