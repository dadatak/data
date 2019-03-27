package com.prilaga.data.serialization;

import com.prilaga.data.utils.ListUtil;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by mobexs on 13.07.16.
 */
public class JsonAbleList<T extends JsonAble> extends ArrayList<T> {
    @Override
    public boolean addAll(Collection<? extends T> collection) {
        return ListUtil.isNotEmpty(collection) ? super.addAll(collection) : false;
    }
}
