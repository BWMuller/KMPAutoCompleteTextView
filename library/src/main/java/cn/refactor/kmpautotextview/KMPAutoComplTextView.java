package cn.refactor.kmpautotextview;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Color;
import androidx.annotation.Nullable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Filterable;
import android.widget.ListAdapter;

import androidx.appcompat.widget.AppCompatAutoCompleteTextView;
import java.util.ArrayList;
import java.util.List;

/**
 * 作者 : andy
 * 日期 : 15/10/26 10:50
 * 邮箱 : andyxialm@gmail.com
 * 描述 : 实现KMP算法的AutoCompleteTextView, 用于字符串模糊匹配
 */
public class KMPAutoComplTextView extends AppCompatAutoCompleteTextView {

    private static final int DEFAULT_HIGHLIGHT = Color.parseColor("#FF4081");
    private static final int DEFAULT_TEXTCOLOR = Color.parseColor("#80000000");
    private static final int DEFAULT_TEXT_PIXEL_SIZE = 14;

    private float mTextSize;
    private boolean mIsIgnoreCase;
    private KMPAdapter mAdapter;
    private String input;

    private ColorStateList mHighLightColor, mTextColor;
    private OnPopupItemClickListener mListener;
    private boolean mShowCurrentTextAsOption;

    public KMPAutoComplTextView(Context context) {
        this(context, null);
    }

    public KMPAutoComplTextView(Context context, AttributeSet attrs) {
        this(context, attrs, android.R.attr.autoCompleteTextViewStyle);
    }

    public KMPAutoComplTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    /**
     * 获得字符串的next函数值
     *
     * @param mode 字符串
     * @return next函数值
     */
    private static int[] next(char[] mode) {
        int[] next = new int[mode.length];
        next[0] = -1;
        int i = 0;
        int j = -1;
        while (i < mode.length - 1) {
            if (j == -1 || mode[i] == mode[j]) {
                i++;
                j++;
                if (mode[i] != mode[j]) {
                    next[i] = j;
                } else {
                    next[i] = next[j];
                }
            } else {
                j = next[j];
            }
        }
        return next;
    }

