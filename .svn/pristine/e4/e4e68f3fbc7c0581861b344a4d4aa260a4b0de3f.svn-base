<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>学费查询</title>
</head>
<body>
	<script type="text/javascript">
		function _stufeeSearchValidate(obj){
			if($('#stufeeSearchForm #_studentName').val()!="" || $('#stufeeSearchForm #_studentNo').val()!=""){
				return navTabSearch(obj);				
			}else{
				alertMsg.warn('请输入学生学号或姓名！');
				return false;
			}
			
		}
	</script>
	<div class="page">
		<div class="pageHeader">
			<form onsubmit="return _stufeeSearchValidate(this);"
				id="stufeeSearchForm"
				action="${baseUrl }/edu3/schoolfee/studentfee/list.html"
				method="post">
				<div class="searchBar">
					<ul class="searchContent">
						<li><label>姓名：</label><input type="text" id="_studentName"
							name="studentName" value="${condition['studentName']}" /></li>
						<li><label>学号：</label><input type="text" id="_studentNo"
							name="studyNo" value="${condition['studyNo']}" /></li>
						<li><span class="tips">请输入学生学号或姓名查询.</span></li>
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

			<table class="table" layouth="111">
				<thead>
					<tr class="head_bg">
						<th width="5%"><input type="checkbox" name="checkall"
							id="check_all_fee"
							onclick="checkboxAll('#check_all_fee','resourceid','#feeBody')" /></th>
						<th width="9%">学号</th>
						<th width="9%">姓名</th>
						<th width="5%">性别</th>
						<th width="9%">专业</th>
						<th width="9%">培养层次</th>
						<th width="9%">学习方式</th>
						<th width="11%">学习中心</th>
						<th width="11%">班级</th>
						<th width="5%">年度</th>
						<th width="9%">学费金额</th>
						<th width="9%">缴费金额</th>
					</tr>
				</thead>
				<tbody id="feeBody">
					<c:set var="totalTuitionFee" value="0.0" />
					<c:set var="totalOccurAmt" value="0.0" />
					<c:forEach items="${feeList}" var="fee" varStatus="vs">
						<c:set var="totalTuitionFee"
							value="${totalTuitionFee + fee['TuitionFee'] }" />
						<c:set var="totalOccurAmt"
							value="${totalOccurAmt + fee['OccurAmt'] }" />
						<tr>
							<td><input type="checkbox" name="resourceid"
								value="${fee['StudentID'] }" autocomplete="off" /></td>
							<td>${fee['StudentID'] }</td>
							<td>${fee['StudentName'] }</td>
							<td>${ghfn:dictCode2Val('CodeSex',fee['Sex']) }</td>
							<td>${fee['ProfessionName'] }</td>
							<td>${fee['LevelName'] }</td>
							<td>${fee['FormalName'] }</td>
							<td>${fee['CollegeName'] }</td>
							<td>${fee['ClassName'] }</td>
							<td>${fee['ChargeYear'] }</td>
							<td>${fee['TuitionFee'] }</td>
							<td>${fee['OccurAmt'] }</td>
						</tr>
					</c:forEach>
					<c:if test="${not empty feeList }">
						<tr>
							<td></td>
							<td class="right">合计：</td>
							<td colspan="8"></td>
							<td>${totalTuitionFee }</td>
							<td>${totalOccurAmt }</td>
						</tr>
					</c:if>
				</tbody>
			</table>

			<gh:page page="${objPage}"
				goPageUrl="${baseUrl}/edu3/schoolfee/studentfee/list.html"
				pageType="sys" condition="${condition }" />
		</div>
	</div>
</body>
</html>