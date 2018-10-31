package com.hnjk.edu.recruit.util;
import java.util.Comparator;

import com.hnjk.edu.recruit.vo.EnrolleeInfoVO;
/**
 * List<EnrolleeInfoVO>降序排序  
 * @author Administrator
 *
 */
public class PaiXuListUtil implements Comparator{



		@Override
		public int compare(Object o1, Object o2) {
			EnrolleeInfoVO a1 = (EnrolleeInfoVO) o1;
			EnrolleeInfoVO a2 = (EnrolleeInfoVO) o2;

			if (a1.getTotal() > a2.getTotal()) {
				return -1;
			} else {
				if (a1.getTotal().equals(a2.getTotal())) {
					if (a1.getKsh() <a2.getKsh()) {
						return -1;
					} else {
						if (a1.getKsh().equals(a2.getKsh())) {
							return 0;
						} else {
							return 1;
						}
					}
				} else {
					return 1;
				}
			}
		}

//		public int ChangeStringToInt(String str) {
//			return new Integer(str).intValue();
//		}
	
}
