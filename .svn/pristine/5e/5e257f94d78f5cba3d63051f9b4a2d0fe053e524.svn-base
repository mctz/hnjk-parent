<div style="width:960px;">
	<div>&nbsp;</div>
	<table  style="border: 1px solid #C1DAE8;" align="center" width="100%" cellspacing="0">
		<tbody>
	  		<tr>
	  		<td style="border: 1px solid #C1DAE8;text-align:center">
  			<a 
  			<#if (course.isQualityResource)?exists && course.isQualityResource=='Y'>
  				href="${baseUrl }/edu3/course/bbs/index.html?courseId=${course.resourceid }"
  			<#else>
  				href="${baseUrl }/edu3/learning/bbs/index.html?courseId=${course.resourceid }"
  			</#if>
  			 style="text-align: center;color: #01336B;"><strong>${course.courseName }论坛</strong></a>
  			</td>
	  		<#list sections as section>
				<#if section?exists>
					<td style="border: 1px solid #C1DAE8;text-align:center">
		  			<a 
		  			<#if (course.isQualityResource)?exists && course.isQualityResource=='Y'>
		  				href="${baseUrl }/edu3/course/bbs/section.html?courseId=${course.resourceid }&sectionId=${section.resourceid }"
		  			<#else>
		  				href="${baseUrl }/edu3/learning/bbs/section.html?courseId=${course.resourceid }&sectionId=${section.resourceid }"
		  			</#if>
		  			 style="text-align: center;color: #01336B;"><strong>${section.sectionName }</strong></a>
		  			</td>
				</#if>
			</#list> 	
	  		</tr>
		</tbody>
	</table>	
</div>