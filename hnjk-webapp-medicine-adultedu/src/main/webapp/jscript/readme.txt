JS使用须知：
*******************************************
1)本目录提供了常规javascript framework 及plugin，所有JS都基于Jquery1.3.2，为了页面规范请不要独自引入额外的JS。如不清楚JQUERY使用，请参见文档中的doc\技术参考文档\jQuery_and_jQuery_UI_Reference_1.2.chm;
2)自行开发的javascript代码或这plugin请遵循jquery规范，为了页面规范请不要引入额外的plugin;
3)所有JS都需要注册为组件，具体方法如下：
	1.创建你的组建目录，拷贝JS到目录下；
	2.找到etc\common\component-file-config.xml文件，将组件注册到该配置中；
	3.在JSP页面引用使用，使用taglib标签，如<gh:loadCom components="jquery,periodicalupdater,multifile,framworkcss"/>
	 	表示引用了jquery,periodicalupdater,multifile,framworkcss四个组件.
