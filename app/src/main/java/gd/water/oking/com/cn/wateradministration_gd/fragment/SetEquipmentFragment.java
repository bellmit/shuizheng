package gd.water.oking.com.cn.wateradministration_gd.fragment;


import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

import gd.water.oking.com.cn.wateradministration_gd.Adapter.EquipmentListAdapter;
import gd.water.oking.com.cn.wateradministration_gd.Adapter.SpinnerArrayAdapter;
import gd.water.oking.com.cn.wateradministration_gd.BaseView.BaseFragment;
import gd.water.oking.com.cn.wateradministration_gd.R;
import gd.water.oking.com.cn.wateradministration_gd.bean.Equipment;
import gd.water.oking.com.cn.wateradministration_gd.http.DefaultContants;
import gd.water.oking.com.cn.wateradministration_gd.main.MyApp;
import gd.water.oking.com.cn.wateradministration_gd.util.LocalSqlite;

/**
 * 执法装备
 */
public class SetEquipmentFragment extends BaseFragment {

    private TextView data_textView;

    private Spinner type_spinner;
    private ListView canSelectListView;
    private EquipmentListAdapter canSelectAdapter;

    private ArrayList<Equipment> canSelectList = new ArrayList<>();

    public SetEquipmentFragment() {
        // Required empty public constructor
    }

