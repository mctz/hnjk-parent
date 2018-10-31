<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>选择模版导出</title>
</head>
<script type="text/javascript">
	function downloadExcelModelFile() {
	    var brSchoolid = "${brSchoolid}";
        var gradeid = "${gradeid}";
        var classicid = "${classicid}";
        var teachingType = "${teachingType}";
        var majorid = "${majorid}";
        var classesid = "${classesid}";
        var term = "${term}";
        var courseId = "${courseId}";
        var status = "${status}";
        var couId = "${couId}";
        var showList = "${showList}";
        var excelModel = document.getElementById("excelModel").value;
        var url = "${baseUrl}/edu3/teaching/teachingplancourse/courseArrangementModelExp.html?excelModel="+excelModel;
		if(brSchoolid!="" || couId!=""){
		    url += "&couId="+couId;
		    url += "&brSchoolid="+brSchoolid;
            url += "&gradeid="+gradeid;
            url += "&classicid="+classicid;
            url += "&teachingType="+teachingType;
            url += "&majorid="+majorid;
            url += "&classesid="+classesid;
            url += "&term="+term;
            url += "&courseId="+courseId;
            url += "&status="+status;
            url += "&couId="+couId;
            url += "&showList="+showList;
        }
        downloadFileByIframe(url,'excelModelIframe');
        $.pdialog.closeCurrent();

	}

</script>
<body>
		<div style="margin-top: 20px;">
			<label>请选择要导出的模版：</label>
			<br><br>
			<select id="excelModel" name="excelModel" style="width: 120px;">
				<option>请选择</option>
				<option value="arrangingInfoVo">排课模版</option>
				<option value="examinationInfoVo">排考模版</option>
			</select>
		</div>
		<div style="margin-top: 50px; margin-right: 5px;" align="right">
			<button type="button" onclick="downloadExcelModelFile()">确 定</button>
			<button type="button" class="close" onclick="$.pdialog.closeCurrent();">取 消</button>
		</div>
</body>
</html>