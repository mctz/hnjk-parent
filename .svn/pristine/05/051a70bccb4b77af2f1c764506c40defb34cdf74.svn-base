<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>教材信息</title>
</head>
<script type="text/javascript">
	//新增
	function addTextbook() {
		navTab.openTab('navTab',
				'${baseUrl}/edu3/sysmanager/textbook/edit.html', '新增教材内容');
	}

	//修改
	function editTextbook() {
		var url = "${baseUrl}/edu3/sysmanager/textbook/edit.html";
		if (isCheckOnlyone('resourceid', '#TextBookBody')) {
			navTab.openTab('_blank', url
					+ '?resourceid='
					+ $("#TextBookBody input[@name='resourceid']:checked")
							.val(), '编辑教材信息');
		}
	}

	//删除
	function deleteTextbook() {
		pageBarHandle("您确定要删除这些记录吗？",
				"${baseUrl}/edu3/sysmanager/textbook/delete.html",
				"#TextBookBody");
	}

	function settingTextbook() {
		var url = "${baseUrl}/edu3/sysmanager/textbook/setting.html";

		if (!isChecked('resourceid', '#TextBookBody')) {
			alertMsg.warn('请选择一条要操作记录！');
			return false;
		}

		var res = "";
		var k = 0;
		var num = $("#TextBookBody input[name='resourceid']:checked")
				.size();
		$("#TextBookBody input[@name='resourceid']:checked").each(
				function() {
					res += $(this).val();
					if (k != num - 1 && res != '')
						res += ",";
					k++;
				})
		if (res == '') {
			alertMsg.warn("没有设置任课老师的课程，无法修改问卷有效时间！请先设置课程任课老师后再试");
			return false;
		}
		alertMsg.confirm("选项：<select id='selectYesOrNo'>  <option value='Y' >是</option> <option value='N'>否 </option> </select>",
				{okCall:function (){
			var flag = $("#selectYesOrNo option:selected").val();
			if(flag!=""){					
				$.post(url,{resourceid:res,flag:flag},navTabAjaxDone,"json");
			} else {
				//重新选择时间
			}
		},okName:'设置'});
		
	}
</script>
<body>
	<div class="page">
		<div class="pageHeader">
			<form onsubmit="return navTabSearch(this);"
				action="${baseUrl }/edu3/sysmanager/textbook/list.html"
				method="post">
				<div class="searchBar">
					<ul class="searchContent">
						<li class="custom-li"><label>课程：</label> <gh:courseAutocomplete name="courseid"
								id="textBook_courseid" tabindex="1" displayType="code"
								style="width:200px" classCss="required"
								value="${condition['courseid']}" /></li>
						<li><label>是否使用：</label> <gh:select name="isUsed"
								dictionaryCode="yesOrNo" value="${condition['isUsed']}"
								style="width:125px" /></li>

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
			<gh:resAuth parentCode="RES_BASEDATA_TEXTBOOK" pageType="list"></gh:resAuth>
			<table class="table" layouth="138">
				<thead>
					<tr>
						<th width="3%">
							<input type="checkbox" name="checkall" id="check_all_textBook"/>
						</th>
						<th width="10%">课程名称</th>
						<th width="10%">课程编码</th>
						<th width="15%">教材名称</th>
						<th width="15%">书号</th>
						<th width="15%">出版社</th>
						<th width="15%">主编</th>
						<th width="10%">单价</th>
						<th width="7%">是否使用</th>
						<th width="10%">最后更新时间</th>
					</tr>
				</thead>
				<tbody id="TextBookBody">
					<c:forEach items="${page.result}" var="tb" varStatus="vs">
						<tr>
							<td><input type="checkbox" name="resourceid"
								value="${tb.resourceid }" autocomplete="off" /></td>
							<td>${tb.course.courseName }</td>
							<td>${tb.course.courseCode }</td>
							<td>${tb.bookName }</td>
							<td>${tb.bookSerial }</td>
							<td>${tb.press }</td>
							<td>${tb.editor }</td>
							<td>${tb.price }</td>
							<td>${ghfn:dictCode2Val('yesOrNo',tb.isUsed) }</td>
							<td>${tb.updatedate }</td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
			<gh:page page="${page}"
				goPageUrl="${baseUrl }/edu3/sysmanager/textbook/list.html"
				pageType="sys" condition="${condition}" />
		</div>
	</div>
</body>
</html>