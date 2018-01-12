package gd.water.oking.com.cn.wateradministration_gd.View;

import android.hardware.Camera;
import android.view.SurfaceHolder;
import android.widget.Toast;

import com.vondear.rxtools.view.RxToast;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import gd.water.oking.com.cn.wateradministration_gd.main.MyApp;
import gd.water.oking.com.cn.wateradministration_gd.main.ShootActivity;

@SuppressWarnings("deprecation")
public class MyCamera implements Camera.PictureCallback, Camera.ShutterCallback{
    private Camera mCamera;
    private ScheduledThreadPoolExecutor mTimerShootingExecutor;
    private ArrayList<String> paths = new ArrayList<String>();
    private  ShootActivity mShootActivity;

    public MyCamera(ShootActivity shootActivity) {
        this.mShootActivity = shootActivity;
    }

    public void openCamera() {
        if (null == mCamera) {
            mCamera = Camera.open();
        }
    }

    public void releaseCamera() {
        if (null != mCamera) {
            if (isTimerShootingStart()) {
                stopTimerShooting();
            }

            mCamera.release();
            mCamera = null;
        }
    }

    public void takePicture() {
        mCamera.takePicture(this, null, this);
    }


    public synchronized void startTimerShooting(int timeMs) {
        if (null == mTimerShootingExecutor) {
            mTimerShootingExecutor = new ScheduledThreadPoolExecutor(1);
            mTimerShootingExecutor.scheduleWithFixedDelay(new Runnable() {
                @Override
                public void run() {
                    takePicture();
                }
            }, 0, timeMs, TimeUnit.MILLISECONDS);
        }
    }

    public synchronized void stopTimerShooting() {
        if (null != mTimerShootingExecutor) {
            mTimerShootingExecutor.shutdown();
            mTimerShootingExecutor = null;
        }
    }

    public synchronized boolean isTimerShootingStart() {
        if (null != mTimerShootingExecutor) {
            return true;
        } else {
            return false;
        }
    }

    public void onSurfaceCreated(SurfaceHolder holder) {
        try {
            //surface创建成功能够拿到回调的holder
            //holder中包含有成功创建的Surface
            //从而交给摄像机预览使用
            mCamera.setPreviewDisplay(holder);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void onSurfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        //surface的尺寸发生变化
        //配置预览参数，如分辨率等
        //这里使用的分辨率简单选取了支持的预览分辨率的第一项
        //网上可以查找对应的优选算法
        Camera.Parameters parameters = mCamera.getParameters();
        List<Camera.Size> sizes = parameters.getSupportedPreviewSizes();
        Camera.Size selected = sizes.get(0);
        parameters.setPreviewSize(selected.width, selected.height);
        parameters.setPictureSize(selected.width, selected.height);

        //给摄像机设置参数，开始预览
        mCamera.setParameters(parameters);
        mCamera.startPreview();
    }

    public void onSurfaceDestroyed(SurfaceHolder holder) {

    }

    @Override
    public void onPictureTaken(byte[] data, Camera camera) {
        try {



            FileOutputStream out;
            String filename = android.text.format.DateFormat
                    .format("yyyyMMdd_HHmmss", System.currentTimeMillis())
                    + ".jpg";
            String filePathname = "/storage/emulated/0/oking/mission_pic/"+ filename;
            out = new FileOutputStream(filePathname);
            out.write(data);
            out.flush();
            out.close();
            paths.add(filePathname);

            RxToast.success(MyApp.getApplictaion(),"拍照成功", Toast.LENGTH_SHORT).show();
            mShootActivity.notyEnablestate(true);
            //重新启动预览
            mCamera.startPreview();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onShutter() {
    }

    public ArrayList<String> completePhotos() {

        return paths;
    }

}
