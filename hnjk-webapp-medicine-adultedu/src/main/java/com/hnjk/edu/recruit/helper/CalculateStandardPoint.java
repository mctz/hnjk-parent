package com.hnjk.edu.recruit.helper;

/**
 * 计算标准分<code>CalculateStandardPoint</code><p>
 * @author： 广东学苑教育发展有限公司
 * @since： 2010-4-12 下午02:39:43
 * @see 
 * @version 1.0
*/
public class CalculateStandardPoint
{
	  /**
     * @param origin
     *            原始分
     * @return 标准分
     */
//    public static int[] getStandards(double[] origin)
//    {
//        if (origin == null)
//        {
//            return null;
//        }
//
//        double avg = getAvg(origin);
//        double sqr = getSqr(origin);
//        int[] result = new int[origin.length];
//        for (int i = 0; i < origin.length; i++)
//        {
//            result[i] = getStandard(origin[i], avg, sqr);
//        }
//        return result;
//    }

    /**
     * 计算标准分
     * 计算公式T=500X+100Z，其中X为可调系数，Z=(原始分-原始分平均分)/原始分
     * @param origin
     *            原始分
     * @param avg
     *            平均分
     * @param sqr
     *            方差
     * @return 标准分
     */
    public static int getStandard(double origin, double avg, double sqr,double x)
    {
        if (origin < 0)
        {
            return (int) origin;
        }

        if (origin == 0)
        {
            return 0;
        }

        if (sqr == 0)
        {
            return 500;
        }
        return (int) (500*x+100 * (origin - avg) / sqr );
    }

    /**
     * 计算原始分的平均分，注意只计算那些大于等于0的分数
     * @param origin
     *            原始分
     * @return 平均分
     */
    public static double getAvg(Double[] origin)
    {
        if (origin == null)
        {
            return 0;
        }
        int length = 0;
        double a = 0;
        for (int i = 0; i < origin.length; i++)
        {
            if (null!=origin[i] && origin[i] >= 0)
            {
                a += origin[i];
                length++;
            }
        }
        if (length == 0)
        {
            return 0;
        }
        return a / length;
    }

    /**
     * 计算原始分的方差，注意只计算那些大于等于0的分数
     * @param origin
     *            原始分
     * @return 方差
     */
    public static double getSqr(Double[] origin)
    {
        if (origin == null)
        {
            return 0;
        }
        int length = 0;
        double a = 0;
        double b = 0;
        for (int i = 0; i < origin.length; i++)
        {
            if (null!=origin[i]&&origin[i] >= 0)
            {
                a += origin[i];
                b += origin[i] * origin[i];
                length++;
            }
        }
        if (length == 0)
        {
            return 0;
        }

        a /= length;
        b = b / length - a * a;
        return Math.sqrt(b);
    }
}