<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>录取查询</title>
<script type="text/javascript">
	 jQuery(document).ready(function(){	
	    /* jQuery("#matriculateQuerySearchForm #_matriculatequery_recruitPlan").change(function(){
	    	 var url    = "${baseUrl}/edu3/recruit/dynamicmenu/recruitplan-recruitmajor.html";
	    	    var planid = jQuery(this).val();
	    	    jQuery.post(url,{recruitPlan:planid},function(myJSON){
	    			jQuery("#matriculateQuerySearchForm #_matriculatequery_recruitMajor").html("");
	    			var selectObj="<option value=''>请选择</option>";
	    			for (var i = 0; i < myJSON.length; i++) {
	    			  selectObj += '<option value="' + myJSON[i].key + '" title="'+myJSON[i].value+'">' + myJSON[i].name + '</option>';    
	    			}
	    			  jQuery("#matriculateQuerySearchForm #_matriculatequery_recruitMajor").html(selectObj);
	    		},"json");
			});	 */
	    matriculate_listQueryBegin();
	});
	 
	//打开页面或者点击查询（即加载页面执行）
		function matriculate_listQueryBegin() {
			var defaultValue = "${condition['branchSchool']}";
			var schoolId = "${linkageQuerySchoolId}";
			
			var teachingType = "${condition['teachingType']}";
			var majorId = "${condition['major']}";
			
			var selectIdsJson = "{unitId:'_matriculatequery_brSchoolId',teachingType:'_matriculatequery_teachingType',majorId:'_matriculatequery_recruitMajor'}";
			cascadeQuery("begin", defaultValue, schoolId, "", "",teachingType, majorId, "", selectIdsJson);
		}

		// 选择教学点
		function matriculate_listQueryUnit() {
			var defaultValue = $("#_matriculatequery_brSchoolId").val();
			var selectIdsJson = "{teachingType:'_matriculatequery_teachingType',majorId:'_matriculatequery_recruitMajor'}";
			cascadeQuery("unit", defaultValue, "", "", "", "", "", "",selectIdsJson);
		}

		// 选择学习形式
		function matriculate_listQueryTeachingType() {
			var defaultValue = $("#_matriculatequery_brSchoolId").val();

			var teachingTypeId = $("#_matriculatequery_teachingType").val();
			var selectIdsJson = "{majorId:'_matriculatequery_recruitMajor'}";
			cascadeQuery("teachingType", defaultValue, "", "", "",teachingTypeId, "", "", selectIdsJson);
		}

	 function openEnrol(){
		 window.open("${baseUrl}/cx.html");   
	 }
	//导出
	function exportMatriculate(){			
			var branchSchool = $("#_matriculatequery_brSchoolId").val();
			var recruitPlan = $("#_matriculatequery_recruitPlan").val();
			var major= $("#_matriculatequery_recruitMajor").val();
			if(recruitPlan==""){
				alertMsg.warn("请选择一个招生计划！");
				return false;
			}
			//以免每次点击下载都创建一个iFrame，把上次创建的删除
			$('#frameForDownload_matriculate').remove();
			var iframe = document.createElement("iframe");
			iframe.id = "frameForDownload_matriculate";
			iframe.src = "${baseUrl}/edu3/recruit/matriculate/matriculatequery-export.html?branchSchool="+branchSchool+"&recruitPlan="+recruitPlan+"&major="+major;
			iframe.style.display = "none";
			//创建完成之后，添加到body中
			document.body.appendChild(iframe);
			//window.location.href=url+"?branchSchool="+branchSchool+"&recruitPlan="+recruitPlan+"&recruitMajor="+recruitMajor+"&name="+name+"&examCertificateNo="+examCertificateNo+"&certNum="+certNum;
		}
