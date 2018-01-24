package gd.water.oking.com.cn.wateradministration_gd.util;

import net.sourceforge.pinyin4j.PinyinHelper;

import java.util.Comparator;

import gd.water.oking.com.cn.wateradministration_gd.bean.RecipientBean;

/**
 * Created by Administrator on 2018/1/8.
 */

public class CBRPinyinComparator implements Comparator<RecipientBean> {
    @Override
    public int compare(RecipientBean t0, RecipientBean t1) {

       String us0 =  t0.getUSERNAME();
       String us1 =  t1.getUSERNAME();
        if (us0.startsWith("曾")){
            us0 = us0.replace("曾","增");
        }

        if (us1.startsWith("曾")){
            us1=us1.replace("曾","增");
        }
        return concatPinyinStringArray(PinyinHelper.toHanyuPinyinStringArray(us0.charAt(0)))
                .compareTo(concatPinyinStringArray(PinyinHelper
                        .toHanyuPinyinStringArray(us1.charAt(0))));
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
