package gd.water.oking.com.cn.wateradministration_gd.util;

import net.sourceforge.pinyin4j.PinyinHelper;

import java.util.Comparator;

import gd.water.oking.com.cn.wateradministration_gd.bean.ApproverBean;

/**
 * Created by Administrator on 2018/1/8.
 */

public class ApproverPinyinComparator implements Comparator<ApproverBean.SZJCBean> {

    @Override
    public int compare(ApproverBean.SZJCBean t0, ApproverBean.SZJCBean t1) {


        String t1Username = t0.getUSERNAME();
        if (t1Username.startsWith("曾")){
            t1Username= t1Username.replace("曾","增");
        }
        String t2Username = t1.getUSERNAME();
        if (t2Username.startsWith("曾")){
            t2Username=t2Username.replace("曾","增");
        }
        return concatPinyinStringArray(PinyinHelper.toHanyuPinyinStringArray(t1Username.charAt(0)))
                .compareTo(concatPinyinStringArray(PinyinHelper
                        .toHanyuPinyinStringArray(t2Username.charAt(0))));
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
