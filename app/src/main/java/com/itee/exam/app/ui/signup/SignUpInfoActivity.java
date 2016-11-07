package com.itee.exam.app.ui.signup;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.itee.exam.R;
import com.itee.exam.app.entity.User;
import com.itee.exam.app.ui.common.BaseActivity;
import com.itee.exam.app.ui.vo.Apply;
import com.itee.exam.app.ui.vo.Order;
import com.itee.exam.core.utils.ProgressTask;
import com.itee.exam.core.utils.Toasts;
import com.itee.exam.utils.PreferenceUtil;
import com.itee.exam.vo.HttpMessage;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SignUpInfoActivity extends BaseActivity {
    private boolean flag = false;
    private boolean no_order = false;
    private TextView done;
    private TextView modify;
    private TextView subjectName;
    private TextView price;
    private TextView payTime;
    private TextView signUpUser;
    private TextView infoStatus;
    private TextView applyStatus;
    private TextView examTime;
    private TextView error;
    private TextView pay;
    private Apply apply;
    private Order order;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_info);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        done = (TextView) findViewById(R.id.tv_done_sign_up_info);
        modify = (TextView) findViewById(R.id.tv_modify_subject);
        done.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
        modify.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final User user = PreferenceUtil.getInstance().getUser();
                new ProgressTask<HttpMessage<Order>>(SignUpInfoActivity.this, "正在获取报考信息...") {
                    @Override
                    public HttpMessage<Order> call() throws Exception {
                        return getAppContext().getHttpService().getOrderinfo(user);
                    }

                    @Override
                    protected void onSuccess(HttpMessage<Order> message) throws Exception {
                        if ("success".equals(message.getResult())) {
                            order = message.getObject();
                            apply = order.getApply();
                            Intent intent = new Intent(SignUpInfoActivity.this, SignUpDetailInfoActivity.class);
                            Bundle bundle = new Bundle();
                            bundle.putSerializable("apply", apply);
                            intent.putExtras(bundle);
                            startActivity(intent);
                        } else {
                            Toasts.showToastInfoShort(SignUpInfoActivity.this, "获取报名信息失败!");
                        }
                        super.onSuccess(message);
                    }
                }.execute();

            }
        });

        modify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ModifyDialogFragment dialogFragment = new ModifyDialogFragment();
                Bundle bundle = new Bundle();
                bundle.putInt("orderId", order.getOrderId());
                dialogFragment.setArguments(bundle);
                dialogFragment.setOnChangedSubjectListener(new ModifyDialogFragment.onChangedSubjectListener() {
                    @Override
                    public void onChanged(Order order1) {
                        order = order1;
                        apply = order.getApply();
                        subjectName.setText(order.getSubject().getSubjectName());
                        price.setText("￥" + String.valueOf(order.getMoney()) + ".00");
                        if (order.getPayTime() == null) {
                            payTime.setText("");
                        } else {
                            DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                            payTime.setText(df.format(order.getPayTime()));
                        }
                        signUpUser.setText(order.getApply().getRealName());
                        String stat = getStatus(order.getState());
                        applyStatus.setText(stat);
                        Date date = order.getSubject().getExamTime();
                        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                        examTime.setText(df.format(date));

                        if ("未缴费".equals(stat)) {
                            pay.setVisibility(View.VISIBLE);
                            //修改报名科目
                            modify.setVisibility(View.VISIBLE);
                        } else {
                            pay.setVisibility(View.GONE);
                            modify.setVisibility(View.GONE);
                        }
                    }
                });
                dialogFragment.show(getSupportFragmentManager(), "Modify");
            }
        });

        pay = (TextView) findViewById(R.id.tv_done_pay);
        pay.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
        pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignUpInfoActivity.this, PayActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("order", order);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        flag = true;
        subjectName = (TextView) findViewById(R.id.tv_sign_up_info_subject_name);
        price = (TextView) findViewById(R.id.tv_sign_up_info_price);
        payTime = (TextView) findViewById(R.id.tv_sign_up_info_paytime);
        signUpUser = (TextView) findViewById(R.id.tv_sign_up_info_user);
        infoStatus = (TextView) findViewById(R.id.tv_sign_up_info_status);
        applyStatus = (TextView) findViewById(R.id.tv_sign_up_info_apply_status);
        examTime = (TextView) findViewById(R.id.tv_sign_up_info_examtime);
        error = (TextView) findViewById(R.id.tv_error_info);
    }

    @Override
    protected void onResume() {
        final User user = PreferenceUtil.getInstance().getUser();
        new ProgressTask<HttpMessage<Order>>(this, "正在获取报考信息...") {
            @Override
            public HttpMessage<Order> call() throws Exception {
                return getAppContext().getHttpService().getOrderinfo(user);
            }

            @Override
            protected void onSuccess(HttpMessage<Order> message) throws Exception {
                if ("success".equals(message.getResult())) {
                    order = message.getObject();
                    apply = order.getApply();
                    subjectName.setText(order.getSubject().getSubjectName());
                    price.setText("￥" + String.valueOf(order.getMoney()) + ".00");
                    if (order.getPayTime() == null) {
                        payTime.setText("");
                    } else {
                        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        payTime.setText(df.format(order.getPayTime()));
                    }
                    signUpUser.setText(order.getApply().getRealName());
                    String stat = getStatus(order.getState());
                    applyStatus.setText(stat);
                    Date date = order.getSubject().getExamTime();
                    DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                    examTime.setText(df.format(date));

                    if ("未缴费".equals(stat)) {
                        pay.setVisibility(View.VISIBLE);
                        //修改报名科目
                        modify.setVisibility(View.VISIBLE);
                    } else {
                        pay.setVisibility(View.GONE);
                        modify.setVisibility(View.GONE);
                    }
                    error.setVisibility(View.GONE);
                } else {
                    subjectName.setText("");
                    price.setText("");
                    payTime.setText("");
                    signUpUser.setText("");
                    applyStatus.setText("");
                    examTime.setText("");
                    pay.setVisibility(View.GONE);
                    done.setVisibility(View.GONE);
                    infoStatus.setVisibility(View.GONE);
                    error.setVisibility(View.VISIBLE);
                }
                super.onSuccess(message);
            }

            @Override
            protected void onException(Exception e) throws RuntimeException {
                subjectName.setText("");
                price.setText("");
                payTime.setText("");
                signUpUser.setText("");
                applyStatus.setText("");
                examTime.setText("");
                pay.setVisibility(View.GONE);
                done.setVisibility(View.GONE);
                infoStatus.setVisibility(View.GONE);
                error.setVisibility(View.VISIBLE);
                no_order = true;
                super.onException(e);
            }

        }.execute();

        new ProgressTask<HttpMessage>(SignUpInfoActivity.this, "正在校验个人详细信息...") {
            @Override
            public HttpMessage call() throws Exception {
                return getAppContext().getHttpService().CheckReginfo(user);
            }

            @Override
            protected void onSuccess(HttpMessage httpMessage) throws Exception {
                if ("success".equals(httpMessage.getResult())) {
                    infoStatus.setText("已完善");
                    done.setText("查看");
                    done.setVisibility(View.VISIBLE);
                } else {
                    if ("no-complete".equals(httpMessage.getMessageInfo())) {
                        infoStatus.setText("未完善");
                        done.setText("去完善");
                        done.setVisibility(View.VISIBLE);
                    } else {
                        infoStatus.setVisibility(View.GONE);
                        done.setVisibility(View.GONE);
                    }

                }
                super.onSuccess(httpMessage);
            }
        }.execute();
        super.onResume();
    }

    private String getStatus(String status) {
        String res = null;
        switch (status) {
            case "0":
                res = "未缴费";
                break;
            case "1":
                res = "已缴费";
                break;
            case "2":
                res = "客服已确认";
                break;
            case "3":
                res = "报名成功";
                break;
            default:
                res = "未知";
                break;
        }
        return res;
    }
}
