package com.leeef.tkstore.base.widget;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.ColorInt;
import androidx.core.content.ContextCompat;

import com.leeef.tkstore.MainActivity;
import com.leeef.tkstore.R;
import com.leeef.tkstore.base.BaseApplication;

//自定义标题栏
public class TitleView extends LinearLayout {

    //默认的顶部导航栏高度
    public static final int NAVIGATIONHEIGHT = 48;
    //默认中间的字体大小
    public static final int TEXTSISE = 15;
    private OnTitleClickListener mListener;
    private TextView mTitleName;
    private ImageView mRightIV;
    private ImageView mLeftIV;
    private View mBottomView;
    private FrameLayout mRightFl;
    //状态栏高度
    private int height = 0;
    private View mstate;
    private RelativeLayout ll;

    private boolean isStateGray = false;//当状态栏我白色的时候设置为透明灰色
    private boolean addmBottomView = false;

    public TitleView(Context context) {
        this(context, null, 0);
    }

    public TitleView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TitleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initData(attrs);
        initAttrs(attrs);
    }

    @TargetApi(21)
    public TitleView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initData(attrs);
        initAttrs(attrs);
    }


    public void initData(AttributeSet attrs) {
        setOrientation(VERTICAL);
        if (getBackground() == null)
            setBackgroundResource(R.color.white);
        //background包括color和Drawable,这里分开取值
        Drawable background = getBackground();
        if (background instanceof ColorDrawable) {
            ColorDrawable colordDrawable = (ColorDrawable) background;
            int color = colordDrawable.getColor();
            if (color == -1) {
                isStateGray = true;
            } else {
                isStateGray = false;
            }
        }


        //最左侧按钮默认是返回
        mLeftIV = new ImageView(getContext());
        mLeftIV.setImageResource(R.mipmap.iv_fanhui);
        mLeftIV.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.onBackClick(TitleView.this, mLeftIV);
                } else {
                    if (getContext() instanceof Activity) {
                        if (BaseApplication.getInstance().getActivityList().size() == 1) {
                            if (getContext() instanceof MainActivity) {
                            } else {
                                getContext().startActivity(new Intent(getContext(), MainActivity.class));
                            }
                        }
                        ((Activity) getContext()).finish();
                    }


                }
            }
        });
        int px18 = (int) (getResources().getDisplayMetrics().density * 18 + 0.5);
        mLeftIV.setPadding(px18, 0, px18, 0);
        //标题
        mTitleName = new TextView(getContext());
        mTitleName.setTextSize(TypedValue.COMPLEX_UNIT_SP, TEXTSISE);
        mTitleName.setTextColor(ContextCompat.getColor(getContext(), R.color.c_111111));
        mTitleName.setSingleLine();
        mTitleName.setGravity(Gravity.CENTER);
        mTitleName.setEllipsize(TextUtils.TruncateAt.END);
        mTitleName.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null)
                    mListener.onTitleClick(TitleView.this, mTitleName.getText().toString());
            }
        });
        //右边的布局
        mRightFl = new FrameLayout(getContext());
        //右边图片
        mRightIV = new ImageView(getContext());
        mRightIV.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) mListener.onRightClick(TitleView.this, mRightIV);
            }
        });
        mRightIV.setPadding(px18, 0, px18, 0);
        mRightFl.addView(mRightIV, ViewGroup.LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
        //横着的线性布局
        ll = new RelativeLayout(getContext());
        ll.addView(mTitleName, new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        ll.addView(mLeftIV, new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT));
        //居于右侧
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
        params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        params.addRule(RelativeLayout.CENTER_VERTICAL);
        ll.addView(mRightFl, params);
        //横线
        mBottomView = new View(getContext());
        mBottomView.setBackgroundResource(R.color.c_f2f2f2);
        //状态栏高度
        mstate = new View(getContext());

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            if (isStateGray) {
                mstate.setBackgroundColor(getResources().getColor(R.color.c_33000));
            } else {
                mstate.setBackgroundColor(getResources().getColor(R.color.transparent));
            }
        }

        int resourceId = getContext().getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            height = getContext().getResources().getDimensionPixelSize(resourceId);
        }
        //将线性布局和横线添加进来
        if (attrs != null) {
            TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.TitleView);
            boolean showMstate = typedArray.getBoolean(R.styleable.TitleView_m_showState, true);
            if (showMstate) {
                addView(mstate, new LayoutParams(LayoutParams.MATCH_PARENT, height));
            }
        } else {
            addView(mstate, new LayoutParams(LayoutParams.MATCH_PARENT, height));
        }

        addView(ll, new LayoutParams(LayoutParams.MATCH_PARENT, dip2px(getContext(), NAVIGATIONHEIGHT)));
        addView(mBottomView, new LayoutParams(LayoutParams.MATCH_PARENT, dip2px(getContext(), 1)));
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    public void setStatusBarColor() {
        mstate.setBackgroundColor(getResources().getColor(android.R.color.transparent));
    }

    public void initAttrs(AttributeSet attrs) {
        if (attrs != null) {
            TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.TitleView);
            int mLayoutResource = typedArray.getResourceId(R.styleable.TitleView_m_layout, 0);
            //如果以上布局不支持可以进行自己设置布局样式
            if (mLayoutResource != 0) {
                ll.removeAllViews();
                ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, dip2px(getContext(), NAVIGATIONHEIGHT));
                View view = inflate(getContext(), mLayoutResource, null);
                view.setBackgroundResource(R.color.transparent);
                view.setLayoutParams(layoutParams);
                ll.addView(view);
                typedArray.recycle();
                return;
            }
            addmBottomView = typedArray.getBoolean(R.styleable.TitleView_m_showLine, false);
            mTitleName.setText(typedArray.getString(R.styleable.TitleView_m_titleName));
            int color = typedArray.getColor(R.styleable.TitleView_m_titleColor, getResources().getColor(R.color.c_111111));
            mTitleName.setTextColor(color);
            setIsVisibleLine(typedArray.getBoolean(R.styleable.TitleView_m_isVisibleLine, true));
            Drawable rightDrawable = typedArray.getDrawable(R.styleable.TitleView_m_rightRes);
            if (rightDrawable != null) {
                mRightIV.setImageDrawable(rightDrawable);
            }
            Drawable leftDrawable = typedArray.getDrawable(R.styleable.TitleView_m_leftRes);
            if (leftDrawable != null && mLeftIV != null) {
                mLeftIV.setImageDrawable(leftDrawable);
            }

            typedArray.recycle();
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //绘制完之后设置高度，不然的话如果背景是图片的话会出现过高  设置的高度为状态栏高度加上导航栏高度默认为48dp
        ViewGroup.LayoutParams layoutParams = getLayoutParams();
        layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
        if (addmBottomView) {
            layoutParams.height = height + dip2px(getContext(), NAVIGATIONHEIGHT + 1);
        } else {
            layoutParams.height = height + dip2px(getContext(), NAVIGATIONHEIGHT);

        }
        this.setLayoutParams(layoutParams);
    }

    /**
     * 自定义监听
     */
    public void setOnTitlerClickListener(OnTitleClickListener listener) {
        mListener = listener;
    }

    //下边的线是否可见
    public void setIsVisibleLine(boolean isVisible) {
        mBottomView.setVisibility(isVisible ? VISIBLE : INVISIBLE);
    }

    /**
     * @param titleText 标题
     */
    public void setTitle(String titleText) {
        mTitleName.setText(titleText);
    }

    public TextView getTitle() {
        if (mTitleName == null)
            return new TextView(getContext());
        return mTitleName;
    }

    //设置标题加粗
    public void setTitleBold() {
        mTitleName.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
    }


    public void setTitleColor(@ColorInt int color) {
        mTitleName.setTextColor(color);
    }

    public void setTitleSize(int size) {
        mTitleName.setTextSize(size);
    }

    //获得右边的iv对象,注意和{@link #setFLRightChild}冲突
    public ImageView getIVRight() {
        return mRightIV;
    }

    //获得返回的iv对象(注意返回键有padding属性)
    public ImageView getIVBack() {
        return mLeftIV;
    }

    /**
     * 自定义右侧的view可以根据不同需求自行设置
     *
     * @param view
     */
    public void setFLRightChild(View view) {
        mRightFl.removeAllViews();
        mRightFl.addView(view);
    }

    public abstract static class OnTitleClickListener {
        protected void onBackClick(TitleView TitleView, ImageView IVBack) {
            if (TitleView.getContext() instanceof Activity)
                ((Activity) TitleView.getContext()).finish();
        }

        protected void onTitleClick(TitleView TitleView, String text) {
        }

        protected abstract void onRightClick(TitleView TitleView, ImageView IVRight);
    }

}
