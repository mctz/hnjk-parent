<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>学费统计</title>
</head>
<body>

	<div class="page">
		<div class="pageHeader">
			<form onsubmit="return navTabSearch(this);"
				action="${baseUrl }/edu3/schoolfee/studentfee/listsum.html"
				method="post">
				<div class="searchBar">
					<ul class="searchContent">
						<li><label>年度：</label>
						<gh:selectModel name="year" bindValue="firstYear"
								displayValue="firstYear" value="${condition['year']}"
								modelClass="com.hnjk.edu.basedata.model.YearInfo"
								orderBy="firstYear desc" style="width:120px" /></li>
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

			<table id="stuFeeTab" class="table" layouth="98">
				<thead>
					<tr class="head_bg">
						<th width="20%">学习中心</th>
						<th width="20%">年级</th>
						<th width="20%">人数</th>
						<th width="20%">学费标准</th>
						<th width="20%">总金额</th>
					</tr>
				</thead>
				<tbody id="feeBody">
					<c:forEach items="${feeList}" var="fee" varStatus="vs">
						<tr>
							<td>${fee['CollegeName'] }</td>
							<td>${fee['EntranceFlagName'] }</td>
							<td><q id="stuNum">${fee['StuNum'] }</q></td>
							<td><q id="tuitionFee">${fee['TuitionFee'] }</q></td>
							<td><q id="sumFee">${fee['SumFee'] }</q></td>
						</tr>
					</c:forEach>
					<tr>
						<td>&nbsp;</td>
						<td>合计&nbsp;</td>
						<td><p id="c_stuNum"></p>&nbsp;</td>
						<td><p id="c_tuitionFee"></p>&nbsp;</td>
						<td><p id="c_sumFee"></p>&nbsp;</td>
					</tr>
				</tbody>
			</table>

		</div>
	</div>
</body>
<script type="text/javascript">
	var v_stuNum = 0 ;
	$("q[id='stuNum']").each(function(){
		v_stuNum = v_stuNum + parseInt($(this).text());
	})
	$("#c_stuNum").text(v_stuNum);

	var v_tuitionFee = 0 ;
	$("q[id='tuitionFee']").each(function(){
		v_tuitionFee = v_tuitionFee + parseFloat($(this).text());
	})
	$("#c_tuitionFee").text(v_tuitionFee.toFixed(2));

	var v_sumFee = 0 ;
	$("q[id='sumFee']").each(function(){
		v_sumFee = v_sumFee + parseFloat($(this).text());
	})
	$("#c_sumFee").text(v_sumFee.toFixed(2));
	
	 $(document).ready(function(){           
        _w_table_rowspan("#stuFeeTab",1);   
    });
</script>

</html>