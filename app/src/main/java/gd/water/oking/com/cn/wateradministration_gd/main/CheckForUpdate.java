package gd.water.oking.com.cn.wateradministration_gd.main;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Environment;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.File;

import gd.water.oking.com.cn.wateradministration_gd.http.CsQueryParams;
import gd.water.oking.com.cn.wateradministration_gd.http.DefaultContants;

public class CheckForUpdate extends Thread {

    private Context context;

    public CheckForUpdate(Context context) {
        super();
        this.context = context;
    }

    @Override
    public void run() {


        final CsQueryParams params = new CsQueryParams();
        params.lx = "app_version";
        Callback.Cancelable cancelable
                = x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                String server_version = "";
                String update_content = "";

                try {
                    JSONObject object = new JSONArray(result).getJSONObject(0);
                    server_version = object.getString("VALUE");
                    update_content = object.getString("BZ");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                try {
                    PackageManager manager = context.getPackageManager();
                    PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
                    final String version = info.versionName;
                    if (!version.equals(server_version)) {

                        new AlertDialog.Builder(context)
                                .setTitle("发现新版本")
                                .setMessage("当前版本："+version+"\n最新版本：" + server_version + "\n" + update_content)
                                .setPositiveButton("下载", new OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog,
                                                        int which) {
                                        String baseUrl = DefaultContants.SERVER_HOST + "/app/gdWater.apk";
                                        String path = Environment.getExternalStorageDirectory().getPath() + "/oking/app/gdWater.apk";
                                        downloadFile(baseUrl, path);

                                    }

                                }).setNegativeButton("取消", new OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                dialog.dismiss();
                            }

                        }).show();
                    } else {

                    }
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {

            }

            @Override
            public void onCancelled(CancelledException cex) {
                Toast.makeText(x.app(), "cancelled", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFinished() {

            }
        });


    }

    private void downloadFile(String baseUrl, String path) {
        final ProgressDialog progressDialog = new ProgressDialog(context);
        RequestParams requestParams = new RequestParams(baseUrl);
        requestParams.setSaveFilePath(path);
        x.http().get(requestParams, new Callback.ProgressCallback<File>() {
            @Override
            public void onWaiting() {
            }

            @Override
            public void onStarted() {
            }

            @Override
            public void onLoading(long total, long current, boolean isDownloading) {
                progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                progressDialog.setMessage("亲，努力下载中。。。");
                progressDialog.show();
                progressDialog.setMax((int) total);
                progressDialog.setProgress((int) current);
            }

            @Override
            public void onSuccess(File result) {
                Toast.makeText(context, "下载成功", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.setDataAndType(Uri.fromFile(result),
                        "application/vnd.android.package-archive");
                context.startActivity(intent);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                ex.printStackTrace();
                Toast.makeText(context, "下载失败，请检查网络和SD卡", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();

            }

            @Override
            public void onCancelled(CancelledException cex) {
            }

            @Override
            public void onFinished() {
            }
        });
    }


}