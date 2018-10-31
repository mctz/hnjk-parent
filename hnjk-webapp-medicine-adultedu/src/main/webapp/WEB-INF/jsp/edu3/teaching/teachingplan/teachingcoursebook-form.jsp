<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>教学计划 - 课程 - 教材</title>
</head>
<body>
	<script type="text/javascript">
		
	function _saveTeachingCourseBook(){
		var teachingPlanId = $('#_ttt_teachingPlanId').val();
		var teachingCourseId = $('#_ttt_teachingCourseId').val();
		if(!isChecked('resourceid','#_teachingplanCourseBookBody')){
 			alertMsg.warn('请选择一条要操作记录！');
			return false;
 		}
		alertMsg.confirm("确定要添加这些教材吗？", {
				okCall: function(){//执行			
					var res = "";
					var k = 0;
					var num  = $("#_teachingplanCourseBookBody input[name='resourceid']:checked").size();
					$("#_teachingplanCourseBookBody input[@name='resourceid']:checked").each(function(){
	                        res+=$(this).val();
	                        if(k != num -1 ) res += ",";
	                        k++;
	                    })
	                
					$.post('${baseUrl}/edu3/framework/teaching/teachingplan/teachingcourse/rel/save.html',{teachingCourseBookId:res,teachingPlanId:teachingPlanId,teachingCourseId:teachingCourseId}, _teachingPlannavTabAjaxDone, "json");
				}
		});	
	}
	
	function _deleteTeachingCourseBook(){
		var teachingPlanId = $('#_ttt_teachingPlanId').val();
		var teachingCourseId = $('#_ttt_teachingCourseId').val();
		if(!isChecked('teachingPlanCourseBookId','#_deleteteachingplanCourseBookBody')){
 			alertMsg.warn('请选择已使用的教材！');
			return false;
 		}
		alertMsg.confirm("确定要删除这些教材吗？", {
				okCall: function(){//执行			
					var res = "";
					var k = 0;
					var num  = $("#_deleteteachingplanCourseBookBody input[name='teachingPlanCourseBookId']:checked").size();
					$("#_deleteteachingplanCourseBookBody input[@name='teachingPlanCourseBookId']:checked").each(function(){
	                        res+=$(this).val();
	                        if(k != num -1 ) res += ",";
	                        k++;
	                    })
	                
					$.post('${baseUrl}/edu3/framework/teaching/teachingplan/teachingcourse/rel/delete.html',{teachingCourseBookId:res,teachingPlanId:teachingPlanId,teachingCourseId:teachingCourseId}, _teachingPlannavTabAjaxDone, "json");
				}
		});	
	}

	</script>

	<div class="page">
		<div class="pageHeader">
			<div class="pageHeader">
				<form onsubmit="return dialogSearch(this);"
					action="${baseUrl }/edu3/framework/teaching/teachingplan/teachingcourse/rel.html"
					method="post">
					<input type="hidden" name="teachingCourseId"
						id="_ttt_teachingCourseId"
						value="${condition['teachingCourseId'] }" /> <input type="hidden"
						name="teachingPlanId" id="_ttt_teachingPlanId"
						value="${condition['teachingPlanId'] }" />
					<div class="searchBar">
						<c:if test="${condition['sendto'] ne 'unit' }">
							<ul class="searchContent">

								<li><label>课程名称：</label><input type="text"
									name="courseName" value="${condition['courseName']}" /></li>
								<li><label>课程编码：</label><input type="text"
									name="courseCode" value="${condition['courseCode']}" /></li>
								<li><label>教材名称：</label><input type="text" name="bookName"
									value="${condition['bookName']}" /></li>
							</ul>

						</c:if>
						<ul class="searchContent">
							<li><label>教学形式：</label>
							<gh:select name="schoolType" value="${courseBook.schoolType }"
									dictionaryCode="CodeTeachingType" style="width:128px" /></li>
						</ul>
						<div class="subBar">
							<ul>
								<li><div class="buttonActive">
										<div class="buttonContent">
											<button type="submit">查 询</button>
										</div>
									</div></li>
								<li><div class="button">
										<div class="buttonContent">
											<button type="button" onclick="_saveTeachingCourseBook()">
												添加教材</button>
										</div>
									</div></li>
								<li><div class="button">
										<div class="buttonContent">
											<button type="button" onclick="_deleteTeachingCourseBook()">
												删除教材</button>
										</div>
									</div></li>
							</ul>
						</div>
					</div>
				</form>
				<div class="searchBar">
					<table class="list" width="100%">
						<tbody id="_deleteteachingplanCourseBookBody">
							<tr>
								<td width="10%"><b>已使用教材:</b></td>
								<td><c:forEach
										items="${teachingCourse.teachingPlanCourseBooks }" var="book">
										<span style="right-padding: 8px"><input type="checkbox"
											name="teachingPlanCourseBookId" id="teachingPlanCourseBookId"
											value="${book.resourceid}" />${book.courseBook.bookName },</span>
									</c:forEach></td>
							</tr>
						</tbody>
					</table>
				</div>
			</div>
			<div class="pageContent">
				<table class="table" layouth="192">
					<thead>
						<tr>
							<th width="5%"><input type="checkbox" name="checkall"
								id="check_all_tp"
								onclick="checkboxAll('#check_all_tp','resourceid','#tplanBody')" /></th>
							<th width="25%">教材名称</th>
							<th width="10%">教材类型</th>
							<th width="10%">出版社</th>
							<th width="10%">作者</th>
							<th width="10%">书刊号</th>
							<th width="10%">使用状态</th>
							<th width="10%">教学模式</th>
						</tr>
					</thead>
					<tbody id="_teachingplanCourseBookBody">
						<c:forEach items="${coursebookListPage.result}" var="coursebook"
							varStatus="vs">
							<tr>
								<td><input type="checkbox" name="resourceid"
									value="${coursebook.resourceid }" autocomplete="off" /></td>
								<td>${coursebook.bookName }</td>
								<td>${ghfn:dictCode2Val('CodeCourseBookStyle',coursebook.bookType)}</td>
								<td>${coursebook.publishCompany }</td>
								<td>${coursebook.author }</td>
								<td>${coursebook.bookSerial }</td>
								<td>${ghfn:dictCode2Val('CodeCourseState',coursebook.status)}</td>
								<td>${ghfn:dictCode2Val('CodeTeachingType',coursebook.schoolType)}
								</td>
							</tr>
						</c:forEach>
					</tbody>
				</table>
				<gh:page page="${coursebookListPage}"
					goPageUrl="${baseUrl }/edu3/framework/teaching/teachingplan/teachingcourse/rel.html"
					pageType="sys" targetType="dialog" pageNumShown="3"
					condition="${condition}" />
			</div>
		</div>
</body>

<script type="text/javascript">

</script>

</html>