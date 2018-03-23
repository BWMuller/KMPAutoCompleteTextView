package cn.refactor.kmpautotextview;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

/**
 * Created by Bernhard Müller on 3/23/2018.
 */

class KMPBeanSet {
    private TreeSet<PopupTextBean> mBeans;
    private PopupTextBean activeTextBean;

    private KMPBeanSet() {
        mBeans = new TreeSet<>();
    }

    static KMPBeanSet create() {
        return new KMPBeanSet();
    }

    int size() {
        return asArray().size();
    }

    PopupTextBean get(int position) {
        return asArray().get(position);
    }

    private ArrayList<PopupTextBean> asArray() {
        ArrayList<PopupTextBean> array = new ArrayList<PopupTextBean>();
        if (activeTextBean != null)
            array.add(activeTextBean);
        array.addAll(mBeans);
        return array;
    }

    /**
     * Returns the index of the first occurrence of the specified element
     * in this list, or -1 if this list does not contain the element.
     * More formally, returns the lowest index <tt>i</tt> such that
     * <tt>(o==null&nbsp;?&nbsp;get(i)==null&nbsp;:&nbsp;o.equals(get(i)))</tt>,
     * or -1 if there is no such index.
     */
    int indexOf(Object item) {
        if (item instanceof String) {
            ArrayList<PopupTextBean> beanList = asArray();
            for (int i = 0; i < beanList.size(); i++) {
                if (beanList.get(i).mTarget.equals(item))
                    return activeTextBean == null ? i : i + 1;
            }
            return -1;
        } else {
            return asArray().indexOf(item);
        }
    }

    void clear() {
        mBeans.clear();
    }

    void addAll(List<PopupTextBean> newDatas) {
        mBeans.addAll(newDatas);
    }

    void addAll(KMPBeanSet newDatas) {
        mBeans.addAll(newDatas.mBeans);
        activeTextBean = newDatas.activeTextBean;
    }

    void setActiveText(PopupTextBean bean) {
        activeTextBean = bean;
    }
}
