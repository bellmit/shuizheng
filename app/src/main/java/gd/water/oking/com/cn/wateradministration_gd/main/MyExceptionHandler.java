package gd.water.oking.com.cn.wateradministration_gd.main;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.os.Looper;
import android.widget.Toast;

import com.vondear.rxtools.view.RxToast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.lang.Thread.UncaughtExceptionHandler;
import java.util.Date;

/**
 * @author razor 全局异常处理器
 */
public class MyExceptionHandler implements UncaughtExceptionHandler {

	// 系统默认的 UncaughtException 处理类
	private Thread.UncaughtExceptionHandler mDefaultHandler;

	public void init() {
		// 获取系统默认的 UncaughtException 处理器
		mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
		Thread.setDefaultUncaughtExceptionHandler(this);

	}

	@Override
	public void uncaughtException(Thread thread, Throwable ex) {

		if (!handleException(ex) && mDefaultHandler != null) {
			// 如果用户没有处理则让系统默认的异常处理器来处理
			mDefaultHandler.uncaughtException(thread, ex);
		} else {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {

			}


			try {
				ex.printStackTrace();
				File root  = new File(Environment.getExternalStorageDirectory(), "oking/System_log/");
				if (!root.exists()) {
					root.mkdirs();
				}

				root = new File(root, "crash"+System.currentTimeMillis()+".txt");
				PrintStream err;
				if (!root.exists()){
					root.createNewFile();
					err = new PrintStream(new FileOutputStream(root));
					err.append(new Date().toString());
					err.append("\n");
					ex.printStackTrace(err);
					err.flush();
					err.close();

					ex.printStackTrace();
				}
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			Intent intent = new Intent(MyApp.getApplictaion(), MainActivity.class);
			@SuppressLint("WrongConstant") PendingIntent restartIntent = PendingIntent.getActivity(
					MyApp.getApplictaion(), 0, intent,
					Intent.FLAG_ACTIVITY_NEW_TASK);
			//退出程序
			AlarmManager mgr = (AlarmManager)MyApp.getApplictaion().getSystemService(Context.ALARM_SERVICE);
			mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 1000,
					restartIntent); // 1秒钟后重启应用
			//退出程序
			android.os.Process.killProcess(android.os.Process.myPid());
			System.exit(0);



		}



	}


	/**
	 * 自定义错误处理，收集错误信息，发送错误报告等操作均在此完成
	 *
	 * @param ex
	 * @return true：如果处理了该异常信息；否则返回 false
	 */
	private boolean handleException(Throwable ex) {
		if (ex == null) {
			return false;
		}

		// 异常的自定义处理
		// 日志书写之类的。。
		new Thread() {
			@Override
			public void run() {
				Looper.prepare();
				//相关UI处理
				RxToast.error(MyApp.getApplictaion(), "数据校验失败,即将退出重启",
						Toast.LENGTH_SHORT).show();
				Looper.loop();
			}
		}.start();
		return true;
	}

}
