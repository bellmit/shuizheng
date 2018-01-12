package gd.water.oking.com.cn.wateradministration_gd.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.listener.OnLoadCompleteListener;
import com.github.barteksc.pdfviewer.listener.OnPageChangeListener;
import com.github.barteksc.pdfviewer.listener.OnPageErrorListener;
import com.github.barteksc.pdfviewer.scroll.DefaultScrollHandle;
import com.github.barteksc.pdfviewer.util.FitPolicy;
import com.shockwave.pdfium.PdfDocument;

import java.util.ArrayList;
import java.util.List;

import gd.water.oking.com.cn.wateradministration_gd.Adapter.PuttedForwardAdapter;
import gd.water.oking.com.cn.wateradministration_gd.BaseView.BaseFragment;
import gd.water.oking.com.cn.wateradministration_gd.BaseView.RecyclerViewDivider;
import gd.water.oking.com.cn.wateradministration_gd.R;
import gd.water.oking.com.cn.wateradministration_gd.main.MyApp;

/**
 * 案例库
 */
public class PuttedForwardFragment extends BaseFragment implements OnPageChangeListener, OnLoadCompleteListener, OnPageErrorListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    Integer pageNumber = 0;
    private PDFView mPdfView;

    public PuttedForwardFragment() {
        // Required empty public constructor
    }

    public static PuttedForwardFragment newInstance(String param1, String param2) {
        PuttedForwardFragment fragment = new PuttedForwardFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }


    @Override
    public View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_putted_forward, container, false);
    }

    @Override
    public void initView(View rootView) {
       final RecyclerView rc = rootView.findViewById(R.id.rc);
        mPdfView = rootView.findViewById(R.id.pdfView);

        mPdfView.fromAsset("case1.pdf")
                .defaultPage(pageNumber)
                .onPageChange(this)
                .enableAnnotationRendering(true)
                .onLoad(this)
                .scrollHandle(new DefaultScrollHandle(MyApp.getApplictaion()))
                .spacing(10) // in dp
                .onPageError(this)
                .pageFitPolicy(FitPolicy.BOTH)
                .load();


        rc.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false));
        rc.setItemAnimator(new DefaultItemAnimator());
        rc.addItemDecoration(new RecyclerViewDivider(
                MyApp.getApplictaion(), LinearLayoutManager.HORIZONTAL, 1, ContextCompat.getColor(MyApp.getApplictaion(), R.color.colorMain3)));
        final ArrayList<String> items = new ArrayList<>();
        items.add("[案例] 冯某某未经同意在河道管理范围建设建筑物案");
        final PuttedForwardAdapter forwardAdapter = new PuttedForwardAdapter(R.layout.putted_forward_item,items);
        forwardAdapter.openLoadAnimation();
        rc.setAdapter(forwardAdapter);

        forwardAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                for (int i=0;i<items.size();i++){
                    if (position==i){
                        view.setBackgroundColor(Color.DKGRAY);
                    }else {
                        View viewByPosition = adapter.getViewByPosition(rc, i, R.id.tv);
                        viewByPosition.setBackgroundColor(Color.TRANSPARENT);
                    }

                }
            }
        });
    }

    @Override
    public void onPageChanged(int page, int pageCount) {
        pageNumber = page;
//        setTitle(String.format("%s %s / %s", pdfFileName, page + 1, pageCount));
    }


    @Override
    public void loadComplete(int nbPages) {
        PdfDocument.Meta meta = mPdfView.getDocumentMeta();

        printBookmarksTree(mPdfView.getTableOfContents(), "-");
    }

    @Override
    public void onPageError(int page, Throwable t) {

    }

    public void printBookmarksTree(List<PdfDocument.Bookmark> tree, String sep) {
        for (PdfDocument.Bookmark b : tree) {


            if (b.hasChildren()) {
                printBookmarksTree(b.getChildren(), sep + "-");
            }
        }
    }
}