</script>
</head>
<body>
	<div class="page">
		<div class="pageHeader">
			<form onsubmit="return navTabSearch(this);"
				id="matriculateQuerySearchForm"
				action="${baseUrl }/edu3/recruit/matriculate/matriculatequery-list.html"
				method="post">
				<div class="searchBar">
					<ul class="searchContent">
						<c:if test="${empty condition['brshSchool']}">
							<li class="custom-li"><label>学习中心：</label> <span
								sel-id="_matriculatequery_brSchoolId" sel-name="branchSchool"
								sel-onchange="matriculate_listQueryUnit()"
								sel-classs="flexselect" ></span></li>
						</c:if>
						<li><label>招生计划：</label> <gh:recruitPlanAutocomplete
								name="recruitPlan" tabindex="2"
								id="_matriculatequery_recruitPlan"
								value="${condition['recruitPlan']}" style="width:120px" /></li>
						<li><label>办学模式：</label> <span
							sel-id="_matriculatequery_teachingType" sel-name="teachingType"
							sel-onchange="matriculate_listQueryTeachingType()"
							dictionaryCode="CodeTeachingType" sel-style="width: 120px"></span>
						</li>
						
					</ul>
					<ul class="searchContent">
						<li class="custom-li"><label>招生专业：</label> <select class="flexselect"
							id="_matriculatequery_recruitMajor" name="major" tabindex=1
							style="width: 240px;">${majorOption}</select> <!--  <span sel-id="_matriculatequery_recruitMajor" sel-name="major" sel-onchange="matriculate_listQueryMajor()" sel-classs="flexselect" sel-style="width: 120px"></span> -->

						</li>
						<li><label> 姓名：</label><input type="text" id="name"
							name="name" value="${condition['name']}" /></li>
						<li><label>准考证号：</label><input type="text"
							id="examCertificateNo" name="examCertificateNo"
							value="${condition['examCertificateNo']}" /></li>
						<li><label>证件号码：</label><input type="text" name="certNum"
							value="${condition['certNum']}" /></li>
					</ul>

					<div class="subBar">
						<ul>
							<li><div class="buttonActive">
									<div class="buttonContent">
										<button type="submit">查询</button>
									</div>
								</div></li>
						</ul>
					</div>
				</div>
			</form>
		</div>
		<div class="pageContent">
			<div class="panelBar">
				<ul class="toolBar">
					<gh:resAuth parentCode="RES_MATRICALATE_MATRICULATEQUERY_LIST"
						pageType="list"></gh:resAuth>
				</ul>
			</div>
			<table class="table" layouth="162">
				<thead>
					<tr>
						<th width="10%">招生计划</th>
						<th width="10%">学习中心</th>
						<th width="10%">招生专业</th>
						<th width="10%">准考证号</th>
						<th width="5%">姓名</th>
						<th width="5%">性别</th>
						<th width="10%">证件号码</th>
						<th width="10%">学号</th>
						<th width="10%">注册号</th>
						<th width="10%">考生号</th>
						<th width="5%">总分</th>
						<th width="5%">录取</th>
					</tr>
				</thead>
				<tbody>
					<c:forEach items="${eiList.result}" var="enrolleeinfo"
						varStatus="vs">
						<tr>
							<td>${enrolleeinfo.recruitMajor.recruitPlan.recruitPlanname}</td>
							<td>${enrolleeinfo.branchSchool.unitName }</td>
							<td>${enrolleeinfo.recruitMajor.recruitMajorName }</td>
							<td>${enrolleeinfo.examCertificateNo }</td>
							<td>${enrolleeinfo.studentBaseInfo.name }</td>
							<td>${ghfn:dictCode2Val('CodeSex',enrolleeinfo.studentBaseInfo.gender) }</td>
							<td>${enrolleeinfo.studentBaseInfo.certNum }</td>
							<td>${enrolleeinfo.matriculateNoticeNo }</td>
							<td>${enrolleeinfo.registorNo}</td>
							<td>${enrolleeinfo.enrolleeCode }</td>
							<td>${enrolleeinfo.totalPoint }</td>
							<td>${ghfn:dictCode2Val('yesOrNo',enrolleeinfo.isMatriculate) }</td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
			<%-- <gh:page page="${eiList}"
				goPageUrl="${baseUrl }/edu3/recruit/matriculate/matriculatequery-list.html"
				pageType="sys" condition="${condition}" /> --%>
		</div>
		<div  class="pageContent" style="position: absolute;bottom: 0px;width: 100%">
			<gh:page page="${eiList}"
				goPageUrl="${baseUrl }/edu3/recruit/matriculate/matriculatequery-list.html"
				pageType="sys" condition="${condition}" /></div>
	</div>
</body>
</html>
