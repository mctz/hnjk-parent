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
			window.setTimeout(function(){				
				var existIds = $("#${condition['codesN']}").val();
				$("#check_all_selector_classesBody input[name=resourceid]").each(function(){
					if($(this).val()!="" && existIds.indexOf($(this).val())>=0){
						$(this).attr("checked",true);
					}
				});
			},500);
			
		});
		
		function msgClassesClickThis(obj){
			if(obj.value!=""){
				if(obj.checked){				
					if($("#${condition['codesN']}").val().indexOf(obj.value)<0){
						$("#${condition['codesN']}").val($("#${condition['codesN']}").val()+obj.value+",");
						$("#${condition['namesN']}").val($("#${condition['namesN']}").val()+$(obj).attr('rel')+",");
					}
				}else{
					if($("#${condition['codesN']}").val().indexOf(obj.value)>=0){
						var ids = $("#${condition['codesN']}").val().replace((obj.value+","),"");
						var names = $("#${condition['namesN']}").val().replace(($(obj).attr('rel')+","),"");
						$("#${condition['codesN']}").val(ids);
						$("#${condition['namesN']}").val(names);
					}
				}
			}			
		}	
		
		function msgClassesCheckboxAll(obj){			
			$("#check_all_selector_classesBody input[name='resourceid']").each(function(){
				$(this).attr("checked",$(obj).attr("checked"));	
				msgClassesClickThis(this);		
			});
		}		

	</script>
</head>
<body>
	<div class="page">
		<div class="pageHeader">
			<form onsubmit="return dialogSearch(this);"
				action="${baseUrl}/edu3/portal/message/classes.html" method="post">
				<div class="searchBar">
					<ul class="searchContent">
						<li><label>班级名称：</label><input type="text"
							id="selector_classes_classname" name="classname"
							value="${condition['classname']}" /> <input type="hidden"
							name="codesN" value="${condition['codesN']}" /> <input
							type="hidden" name="namesN" value="${condition['namesN']}" /></li>
						<li><label>教学站：</label> <gh:brSchoolAutocomplete
								name="brSchoolid" tabindex="1" id="selector_classes_brSchoolid"
								defaultValue="${condition['brSchoolid']}" displayType="code"
								style="width:52%;" /></li>
					</ul>
					<ul class="searchContent">
						<li><label>年级：</label> <gh:selectModel
								id="selector_classes_gradeid" name="gradeid"
								bindValue="resourceid" displayValue="gradeName"
								modelClass="com.hnjk.edu.basedata.model.Grade"
								value="${condition['gradeid']}"
								orderBy="yearInfo.firstYear desc,term desc,resourceid desc"
								style="width: 52%;" /></li>
						<li><label>层次：</label> <gh:selectModel name="classicid"
								id="selector_classes_classicid" bindValue="resourceid"
								displayValue="classicName" value="${condition['classicid']}"
								modelClass="com.hnjk.edu.basedata.model.Classic"
								style="width:52%;" /></li>
					</ul>
					<ul class="searchContent">
						<li><label>专业：</label> <select class="flexselect"
							id="selector_classes_majorid" name="majorid" tabindex=1
							style="width: 129px;">${majorOption}</select></li>
						<li><label>办学模式：</label> <gh:select
								id="selector_classes_teachingType" name="teachingType"
								value="${condition['teachingType']}"
								dictionaryCode="CodeTeachingType" style="width:52%" /></li>
					</ul>
					<div class="subBar">
						<ul>
							<li><div class="buttonActive">
									<div class="buttonContent">
										<button type="submit">查 询</button>
									</div>
								</div></li>
							<li><div class="button">
									<div class="buttonContent">
										<button type="button" class="close">确 定</button>
									</div>
								</div></li>
						</ul>
					</div>
				</div>
			</form>
		</div>
		<div class="pageContent">
			<table class="table" layouth="180">
				<thead>
					<tr>
						<th width="5%"><input type="checkbox" name="checkall"
							id="check_all_selector_classes"
							onclick="msgClassesCheckboxAll(this)" /></th>
						<th width="25%">班级名称</th>
						<th width="8%">年级</th>
						<th width="22%">教学站</th>
						<th width="22%">专业</th>
						<th width="10%">层次</th>
						<th width="8%">学习形式</th>
					</tr>
				</thead>
				<tbody id="check_all_selector_classesBody">
					<c:forEach items="${classesPage.result}" var="cl" varStatus="vs">
						<tr>
							<td><input type="checkbox" name="resourceid"
								value="${cl.resourceid }" rel="${cl.classname }"
								onclick="msgClassesClickThis(this)" /></td>
							<td>${cl.classname }</td>
							<td>${cl.grade.gradeName }</td>
							<td>${cl.brSchool.unitName }</td>
							<td>${cl.major.majorName }</td>
							<td>${cl.classic.classicName }</td>
							<td>${ghfn:dictCode2Val('CodeTeachingType',cl.teachingType) }</td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
			<gh:page page="${classesPage}"
				goPageUrl="${baseUrl }/edu3/portal/message/classes.html"
				pageType="sys" targetType="dialog" condition="${condition}" />
		</div>
	</div>
</body>
</html>