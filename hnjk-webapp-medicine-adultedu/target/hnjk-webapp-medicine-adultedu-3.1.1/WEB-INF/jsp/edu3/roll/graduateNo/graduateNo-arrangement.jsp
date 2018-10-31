<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>延迟毕业申请时间设置</title>

<script type="text/javascript">
//增
function newGraduateNoSetting(){
	var url="${baseUrl}/edu3/roll/graduateNoSetting/edit.html";//无resourceid参数则为新建
	navTab.openTab('NOGRADUATE_TIME_NEW',url,'新增申请时间段');
}
//删
function delGraduateNoSetting(){
	var graduateSettingObj = $("#noGraduateSettingBody input[name='graNoSettingId']:checked");
	if(graduateSettingObj.size()<1){
			alertMsg.warn('请至少选择一条要操作记录！');
		return false;
 	}
	alertMsg.confirm("您确定要将选定的延迟毕业申请时间段删除么？", {
		okCall: function(){//执行			
			var res = "";
			var k = 0;
			var num  = graduateSettingObj.size();
			$("#noGraduateSettingBody input[name='graNoSettingId']:checked").each(function(){
                    res+=$(this).val();
                    if(k != num -1 ) res += ",";
                    k++;
            });
			var postUrl2 = "${baseUrl}/edu3/roll/graduateNoSetting/delete.html?resourceid="+res;
			$.ajax({
				type:"post",
				url:postUrl2,
				dataType:"json",
				success:function(data){
						alertMsg.warn(data['result']);
						var url = "${baseUrl}/edu3/roll/graduateNo/noGraduateApplyArrangement.html";
						//刷新页面
						navTab.openTab('NOGRADUATE_TIME', url, '延迟毕业申请时间段');
				}
			});
		}
	});	
}
//改
function modGraduateNoSetting(){
	var graduateSettingObj = $("#noGraduateSettingBody input[name='graNoSettingId']:checked");
	if(graduateSettingObj.size()>1||graduateSettingObj.size()<1){
		alertMsg.warn("请只选择一条记录!");
		return false;
	}
	var url="${baseUrl}/edu3/roll/graduateNoSetting/edit.html?resourceid="+graduateSettingObj.val();//无resourceid参数则为新建
	navTab.openTab('NOGRADUATE_TIME_MOD',url,'修改申请时间段');
}
</script>
</head>
<body>
	<div class="page">
		<div class="pageHeader">
			<form id="yearInfoSettingSearchForm"
				onsubmit="return navTabSearch(this);"
				action="${baseUrl }/edu3/roll/graduateNo/noGraduateApplyArrangement.html"
				method="post">
				<div class="searchBar">
					<ul class="searchContent">
						<li><label>年度：</label>
						<gh:selectModel name="yearInfo" bindValue="resourceid"
								displayValue="yearName" style="width:50%"
								modelClass="com.hnjk.edu.basedata.model.YearInfo"
								value="${condition['yearInfo']}" /></li>
						<li><label>学期：</label> <gh:select name="term"
								value="${condition['term']}" dictionaryCode="CodeTerm" /></li>
						<li><label>开始日期：</label><input type="text"
							value="${condition['startDate'] }" name="startDate"
							readonly="readonly" onfocus="WdatePicker({isShowWeek:true})" /></li>
						<li><label>截至日期：</label><input type="text"
							value="${condition['endDate'] }" name="endDate"
							readonly="readonly" onfocus="WdatePicker({isShowWeek:true})" /></li>
						<li><label>自动恢复日期：</label><input type="text"
							value="${condition['revokeDate'] }" name="revokeDate"
							readonly="readonly" onfocus="WdatePicker({isShowWeek:true})" /></li>
					</ul>
					<div class="buttonActive" style="float: right">
						<div class="buttonContent">
							<button type="submit">查 询</button>
						</div>
					</div>

				</div>
			</form>
		</div>
		<div class="pageContent">
			<gh:resAuth parentCode="RES_TEACHING_THESIS_NOGRADUATE"
				pageType="sublist"></gh:resAuth>
			<table class="table" layouth="138">
				<thead>
					<tr>
						<th width="3%"><input type="checkbox" name="checkall"
							id="check_all_yearInfoSetting"
							onclick="checkboxAll('#check_all_yearInfoSetting','graNoSettingId','#noGraduateSettingBody')" /></th>
						<th width="10%">年度</th>
						<th width="5%">学期</th>
						<th width="20%">开始时间</th>
						<th width="20%">结束时间</th>
						<th width="20%">自动恢复时间</th>
						<th width="22%"></th>
					</tr>
				</thead>
				<tbody id="noGraduateSettingBody">
					<c:forEach items="${page.result}" var="setting" varStatus="vs">
						<tr>
							<td><input type="checkbox" name="graNoSettingId"
								value="${setting.resourceid }" autocomplete="off" /></td>
							<td>${setting.yearInfo.yearName }</td>
							<td>${ghfn:dictCode2Val('CodeTerm',setting.term) }</td>
							<td><fmt:formatDate value="${setting.startDate}"
									pattern="yyyy-MM-dd" /></td>
							<td><fmt:formatDate value="${setting.endDate}"
									pattern="yyyy-MM-dd" /></td>
							<td><fmt:formatDate value="${setting.revokeDate}"
									pattern="yyyy-MM-dd" /></td>
							<td></td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
			<gh:page page="${page}"
				goPageUrl="${baseUrl }/edu3/roll/graduateNo/noGraduateApplyArrangement.html"
				pageType="sys" condition="${condition}" />
		</div>

	</div>

</body>
</html>