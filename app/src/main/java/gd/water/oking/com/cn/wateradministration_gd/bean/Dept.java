package gd.water.oking.com.cn.wateradministration_gd.bean;

import java.util.List;

/**
 * Created by Administrator on 2017/9/18.
 */

public class Dept {

    /**
     * rows : [{"account":"13826292872","deptid":"001001","deptname":"广东省水利厅水利水政监察局","num":1,"phone":"13826292872","remark":"省水利厅水利水政监察局正科级以下干部","sex":"1","tel":"38356363","userid":"127cd1e4ce5c41cfa2ddd33af9cb38b9","username":"易海峰","usertype":"1","xh":1},{"account":"13602713573","deptid":"001001","deptname":"广东省水利厅水利水政监察局","num":2,"phone":"13602713573","remark":"省水利厅水利水政监察局副处以上干部","sex":"1","tel":"38356368","userid":"13e91a2a321e40cbbf767cf66b90406f","username":"欧阳志刚","usertype":"1","xh":2},{"account":"18925139796","deptid":"001001","deptname":"广东省水利厅水利水政监察局","num":3,"phone":"18925139796","remark":"省水利厅水利水政监察局正科级以下干部","sex":"1","tel":"38356366","userid":"25b9063399db4a95af18310c5dd791d0","username":"张  涛","usertype":"1","xh":3},{"account":"13798174190","deptid":"001001","deptname":"广东省水利厅水利水政监察局","num":4,"phone":"13798174190","remark":"省水利厅水利水政监察局正科级以下干部","sex":"1","tel":"38356045","userid":"337fcfdb5580406f8c951a5206b59709","username":"丁庆安","usertype":"1","xh":4},{"account":"13580378922","deptid":"001001","deptname":"广东省水利厅水利水政监察局","num":5,"phone":"13580378922","remark":"省水利厅水利水政监察局正科级以下干部","sex":"1","tel":"38356335","userid":"33f790cf34424a4d94abd01041092dd1","username":"郭建强","usertype":"1","xh":5},{"account":"13926439994","deptid":"001001","deptname":"广东省水利厅水利水政监察局","num":6,"phone":"13926439994","remark":"省水利厅水利水政监察局副处以上干部","sex":"1","tel":"38356948","userid":"3ede8858fd6543b08f99cbe8f52c4f72","username":"许晓明","usertype":"1","xh":6},{"account":"13631364613","deptid":"001001","deptname":"广东省水利厅水利水政监察局","num":7,"phone":"13631364613","remark":"省水利厅水利水政监察局副处以上干部","sex":"1","tel":"38356355","userid":"4b7d309839db41e18a3439623c4fdf1d","username":"张立强","usertype":"1","xh":7},{"account":"13925025050","deptid":"001001","deptname":"广东省水利厅水利水政监察局","num":8,"phone":"13925025050","remark":"省水利厅水利水政监察局正科级以下干部","sex":"1","tel":"38356961","userid":"513e9e04e7854e9da6d5ffc177508c35","username":"马钊鸿","usertype":"1","xh":8},{"account":"13925031639","deptid":"001001","deptname":"广东省水利厅水利水政监察局","num":9,"phone":"13925031639","remark":"省水利厅水利水政监察局正科级以下干部","sex":"1","tel":"38356073","userid":"548191ec015348f1ab912e1eb3036b71","username":"谢湖亮","usertype":"1","xh":9},{"account":"13602753219","deptid":"001001","deptname":"广东省水利厅水利水政监察局","num":10,"phone":"13602753219","remark":"省水利厅水利水政监察局副处以上干部","sex":"1","tel":"38356373","userid":"5b7b6d8994294496a9df5b92dffcb472","username":"刘子鹏","usertype":"1","xh":10}]
     * total : 28
     */

    private int total;
    private List<RowsBean> rows;

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<RowsBean> getRows() {
        return rows;
    }

    public void setRows(List<RowsBean> rows) {
        this.rows = rows;
    }

    public static class RowsBean {
        /**
         * account : 13826292872
         * deptid : 001001
         * deptname : 广东省水利厅水利水政监察局
         * num : 1
         * phone : 13826292872
         * remark : 省水利厅水利水政监察局正科级以下干部
         * sex : 1
         * tel : 38356363
         * userid : 127cd1e4ce5c41cfa2ddd33af9cb38b9
         * username : 易海峰
         * usertype : 1
         * xh : 1
         */

        private String account;
        private String deptid;
        private String deptname;
        private int num;
        private String phone;
        private String remark;
        private String sex;
        private String tel;
        private String userid;
        private String username;
        private String usertype;
        private int xh;

        public String getAccount() {
            return account;
        }

        public void setAccount(String account) {
            this.account = account;
        }

        public String getDeptid() {
            return deptid;
        }

        public void setDeptid(String deptid) {
            this.deptid = deptid;
        }

        public String getDeptname() {
            return deptname;
        }

        public void setDeptname(String deptname) {
            this.deptname = deptname;
        }

        public int getNum() {
            return num;
        }

        public void setNum(int num) {
            this.num = num;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getRemark() {
            return remark;
        }

        public void setRemark(String remark) {
            this.remark = remark;
        }

        public String getSex() {
            return sex;
        }

        public void setSex(String sex) {
            this.sex = sex;
        }

        public String getTel() {
            return tel;
        }

        public void setTel(String tel) {
            this.tel = tel;
        }

        public String getUserid() {
            return userid;
        }

        public void setUserid(String userid) {
            this.userid = userid;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getUsertype() {
            return usertype;
        }

        public void setUsertype(String usertype) {
            this.usertype = usertype;
        }

        public int getXh() {
            return xh;
        }

        public void setXh(int xh) {
            this.xh = xh;
        }
    }
}
