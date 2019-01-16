package cn.refactor.kmpautotextview;

import java.io.Serializable;

public class PopupTextBean implements Serializable, Comparable<PopupTextBean> {
    public Object mTarget;
    public int mStartIndex = -1;
    public int mEndIndex = -1;

    public PopupTextBean(Object target) {
        this.mTarget = target;
    }

    public PopupTextBean(Object target, int startIndex) {
        this.mTarget = target;
        this.mStartIndex = startIndex;
        if (-1 != startIndex) {
            this.mEndIndex = startIndex + target.toString().length();
        }
    }

    public PopupTextBean(Object target, int startIndex, int endIndex) {
        this.mTarget = target;
        this.mStartIndex = startIndex;
        this.mEndIndex = endIndex;
    }

    @Override
    public int compareTo(PopupTextBean popupTextBean) {
        return mTarget.toString().compareTo(popupTextBean.mTarget.toString());
    }
}