package com.itee.exam.app.ui.personal.examhistory;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.itee.exam.R;
import com.itee.exam.app.entity.ExamHistory;
import com.itee.exam.app.entity.User;
import com.itee.exam.app.ui.common.BaseActivity;
import com.itee.exam.app.ui.personal.adapter.ExamHistoryAdapter;
import com.itee.exam.app.widget.ActionSheetDialog;
import com.itee.exam.core.utils.JupiterAsyncTask;
import com.itee.exam.core.utils.Page;
import com.itee.exam.core.utils.ProgressTask;
import com.itee.exam.core.utils.Toasts;
import com.itee.exam.utils.PreferenceUtil;
import com.itee.exam.vo.HttpMessage;
import com.ruanko.swipemenulistview.SwipeMenu;
import com.ruanko.swipemenulistview.SwipeMenuCreator;
import com.ruanko.swipemenulistview.SwipeMenuItem;
import com.ruanko.swipemenulistview.SwipeMenuListView;
import com.ruanko.swipemenulistview.SwipeMenuListView.OnMenuItemClickListener;

import java.util.ArrayList;
import java.util.List;

public class ExamHistoryActivity extends BaseActivity {
    private SwipeRefreshLayout swipe;
    private SwipeMenuListView recyclerView;
    private ExamHistoryAdapter adapter;
    private List<ExamHistory> history;
    private int lastVisibleItem;
    private int pageNo = 1;
    private int pageSize = 20;
    private Page<ExamHistory> page;
    private int userId;
    private String userName;
    private String appToken;
    private LinearLayout empty;
    private int firstItem;
    private int lastItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exam_history);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        empty = (LinearLayout) findViewById(R.id.jump_layout);
        page = new Page<ExamHistory>();
        page.setPageSize(pageSize);
        User user = PreferenceUtil.getInstance().getUser();
        userId = user.getUserId();
        userName = user.getUserName();
        history = new ArrayList<ExamHistory>();
        adapter = new ExamHistoryAdapter(this, history);
        swipe = (SwipeRefreshLayout) findViewById(R.id.swipe);
        // 设置刷新时动画颜色
        swipe.setColorSchemeResources(android.R.color.holo_blue_light,
                android.R.color.holo_red_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_green_light);
        swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshData();
            }
        });
        recyclerView = (SwipeMenuListView) findViewById(R.id.listView);
        // 创建线性布局管理器（默认是垂直方向）
        final LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (scrollState == SCROLL_STATE_IDLE && lastItem == adapter.getCount() && adapter.getCount() >= pageSize) {
                    loadMore();
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                lastItem = firstVisibleItem + visibleItemCount;
                firstItem = firstVisibleItem;
            }
        });
        recyclerView.setAdapter(adapter);
        SwipeMenuCreator creator = new SwipeMenuCreator() {

            @Override
            public void create(SwipeMenu menu) {
                SwipeMenuItem deleteItem = new SwipeMenuItem(
                        getApplicationContext());
                deleteItem.setBackground(new ColorDrawable(Color.rgb(0xF9, 0x3F, 0x25)));
                deleteItem.setWidth(dp2px(90));
                deleteItem.setIcon(R.drawable.ic_delete);
                menu.addMenuItem(deleteItem);
            }
        };
        recyclerView.setMenuCreator(creator);

        recyclerView.setOnMenuItemClickListener(new OnMenuItemClickListener() {
            @Override
            public void onMenuItemClick(final int position, SwipeMenu menu, int index) {
                final ExamHistory item = history.get(position);
                switch (index) {
                    case 0:
                        new ActionSheetDialog(ExamHistoryActivity.this)
                                .builder()
                                .setTitle("考试记录删除之后，您将无法查看本次的考试结果及试题详解，是否继续删除？")
                                .setCancelable(false)
                                .setCanceledOnTouchOutside(false)
                                .addSheetItem("继续删除", ActionSheetDialog.SheetItemColor.Blue,
                                        new ActionSheetDialog.OnSheetItemClickListener() {
                                            @Override
                                            public void onClick(int which) {
                                                delete(item, position);
                                            }
                                        }).show();
                        break;
                }
            }
        });

        recyclerView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ExamHistory examCache = (ExamHistory) recyclerView.getAdapter().getItem(position);
                String examType = examCache.getPaper_flag();
                if("1".equals(examType)){
                    String hisId = ((TextView) view.findViewById(R.id.exam_history_exam_hisid)).getText().toString();
                    String examName = ((TextView) view.findViewById(R.id.exam_history_examname)).getText().toString();
                    Bundle bundle = new Bundle();
                    bundle.putInt("hisId", Integer.valueOf(hisId));
                    bundle.putString("examName", examName);
                    Intent intent = new Intent(ExamHistoryActivity.this, ExamHistoryDetailActivity.class);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }else if("2".equals(examType)){
                    String hisId = ((TextView) view.findViewById(R.id.exam_history_exam_hisid)).getText().toString();
                    String examName = ((TextView) view.findViewById(R.id.exam_history_examname)).getText().toString();
                    Bundle bundle = new Bundle();
                    bundle.putInt("hisId", Integer.valueOf(hisId));
                    bundle.putString("examName", examName);
                    Intent intent = new Intent(ExamHistoryActivity.this, AfternonnExamHistoryActivity.class);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            }
        });
    }

    private int dp2px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                getResources().getDisplayMetrics());
    }

    private void delete(final ExamHistory item, final int position) {
        appToken = PreferenceUtil.getInstance().getAppToken().getAppToken();
        new ProgressTask<HttpMessage>(ExamHistoryActivity.this, "正在删除考试记录，请稍后...") {

            @Override
            public HttpMessage call() throws Exception {
                return getAppContext().getHttpService().DeleteExamHistory(item.getHistId(), appToken, userName);
            }

            @Override
            protected void onSuccess(HttpMessage message) throws Exception {
                if ("success".equals(message.getResult())) {
                    history.remove(position);
                    adapter.notifyDataSetChanged();
                    Toasts.showToastInfoShort(ExamHistoryActivity.this, message.getMessageInfo());
                } else {
                    Toasts.showToastInfoShort(ExamHistoryActivity.this, message.getMessageInfo());
                }
            }
        }.execute();

    }

    @Override
    protected void onStart() {
        super.onStart();
        refreshData();
    }

    public void refreshData() {
        pageNo = 1;
        page.setPageNo(pageNo);
        appToken = PreferenceUtil.getInstance().getAppToken().getAppToken();
        new JupiterAsyncTask<HttpMessage<Page<ExamHistory>>>(this) {

            @Override
            protected void onPreExecute() throws Exception {
                setShowExceptionTip(true);
                swipe.setRefreshing(true);
            }

            @Override
            public HttpMessage<Page<ExamHistory>> call() throws Exception {
                return getAppContext().getHttpService().PageExamHistory(userId, page, appToken, userName);
            }

            @Override
            protected void onSuccess(HttpMessage<Page<ExamHistory>> pageResult) throws Exception {
                if(pageResult.getObject() == null){
                    showToastShort("没有历史考试记录");
                    return;
                }
                history = pageResult.getObject().getResults();
                adapter = new ExamHistoryAdapter(ExamHistoryActivity.this, history);
                recyclerView.setAdapter(adapter);
                int len = history.size();
                if (len == 0) {
                    empty.setVisibility(View.VISIBLE);
                } else {
                    empty.setVisibility(View.GONE);
                }
            }

            @Override
            protected void onFinally() throws RuntimeException {
                swipe.setRefreshing(false);
            }
        }.execute();
    }

    public void loadMore() {
        pageNo++;
        page.setPageNo(pageNo);
        appToken = PreferenceUtil.getInstance().getAppToken().getAppToken();
        new JupiterAsyncTask<HttpMessage<Page<ExamHistory>>>(this) {

            @Override
            protected void onPreExecute() throws Exception {
                setShowExceptionTip(true);
                swipe.setRefreshing(true);
            }

            @Override
            public HttpMessage<Page<ExamHistory>> call() throws Exception {
                return getAppContext().getHttpService().PageExamHistory(userId, page, appToken, userName);
            }

            @Override
            protected void onSuccess(HttpMessage<Page<ExamHistory>> pageResult) throws Exception {
                history.addAll(pageResult.getObject().getResults());
                if (history.isEmpty()) {
                    showToastShort("没有更多考试");
                    return;
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            protected void onThrowable(Throwable t) throws RuntimeException {
                super.onThrowable(t);
                pageNo--;
            }

            @Override
            protected void onFinally() throws RuntimeException {
                swipe.setRefreshing(false);
            }
        }.execute();
    }
}
