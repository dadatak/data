package com.prilaga.data.utils;

import android.util.SparseArray;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by Oleg Tarashkevich on 02.10.15.
 */
public final class ListUtil {

    private ListUtil() {
    }

    public static <T> T getFirst(List<T> list) {
        return list != null && !list.isEmpty() ? list.get(0) : null;
    }

    public static <T> T getItem(List<T> list, int position) {
        return list != null && !list.isEmpty() && list.size() > position ? list.get(position) : null;
    }

    public static <T> T getLast(List<T> list) {
        return list != null && !list.isEmpty() ? list.get(list.size() - 1) : null;
    }

    public static <T> boolean isEmpty(Collection<? extends T> collection) {
        return collection == null || collection.isEmpty();
    }

    public static <T> boolean isNotEmpty(Collection<? extends T> collection) {
        return !isEmpty(collection);
    }

    public static boolean isEmpty(Object[] array) {
        return array == null || array.length == 0;
    }

    public static boolean isNotEmpty(Object[] array) {
        return !isEmpty(array);
    }

    public static <T> boolean isEmpty(Map<? extends T, ? extends T> map) {
        return map == null || map.isEmpty();
    }

    public static <T> boolean isNotEmpty(Map<? extends T, ? extends T> map) {
        return !isEmpty(map);
    }

    public static <T> boolean isEmpty(SparseArray<? extends T> sparseArray) {
        return sparseArray == null || sparseArray.size() == 0;
    }

    public static <T> boolean isNotEmpty(SparseArray<? extends T> sparseArray) {
        return !isEmpty(sparseArray);
    }

    public static <C> List<C> toList(SparseArray<C> sparseArray) {
        if (sparseArray == null) return null;
        List<C> arrayList = new ArrayList<C>(sparseArray.size());
        for (int i = 0; i < sparseArray.size(); i++)
            arrayList.add(sparseArray.valueAt(i));
        return arrayList;
    }

    public static <C> SparseArray<C> toSparseArray(List<C> list) {
        if (list == null) return null;
        SparseArray<C> sparseArray = new SparseArray<>(list.size());
        for (C object : list)
            sparseArray.put(object.hashCode(), object);
        return sparseArray;
    }

    public static <T> int indexOf(T needle, T[] haystack) {
        for (int i = 0; i < haystack.length; i++) {
            if (haystack[i] != null && haystack[i].equals(needle)
                    || needle == null && haystack[i] == null) return i;
        }

        return -1;
    }

    public static <T> Set<T> findDuplicates(Collection<T> list) {
        Set<T> duplicates = new HashSet<T>();
        Set<T> uniques = new HashSet<T>();
        for (T t : list) {
            if (!uniques.add(t)) {
                duplicates.add(t);
            }
        }
        return duplicates;
    }
}