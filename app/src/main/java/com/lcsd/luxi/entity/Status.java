package com.lcsd.luxi.entity;

import java.util.List;

/***
 *
 * Json To JavaBean
 * @author www.json123.com
 *
 */
public class Status {

    private	String	content;	/*正常*/
    private	String	status;	/*1*/

    public void setContent(String value){
        this.content = value;
    }
    public String getContent(){
        return this.content;
    }

    public void setStatus(String value){
        this.status = value;
    }
    public String getStatus(){
        return this.status;
    }

}
