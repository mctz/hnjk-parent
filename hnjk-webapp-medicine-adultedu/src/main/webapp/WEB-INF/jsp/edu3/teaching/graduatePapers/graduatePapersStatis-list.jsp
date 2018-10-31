<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>预约统计</title>
<script type="text/javascript">
	function view_orderStu(){
		var url = "${baseUrl}/edu3/teaching/graduatePapers/viewStatis.html";
		if(isCheckOnlyone('resourceid','#gpsBody888')){
			navTab.openTab('RES_TEACHING_THESIS_COUNT_VIEW', url+'?resourceid='+$("#gpsBody888 input[@name='resourceid']:checked").val(), '查看学生信息');
		}
	}
</script>
</head>
<body>

	<div class="page">
		<div class="pageHeader">
			<form onsubmit="return navTabSearch(this);"
				action="${baseUrl }/edu3/teaching/graduatePapers/listStatis.html"
				method="post">
				<div class="searchBar">
					<ul class="searchContent">
						<li><label>批次：</label>
						<gh:selectModel name="batchId" bindValue="resourceid"
								displayValue="batchName" value="${condition['batchId']}"
								modelClass="com.hnjk.edu.teaching.model.ExamSub"
								style="width:130px" condition="batchType='thesis'" /></li>
						<c:if test="${isBrschool != true}">
							<li><label>教学站：</label> <gh:brSchoolAutocomplete
									name="branchSchool" tabindex="1"
									id="graduateno_eiinfo_brSchoolName"
									defaultValue="${condition['branchSchool']}" displayType="code"
									style="width:120px" /></li>
						</c:if>
					</ul>
					<ul class="searchContent">
						<li><label>专业：</label>
						<gh:selectModel name="major" bindValue="resourceid"
								displayValue="majorCodeName" value="${condition['major']}"
								orderBy="majorCode"
								modelClass="com.hnjk.edu.basedata.model.Major"
								style="width:130px" /></li>
						<li><label>层次：</label>
						<gh:selectModel name="classic" bindValue="resourceid"
								displayValue="classicName" value="${condition['classic']}"
								modelClass="com.hnjk.edu.basedata.model.Classic"
								style="width:130px" /></li>
						<div class="buttonActive" style="float: right">
							<div class="buttonContent">
								<button type="submit">查 询</button>
							</div>
						</div>
					</ul>
				</div>
			</form>
		</div>
		<div class="pageContent">
			<gh:resAuth parentCode="RES_TEACHING_THESIS_COUNT" pageType="list"></gh:resAuth>
			<table class="table" layouth="138">
				<thead>
					<tr>
						<th width="5%"><input type="checkbox" name="checkall"
							id="check_all_gps"
							onclick="checkboxAll('#check_all_gps','resourceid','#gpsBody888')" /></th>
						<th width="20%">论文批次</th>
						<th width="20%">教学站</th>
						<th width="20%">专业</th>
						<th width="20%">层次</th>
						<th width="15%">人数</th>
					</tr>
				</thead>
				<tbody id="gpsBody888">
					<c:forEach items="${stuList.result}" var="orderNum" varStatus="vs">
						<tr>
							<td><input type="checkbox" name="resourceid"
								value="${orderNum.batchid},${orderNum.branchschoolid},${orderNum.majorid},${orderNum.classicid}"
								autocomplete="off" /></td>
							<td>${orderNum.batchname }</td>
							<td>${orderNum.unitname}</td>
							<td>${orderNum.majorname }</td>
							<td>${orderNum.classicname }</td>
							<td>${orderNum.ordernum }</td>
						</tr>
					</c:forEach>
				</tbody>
			</table>

			<gh:page page="${stuList}"
				goPageUrl="${baseUrl}/edu3/teaching/graduatePapers/listStatis.html"
				pageType="sys" condition="${condition}" />
		</div>
	</div>

</body>
</html>