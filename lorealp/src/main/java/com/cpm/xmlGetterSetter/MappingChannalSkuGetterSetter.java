package com.cpm.xmlGetterSetter;

import java.util.ArrayList;

/**
 * Created by upendra on 10-05-2018.
 */
public class MappingChannalSkuGetterSetter {
    String mapping_channel_sku_table;
    ArrayList<String> channel_cd = new ArrayList<String>();
    ArrayList<String> channel = new ArrayList<String>();

    public String getMapping_channel_sku_table() {
        return mapping_channel_sku_table;
    }

    public void setMapping_channel_sku_table(String mapping_channel_sku_table) {
        this.mapping_channel_sku_table = mapping_channel_sku_table;
    }

    public ArrayList<String> getChannel_cd() {
        return channel_cd;
    }

    public void setChannel_cd(String channel_cd) {
        this.channel_cd.add(channel_cd);
    }

    public ArrayList<String> getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel.add(channel);
    }
}
