<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>考生转点记录</title>
<script type="text/javascript">
	// 打印转点记录  RES_RECRUIT_EXAMINEECHANGEINFO_PRINT
	function printChangeSchoolRecord() {
		var isBrschool = "${isBrschool}";
		if(isBrschool=='false'){
			alertMsg.warn("对不起，您没有权限操作此功能!");
			return false;
		}
		var examineeChangeInfoIds = new Array();
		$("#examineeChangeInfoBody input[name=resourceid]:checked").each(function(){
			examineeChangeInfoIds.push($(this).val());
		});
		var url = "${baseUrl}/edu3/recruit/examineeChangeInfo/printView.html?";
		var param = "";
		if(examineeChangeInfoIds.length>0){
			alertMsg.confirm("你确定打印这些转点记录？", {
				okCall:function(){
					param = "type=check&examineeChangeInfoIds="+examineeChangeInfoIds.toString();
					$.pdialog.open(url+param,"RES_RECRUIT_EXAMINEECHANGEINFO_PRINT","转点记录打印预览",{mask:true,width:800, height:600});
				}
			});
		} else {
			alertMsg.confirm("你确定按查询条件打印转点记录？", {
				okCall:function(){
					param = "type=select&"+$("#examineeChangeInfoForm").serialize();
					$.pdialog.open(url+param,"RES_RECRUIT_EXAMINEECHANGEINFO_PRINT","转点记录打印预览",{mask:true,width:800, height:600});
				}
			});
		}
	}
</script>
</head>
<body>
	<div class="page">
		<div class="pageHeader">
			<form id="examineeChangeInfoForm"
				onsubmit="return navTabSearch(this);"
				action="${baseUrl }/edu3/recruit/examineeChangeInfo/list.html"
				method="post">
				<div class="searchBar">
					<ul class="searchContent">
						<li><label>招生批次：</label> <gh:recruitPlanAutocomplete
								name="recruitPlanId" tabindex="1"
								id="examineeChangeInfo_recruitPlanId"
								value="${condition['recruitPlanId']}" style="width:55%;" /></li>
						<c:choose>
							<c:when test="${!isBrschool }">
								<li><label>转出教学站：</label> <gh:brSchoolAutocomplete
										name="rolloutSchool" id="examineeChangeInfo_rolloutSchoolName"
										tabindex="1" style="width:55%;"
										defaultValue="${condition['rolloutSchool']}"
										displayType="code" /></li>
								<li><label>转入教学站：</label> <gh:brSchoolAutocomplete
										name="rollinSchool" id="examineeChangeInfo_rollintSchoolName"
										tabindex="1" style="width:55%;"
										defaultValue="${condition['rollinSchool']}" displayType="code" />
								</li>
							</c:when>
							<c:otherwise>
								<li><label>转点方式：</label> <select name="rollType"
									id="examineeChangeInfo_rollType" style="width: 55%;">
										<option value="rollinOrOut"
											${condition['rollType']=='rollinOrOut'?'selected':'' }></option>
										<option value="rollin"
											${condition['rollType']=='rollin'?'selected':'' }>转入教学站</option>
										<option value="rollout"
											${condition['rollType']=='rollout'?'selected':'' }>转出教学站</option>
								</select></li>
							</c:otherwise>
						</c:choose>
						<li><label>层次：</label>
						<gh:selectModel id="examineeChangeInfo_classic" name="classic"
								bindValue="resourceid" displayValue="classicName"
								value="${condition['classic']}"
								modelClass="com.hnjk.edu.basedata.model.Classic"
								style="width:55%" /></li>
					</ul>
					<ul class="searchContent">
						<li>
							<label>专业：</label> 
							<gh:majorAutocomplete name="major"  id="examineeChangeInfo_major" tabindex="1" style="width:55%;"
								defaultValue="${condition['major']}" displayType="code" orderBy="majorCode desc" />
						</li>
						<li>
							<label>姓名：</label> 
							<input type="text"  id="examineeChangeInfo_examineeName"  name="examineeName"  value="${condition['examineeName']}"  style="width: 55%;" />
						</li>
						<li>
							<label>考生号：</label> 
							<input type="text" id="examineeChangeInfo_enrolleeCode" name="enrolleeCode"
								value="${condition['enrolleeCode']}" style="width: 55%;" />
						</li>
						<li>
							<label>准考证号：</label>
							<input type="text"
								id="examineeChangeInfo_examCertificateNo" name="examCertificateNo"
								value="${condition['examCertificateNo']}" style="width: 55%;" />
						</li>
					</ul>
					<div class="subBar">
						<ul>
							<li><div class="buttonActive">
									<div class="buttonContent">
										<button type="submit">查询</button>
									</div>
								</div></li>
						</ul>
					</div>
				</div>
			</form>
		</div>
		<div class="pageContent">
			<gh:resAuth parentCode="RES_RECRUIT_EXAMINEECHANGEINFO"
				pageType="list"></gh:resAuth>
			<table class="table" layouth="168">
				<thead>
					<tr>
						<th width="5%"><input type="checkbox" name="checkall"
							id="check_all_examineeChangeInfo"
							onclick="checkboxAll('#check_all_examineeChangeInfo','resourceid','#examineeChangeInfoBody')" /></th>
						<th width="10%">招生批次</th>
						<th width="7%">考生号</th>
						<th width="8%">准考证号</th>
						<th width="7%">姓名</th>
						<th width="6%">培养层次</th>
						<th width="13%">录取专业</th>
						<th width="15%">转出教学点</th>
						<th width="15%">转入教学点</th>
						<th width="8%">联系电话</th>
						<th width="6%">备注</th>
					</tr>
				</thead>
				<tbody id="examineeChangeInfoBody">
					<c:forEach items="${examineeChangeInfoPage.result}"
						var="examineeChangeInfo" varStatus="vs">
						<tr>
							<td><input type="checkbox" name="resourceid"
								value="${examineeChangeInfo.resourceid}" autocomplete="off" /></td>
							<td>${examineeChangeInfo.recruitPlan.recruitPlanname}</td>
							<td>${examineeChangeInfo.enrolleeInfo.enrolleeCode}</td>
							<td>${examineeChangeInfo.enrolleeInfo.examCertificateNo}</td>
							<td>${examineeChangeInfo.examineeName}</td>
							<td>${examineeChangeInfo.classic.classicName}</td>
							<td>${examineeChangeInfo.major.majorName}</td>
							<td>${examineeChangeInfo.rolloutSchool.unitName}</td>
							<td>${examineeChangeInfo.rollinSchool.unitName}</td>
							<td>${examineeChangeInfo.enrolleeInfo.studentBaseInfo.contactPhone}</td>
							<td>${examineeChangeInfo.enrolleeInfo.memo}</td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
			<gh:page page="${examineeChangeInfoPage}"
				goPageUrl="${baseUrl }/edu3/recruit/examineeChangeInfo/list.html"
				pageType="sys" condition="${condition}" />
		</div>
	</div>
</body>
</html>
