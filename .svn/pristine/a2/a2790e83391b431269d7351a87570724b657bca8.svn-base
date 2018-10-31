package com.hnjk.core.extend.plugin.excel;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import jxl.Workbook;
import jxl.format.UnderlineStyle;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableHyperlink;
import jxl.write.WritableImage;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

public class TestJxl {

	@Test
	public void test() throws Exception{
		File distFile = new File("c:\\aa.xls");
		WritableWorkbook book = Workbook.createWorkbook(distFile); // 第一步

		   /**
		   * 定义与设置Sheet
		   */
		   WritableSheet sheet = book.createSheet("sheet", 0); // 创建Sheet
		   sheet.setColumnView(0, 30); // 设置列的宽度
		   sheet.setColumnView(1, 30); // 设置列的宽度
		   sheet.setColumnView(2, 30); // 设置列的宽度
		   sheet.setRowView(6, 1000); // 设置行的高度
		   sheet.setRowView(4, 1000); // 设置行的高度
		   sheet.setRowView(5, 1000); // 设置行的高度

		   /**
		   * 定义单元格样式
		   */
		   WritableFont wf = new WritableFont(WritableFont.ARIAL, 15,
		     WritableFont.BOLD, false, UnderlineStyle.NO_UNDERLINE,
		     jxl.format.Colour.CORAL); // 定义格式 字体 下划线 斜体 粗体 颜色
		   WritableCellFormat wcf = new WritableCellFormat(wf); // 单元格定义
		   wcf.setBackground(jxl.format.Colour.BLACK); // 设置单元格的背景颜色
		   wcf.setAlignment(jxl.format.Alignment.CENTRE); // 设置对齐方式

		   /**
		   * 使用样式的单元格
		   */
		   sheet.addCell(new Label(0, 0, "邮箱asasasasa", wcf)); // 普通的带有定义格式的单元格
		   sheet.addCell(new Label(1, 0, "动作asasasasa", wcf));
		   sheet.addCell(new Label(2, 0, "时间asasasasa", wcf));

		   sheet.addCell(new Label(0, 1, "nilpower@sina.com"));
		   sheet.addCell(new Label(1, 1, "action"));
		   sheet.addCell(new Label(2, 1, "time"));

		   /**
		   * excel合并单元格
		   */
		   sheet.addCell(new Label(4, 0, "合并单元格", wcf)); // 合并单元格

		   sheet.addCell(new Label(4, 1, "测试合并"));
		   sheet.addCell(new Label(5, 1, "测试合并"));
		   sheet.addCell(new Label(6, 1, "测试合并"));

		   ///sheet.mergeCells(4, 0, 6, 0); // 合并单元格
		   //				起始列，起始行，终止列，终止行
		   sheet.mergeCells(4, 1, 6, 1);
		   /**
		   * excel图片
		   */
		   sheet.addCell(new Label(0, 3, "展示图片 jxl只支持png格式的", wcf)); // 展示图片标题
		   sheet.mergeCells(0, 3, 3, 3); // 合并图片标题单元格

		   File file = new File("c:\\1.png"); // 获得图片
		   WritableImage image = new WritableImage(0, 4, 3, 3, file); // 设置图片显示位置
		   // 4，4代表图片的高和宽占4个单元格

		   sheet.addImage(image); // 加载图片

		   /**
		   * excel链接
		   */
		   sheet.addCell(new Label(0, 8, "excel链接测试", wcf)); // 链接标题
		   sheet.mergeCells(0, 8, 2, 8);
		   WritableHyperlink link = new WritableHyperlink(0, 9, new URL("http://www.nilpower.com"));
		   link.setDescription("链接使用 链接到NilPower");
		   sheet.mergeCells(0, 9, 1, 9);
		   sheet.addHyperlink(link);

		   book.write();
		   book.close();
	}
	
	@Test
	public void testMuiltSheets() throws Exception{
				
		File distFile = new File("c:\\aa.xls");
		WritableWorkbook book = Workbook.createWorkbook(distFile); // 第一步
		for(int i=0;i<10;i++){
			WritableSheet sheet = book.createSheet("sheet"+i, i); // 创建Sheet
			 sheet.addCell(new Label(0, 0, "邮箱asasasasa")); // 普通的带有定义格式的单元格
			 sheet.addCell(new Label(1, 0, "动作asasasasa"));
			 sheet.addCell(new Label(2, 0, "时间asasasasa"));

			 sheet.addCell(new Label(0, 1, "nilpower@sina.com"));
			 sheet.addCell(new Label(1, 1, "action"));
			 sheet.addCell(new Label(2, 1, "time"));
		}
		book.write();
		book.close();
		 
	}
	
}
