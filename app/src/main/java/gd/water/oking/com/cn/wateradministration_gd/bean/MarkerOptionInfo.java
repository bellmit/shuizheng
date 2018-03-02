package gd.water.oking.com.cn.wateradministration_gd.bean;

/**
 * Created by Administrator on 2018/2/3.
 */

public class MarkerOptionInfo {
    private int optionType;
    private String sourcePath;

    public int getOptionType() {
        return optionType;
    }

    public void setOptionType(int optionType) {
        this.optionType = optionType;
    }

    public String getSourcePath() {
        return sourcePath;
    }

    public void setSourcePath(String sourcePath) {
        this.sourcePath = sourcePath;
    }

    @Override
    public String toString() {
        return "MarkerOptionInfo{" +
                "optionType=" + optionType +
                ", sourcePath='" + sourcePath + '\'' +
                '}';
    }
}
