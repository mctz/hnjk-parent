<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>课室管理</title>
<script type="text/javascript">
	//新增
	function addClassroom(){
		navTab.openTab('RES_BASEDATA_CLASSROOM_INPUT', '${baseUrl}/edu3/sysmanager/exclassroom/input.html?buildingId='+$('#classroomForm #buildingId').val(), '新增教室');
	}
	
	//修改
	function modifyClassroom(){
		var url = "${baseUrl}/edu3/sysmanager/exclassroom/input.html";
		if(isCheckOnlyone('resourceid','#classroomBody')){
			navTab.openTab('RES_BASEDATA_CLASSROOM_INPUT', url+'?resourceid='+$("#classroomBody input[@name='resourceid']:checked").val(), '编辑教室');
		}			
	}
		
	//删除
	function removeClassroom(){	
		pageBarHandle("您确定要删除这些记录吗？","${baseUrl}/edu3/sysmanager/classroom/remove.html","#classroomBody");
	}
	
	//导出课室
	function exportClassroom(){
		var classroomIds = [];
		//条件 查询
		var brschoolid 		= $("#classroom_brschoolid").val();
		var classroomName   = $("#classroomName").val();
		var classroomType 	= $("#classroomType").val();
		
		$("#classroomBody input[@name='resourceid']:checked").each(function(){
			classroomIds.push($(this).val());
		});
		
		alertMsg.confirm("您确定要根据查询条件导出教室吗?", {
			okCall: function(){	
				var param = "brschoolid="+brschoolid+"&classroomName="+encodeURIComponent(encodeURIComponent(classroomName))+"&classroomType="+classroomType
				+"&classroomIds="+classroomIds.join(',');
				//alert(param);return false;
				$('#frame_exportXlsStudentDivideclass').remove();
				var iframe = document.createElement("iframe");
				iframe.id = "frame_exportXlsStudentDivideclass";
				iframe.src = "${baseUrl }/edu3/sysmanager/classroominfo/exportXlsClassroom.html?"+param;
				iframe.style.display = "none";
				//创建完成之后，添加到body中
				document.body.appendChild(iframe);
			}
		});	
	}	
	//导入课室信息xls
	function importClassroom(){
		var url ="${baseUrl}/edu3/sysmanager/classroominfo/importXlsClassroom.html";
		$.pdialog.open(url,"RES_BASEDATA_CLASSROOM_IMPORT","导入教室信息", {mask:true,width:450, height:210});
	}
</script>
</head>
<body>
<div class="page">
	<div class="pageHeader">
		<form id="classroomForm" onsubmit="return navTabSearch(this);" action="${baseUrl }/edu3/sysmanager/exclassroom/list.html" method="post">
		<div class="searchBar">
			<ul class="searchContent">	
			<c:if test="${not isBrschool }">		
				<li>
					<label>教学站：</label>
					<gh:brSchoolAutocomplete name="brSchoolid" tabindex="1" id="classroom_brschoolid" defaultValue="${condition['brSchoolid'] }" displayType="code" style="width:52%;"></gh:brSchoolAutocomplete>
				</li>
				</c:if>	
				<li>
					<label>教室名称：</label><input type="text" id="classroomName" name="classroomName" value="${condition['classroomName']}"/>
				</li>
				<li>
					<label>教室类型：</label><gh:select name="classroomType" value="${condition['classroomType']}" dictionaryCode="CodeClassRoomStyle" style="width:52%;"/>	
				</li>
			</ul>
			<ul class="searchContent">	
				<li>
					<label>排课可用：</label>
					<gh:select name="isUseCourse" value="${condition['isUsecourse']}" dictionaryCode="yesOrNo" style="width:52%;"/>	
				</li>
				<li>
					<label>排考可用：</label><gh:select name="isUseExam" value="${condition['isUseExam']}" dictionaryCode="yesOrNo" style="width:52%;"/>	
				</li>
				<li>
					<label>有无空调：</label><gh:select name="hasAir" value="${condition['hasAir']}" dictionaryCode="yesOrNo" style="width:52%;"/>	
				</li>
				<li>
					<label>状态：</label><gh:select name="status" value="${condition['status']}" dictionaryCode="CodeClassroomStatus" style="width:52%;"/>	
				</li>
			</ul>		
			<div class="subBar">
				<ul>
					<li><div class="buttonActive"><div class="buttonContent"><button type="submit"> 查 询 </button></div></div></li>					
				</ul>
			</div>
		</div>
		</form>
	</div>
	<div class="pageContent">
		<gh:resAuth parentCode="RES_BASEDATA_BUILDING" pageType="sublist"></gh:resAuth>
		<table class="table" layouth="163">
			<thead>
			    <tr>
			    	<th width="3%"><input type="checkbox" name="checkall" id="check_all_classroom" onclick="checkboxAll('#check_all_classroom','resourceid','#classroomBody')"/></th>
			        <th width="10%">教学站</th>
			        <th width="6%">课室号</th>
			        <th width="6%">教室名称</th>
			        <th width="6%">教室类型</th>
			        <th width="5%">楼号</th>
			        <th width="5%">试室号</th>
			        <th width="5%">座位数</th>	
			        <th width="5%">单隔位数</th>		
			        <th width="5%">双隔位数</th>	
			        <th width="5%">排课可用</th>
			        <th width="5%">排考可用</th>
			        <th width="5%">开始时间</th>
			        <th width="5%">结束时间</th>
			        <th width="5%">有无空调</th>
			        <th width="5%">是否直播室</th>	
			        <th width="5%">状态</th>
			        <th width="5%">备注</th>	  
			    </tr>
		   	</thead>
		   	<tbody id="classroomBody">
	     	<c:forEach items="${classroomPage.result}" var="classroom" varStatus="vs">
		        <tr>
		        	<td><input type="checkbox" name="resourceid" value="${classroom.resourceid }" autocomplete="off" /></td>
		        	<td align="left">${classroom.building.branchSchool.unitName }</td>
					<td align="left">${classroom.roomCode }</td>
		            <td align="left">${classroom.classroomName }</td>
		            <td align="left">${ghfn:dictCode2Val('CodeClassRoomStyle',classroom.classroomType ) }</td>
		            <td align="left">${classroom.layerNo }</td>
		            <td align="left">${classroom.unitNo }</td>
		            <td align="left">${classroom.seatNum }</td>
		            <td align="left">${classroom.singleSeatNum }</td>
		            <td align="left">${classroom.doubleSeatNum }</td>
		            <td align="left">${classroom.isUseCourse }</td>
		            <td align="left">${classroom.isUseExam }</td>
		            <td align="left"><fmt:formatDate value="${classroom.startDate }" pattern="yyyy-MM-dd" /></td>
		            <td align="left"><fmt:formatDate value="${classroom.endDate }" pattern="yyyy-MM-dd" /></td>
		            <td align="left">${classroom.hasAir }</td>
		            <td align="left">${ghfn:dictCode2Val('yesOrNo',classroom.isLiving ) }</td>
		            <td align="left">${classroom.status }</td>
		            <td align="left">${classroom.memo }</td>
		        </tr>
	       </c:forEach>
	       </tbody>
	</table>
	 <gh:page page="${classroomPage}" goPageUrl="${baseUrl }/edu3/sysmanager/exclassroom/list.html" pageType="sys" condition="${condition}"/>
	</div>
</div>
</body>
</html>