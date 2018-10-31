<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>学生缴费标准管理</title>
<script type="text/javascript">
	//修改
	function modifyStudentPayment(){
		if(isCheckOnlyone('resourceid','#studentPaymentBody')){
			if($("#studentPaymentBody input[@name='resourceid']:checked").attr('rel')!='0'){//只能编辑未缴费记录
				alertMsg.warn("请选择一条未缴费记录.");	
				return false;
			} else if($("#studentPaymentBody input[@name='resourceid']:checked").attr('defer')=='Y' 
					&& $("#studentPaymentBody input[@name='resourceid']:checked").attr('isMultiDefer')=='Y'){//多次缓缴记录
				alertMsg.warn("多次缓缴记录请在原标准的多次缓缴管理进行设置.");	
				return false;
			} else {
				navTab.openTab('RES_FINANCE_STUDENTPAYMENT_INPUT', "${baseUrl}/edu3/finance/studentpayment/input.html?resourceid="+$("#studentPaymentBody input[@name='resourceid']:checked").val(), '修改学生缴费标准');	
			}		
		}
	}
	//减免
	function derateStudentPayment(){
		if(isCheckOnlyone('resourceid','#studentPaymentBody')){
			if($("#studentPaymentBody input[@name='resourceid']:checked").attr('rel')!='0'){//只能编辑未缴费记录
				alertMsg.warn("请选择一条未缴费记录.");	
				return false;
			} else {
				navTab.openTab('RES_FINANCE_STUDENTPAYMENT_INPUT', "${baseUrl}/edu3/finance/studentpayment/input.html?isDerate=Y&resourceid="+$("#studentPaymentBody input[@name='resourceid']:checked").val(), '修改学生缴费标准');	
			}		
		}
	}
	//导出
	function exportStudentPayment(){
		var isempty = true;
		$("#studentPayment_search_form [name]").each(function (){
			if($(this).val()!=""){
				isempty = false;//查询条件不为空
			}
		});
		if(isempty){
			alertMsg.warn("请选择一个条件进行导出.");
			return false;
		}
		var name1 = encodeURIComponent(encodeURIComponent($("#name1").val()));
		var url = "${baseUrl}/edu3/finance/studentpayment/defere/export.html?"+$("#studentPayment_search_form").serialize()+"&name1="+name1;
		downloadFileByIframe(url,"studentExerciseStatExportIframe");
	}
	//导入缴费信息
		//导入批量注册
	function importPay(){
		var url = "${baseUrl}/edu3/finance/studentpayment/upload.html";
		$.pdialog.open(url,"RES_RECRUIT_EXAMEEINFO_FEE_IMP","导入批量注册",{width:480, height:320});		
				
	}
	//设置缓缴期限
	function deferStudentPayment(deferType){
		if(isCheckOnlyone('resourceid','#studentPaymentBody')){
			if($("#studentPaymentBody input[@name='resourceid']:checked").attr('rel')!='0'){//只能编辑未缴费记录
				alertMsg.warn("请选择一条未缴费记录的缓缴记录.");	
				return false;
			} else {
				var res = $("#studentPaymentBody input[@name='resourceid']:checked").val();
				if(deferType==0){//取消缓缴					
					if($("#studentPaymentBody input[@name='resourceid']:checked").attr('defer')!='Y'){
						alertMsg.warn("请选择一条缓缴标准.");	
						return false;
					} else if($("#studentPaymentBody input[@name='resourceid']:checked").attr('isMultiDefer')=='Y'){//多次缓缴记录
						alertMsg.warn("请在原标准的多次缓缴管理进行设置.");	
						return false;
					} else {
						$.post("${baseUrl}/edu3/finance/studentpayment/deferenddate/edit.html",{resourceid:res,deferType:0},navTabAjaxDone,"json");	
					}					
				} else {//设置缓缴期限
					alertMsg.confirm("缓缴期限：<input type='text' id='studentPayment_list_deferEndDate' onFocus=\"WdatePicker({dateFmt:'yyyy-MM-dd',minDate:'%y-%M-{%d}'})\">",{okCall:function (){
						var deferEndDate = $("#studentPayment_list_deferEndDate").val();
						if(deferEndDate!=""){									
							$.post("${baseUrl}/edu3/finance/studentpayment/deferenddate/edit.html",{resourceid:res,deferEndDate:deferEndDate,deferType:1},navTabAjaxDone,"json");
						} else {
							deferStudentPayment();//重新选择时间
						}
					},okName:'设置'});
				}					
			}		
		}
	}
	//单独缴费
	function setPay(){
		if(isCheckOnlyone('resourceid','#studentPaymentBody')){
			if($("#studentPaymentBody input[@name='resourceid']:checked").attr('rel')=='1'){//只能编辑欠费记录
				alertMsg.warn("请选择一条欠费记录.");	
				return false;
			}else {//修改缴费信息
				var res = $("#studentPaymentBody input[@name='resourceid']:checked").val();
				alertMsg.confirm("已缴金额：<input type='text' id='studentPayment_list_setPay' />",{okCall:function (){
					var pay = $("#studentPayment_list_setPay").val();
					if(pay!=""){									
						$.post("${baseUrl}/edu3/finance/studentpayment/pay/edit.html",{resourceid:res,pay:pay},navTabAjaxDone,"json");
					} else {
						setPay();//重新设置
					}
				},okName:'设置'});
			}
		}
	}
	
	
	
	//多次缓缴管理
	function listDeferStudentPayment(){
		if(isCheckOnlyone('resourceid','#studentPaymentBody')){
			if($("#studentPaymentBody input[@name='resourceid']:checked").attr('rel')!='0'){//只能编辑未缴费记录
				alertMsg.warn("请选择一条未缴费记录.");	
				return false;
			} else if($("#studentPaymentBody input[@name='resourceid']:checked").attr('defer')=='Y'){//缓缴记录
				alertMsg.warn("缓缴记录请在原标准的多次缓缴管理进行设置.");	
				return false;
			} else {
				$.pdialog.open("${baseUrl}/edu3/finance/studentpayment/defere/list.html?resourceid="+$("#studentPaymentBody input[@name='resourceid']:checked").val(), "studentPaymentDeferList", "多次缓缴管理",{width: 800, height: 600 });	
			}		
		}		
	}
