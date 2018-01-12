package gd.water.oking.com.cn.wateradministration_gd.util;

import net.sourceforge.pinyin4j.PinyinHelper;

import java.util.Comparator;

/**
 * Created by Administrator on 2018/1/8.
 */

public class CBRPinyinComparator implements Comparator<String> {
    @Override
    public int compare(String t0, String t1) {


        if (t0.startsWith("曾")){
            t0= t0.replace("曾","增");
        }

        if (t1.startsWith("曾")){
            t1=t1.replace("曾","增");
        }
        return concatPinyinStringArray(PinyinHelper.toHanyuPinyinStringArray(t0.charAt(0)))
                .compareTo(concatPinyinStringArray(PinyinHelper
                        .toHanyuPinyinStringArray(t1.charAt(0))));
    }

    private String concatPinyinStringArray(String[] pinyinArray) {
        StringBuffer pinyinSbf = new StringBuffer();
        if ((pinyinArray != null) && (pinyinArray.length > 0)) {
            for (int i = 0; i < pinyinArray.length; i++) {
                pinyinSbf.append(pinyinArray[i]);
            }
        }
        return pinyinSbf.toString();
    }
}
