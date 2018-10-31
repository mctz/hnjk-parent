<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>年级预约学习权限设置</title>


</head>
<body>
	<div class="page">
		<div class="pageHeader">
			<form id="yearInfoSettingSearchForm"
				onsubmit="return navTabSearch(this);"
				action="${baseUrl }/edu3/teaching/courseorder/gradelearnbook-list.html"
				method="post">
				<div class="searchBar">
					<ul class="searchContent">
						<li><label>年度：</label>
						<gh:selectModel name="yearInfo" bindValue="resourceid"
								displayValue="yearName" style="width:50%"
								modelClass="com.hnjk.edu.basedata.model.YearInfo"
								value="${condition['yearInfo']}" /></li>
						<li><label>开始预约：</label><input type="text"
							value="${condition['startDate'] }" name="startDate"
							readonly="readonly" onfocus="WdatePicker({isShowWeek:true})" /></li>
						<li><label>截止预约：</label><input type="text"
							value="${condition['endDate'] }" name="endDate"
							readonly="readonly" onfocus="WdatePicker({isShowWeek:true})" /></li>
						<li><label>是否开放：</label><select name="isOpened">
								<option value="">请选择</option>
								<option value="Y"
									<c:if test="${condition['isOpened'] eq 'Y'}"> selected="selected" </c:if>>是</option>
								<option value="N"
									<c:if test="${condition['isOpened'] eq 'N'}"> selected="selected" </c:if>>否</option>
						</select></li>
						<li><label>学期：</label> <gh:select name="term"
								value="${condition['term']}" dictionaryCode="CodeTerm" /></li>
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
			<gh:resAuth parentCode="RES_TEACHING_BOOKING_GRADE_CONFIG"
				pageType="list"></gh:resAuth>
			<table class="table" layouth="138">
				<thead>
					<tr>
						<th width="5%"><input type="checkbox" name="checkall"
							id="check_all_yearInfoSetting"
							onclick="checkboxAll('#check_all_yearInfoSetting','resourceid','#yearInfoSettingBody')" /></th>
						<th width="10%">年度</th>
						<th width="10%">学期</th>
						<th width="20%">开始时间</th>
						<th width="20%">结束时间</th>
						<th width="15%">预约学习状态</th>
						<th width="10%">允许预约科目数</th>
						<th width="10%">操作</th>
					</tr>
				</thead>
				<tbody id="yearInfoSettingBody">
					<c:forEach items="${page.result}" var="setting" varStatus="vs">
						<tr>
							<td><input type="checkbox" name="resourceid"
								value="${setting.resourceid }" autocomplete="off" /></td>
							<td>${setting.yearInfo.yearName }</td>
							<td>${ghfn:dictCode2Val('CodeTerm',setting.term) }</td>
							<td><fmt:formatDate value="${setting.startDate}"
									pattern="yyyy-MM-dd HH:mm:ss" /></td>
							<td><fmt:formatDate value="${setting.endDate}"
									pattern="yyyy-MM-dd HH:mm:ss" /></td>
							<td><c:if test="${setting.isOpened eq 'Y' }">开放</c:if> <c:if
									test="${setting.isOpened eq 'N' }">屏蔽</c:if></td>
							<td>${setting.limitOrderNum }</td>
							<td><a href="javascript:void(0)"
								onclick="editGradeBooking('${setting.resourceid }')">查看</a></td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
			<gh:page page="${page}"
				goPageUrl="${baseUrl }/edu3/teaching/courseorder/gradelearnbook-list.html"
				pageType="sys" condition="${condition}" />
		</div>

	</div>
	<script type="text/javascript">
	function  editGradeBooking(id){//编辑
		var url = "${baseUrl}/edu3/teaching/courseorder/gradelearnbook-edit.html?resourceid="+id
		navTab.openTab("RES_TEACHING_BOOKING_EDIT_GRADE",url,"编辑年级预约权限");
	}
	function  delGradeBooking(){//删除
		var idArray = new Array();
		var ids = "";
		jQuery("input[name='resourceid']:checkbox").each(function(index){
			if(jQuery(this).attr("checked")){
				ids = ids + ","+jQuery(this).val() ;
				idArray.push(index);
			}
		})
		if(""==ids){
			alertMsg.info('请选择要删除的记录!')
			return ;
		}else{
			var url = "${baseUrl}/edu3/teaching/courseorder/gradelearnbook-del.html?resourceIds="+ids.substring(1);
			alertMsg.confirm("确认要删除吗！", {
                okCall: function(){
				    $.ajax({
							type:"post",
							url:url,
							success:function(msg){          	   		
				         		 alertMsg.correct(msg);
				         		 navTab.reload("${baseUrl}/edu3/teaching/courseorder/gradelearnbook-list.html",$("#searchForm").serializeArray());
				          	}      
					});
                }
			});
		}
	}
	function addGradeBooking(){//增加
		var url = "${baseUrl}/edu3/teaching/courseorder/gradelearnbook-edit.html";
		navTab.openTab("RES_TEACHING_BOOKING_ADD_GRADE",url,"新增年级预约权限");
	}
	
	function gradeLearnBookDisenable(){//屏蔽
		
		var idArray = new Array();
		var ids = "";
		jQuery("#yearInfoSettingBody input[name='resourceid']:checkbox").each(function(index){
			if(jQuery(this).attr("checked")){
				ids = ids + ","+jQuery(this).val() ;
				idArray.push(index);
			}
		})
		if(""==ids){
			alertMsg.info('请选择要屏蔽预约权限的年级!')
			return ;
		}else{
			var url = "${baseUrl}/edu3/teaching/courseorder/bookingStatus.html?orderCourseStatusConfigType=yearInfo&isOpen=N&settingId="+ids.substring(1);
			$.ajax({
				type:"post",
				url:url,
				success:function(msg){          	   		
	         		 alertMsg.info(msg);
	         		 navTab.reload("${baseUrl}/edu3/teaching/courseorder/gradelearnbook-list.html",$("#searchForm").serializeArray());
	          	}      
			});
		}
	}
	function gradeLearnBookEnable(){//开放
	
		if(isCheckOnlyone('resourceid','#yearInfoSettingBody')){
			var settingId = jQuery("#yearInfoSettingBody input[name='resourceid']:checked").val();
			var url = "${baseUrl}/edu3/teaching/courseorder/bookingStatus.html?orderCourseStatusConfigType=yearInfo&isOpen=Y&settingId="+settingId;
			$.ajax({
				type:"post",
				url:url,
				success:function(msg){          	   		
	         		 alertMsg.info(msg);
	         		 navTab.reload($("#yearInfoSettingSearchForm").attr("action"),$("#yearInfoSettingSearchForm").serializeArray());
	          	}      
			});
		}
	}
</script>
</body>
</html>