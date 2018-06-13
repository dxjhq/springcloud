package com.hhly.elasticsearch.model;


/**
 * @author wangxianchen
 * @create 2017-10-19
 * @desc ES分页实体封装
 */
public class EsPage {

    //当前页数
    private int page = 1;

    //ES默认起始位置为0
    private int from = 0;

    //分页大小
    private int size = 10;

    public EsPage(){
        super();
    }
    public EsPage(int page){
        this.setPage(page);
    }
    public EsPage(int page,int size){
        this.setPage(page);
        this.setSize(size);
    }
    public int getFrom() {
        return (page-1)*size;
    }

    public int getSize() {
        return size;
    }

    private void setSize(int size) {
        if(size <= 0){
            size = 10;
        }
        this.size = size;
    }

    public int getPage() {
        return page;
    }

    private void setPage(int page) {
        if(page <= 0){
            page = 1;
        }
        this.page = page;
    }

}
