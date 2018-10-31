<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>查看申报专业信息</title>
<gh:loadCom components="tab,validator,ajaxForm,blockUI,datePicker" />
<script type="text/javascript">
	$().ajaxStop($.unblockUI);
	
	$(function() {
         $('#container-1').tabs();
             });
     
	}
 </script>
</head>
<body>
	<div style="height: 500px">
		<table border="1" class="search">
			<tr>
				<td align="left" id="blue"><div align="left">招生管理
						&gt;&gt; 招生专业申报 &gt;&gt; 查看申报信息</div></td>
			</tr>
		</table>
		<div id="container-1" style="width: 98%">
			<div id="fragment-1">
				<input type="hidden" name="resourceid"
					value="${schoolmajor.resourceid }" />
				<table class="form">
					<tr>
						<td>招生批次:</td>
						<td>${schoolmajor.recruitMajor.recruitPlan.recruitPlanname }
						</td>
						<td>招生专业名称:</td>
						<td>${schoolmajor.recruitMajor.recruitMajorName }</td>
					</tr>
					<tr>
						<td>教学站:</td>
						<td>${schoolmajor.branchSchool.unitName }</td>
						<td>学制:</td>
						<td>${schoolmajor.recruitMajor.studyperiod }</td>
					</tr>
					<tr>
						<td>上限人数:</td>
						<td>${schoolmajor.recruitMajor.limitNum }</td>
						<td>下限人数:</td>
						<td>${schoolmajor.recruitMajor.lowerNum }</td>
					</tr>
					<tr>
						<td>学费:</td>
						<td>${schoolmajor.tuitionFee }</td>
						<td>文号:</td>
						<td>${schoolmajor.documentCode }</td>
					</tr>
					<tr>
						<td>录取分数线:</td>
						<td>${schoolmajor.recruitMajor.matriculateLine }</td>
						<td>审批状态:</td>
						<td>${ghfn:dictCode2Val('CodeCheckStatus',schoolmajor.status)}</td>
					</tr>
					<tr>
						<td>申报人:</td>
						<td>${schoolmajor.fillinMan }</td>
						<td>申报日期:</td>
						<td>${schoolmajor.fillinDate }</td>
					</tr>
					<tr>
						<td>审批人:</td>
						<td>${schoolmajor.auditman }</td>
						<td>审批日期:</td>
						<td>${schoolmajor.auditDate }</td>
					</tr>
					<tr>
						<td>备注:</td>
						<td colspan="3"><textarea name="memo" cols="60" rows="5"
								readonly="true">${schoolmajor.memo }</textarea></td>
					</tr>
				</table>

			</div>
		</div>
		<div style="text-align: center">
			<input type="button" value=" 返 回 " onclick="history.go(-1);"
				class="b1" onmouseover="this.className='b2'"
				onmouseout="this.className='b1'" />
		</div>
	</div>
</body>
