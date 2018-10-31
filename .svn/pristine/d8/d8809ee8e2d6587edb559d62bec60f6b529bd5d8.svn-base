<#-- 定义要显示的列数 columnCount -->
<#assign columnCount = 9>
<#-- 计算显示当前记录集需要的表格行数 rowCount -->
<#if linkList ? size% columnCount == 0>
    <#assign rowCount = ( linkList ? size / columnCount) - 1 >
<#else>
    <#assign rowCount = ( linkList ? size / columnCount) >
</#if>
 <!--友情点击-->
        <table  width="99%" border="0" cellspacing="0" cellpadding="0">
            <tr>
              <td colspan="${columnCount}">
              <img src="${baseUrl}/style/default/portal-images-2/Rlj.gif" width="770" height="27" /></td>
            </tr>
            <#list 0..rowCount as row >
            <tr>
            	<#list 0..columnCount - 1 as cell >
              		<td width="85">
              		<#if linkList[row * columnCount + cell] ? exists >   
              			 <#assign link = linkList[row * columnCount + cell]> 
              			 <#if  link.isImg == 'Y'>
              				 <a href="${link.linkHref}"  target="${link.openType}" title="${link.linkName}"><img src="${rootUrl}portal/link/${link.imgPath}" width="83" height="39" border="0" /></a>
              			 </#if> 
              		  <#else>
		                &nbsp;		            		
              		</#if>
              		</td>
          		</#list>
            </tr>
            </#list>
          </table>
       