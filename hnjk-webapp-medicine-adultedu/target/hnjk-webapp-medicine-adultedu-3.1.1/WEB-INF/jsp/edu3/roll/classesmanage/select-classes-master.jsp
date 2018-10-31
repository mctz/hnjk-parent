<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<script type="text/javascript">
		$(document).ready(function(){
			window.setTimeout(function(){
				var existIds = $("#${idsN}").val();
				$("#masterSearchBody input[name='resourceid']").each(function(){
					if(existIds.indexOf($(this).val())>=0){
						$(this).attr("checked",true);
					}					
				});				
			},500);			
		});
					
		function clickMaster(obj){
			
			var idsN   = $("#masterSearchForm input[name='idsN']").val();
			var namesN = $("#masterSearchForm input[name='namesN']").val();
			if(obj.checked){
				$("#masterSearchBody input[name='resourceid']").each(function(){
					if($(this).val()!=obj.value){
						$(this).attr("checked",false);
					}	
				});		
				if($("#"+idsN+"").val().indexOf(obj.value)<0){
			    	$("#"+idsN+"").val(obj.value);
					$("#"+namesN+"").val($(obj).attr('rel'));
				}
			}else{
				if($("#"+idsN+"").val().indexOf(obj.value)>=0){
					$("#"+idsN+"").val("");
					$("#"+namesN+"").val("");
				}
			}
		}
		function clickThisTask(obj){
			var type = $("#masterSearchForm input[name='type']").val();
			if(type=='0'){
				if(obj.checked){
					$("#masterSearchBody input[name='resourceid']").each(function(){
						if($(this).val()!=obj.value){
							$(this).attr("checked",false);
						}	
					});				
					if($("#${idsN}").val().indexOf(obj.value)<0){
						$("#${idsN}").val(obj.value);
						$("#${namesN}").val($(obj).attr('rel'));
					}
				}else{
					if($("#${idsN}").val().indexOf(obj.value)>=0){
						$("#${idsN}").val("");
						$("#${namesN}").val("");
					}
				}
			} else {
				if(obj.checked){				
					if($("#${idsN}").val().indexOf(obj.value)<0){
						$("#${idsN}").val($("#${idsN}").val()+obj.value+",");
						$("#${namesN}").val($("#${namesN}").val()+$(obj).attr('rel')+",");
					}
				}else{
					if($("#${idsN}").val().indexOf(obj.value)>=0){
						var ids = $("#${idsN}").val().replace((obj.value+","),"");
						var names = $("#${namesN}").val().replace(($(obj).attr('rel')+","),"");
						$("#${idsN}").val(ids);
						$("#${namesN}").val(names);
					}
				}
			}
		}
		
		function clickAllTask(obj){			
			$("#masterSearchBody input[name='resourceid']").each(function(){
				$(this).attr("checked",$(obj).attr("checked"));	
				clickThisTask(this);			
			});
		}
		
		function setClassesMaster(){
			var classIds = "${classIds}";
			if(classIds!=""){
				if(!isChecked('resourceid',"#masterSearchBody")){
		 			alertMsg.warn('请选择一条记录。');
					return false;
			 	}
				var classesMasterId = "";
				$("#masterSearchBody input[@name='resourceid']:checked").each(function(){
					classesMasterId = $(this).val();
		   		 });
				var url = "${baseUrl}/edu3/roll/classes/save.html";
				//alert(url);
				$.ajax({
					type:"post",
					url:url,
					data:{"classIds":classIds,"classesMasterId":classesMasterId},
					dataType:"json",
					success:function(data){
						if(data.statusCode==200){
							alertMsg.correct(data.message);
							navTab.reload("${baseUrl}/edu3/roll/classes/list.html", $("#classesListForm").serializeArray(), "RES_ROLL_CLASSES");
						}else{
							alertMsg.error(data.message);
						}
					}
				});
			}else{
				$.pdialog.closeCurrent();
			}
		}
	</script>
</head>

<body>
	<div class="page">
		<div class="pageHeader">
			<div class="pageHeader">
				<form id="masterSearchForm" onsubmit="return dialogSearch(this);"
					action="${baseUrl}/edu3/roll/classes/select-classesmaster.html"
					method="post">
					<input type="hidden" name="classIds" value="${classIds }">
					<div class="searchBar">
						<ul class="searchContent">
							<li><label>学习中心：</label>
							<gh:selectModel name="unitId" bindValue="resourceid"
									displayValue="unitName" style="width:120px"
									modelClass="com.hnjk.security.model.OrgUnit"
									value="${condition['unitId']}" orderBy="unitName" /> <input
								type="hidden" name="idsN" value="${idsN}" /> <input
								type="hidden" name="namesN" value="${namesN}" /></li>
							<!-- 
					<li>
						<label>授课模式：</label><gh:select name="teachingType" value="${condition['teachingType']}" dictionaryCode="CodeTeachingType"/>
					</li>										
				</ul>
				<ul class="searchContent">	
				 -->
							<li><label>姓名：</label><input type="text" name="cnName"
								value="${condition['cnName']}" /></li>
							<li><label>人员编号：</label><input type="text"
								name="teacherCode" value="${condition['teacherCode']}" /></li>
						</ul>
						<div class="subBar">
							<div class="buttonActive">
								<div class="buttonContent">
									<button type="submit">查 询</button>
								</div>
							</div>
							<div class="buttonActive">
								<div class="buttonContent">
									<button type="button" onclick="setClassesMaster()">确 定</button>
								</div>
							</div>
						</div>
					</div>
				</form>
			</div>
			<div class="pageContent">
				<table class="table" layouth="168">
					<thead>
						<tr>
							<th width="10%"><c:if test="${condition['type'] ne '0'}">
									<input type="checkbox" onclick="clickAllTask(this)" />
								</c:if>&nbsp;</th>
							<th width="15%">人员编号</th>
							<th width="15%">姓名</th>
							<th width="10%">性别</th>
							<th width="15%">职务</th>
							<th width="15%">办公电话</th>
							<th width="20%">学习中心</th>
							<!--       <th width="10%">授课模式</th> -->
						</tr>
					</thead>
					<tbody id="masterSearchBody">
						<c:forEach items="${teacherlist.result}" var="t" varStatus="vs">
							<tr>
								<td><input type="checkbox" name="resourceid"
									value="${t.resourceid }" rel="${t.cnName }"
									onclick="clickMaster(this)" /></td>
								<td>${t.teacherCode}</td>
								<td>${t.cnName }</td>
								<td>${ghfn:dictCode2Val('CodeSex',t.gender) }</td>
								<td>${ghfn:dictCode2Val('CodeDuty',t.duty) }</td>
								<td>${t.officeTel}</td>
								<td>${t.orgUnit.unitName}</td>
								<!--         <td>${ghfn:dictCode2Val('CodeTeachingType',t.teachingType) }</td>		     -->
							</tr>
						</c:forEach>
					</tbody>
				</table>
				<gh:page page="${teacherlist}"
					goPageUrl="${baseUrl }/edu3/roll/classes/select-classesmaster.html"
					pageType="sys" targetType="dialog" condition="${condition}" />
			</div>
		</div>
</body>