package com.mycomp.domain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Linkman {
    private Long link_id;
    private String link_name;
    private Character link_gender;
    private String link_phone;
    private String link_mobile;
    private String link_email;
    private String link_qq;
    private String link_position;
    private String link_memo;
    private Long link_cust_id;

    // 一个Linkman只对应一个Customer
    private Customer customer;

    @Override
    public String toString() {
        return "Linkman{" +
                "link_id=" + link_id +
                ", link_name='" + link_name + '\'' +
                ", link_gender=" + link_gender +
                ", link_phone='" + link_phone + '\'' +
                ", link_mobile='" + link_mobile + '\'' +
                ", link_email='" + link_email + '\'' +
                ", link_qq='" + link_qq + '\'' +
                ", link_position='" + link_position + '\'' +
                ", link_memo='" + link_memo + '\'' +
                ", link_cust_id=" + link_cust_id +
                // ", customer=" + customer +
                '}';
    }
}
