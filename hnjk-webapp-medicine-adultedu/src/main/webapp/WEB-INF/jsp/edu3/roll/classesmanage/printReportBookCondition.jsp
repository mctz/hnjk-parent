<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>打印成绩册</title>
<script type="text/javascript">
		// 打印成绩册
		function reportBookPrint(){
			var classIds = $("#classIds").val();
			var yearId = $("#yearId").val();
			var term = $("#term").val();
			var examType = $("#examType").val();
			var url = "${baseUrl}/edu3/roll/classes/printReportBook-view.html?classIds="+classIds
					+"&yearId="+encodeURIComponent(yearId)+"&term="+term+"&examType="+examType;
			$.pdialog.open(url,"print","打印预览",{mask:true,width:800, height:600});	
		}
	</script>
</head>
<body>
	<div align="center">
		<input type="hidden" id="classIds" name="classIds"
			value="${classIds }" />
		<div style="margin-top: 25px;">
			<label>学年：</label>
			<gh:selectModel id="yearId" name="yearId" bindValue="resourceid" value="${yearId }" displayValue="yearName" orderBy="firstYear"
						modelClass="com.hnjk.edu.basedata.model.YearInfo" condition="firstYear>=${gradeFirstYear }" />
		</div>
		<div style="margin-top: 25px;">
			<label>学期：</label> 
			<gh:select id="term" name="term" dictionaryCode="CodeTerm" value="1" />
		</div>
		<div style="margin-top: 25px;">
			<label>考试类型：</label>
			<gh:select id="examType" name="examType" dictionaryCode="ExamResult" value="N" />
		</div>
		<div style="margin-top: 50px; margin-right: 5px;" align="right">
			<button type="button" onclick="reportBookPrint();"
				style="cursor: pointer;">打印</button>
			<button type="button" class="close"
				onclick="$.pdialog.closeCurrent();" style="cursor: pointer;">取消</button>
		</div>
	</div>
</body>
</html>