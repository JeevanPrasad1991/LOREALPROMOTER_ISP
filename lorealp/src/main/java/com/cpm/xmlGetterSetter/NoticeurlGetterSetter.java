package com.cpm.xmlGetterSetter;

import java.util.ArrayList;

/**
 * Created by rishikaa on 10-05-2016.
 */
public class NoticeurlGetterSetter {
    String noticeurl_table;
    ArrayList<String> NOTICE_URL = new ArrayList<String>();

    public String getNoticeurl_table() {
        return noticeurl_table;
    }

    public void setNoticeurl_table(String noticeurl_table) {
        this.noticeurl_table = noticeurl_table;
    }

    public ArrayList<String> getNOTICE_URL() {
        return NOTICE_URL;
    }

    public void setNOTICE_URL(String NOTICE_URL) {
        this.NOTICE_URL.add(NOTICE_URL);
    }
}