package org.lhh.spongehome.utils;

public class PageRequest {
    /**
     * 当前页号
     */
    private int current =1;
    /**
     * 页面大小
     */
    private int pageSize =10;
    /**
     * 排序字段
     */
    private String sortFiled;
    /**
     * 排序顺序（默认降序）
     */
    private String sortorder = "descend";
}
