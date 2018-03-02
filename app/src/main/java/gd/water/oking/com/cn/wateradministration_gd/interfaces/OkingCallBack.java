package gd.water.oking.com.cn.wateradministration_gd.interfaces;

import gd.water.oking.com.cn.wateradministration_gd.bean.MenuBund;

/**
 * Created by Administrator on 2018/2/1.
 * 接口管理类
 */

public interface OkingCallBack {

     interface MyCallBack {
        void refreshMission();
        void refreshTemp();
        void refreshCase();
    }


     interface LoginCallBack {
        void loginSucc(MenuBund menuBund);
        void offlineLoginSucc(MenuBund menuBund);
        void loginFail(Exception e);
    }

     interface AppVersionCallBck {
        void reqSucc(String result);
        void reqFail(Throwable ex);
    }


    //获取任务日志
    interface GetJobLog{
         void getSucc(String result);
         void getFail(Throwable ex);
    }


    //上传任务日志文本资料
    interface UploadJobLogForText{
         void uploadSucc(String result);
         void uploadFail(Throwable ex);
    }

    interface CheckPicForServer{
         void checkedSucc(String result);
         void checkedFail(Throwable ex);
    }


    //上传图片
    interface  UploadJobLogForPic{
        void uploadSucc(String result);
        void uploadFail(Throwable ex);
        void uploadPositionFail(Throwable ex);
    }


    //上传位置到服务器
    interface UploadLocationToServer{
        void uploadSucc(String result);
        void uploadFail(Throwable ex);
    }

}