</script>
</head>
<body>
	<div class="page">
		<div class="pageHeader">
			<form id="studentPayment_search_form"
				onsubmit="return navTabSearch(this);"
				action="${baseUrl }/edu3/finance/studentpayment/list.html"
				method="post">
				<div class="searchBar">
					<ul class="searchContent">
						<li><label>学习中心：</label> <gh:brSchoolAutocomplete
								name="brSchool" tabindex="1" id="studentPayment_brSchool"
								defaultValue="${condition['brSchool'] }" displayType="code"
								style="width:52;" /></li>
						<li><label>专业：</label> <gh:selectModel name="majorid"
								id="studentPayment_majorid" bindValue="resourceid"
								displayValue="majorCodeName"
								modelClass="com.hnjk.edu.basedata.model.Major"
								value="${condition['majorid']}" orderBy="majorCode,majorName"
								style="width:52%;" /></li>
						<li><label>层次：</label>
						<gh:selectModel name="classicid" id="studentPayment_classicid"
								bindValue="resourceid" displayValue="classicName"
								value="${condition['classicid']}"
								modelClass="com.hnjk.edu.basedata.model.Classic"
								style="width:52%;" /></li>
					</ul>
					<ul class="searchContent">
						<li><label>年级：</label> <gh:selectModel
								id="studentPayment_gradeid" name="gradeid"
								bindValue="resourceid" displayValue="gradeName"
								modelClass="com.hnjk.edu.basedata.model.Grade"
								value="${condition['gradeid']}"
								orderBy="yearInfo.firstYear desc,term desc" style="width:52%;" />
						</li>
						<li><label>姓名：</label><input id="name1" type="text"
							name="name" value="${condition['name']}" style="width: 52%;" /></li>
						<li><label>学号：</label><input type="text" name="studyNo"
							value="${condition['studyNo']}" style="width: 52%;" /></li>
					</ul>
					<ul class="searchContent">
						<li><label>缴费年度：</label> <gh:selectModel
								id="studentPayment_yearInfoId" name="yearInfoId"
								bindValue="resourceid" displayValue="yearName"
								modelClass="com.hnjk.edu.basedata.model.YearInfo"
								value="${condition['yearInfoId']}" style="width:52%;"
								orderBy="firstYear desc " /></li>
						<!--<li>
					<label>缴费学期：</label><gh:select id="studentPayment_term" name="term" value="${condition['term']}" dictionaryCode="CodeTerm" style="width:52%;" />
				</li>
				-->
						<li><label>缴费状态：</label>
						<gh:select name="chargeStatus" id="studentPayment_chargeStatus"
								dictionaryCode="CodeChargeStatus"
								value="${condition['chargeStatus']}" style="width:52%;" /></li>
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
			<gh:resAuth parentCode="RES_FINANCE_STUDENTPAYMENT" pageType="list"></gh:resAuth>
			<table class="table" layouth="188">
				<thead>
					<tr>
						<th width="5%"><input type="checkbox" name="checkall"
							id="check_all_studentPayment"
							onclick="checkboxAll('#check_all_studentPayment','resourceid','#studentPaymentBody')" /></th>
						<th width="5%">缴费年度</th>
						<th width="9%">所属年度</th>
						<th width="8%">学号</th>
						<th width="6%">学生姓名</th>
						<th width="10%">学习中心</th>
						<th width="7%">办学模式</th>
						<th width="8%">年级</th>
						<th width="6%">应缴金额</th>
						<th width="6%">减免金额</th>
						<th width="8%">缴费期限</th>
						<th width="6%">已缴金额</th>
						<th width="10%">是否缓缴</th>
						<th width="6%">缴费状态</th>
					</tr>
				</thead>
				<tbody id="studentPaymentBody">
					<c:forEach items="${studentPaymentList.result}"
						var="studentPayment" varStatus="vs">
						<tr>
							<td><input type="checkbox" name="resourceid"
								value="${studentPayment.resourceid }"
								rel="${studentPayment.chargeStatus }"
								defer="${studentPayment.isDefer }"
								isMultiDefer="${studentPayment.isMultiDefer }"
								autocomplete="off" /></td>
							<td title="${studentPayment.chargeYear }年">${studentPayment.chargeYear }年</td>
							<!--<td title="${studentPayment.yearInfo.yearName }${ghfn:dictCode2Val('CodeTerm',studentPayment.term) }">${studentPayment.yearInfo.yearName }${ghfn:dictCode2Val('CodeTerm',studentPayment.term) }</td>
		        	-->
							<td title="${studentPayment.yearInfo.yearName }">${studentPayment.yearInfo.yearName }</td>
							<td title="${studentPayment.studyNo }">${studentPayment.studyNo }</td>
							<td title="${studentPayment.name }">${studentPayment.name }</td>
							<td title="${studentPayment.branchSchool.unitName }">${studentPayment.branchSchool.unitName }</td>
							<td
								title="${ghfn:dictCode2Val('CodeTeachingType',studentPayment.teachingType) }">${ghfn:dictCode2Val('CodeTeachingType',studentPayment.teachingType) }</td>
							<td title="${studentPayment.grade.gradeName }">${studentPayment.grade.gradeName }</td>
							<td
								title="<fmt:formatNumber value='${studentPayment.recpayFee }' pattern='####.##' />"><fmt:formatNumber
									value="${studentPayment.recpayFee }" pattern="####.##" /></td>
							<td
								title="<fmt:formatNumber value='${studentPayment.derateFee }' pattern='####.##' />"><fmt:formatNumber
									value="${studentPayment.derateFee }" pattern="####.##" /></td>
							<td
								title="<fmt:formatDate value='${studentPayment.chargeEndDate }' pattern='yyyy-MM-dd' />"><fmt:formatDate
									value="${studentPayment.chargeEndDate }" pattern="yyyy-MM-dd" /></td>
							<td
								title="<fmt:formatNumber value='${studentPayment.facepayFee }' pattern='####.##' />"><fmt:formatNumber
									value="${studentPayment.facepayFee }" pattern="####.##" /></td>
							<td
								title="${ghfn:dictCode2Val('yesOrNo',studentPayment.isDefer) }<c:if test='${studentPayment.isDefer eq "Y" }'>(<fmt:formatDate value='${studentPayment.deferEndDate }' pattern='yyyy-MM-dd' />前)</c:if>">
								${ghfn:dictCode2Val('yesOrNo',studentPayment.isDefer) } <c:if
									test="${studentPayment.isDefer eq 'Y' }">(<fmt:formatDate
										value="${studentPayment.deferEndDate }" pattern="yyyy-MM-dd" />前)</c:if>
							</td>
							<td
								title="${ghfn:dictCode2Val('CodeChargeStatus',studentPayment.chargeStatus) }">${ghfn:dictCode2Val('CodeChargeStatus',studentPayment.chargeStatus) }</td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
			<gh:page page="${studentPaymentList}"
				goPageUrl="${baseUrl }/edu3/finance/studentpayment/list.html"
				pageType="sys" condition="${condition}" />
		</div>
	</div>
</body>
</html>