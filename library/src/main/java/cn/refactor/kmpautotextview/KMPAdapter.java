package cn.refactor.kmpautotextview;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.TreeSet;

/**
 * Created by Bernhard Müller on 9/1/2016.
 */
public class KMPAdapter extends ArrayAdapter<String> implements Filterable {
    private static final int DEFAULT_HIGHLIGHT = Color.parseColor("#FF4081");
    private static final int DEFAULT_TEXTCOLOR = Color.parseColor("#80000000");
    private static final int DEFAULT_TEXT_PIXEL_SIZE = 14;

    private Context mContext;
    TreeSet<PopupTextBean> mTempDatas;
    private float mTextSize;
    private ColorStateList mHighLightColor, mTextColor;
    private HashSet<String> mItems;
    Filter mFilter;
    private KMPAutoComplTextView mKMPAutoComplTextView;


    public KMPAdapter(Context context, int resource, List<String> items) {
        super(context, resource, 0, items);
        mContext = context;
        mItems = new HashSet<>(items);
        mTempDatas = new TreeSet<>();
        getFilter().filter("");
    }

    @Override
    public void add(String object) {
        super.add(object);
        mItems.add(object);
        getFilter().filter("");
    }

    @Override
    public void addAll(Collection<? extends String> collection) {
        super.addAll(collection);
        mItems.addAll(collection);
        getFilter().filter("");
    }

    @Override
    public void addAll(String... items) {
        super.addAll(items);
        Collections.addAll(this.mItems, items);
        getFilter().filter("");
    }

    @Override
    public int getCount() {
        if (mTempDatas == null)
            return 0;
        return mTempDatas.size();
    }

    public int getItemCount() {
        if (mItems == null)
            return 0;
        return mItems.size();
    }

    @Override
    public String getItem(int position) {
        return new ArrayList<>(mTempDatas).get(position).mTarget;
    }

    public String getItemForPosition(int position) {
        return new ArrayList<>(mItems).get(position);
    }

    public PopupTextBean get(int position) {
        return new ArrayList<>(mTempDatas).get(position);
    }

    @Override
    public int getPosition(String item) {
        return new ArrayList<>(mTempDatas).indexOf(item);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            TextView tv = new TextView(mContext);
            int paddingX = DisplayUtils.dp2px(getContext(), 10.0f);
            int paddingY = DisplayUtils.dp2px(getContext(), 5.0f);
            tv.setPadding(paddingX, paddingY, paddingX, paddingY);

            holder.tv = tv;
            convertView = tv;
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        PopupTextBean bean = get(position);
        SpannableString ss = new SpannableString(bean.mTarget);
        holder.tv.setTextColor(mTextColor == null ? DEFAULT_TEXTCOLOR : mTextColor.getDefaultColor());
        holder.tv.setTextSize(mTextSize == 0 ? DEFAULT_TEXT_PIXEL_SIZE : DisplayUtils.px2sp(getContext(), mTextSize));

        // Change Highlight Color
        if (-1 != bean.mStartIndex) {
            ss.setSpan(new ForegroundColorSpan(mHighLightColor == null ? DEFAULT_HIGHLIGHT : mHighLightColor.getDefaultColor()),
                    bean.mStartIndex, bean.mEndIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            holder.tv.setText(ss);
        } else {
            holder.tv.setText(bean.mTarget);
        }

        return convertView;
    }

    public void setTempDatas(List<PopupTextBean> newDatas) {
        mTempDatas = new TreeSet<>();
        mTempDatas.clear();
        mTempDatas.addAll(newDatas);
    }

    public void init(KMPAutoComplTextView kmpAutoComplTextView, float textSize, ColorStateList textColor, ColorStateList highLightColor) {
        mKMPAutoComplTextView = kmpAutoComplTextView;
        mTextSize = textSize;
        mHighLightColor = highLightColor;
        mTextColor = textColor;
    }

    public void addAllItems(List<String> collection) {
        super.addAll(collection);
        mItems.addAll(collection);
    }

    private class ViewHolder {
        TextView tv;
    }

    @Override
    public Filter getFilter() {
        if (mFilter == null) {
            mFilter = new MyFilter();
        }
        return mFilter;
    }

    private class MyFilter extends Filter {

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();
            ArrayList mList = new ArrayList<>();
            for (int i = 0; i < getItemCount(); i++) {
                mList.add(getItemForPosition(i));
            }

            results.values = mList;
            results.count = mList.size();
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            if (results.count > 0) {
                notifyDataSetChanged();
            } else {
                notifyDataSetInvalidated();
            }
            mKMPAutoComplTextView.matchResult();
        }
    }
}