    @Override
    public View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_set_equipment, container, false);
    }

    @Override
    public void initView(View rootView) {
        Button backBtn = (Button) rootView.findViewById(R.id.back_button);
        type_spinner = (Spinner) rootView.findViewById(R.id.type_spinner);
        Button close_btn = (Button) rootView.findViewById(R.id.close_button);
        canSelectListView = (ListView) rootView.findViewById(R.id.canSelect_listView);

        getHttpEquipment();

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SetEquipmentFragment.this.getParentFragment().getChildFragmentManager().popBackStack();
            }
        });




        close_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SetEquipmentFragment.this.getParentFragment().getChildFragmentManager().popBackStack();
            }
        });

        Button save_btn = (Button) rootView.findViewById(R.id.save_button);
        save_btn.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {
                List<Equipment> checkItem = canSelectAdapter.getCheckItem();
                String equipmentString = createEquipmentString(checkItem);
                data_textView.setText(equipmentString);
                SetEquipmentFragment.this.getParentFragment().getChildFragmentManager().popBackStack();
            }
        });


        String[] typeArray = getResources().getStringArray(R.array.spinner_equipment_type);
        SpinnerArrayAdapter typeArrayAdapter = new SpinnerArrayAdapter(typeArray);
        type_spinner.setAdapter(typeArrayAdapter);
        type_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                setCanSelectList();
            }



            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });






    }

    private void setCanSelectList() {
        ArrayList<Equipment> aList = new ArrayList<Equipment>();
        if ("0".equals(String.valueOf(type_spinner.getSelectedItemPosition()))){           //全部
            for (Equipment equipment:canSelectList){
                    aList.add(equipment);
            }

        }else if ("1".equals(String.valueOf(type_spinner.getSelectedItemPosition()))){     //
            for (Equipment equipment:canSelectList){
                if ("交通工具".equals(equipment.getMc1())){
                    aList.add(equipment);
                }
            }
        }else if ("2".equals(String.valueOf(type_spinner.getSelectedItemPosition()))){
            for (Equipment equipment:canSelectList){
                if ("通讯工具".equals(equipment.getMc1())){
                    aList.add(equipment);
                }
            }

        }else if ("3".equals(String.valueOf(type_spinner.getSelectedItemPosition()))){
            for (Equipment equipment:canSelectList){
                if ("取证工具".equals(equipment.getMc1())){
                    aList.add(equipment);
                }
            }

        }else if ("4".equals(String.valueOf(type_spinner.getSelectedItemPosition()))){
            for (Equipment equipment:canSelectList){
                if ("办公设备及场所".equals(equipment.getMc1())){
                    aList.add(equipment);
                }
            }

        }
        if (canSelectAdapter!=null){
            canSelectAdapter.setData(aList);
        }else {
            canSelectAdapter = new EquipmentListAdapter( getActivity(),aList);
            canSelectListView.setAdapter(canSelectAdapter);
        }
    }


    public void setData_textView(TextView data_textView) {
        this.data_textView = data_textView;
    }

    private void getHttpEquipment() {
        if (DefaultContants.ISHTTPLOGIN) {
            RequestParams params1 = new RequestParams(DefaultContants.SERVER_HOST+"/equiPmentController/getEquipmentsByDeptIdForAndroid");
            Callback.Cancelable cancelable1 = x.http().post(params1, new Callback.CommonCallback<String>() {

                @Override
                public void onSuccess(final String result) {
                    canSelectList.clear();
                    int deleteCount = MyApp.localSqlite.delete(LocalSqlite.EQUIPMENT_TABLE,
                            "deptid = ?", new String[]{DefaultContants.CURRENTUSER.getDeptId()});


                    try {
                        JSONArray jsonArray = new JSONArray(result);
                        for (int i = 0;i<jsonArray.length();i++){
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            Equipment equipment = new Equipment();
                            equipment.setDeptId(DefaultContants.CURRENTUSER.getDeptId());
                            equipment.setType(jsonObject.getString("type"));
                            equipment.setType2(jsonObject.getString("type2"));
                            equipment.setMc1(jsonObject.getString("mc1"));
                            equipment.setMc2(jsonObject.getString("mc2"));
                            equipment.setLy(jsonObject.getString("ly"));
                            equipment.setValue(jsonObject.getString("value"));
                            equipment.setRemarks(jsonObject.getString("remarks"));
                            canSelectList.add(equipment);
                            equipment.insertDB(MyApp.localSqlite);
                        }

                        canSelectAdapter = new EquipmentListAdapter(getActivity(), canSelectList);
                        canSelectListView.setAdapter(canSelectAdapter);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }

                @Override
                public void onError(Throwable ex, boolean isOnCallback) {
                    canSelectList.clear();


                    fromDB();
                }

                @Override
                public void onCancelled(CancelledException cex) {

                }

                @Override
                public void onFinished() {

                }
            });
        } else {
            canSelectList.clear();
            fromDB();

        }
    }

    /**
     * //从数据库加载
     */
    private void fromDB() {
        Cursor cursor = MyApp.localSqlite.select(LocalSqlite.EQUIPMENT_TABLE,
                null,
                "deptid = ?",
                new String[]{DefaultContants.CURRENTUSER.getDeptId()}, null, null, null);

            while (cursor.moveToNext()) {
                String deptid = cursor.getString(cursor.getColumnIndex("deptid"));
                String type = cursor.getString(cursor.getColumnIndex("type"));
                String type2 = cursor.getString(cursor.getColumnIndex("type2"));
                String value = cursor.getString(cursor.getColumnIndex("value"));
                String mc1 = cursor.getString(cursor.getColumnIndex("mc1"));
                String mc2 = cursor.getString(cursor.getColumnIndex("mc2"));
                String ly = cursor.getString(cursor.getColumnIndex("ly"));
                String remarks = cursor.getString(cursor.getColumnIndex("remarks"));
                Equipment equipment = new Equipment();
                equipment.setDeptId(deptid);
                equipment.setType(type);
                equipment.setType2(type2);
                equipment.setValue(value);
                equipment.setMc1(mc1);
                equipment.setMc2(mc2);
                equipment.setLy(ly);
                equipment.setRemarks(remarks);
                canSelectList.add(equipment);

            }
            cursor.close();


        if (canSelectAdapter==null){
            canSelectAdapter = new EquipmentListAdapter(getActivity(), canSelectList);
            canSelectListView.setAdapter(canSelectAdapter);
        }else {
            canSelectAdapter.setData(canSelectList);
        }

    }

    private String createEquipmentString(List<Equipment> checkItem) {

        String dataStr = "", trafficStr = "", communicationStr = "", forensicsStr = "",officeStr = "";

        for (int i = 0; i < checkItem.size(); i++) {
            Equipment e = checkItem.get(i);
            if ("交通工具".equals(e.getMc1())) {
                trafficStr = trafficStr + e.getType2() + ",";
            } else if ("通讯工具".equals(e.getMc1())) {
                communicationStr = communicationStr + e.getType2() + ",";
            } else if ("取证工具".equals(e.getMc1())) {
                forensicsStr = forensicsStr + e.getType2() + ",";
            }else if ("办公设备及场所".equals(e.getMc1())) {
                officeStr = officeStr + e.getType2() + ",";
            }
        }

        if (!"".equals(trafficStr)) {
            dataStr += "交通工具：" + trafficStr.subSequence(0, trafficStr.length() - 1) + "  ";
        }

        if (!"".equals(communicationStr)) {
            dataStr += "通讯工具：" + communicationStr.subSequence(0, communicationStr.length() - 1) + "  ";
        }

        if (!"".equals(forensicsStr)) {
            dataStr += "取证工具：" + forensicsStr.subSequence(0, forensicsStr.length() - 1);
        }

         if (!"".equals(officeStr)) {
            dataStr += "办公设备及场所：" + officeStr.subSequence(0, officeStr.length() - 1);
        }



        return dataStr;
    }

}
