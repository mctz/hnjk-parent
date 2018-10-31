<%@ page language="java" contentType="text/html;charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
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
			$("#cd_right").css("height",parseInt($(".dialogContent").css("height")));
			
			window.setTimeout(function(){
				var existIds = $("#${param.teacherId}").val();				
				$("#teachersBody input[name=resourceid]").each(function(){
					if(existIds.indexOf($(this).val())>=0){
						$(this).attr("checked",true);
					}
				});
			},500);
			
		});
		function dir(type){
			$.pdialog.reload("${baseUrl}/edu3/teaching/teachingcourse/teacher.html?teacherId=${param.teacherId}&teacherName=${param.teacherName}&unitId="+type);
		}
		
		function ctx(){
			$.pdialog.reload("${baseUrl}/edu3/teaching/teachingcourse/teacher.html?teacherId=${param.teacherId}&teacherName=${param.teacherName}&unitId="
			+$('#unitId').val()+			
			"&titleOfTechnical="
			+$('#titleOfTechnical').val()+
			"&gender="
			+$('#gender').val()+
			"&cnName="
			+$('#cnName').val());
		}
		
		function clickThis(obj){
			if(obj.checked){
				$("#teachersBody input[name=resourceid]").each(function(){
					if($(this).val() != obj.value){
						$(this).attr("checked",false);
					}
				});
				if($("#${param.teacherId}").val().indexOf(obj.value)<0){
					$("#${param.teacherId}").val(obj.value);
					$("#${param.teacherName}").val($(obj).attr('rel'));
				}
			}else{
				$("#${param.teacherId}").val("");
				$("#${param.teacherName}").val("");
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

	<table width="100%">
		<tr>
			<td>
				<!-- right -->
				<div id="cd_right" style="width: 100%; border-left: #ccc solid 1px;">
					<table>
						<tr>
							<td><span class="buttonActive" style="margin-right: 8px"><div
										class="buttonContent">
										<button type="button" onclick="ctx()">查 询</button>
									</div></span><span class="buttonActive" style="margin-left: 8px"><div
										class="buttonContent">
										<button type="button" onclick="$.pdialog.closeCurrent();">
											确 定</button>
									</div></span></td>
						</tr>
					</table>
					<table>
						<tr>
							<td>教学站：</td>
							<td><gh:selectModel name="unitId" bindValue="resourceid"
									displayValue="unitName" style="width:120px"
									modelClass="com.hnjk.security.model.OrgUnit"
									value="${condition['unitId']}" condition="unitType='brSchool'" />
							</td>
							<td>中文名称：</td>
							<td><input type="text" name="cnName" id="cnName"
								value="${condition['cnName']}" style="width: 115px" /></td>
						</tr>
						<tr>
							<td>职称：</td>
							<td><gh:select name="titleOfTechnical"
									value="${condition['titleOfTechnical']}"
									dictionaryCode="CodeTitleOfTechnicalCode" style="width:120px" />
							</td>
							<td>性别：</td>
							<td><gh:select name="gender" value="${condition['gender']}"
									dictionaryCode="CodeSex" style="width:120px" /></td>
						</tr>
					</table>
					<table class="table" layouth="142">
						<thead>
							<tr>
								<th width="10%"><input type="checkbox" name="checkall"
									id="check_all_teachers" onclick="clickAll(this)" /></th>
								<th width="10%">姓名</th>
								<th width="10%">性别</th>
								<th width="10%">人员编号</th>
								<th width="10%">职称</th>
								<th width="10%">职务</th>
								<th width="10%">文化程度</th>
								<th width="10%">办公电话</th>
								<th width="10%">家庭电话</th>
								<th width="10%">教学站</th>
							</tr>
						</thead>
						<tbody id="teachersBody">
							<c:forEach items="${teacherlist.result}" var="t" varStatus="vs">
								<tr>
									<td><input type="checkbox" name="resourceid"
										value="${t.resourceid }" rel="${t.cnName }"
										onclick="clickThis(this)" /></td>
									<td>${t.cnName }</td>
									<td>${ghfn:dictCode2Val('CodeSex',t.gender) }</td>
									<td>${t.teacherCode}</td>
									<td>${ghfn:dictCode2Val('CodeTitleOfTechnicalCode',t.titleOfTechnical) }</td>
									<td>${ghfn:dictCode2Val('CodeDuty',t.duty) }</td>
									<td>${t.education}</td>
									<td>${t.officeTel}</td>
									<td>${t.homeTel}</td>
									<td>${t.orgUnit.unitName}</td>
								</tr>
							</c:forEach>
						</tbody>
					</table>
					<gh:page page="${teacherlist}"
						goPageUrl="${baseUrl }/edu3/teaching/teachingcourse/teacher.html"
						pageType="sys" targetType="dialog" condition="${condition}" />
				</div>
			</td>
		</tr>
	</table>
</body>