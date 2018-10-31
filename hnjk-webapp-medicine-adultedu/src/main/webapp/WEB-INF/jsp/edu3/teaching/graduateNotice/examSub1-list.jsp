<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>考试批次</title>
<script type="text/javascript">
	function ctx147(){
		$.pdialog.reload("${baseUrl}/edu3/teaching/graduateNotice/chooseExam.html?type=${type}&batchType=${batchType}&yearId="+$('#yearId58').val()+"&term="+$('#term69').val());
	}
		
	function clickThis15(obj){
		if(obj.checked){
			if(${type eq 'notice'}){
				$("#examSubId").val(obj.value);
				$("#examSubName").val(obj.alt);
			}else{
				$("#examSubId2").val(obj.value);
				$("#examSubName2").val(obj.alt);
			}
		}		
	}
	
	function clickThisRow87(tr){
		var tdEle = tr.children[0].children[0].children[0];
		tdEle.checked = true;
		clickThis15(tdEle);
	}
	
	$(document).ready(function(){
			// 修改高度
			$("#cd_right").css("height",parseInt($(".dialogContent").css("height")));
			
			window.setTimeout(function(){
				var existId = "";
				if(${type eq 'notice'})
					existId = $("#examSubId").val();
				else
					existId = $("#examSubId2").val();
					
				$("#examSubBody input[name=resourceid]").each(function(){
					if(existId==$(this).val()){
						$(this).attr("checked",true);
					}
				});
			},500);
			
		});
</script>
</head>
<body>

	<div class="page">
		<div class="pageHeader">
			<div class="searchBar">
				<ul class="searchContent">
					<li><label>学期：</label> <gh:select name="term69"
							dictionaryCode="CodeTerm" value="${condition['term']}"
							style="width:120px" /></li>
					<li><label>年度：</label> <gh:selectModel id="yearId58"
							name="yearId" bindValue="resourceid" displayValue="yearName"
							style="width:120px"
							modelClass="com.hnjk.edu.basedata.model.YearInfo"
							value="${condition['yearId']}" /></li>
				</ul>
				<div class="subBar">
					<ul>
						<a class="button" href="javascript:;" onclick="ctx147()"><span>查
								询</span></a>&nbsp;&nbsp;
						<a class="button" href="javascript:;"
							onclick="$.pdialog.closeCurrent();"><span>确 定</span></a>
					</ul>
				</div>
			</div>
		</div>
		<div class="pageContent">
			<table class="table" layouth="138">
				<thead>
					<tr>
						<th width="5%">&nbsp;</th>
						<th width="15%">批次名称</th>
						<th width="15%">年度</th>
						<th width="10%">学期</th>
						<th width="10%">预约开始时间</th>
						<th width="10%">预约截止时间</th>
						<!--<th width="10%">添加预约信息截止时间</th>
		            -->
						<th width="10%">预约状态</th>
						<!--<th width="15%">是否发布座位</th>
				-->
					</tr>
				</thead>
				<tbody id="examSubBody">
					<c:forEach items="${examSubList.result}" var="examSub"
						varStatus="vs">
						<tr onclick="clickThisRow87(this)">
							<td><input type="radio" name="resourceid"
								value="${examSub.resourceid }" onclick="clickThis15(this)"
								alt="${examSub.batchName }" /></td>
							<td>${examSub.batchName }</td>
							<td>${examSub.yearInfo.yearName }</td>
							<td>${ghfn:dictCode2Val('CodeTerm',examSub.term)}</td>
							<td>${examSub.startTime }</td>
							<td>${examSub.endTime }</td>
							<!--<td >${examSub.examsubEndTime }</td>
			            -->
							<td>${ghfn:dictCode2Val('CodeExamSubscribeState',examSub.examsubStatus)}</td>
							<!-- 
			            <td >${ghfn:dictCode2Val('yesOrNo',examSub.isseatPublished )}</td> 
			        -->
						</tr>
					</c:forEach>
				</tbody>
			</table>

			<gh:page page="${examSubList}"
				goPageUrl="${baseUrl }/edu3/teaching/graduateNotice/chooseExam.html"
				pageType="sys" targetType="dialog" condition="${condition}" />
		</div>
	</div>
</body>

