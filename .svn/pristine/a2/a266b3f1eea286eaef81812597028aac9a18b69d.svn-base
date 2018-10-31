<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<script type="text/javascript">
		function selectall(){
			$("#onlineClassesBody input[name='resourceid']").each(function(){
				if($('#onlineCheckAll').attr("checked")){
					$(this).attr("checked",true);
				}else{
					$(this).attr("checked",false);
				}				
			});
			selectok();
		}
	
		function selectok(){
			var arrayObj = new Array();
			var arrayVl = new Array();
			$("#onlineClassesBody input[name='resourceid']").each(function(){
				if ($(this).attr("checked")) {  
					arrayObj.push($(this).val());	
					arrayVl.push($(this).attr("rel"));
	            } 	
			});
			$("#onlineClasses").val();
			$("#onlineClasses").val(arrayObj);
			$("#onlineClassesId").val();
			$("#onlineClassesId").val(arrayVl);
		}
	</script>
</head>

<body>
	<div class="page">
		<div class="pageContent">
			<table class="table" layouth="10">
				<thead>
					<tr>
						<th width="10%"><input type="checkbox" id="onlineCheckAll"
							onclick="selectall()" /></th>
						<th width="15%">名称</th>
						<th width="10%">年级</th>
						<th width="10%">层次</th>
						<th width="15%">专业</th>
						<th width="10%">学习形式</th>
					</tr>
				</thead>
				<tbody id="onlineClassesBody">
					<c:forEach items="${classesList.result}" var="t" varStatus="vs">
						<tr>
							<td><input type="checkbox" name="resourceid"
								value="${t[0] }" rel="${t[1] }" onclick="selectok()"
								<c:if test="${!empty classesIdAry }">
			            <c:forEach items="${classesIdAry }" var="classesId">
			            	<c:if test="${classesId == t[1] }"> checked="checked"</c:if>
					</c:forEach>
					</c:if>
					/>
					</td>
					<td>${t[0]}</td>
					<td>${t[2] }</td>
					<td>${t[3] }</td>
					<td>${t[4] }</td>
					<td>${ghfn:dictCode2Val('CodeTeachingType',t[5]) }</td>
					</tr>
					</c:forEach>
				</tbody>
			</table>
			<gh:page page="${classesList}"
				goPageUrl="${baseUrl }/edu3/metares/exercise/unactiveexercise/classes-select.html?courseId=${courseId }&term=${term }&yearInfo=${yearInfo }"
				pageType="sys" targetType="dialog" condition="${condition}" />
		</div>
	</div>
</body>
<script type="text/javascript">
		/*不知道为什么执行完，复选框全部变成不选中
		//$(document).ready(function(){
		function setClassesIdCheck() {
			var classesIds = "${classesIds}";
			if (classesIds != "") {
				var classesIdAry = new Array();
				classesIdAry = classesIds.split(",");
				$("#onlineClassesBody input[name='resourceid']").each(function(){
					for (var i=0; i<classesIdAry.length; i++) {
						alert("i="+i+"----"+$(this).attr("rel") +"----"+ classesIdAry[i]);
						if ($(this).attr("rel") == classesIdAry[i]) {
							$(this).attr("checked", true);
							break;
			            } 	
					}
				});
			}
		}
		//});
		setClassesIdCheck();
		*/
</script>