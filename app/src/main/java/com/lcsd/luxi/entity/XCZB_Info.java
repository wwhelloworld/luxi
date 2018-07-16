package com.lcsd.luxi.entity;

import java.util.List;

/***
 *
 * Json To JavaBean
 * @author www.json123.com
 *
 */
public class XCZB_Info {

    public static class TContent{

        public static class TRslist{

            private	String	id;	/*2050*/
            private	String	zbstatus;	/*4*/
            private	String	title;	/*测试赛*/
            private	String	hits;	/*0*/
            private	String	zbaddress;	/**/
            private	String	cate_url;	/**/
            private	String	cate_id;	/*0*/
            private	String	attr;	/**/
            private	String	dateline;	/*1531716125*/
            private	String	thumb;	/*http://luxi.5kah.com/res/img/201805/23/efa0c3ccbab94644.jpg*/
            private	String	url;	/*http://luxi.5kah.com/app/index.php?id=2050*/
            private	String	video;	/**/
            private	String	cate_name;	/**/

            public void setId(String value){
                this.id = value;
            }
            public String getId(){
                return this.id;
            }

            public void setZbstatus(String value){
                this.zbstatus = value;
            }
            public String getZbstatus(){
                return this.zbstatus;
            }

            public void setTitle(String value){
                this.title = value;
            }
            public String getTitle(){
                return this.title;
            }

            public void setHits(String value){
                this.hits = value;
            }
            public String getHits(){
                return this.hits;
            }

            public void setZbaddress(String value){
                this.zbaddress = value;
            }
            public String getZbaddress(){
                return this.zbaddress;
            }

            public void setCate_url(String value){
                this.cate_url = value;
            }
            public String getCate_url(){
                return this.cate_url;
            }

            public void setCate_id(String value){
                this.cate_id = value;
            }
            public String getCate_id(){
                return this.cate_id;
            }

            public void setAttr(String value){
                this.attr = value;
            }
            public String getAttr(){
                return this.attr;
            }

            public void setDateline(String value){
                this.dateline = value;
            }
            public String getDateline(){
                return this.dateline;
            }

            public void setThumb(String value){
                this.thumb = value;
            }
            public String getThumb(){
                return this.thumb;
            }

            public void setUrl(String value){
                this.url = value;
            }
            public String getUrl(){
                return this.url;
            }

            public void setVideo(String value){
                this.video = value;
            }
            public String getVideo(){
                return this.video;
            }

            public void setCate_name(String value){
                this.cate_name = value;
            }
            public String getCate_name(){
                return this.cate_name;
            }

        }
        private	List<TRslist>	rslist;	/*List<TRslist>*/
        public void setRslist(List<TRslist> value){
            this.rslist = value;
        }
        public List<TRslist> getRslist(){
            return this.rslist;
        }

        private	Integer	total;	/*1*/
        private	Integer	psize;	/*10*/
        private	Integer	pageid;	/*1*/

        public void setTotal(Integer value){
            this.total = value;
        }
        public Integer getTotal(){
            return this.total;
        }

        public void setPsize(Integer value){
            this.psize = value;
        }
        public Integer getPsize(){
            return this.psize;
        }

        public void setPageid(Integer value){
            this.pageid = value;
        }
        public Integer getPageid(){
            return this.pageid;
        }

    }
    private	TContent	content;	/*TContent*/
    private	String	status;	/*ok*/

    public void setContent(TContent value){
        this.content = value;
    }
    public TContent getContent(){
        return this.content;
    }

    public void setStatus(String value){
        this.status = value;
    }
    public String getStatus(){
        return this.status;
    }

}
