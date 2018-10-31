	<!--个人信息卡-->
	<div id="left_card">
		<div class="img">
			<img src="${basePath}/style/default/images/person.jpg" width="79" height="103" class="img_line">
		</div>
		<div class="inf">
			${userCnName}（${userRole}）<br>
			单位：华工本部	<br>
			${cureentDate}	<br>
			<a href="#" class="red12">联系网院</a><br />
			<a href="javascript:logout()" class="red12">退出</a>
		</div>
	 </div>

	<div id="leftmenu_con" style="height:500px">
			<!--左菜单列表-->
			<div id="my_menu" class="sdmenu">
			<#list menus as menu>
				<div class="collapsed">
					<span>${menu.resourceName}</span>
					<#assign parentCode=menu.resourceCode/>					
						 <#list menu.children as child>
							<#if child?exists>
								<#list twomenus as two>
									<#if child.resourceid = two.resourceid>
									<a href="${basePath}${child.resourcePath}">${child.resourceName }</a>
									</#if>
								</#list>
							</#if>
						</#list> 					
				</div>	
				</#list>
			</div>
	</div>