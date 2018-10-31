<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>联动查询列表</title>
<script type="text/javascript">
	$(document).ready(function(){
		linkageQueryBegin();
	});
	
	// 打开页面或者点击查询（即加载页面执行）
   function linkageQueryBegin() {
	   var defaultValue = "${condition['brSchoolId']}";
	   var schoolId = "${linkageQuerySchoolId}";
	   var gradeId = "${condition['gradeId']}";
	   var classicId = "${condition['classicId']}";
	   var teachingType = "${condition['teachingType']}";
	   var majorId = "${condition['majorId']}";
	   var classesId = "${condition['classesId']}";
	   var selectIdsJson = "{unitId:'linkageQuery-list-brSchoolId',gradeId:'linkageQuery-list-gradeId',classicId:'linkageQuery-list-classicId',"
	 							  +"teachingType:'linkageQuery-list-teachingType',majorId:'linkageQuery-list-majorId'}";
	   cascadeQuery("begin", defaultValue, schoolId, gradeId, classicId, teachingType, majorId, classesId, selectIdsJson);
   }
	// 选择教学点
   function linkageQueryUnit() {
	   var defaultValue = $("#linkageQuery-list-brSchoolId").val();
	   var selectIdsJson = "{gradeId:'linkageQuery-list-gradeId',classicId:'linkageQuery-list-classicId',"
			  +"teachingType:'linkageQuery-list-teachingType',majorId:'linkageQuery-list-majorId'}";
	   cascadeQuery("unit", defaultValue, "", "", "", "", "", "", selectIdsJson);
   }
	// 选择年级
   function linkageQueryGrade() {
	   var defaultValue = $("#linkageQuery-list-brSchoolId").val();
	   var gradeId = $("#linkageQuery-list-gradeId").val();
	   var selectIdsJson = "{classicId:'linkageQuery-list-classicId',"
			  +"teachingType:'linkageQuery-list-teachingType',majorId:'linkageQuery-list-majorId'}";
	   cascadeQuery("grade", defaultValue, "", gradeId, "", "", "", "", selectIdsJson);
   }
	// 选择层次
   function linkageQueryClassic() {
	   var defaultValue = $("#linkageQuery-list-brSchoolId").val();
	   var gradeId = $("#linkageQuery-list-gradeId").val();
	   var classicId = $("#linkageQuery-list-classicId").val();
	   var selectIdsJson = "{teachingType:'linkageQuery-list-teachingType',majorId:'linkageQuery-list-majorId'}";
	   cascadeQuery("classic", defaultValue, "", gradeId, classicId, "", "", "", selectIdsJson);
   }
	// 选择学习形式
   function linkageQueryTeachingType() {
	   var defaultValue = $("#linkageQuery-list-brSchoolId").val();
	   var gradeId = $("#linkageQuery-list-gradeId").val();
	   var classicId = $("#linkageQuery-list-classicId").val();
	   var teachingTypeId = $("#linkageQuery-list-teachingType").val();
	   var selectIdsJson = "{majorId:'linkageQuery-list-majorId'}";
	   cascadeQuery("teachingType", defaultValue, "", gradeId, classicId, teachingTypeId, "", "", selectIdsJson);
   }
	
    // 新增
	function addLinkageQuery() {
    	var url = "${baseUrl}/edu3/teaching/linkageQuery/addOrEdit.html";
    	navTab.openTab('RES_TEACHING_LINKAGEQUERY_ADD', url, "新增联动查询");
	}
    
    // 编辑
    function editLinkageQuery() {
    	var linkageQueryArray = new Array();
    	$("#linkageQueryBody input[name=resourceid]:checked").each(function(){
    		linkageQueryArray.push($(this).val());
    	});
    	if(linkageQueryArray.length==0){
    		alertMsg.warn("'请选择一条要操作记录！");
    		return false;
    	} else if(linkageQueryArray.length > 1) {
    		alertMsg.warn("只能选择一条记录操作！");
    		return false;
    	}
    	var url = "${baseUrl}/edu3/teaching/linkageQuery/addOrEdit.html?linkageQueryId="+linkageQueryArray[0];
    	navTab.openTab('RES_TEACHING_LINKAGEQUERY_EDIT', url, "编辑联动查询");
    }
    
    // 删除
    function deleteLinkageQuery() {
    	var linkageQueryArray = new Array();
    	$("#linkageQueryBody input[name=resourceid]:checked").each(function(){
    		linkageQueryArray.push($(this).val());
    	});
    	if(linkageQueryArray.length==0){
    		alertMsg.warn("'请选择一条要操作记录！");
    		return false;
    	} 
    	var url ="${baseUrl}/edu3/teaching/linkageQuery/delete.html";
    	alertMsg.confirm("您确定要删掉所选的记录吗？",{
    		okCall: function(){
    			$.ajax({
    				type:'post',
    				url:url,
    				data:{linkageQueryIds:linkageQueryArray.toString()},
    				dataType:"json",
    				cache:false,
    				error:DWZ.ajaxError,
    				success:function(data){
    					if(data['statusCode']==200){
    						alertMsg.correct(data['message']);
    						var pageNum = "${page.pageNum}";
    						if(pageNum==""){
    							pageNum = "1";
    						}
    						navTabPageBreak({pageNum:pageNum});
    					}else{
    						alertMsg.error(data['message']);
    					}
    				}
    			});
    		}
    	});
    }
    
    // 根据年级同步联动信息（招生专业）
   function syncLinkageQuery() {
    	var selectGradeUrl = "${baseUrl}/edu3/teaching/linkageQuery/selectGrade.html";
	   $.pdialog.open(selectGradeUrl, 'RES_TEACHING_LINKAGEQUERY_SELECTGRADE', '选择年级', {mask:true,width: 300, height: 200});
    }
