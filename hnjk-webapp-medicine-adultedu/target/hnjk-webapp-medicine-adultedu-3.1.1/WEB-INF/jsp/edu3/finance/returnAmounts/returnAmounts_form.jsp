<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
</head>
<body>

	<div class="page">
	<div class="pageContent">	
	<form method="post" action="${baseUrl}/edu3/finance/returnAmount/save.html" class="pageForm" onsubmit="return validateCallback(this);">
		<input type="hidden" name="resourceid" value="${returnAmounts.resourceid }"/>
		<div class="pageFormContent" layoutH="97">
			<table class="form">
				<tr>
					<td width="20%">所属教点：</td>
					<td width="30%"><gh:selectModel name="branchSchoolId" bindValue="resourceid" displayValue="unitName"
							modelClass="com.hnjk.security.model.OrgUnit" value="${returnAmounts.unit.resourceid}" condition="unitType='brSchool'"
							classCss="required" disabled="${brschool?'disabled':'' }" style="width :80%;" />
						<span style="color: red; ">*</span>
						<c:if test="${brschool }"><input type="hidden" name="branchSchoolId" value="${returnAmounts.unit.resourceid}"/></c:if>
					</td>
					<td width="20%">所属年度：</td>
					<td width="30%">
						<gh:selectModel id="returnAmountsPage_yearId" name="yearInfoid" classCss="required"
										modelClass="com.hnjk.edu.basedata.model.YearInfo" bindValue="resourceid"
										value="${returnAmounts.yearInfo.resourceid}" displayValue="yearName" orderBy="firstYear desc" />
					</td>
				</tr>
				<tr>
					<td>次数</td>
					<td>
						<select name="count" style="width: 50%">
							<option value="1">1</option><option value="2" <c:if test="${returnAmounts.count eq '2'}">selected="selected"</c:if>>2</option>
						</select>
					</td>
					<td>已返金额：</td>
					<td>
						<input name="amounts" value="${returnAmounts.amounts}" class="required number">
					</td>

				</tr>
				<tr>
					<td>日期：</td>
					<td><input type="text"  name="operateDate" size="40" value="<fmt:formatDate value="${returnAmounts.operateDate }" pattern="yyyy-MM-dd" />"
							   onFocus="WdatePicker({isShowWeek:true})" class="required"/></td>
				</tr>
			</table>
		</div>
		<div class="formBar">
			<ul>
				<li><div class="buttonActive"><div class="buttonContent">
					<button type="submit">提交</button>
					</div></div></li>
					<li><div class="button"><div class="buttonContent"><button type="button" class="close" onclick="$.pdialog.closeCurrent();">取消</button></div></div></li>
			</ul>
		</div>
	</form>
	</div>
	</div>	
</body>
</html>