<div class="yqlj">友情链接 　　　　　　　　　　  
  <select name="select" onchange="javascript:window.open(this.options[this.selectedIndex].value)">
  <option value="">--请选择--</option>
  	<#if linkList ? exists> 
 	<#list  linkList as link>　
 		<#if link.isImg == 'N' && link.linkPosition == 'FOOTER_LEFT'> 　
 			<option value="${link.linkHref}">${link.linkName}</option>
 		</#if>
 	</#list>
 	</#if>
  </select>
  　　　　　　　　
  <select name="select2" onchange="javascript:window.open(this.options[this.selectedIndex].value)">
   <option value="">--请选择--</option>
   <#if linkList ? exists> 
 	<#list  linkList as link>　
 		<#if link.isImg == 'N' && link.linkPosition == 'FOOTER_RIGHT'> 　
 			<option value="${link.linkHref}">${link.linkName}</option>
 		</#if>
 	</#list>
 	</#if>
    </select>　  
    </div>
    