</script>
</head>
<body>

	<div class="page">
		<div class="pageHeader">
			<form onsubmit="return navTabSearch(this);"
				action="${baseUrl }/edu3/teaching/linkageQuery/list.html"
				method="post">
				<div class="searchBar">
					<ul class="searchContent">
						<li style="width: 360px;"><label>教学点：</label> <span
							sel-id="linkageQuery-list-brSchoolId" sel-name="brSchoolId"
							sel-onchange="linkageQueryUnit()" sel-classs="flexselect"
							> </span></li>
						<li style="width: 200px;">年级： <span
							sel-id="linkageQuery-list-gradeId" sel-name="gradeId"
							sel-onchange="linkageQueryGrade()" sel-style="width: 100px;"></span>
						</li>
						<li style="width: 200px;">层次： <span
							sel-id="linkageQuery-list-classicId" sel-name="classicId"
							sel-onchange="linkageQueryClassic()" sel-style="width: 100px;"></span>
						</li>
						<li style="width: 200px;">学习形式： <span
							sel-id="linkageQuery-list-teachingType" sel-name="teachingType"
							sel-onchange="linkageQueryTeachingType()" sel-style="width: 100px;"></span>
						</li>
					</ul>
					<ul class="searchContent">
						
						<li style="width: 360px;"><label>专业：</label> <span
							sel-id="linkageQuery-list-majorId" sel-name="majorId"
							sel-classs="flexselect"></span></li>
					</ul>
					<div class="buttonActive" style="float: right;">
						<div class="buttonContent">
							<button type="submit">查 询</button>
						</div>
					</div>
				</div>
			</form>
		</div>
		<div class="pageContent">
			<gh:resAuth parentCode="RES_TEACHING_LINKAGEQUERY" pageType="list"></gh:resAuth>
			<table class="table" layouth="163">
				<thead>
					<tr>
						<th style="width: 4%; text-align: center;"><input
							type="checkbox" name="checkall" id="check_all_linkageQuery"
							onclick="checkboxAll('#check_all_linkageQuery','resourceid','#linkageQueryBody')" /></th>
						<th style="width: 35%; text-align: center;">教学点</th>
						<th style="width: 10%; text-align: center;">年级</th>
						<th style="width: 10%; text-align: center;">层次</th>
						<th style="width: 10%; text-align: center;">学习形式</th>
						<th style="width: 35%; text-align: center;">专业</th>
					</tr>
				</thead>
				<tbody id="linkageQueryBody">
					<c:forEach items="${linkageQueryList.result}" var="linkageQuery"
						varStatus="vs">
						<tr>
							<td style="text-align: center;"><input type="checkbox"
								name="resourceid" value="${linkageQuery.resourceid }"
								autocomplete="off" /></td>
							<td title="${linkageQuery.unit.unitName }">${linkageQuery.unit.unitName }</td>
							<td title="${linkageQuery.grade.gradeName }">${linkageQuery.grade.gradeName }</td>
							<td title="${linkageQuery.classic.classicName }">${linkageQuery.classic.classicName }</td>
							<td style="text-align: center;">${ghfn:dictCode2Val('CodeTeachingType',linkageQuery.teachingType )}</td>
							<td title="${linkageQuery.major.majorName }">${linkageQuery.major.majorName }</td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
			<gh:page page="${linkageQueryList}"
				goPageUrl="${baseUrl }/edu3/teaching/linkageQuery/list.html"
				pageType="sys" condition="${condition }" />
		</div>
	</div>

</body>
