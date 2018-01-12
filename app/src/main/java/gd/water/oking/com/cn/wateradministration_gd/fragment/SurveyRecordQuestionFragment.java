package gd.water.oking.com.cn.wateradministration_gd.fragment;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.UUID;

import gd.water.oking.com.cn.wateradministration_gd.BaseView.BaseFragment;
import gd.water.oking.com.cn.wateradministration_gd.R;
import gd.water.oking.com.cn.wateradministration_gd.bean.Case;
import gd.water.oking.com.cn.wateradministration_gd.bean.QuestionAnswer;
import gd.water.oking.com.cn.wateradministration_gd.bean.SurveyRecord;
import gd.water.oking.com.cn.wateradministration_gd.main.MyApp;
import gd.water.oking.com.cn.wateradministration_gd.util.DataUtil;

import static android.content.Context.MODE_PRIVATE;

/**
 * A simple {@link Fragment} subclass.
 */
public class SurveyRecordQuestionFragment extends BaseFragment {

    private Case mycase;
    private SurveyRecord surveyRecord;

    private EditText question_editText, answer_editText;
    private ListView question_listView;
    private ArrayList<QuestionAnswer> questions = new ArrayList<>();
    private BaseAdapter adapter;
    private Button save_button, next_button, add_newQuestion_button, close_button;
    private int selectIndex;
    private boolean isSaveNewQuestion = false;

    public SurveyRecordQuestionFragment() {
        // Required empty public constructor
    }

    @Override
    public View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_survey_record_question, container, false);
    }

    @Override
    public void initView(View rootView) {

        question_editText = (EditText) rootView.findViewById(R.id.question_editText);
        answer_editText = (EditText) rootView.findViewById(R.id.answer_editText);

        getQuestion();

        question_listView = (ListView) rootView.findViewById(R.id.question_listView);
        adapter = new BaseAdapter() {
            @Override
            public int getCount() {
                return questions.size();
            }

            @Override
            public Object getItem(int position) {
                return questions.get(position);
            }

            @Override
            public long getItemId(int position) {
                return position;
            }

            @Override
            public View getView(final int position, View convertView, ViewGroup parent) {
                View view = View.inflate(MyApp.getApplictaion(), R.layout.list_item_question, null);

                TextView XH_textView = (TextView) view.findViewById(R.id.XH_textView);
                TextView WT_textView = (TextView) view.findViewById(R.id.WT_textView);
                Button del_button = (Button) view.findViewById(R.id.del_button);
                del_button.setVisibility(surveyRecord.isUpload() ? View.INVISIBLE : View.VISIBLE);

                XH_textView.setText((position + 1) + "");
                WT_textView.setText(questions.get(position).getWT());
                del_button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        AlertDialog dialog = new AlertDialog.Builder(getContext()).setTitle("是否删除该问题记录？").
                                setPositiveButton("是", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        questions.remove(position);
                                        localSaveCase();
                                        notifyDataSetChanged();
                                    }
                                }).setNegativeButton("否", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        }).create();
                        dialog.show();
                    }
                });

                if (surveyRecord.getQuestionAnswers().get(position).getHD() != null && !"".equals(surveyRecord.getQuestionAnswers().get(position).getHD())) {
                    XH_textView.setBackgroundResource(R.drawable.member_item_bg1);
                    WT_textView.setBackgroundResource(R.drawable.member_item_bg1);
                    ((FrameLayout) (del_button.getParent())).setBackgroundResource(R.drawable.member_item_bg1);
                } else {
                    XH_textView.setBackgroundResource(R.drawable.member_item_bg3);
                    WT_textView.setBackgroundResource(R.drawable.member_item_bg3);
                    ((FrameLayout) (del_button.getParent())).setBackgroundResource(R.drawable.member_item_bg3);
                }

                return view;
            }
        };
        question_listView.setAdapter(adapter);
        question_listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectIndex = position;
                question_editText.setText(questions.get(position).getWT());
                answer_editText.setText(questions.get(position).getHD());
            }
        });

        add_newQuestion_button = (Button) rootView.findViewById(R.id.add_newQuestion_button);
        add_newQuestion_button.setVisibility(surveyRecord.isUpload() ? View.GONE : View.VISIBLE);
        add_newQuestion_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isSaveNewQuestion = true;
                question_editText.setText("");
                answer_editText.setText("");
            }
        });

        save_button = (Button) rootView.findViewById(R.id.save_button);
        save_button.setVisibility(surveyRecord.isUpload() ? View.GONE : View.VISIBLE);
        save_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isSaveNewQuestion) {
                    QuestionAnswer qa = new QuestionAnswer();
                    qa.setXH(UUID.randomUUID().toString());
                    qa.setWSID(surveyRecord.getWSID());
                    qa.setAJID(surveyRecord.getAJID());
                    int sx = Integer.valueOf(surveyRecord.getQuestionAnswers().get(surveyRecord.getQuestionAnswers().size() - 1).getSX());
                    qa.setSX((sx + 1) + "");
                    surveyRecord.getQuestionAnswers().add(qa);
                    selectIndex = surveyRecord.getQuestionAnswers().size() - 1;
                    isSaveNewQuestion = false;
                }

                questions.get(selectIndex).setWT(question_editText.getText().toString());
                questions.get(selectIndex).setHD(answer_editText.getText().toString());
                localSaveCase();
                question_listView.setSelection(selectIndex);
                adapter.notifyDataSetChanged();

                next_button.callOnClick();
            }
        });
        next_button = (Button) rootView.findViewById(R.id.next_button);
        next_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //save_button.callOnClick();

                ++selectIndex;
                if (selectIndex < questions.size()) {
                    question_listView.setSelection(selectIndex);
                    question_editText.setText(questions.get(selectIndex).getWT());
                    answer_editText.setText(questions.get(selectIndex).getHD());
                }
            }
        });

        close_button = (Button) rootView.findViewById(R.id.close_button);
        close_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SurveyRecordQuestionFragment.this.getParentFragment().getChildFragmentManager().popBackStack();
            }
        });
    }

    private void localSaveCase() {

        String jsonStr = DataUtil.toJson(mycase);
        SharedPreferences sharedPreferences = MyApp.getApplictaion().getSharedPreferences("case", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(mycase.getAJID(), jsonStr);
        editor.commit();
    }

    private void getQuestion() {
        questions = surveyRecord.getQuestionAnswers();
    }

    public void setSurveyRecord(SurveyRecord surveyRecord) {
        this.surveyRecord = surveyRecord;
    }

    public void setMycase(Case mycase) {
        this.mycase = mycase;
    }
}
