package cn.refactor.kmpautotextview;

import java.io.Serializable;

public class PopupTextBean implements Serializable, Comparable<PopupTextBean> {
    public String mTarget;
    public int mStartIndex = -1;
    public int mEndIndex = -1;

    public PopupTextBean(String target) {
        this.mTarget = target;
    }

    public PopupTextBean(String target, int startIndex) {
        this.mTarget = target;
        this.mStartIndex = startIndex;
        if (-1 != startIndex) {
            this.mEndIndex = startIndex + target.length();
        }
    }

    public PopupTextBean(String target, int startIndex, int endIndex) {
        this.mTarget = target;
        this.mStartIndex = startIndex;
        this.mEndIndex = endIndex;
    }

    @Override
    public int compareTo(PopupTextBean popupTextBean) {
        return mTarget.compareTo(popupTextBean.mTarget);
    }
}