    private void init(Context context, AttributeSet attrs) {
        if (attrs != null) {
            final TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.KMPAutoComplTextView);
            mTextColor = a.getColorStateList(R.styleable.KMPAutoComplTextView_completionTextColor);
            mHighLightColor = a.getColorStateList(R.styleable.KMPAutoComplTextView_completionHighlightColor);
            mTextSize = a.getDimensionPixelSize(R.styleable.KMPAutoComplTextView_completionTextSize, DEFAULT_TEXT_PIXEL_SIZE);
            mIsIgnoreCase = a.getBoolean(R.styleable.KMPAutoComplTextView_completionIgnoreCase, false);
            a.recycle();
        }
        initListener();
    }

    private void initListener() {

        addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                onInputTextChanged(s.toString());
            }
        });

    }

    public void requestDropdownOpen() {
        if (getText() != null && !TextUtils.isEmpty(getText().toString()))
            onInputTextChanged(getText().toString());
    }

    private void onInputTextChanged(String input) {
        matchResult(input);

        if (mAdapter.getCount() == 0) {
            KMPAutoComplTextView.this.dismissDropDown();
            return;
        }
        mAdapter.notifyDataSetChanged();

        if (!KMPAutoComplTextView.this.isPopupShowing() || mAdapter.getCount() > 0) {
            showDropDown();
        }
    }

    @Override
    public <T extends ListAdapter & Filterable> void setAdapter(T adapter) {
        mAdapter = (KMPAdapter) adapter;
        mAdapter.init(this, mTextSize, mTextColor, mHighLightColor);
        super.setAdapter(adapter);
    }

    public void setOnPopupItemClickListener(OnPopupItemClickListener listener) {
        mListener = listener;
        this.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (mListener == null) {
                    return;
                }

                Object item = null;
                try {
                    if (mShowCurrentTextAsOption && position == 0) {
                        //Ignoring item so that it is clear that this is the current text item
                    } else {
                        item = view.getTag(R.id.adapter_item);
                    }
                } catch(Throwable ignored) {
                }
                mListener.onPopupItemClick(item, KMPAutoComplTextView.this.getText().toString());
            }
        });

    }

    protected void matchResult() {
        matchResult(input);
    }

    protected void matchResult(String input) {
        this.input = input;
        List<PopupTextBean> resultDatas = getResultDatas();
        if (TextUtils.isEmpty(input) || resultDatas == null || resultDatas.size() == 0) {
            return;
        }

        List<PopupTextBean> listDatas = new ArrayList<PopupTextBean>();
        List<Object> newDataStrings = new ArrayList<Object>();
        for (PopupTextBean resultBean : resultDatas) {
            int matchIndex = matchString(resultBean.mTarget.toString(), input, mIsIgnoreCase);
            if (-1 != matchIndex) {
                PopupTextBean bean = new PopupTextBean(resultBean.mTarget, matchIndex, matchIndex + input.length());
                listDatas.add(bean);
                newDataStrings.add(resultBean.mTarget);
            }
        }
        KMPBeanSet newDatas = KMPBeanSet.create();
        newDatas.addAll(listDatas);
        if (mShowCurrentTextAsOption) {
            boolean found = false;
            for (Object item : newDataStrings) {
                if (item.toString().equalsIgnoreCase(input)) {
                    found = true;
                }
            }
            if (!found) {
                int matchIndex = matchString(input, input, mIsIgnoreCase);
                if (-1 != matchIndex) {
                    PopupTextBean bean = new PopupTextBean(input, matchIndex, matchIndex + input.length());
                    newDatas.setActiveText(bean);
                }
            } else {

            }
        }

        mAdapter.setTempDatas(newDatas);

        mAdapter.clear();
        mAdapter.addAllItems(newDataStrings);
        mAdapter.notifyDataSetChanged();
    }

    public boolean isShowCurrentTextAsOption() {
        return mShowCurrentTextAsOption;
    }

    public void setShowCurrentTextAsOption(boolean showCurrentTextAsOption) {
        this.mShowCurrentTextAsOption = showCurrentTextAsOption;
    }

    /**
     * 设置数据集
     *
     * @param dataItems
     */
    public void setDatas(List<Object> dataItems) {
        if (mAdapter == null) {
            mAdapter = new KMPAdapter(getContext(), 0, dataItems);
            setAdapter(mAdapter);
        } else {
            mAdapter.clear();
            mAdapter.addAll(dataItems);
        }
    }

    private List<PopupTextBean> getResultDatas() {
        if (mAdapter == null || mAdapter.getItemCount() == 0) {
            return null;
        }

        List<PopupTextBean> list = new ArrayList<PopupTextBean>();
        for (int i = 0; i < mAdapter.getItemCount(); i++) {
            list.add(new PopupTextBean(mAdapter.getItemForPosition(i)));
        }

        return list;
    }

    public boolean getMatchIgnoreCase() {
        return mIsIgnoreCase;
    }

    public void setMatchIgnoreCase(boolean ignoreCase) {
        mIsIgnoreCase = ignoreCase;
    }

    /**
     * KMP匹配字符串
     *
     * @param source       主串
     * @param modeStr      模式串
     * @param isIgnoreCase 是否忽略大小写
     * @return 若匹配成功，返回下标，否则返回-1
     */
    public int matchString(CharSequence source, CharSequence modeStr, boolean isIgnoreCase) {
        char[] modeArr = modeStr.toString().toCharArray();
        char[] sourceArr = source.toString().toCharArray();
        int[] next = next(modeArr);
        int i = 0;
        int j = 0;
        while (i <= sourceArr.length - 1 && j <= modeArr.length - 1) {
            if (isIgnoreCase) {
                if (j == -1 || sourceArr[i] == modeArr[j] || String.valueOf(sourceArr[i]).equalsIgnoreCase(String.valueOf(modeArr[j]))) {
                    i++;
                    j++;
                } else {
                    j = next[j];
                }
            } else {
                if (j == -1 || sourceArr[i] == modeArr[j]) {
                    i++;
                    j++;
                } else {
                    j = next[j];
                }
            }
        }
        if (j < modeArr.length) {
            return -1;
        } else
            return i - modeArr.length; // 返回模式串在主串中的头下标
    }

    public interface OnPopupItemClickListener {

        /**
         *
         * @param item Nullable item. If null then its the current text item. Otherwise it should be the item as put into this auto complete
         * @param charSequence The text that is associated to this item
         */
        void onPopupItemClick(@Nullable Object item, CharSequence charSequence);
    }
}