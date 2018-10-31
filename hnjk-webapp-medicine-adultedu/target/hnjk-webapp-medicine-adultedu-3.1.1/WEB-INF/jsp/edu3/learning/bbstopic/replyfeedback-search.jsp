<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>高级查询</title>
</head>
<body>
	<script type="text/javascript">
</script>
	<div class="page">
		<div class="pageContent">
			<form method="post"
				action="${baseUrl }/edu3/teacher/feedback/list.html"
				class="pageForm" onsubmit="return navTabSearch(this);">
				<div class="pageFormContent" layoutH="58">
					<div>
						<label>反馈类型：</label>
						<gh:select dictionaryCode="CodeFacebookType"
							value="${condition['facebookType'] }"
							id="teacher_search_facebookType" name="facebookType" />
					</div>
					<div>
						<label>反馈人：</label> <input type="text" name="fillinMan"
							value="${condition['fillinMan'] }" />
					</div>
					<div>
						<label>是否回复：</label> <select name="isAnswered">
							<option value="">请选择</option>
							<option value="0"
								<c:if test="${condition['isAnswered'] eq '0' }">selected="selected"</c:if>>未回复</option>
							<option value="1"
								<c:if test="${condition['isAnswered'] eq '1' }">selected="selected"</c:if>>已回复</option>
						</select>
					</div>
					<div>
						<label>开始时间：</label> <input type="text" name="fillinDateStartStr"
							value="${condition['fillinDateStartStr']}" class="Wdate"
							id="fillinDateStartStrbeginTime1"
							onFocus="WdatePicker({isShowWeek:true,maxDate:'#F{$dp.$D(\'fillinDateStartStrendTime1\')}'})" />

					</div>
					<div>
						<label>结束时间：</label> <input type="text" name="fillinDateEndStr"
							value="${condition['fillinDateEndStr']}" class="Wdate"
							id="fillinDateStartStrendTime1"
							onFocus="WdatePicker({isShowWeek:true,minDate:'#F{$dp.$D(\'fillinDateStartStrbeginTime1\')}'})" />
					</div>
					<div>
						<label>课程：</label>
						<gh:courseAutocomplete name="courseId" tabindex="2"
							id="teacher_search_courseId" />
					</div>
					<div>
						<label>排序条件：</label> <select name="orderBy">
							<option value="">--请选择--</option>
							<option value="facebookType">反馈类型</option>
							<option value="fillinDate">反馈日期</option>
							<option value="lastReplyDate">最后回帖日期</option>
						</select> <label class="radioButton"><input name="orderType"
							type="radio" value="ASC" checked="checked" />顺序</label> <label
							class="radioButton"><input name="orderType" type="radio"
							value="DESC" />倒序</label>
					</div>
				</div>
				<div class="formBar">
					<ul>
						<li><div class="buttonActive">
								<div class="buttonContent">
									<button type="submit">开始检索</button>
								</div>
							</div></li>
						<li><div class="button">
								<div class="buttonContent">
									<button type="reset">清空重输</button>
								</div>
							</div></li>
					</ul>
				</div>
			</form>
		</div>
	</div>
</body>
</html>
