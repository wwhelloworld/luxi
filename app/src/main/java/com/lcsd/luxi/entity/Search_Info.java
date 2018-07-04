package com.lcsd.luxi.entity;

import java.util.List;

/***
 *
 * Json To JavaBean
 * @author www.json123.com
 *
 */
public class Search_Info {

    public static class TContent{

        public static class TRslist{

            private	String	cate_id;	/*690*/
            private	String	attr;	/**/
            private	String	dateline;	/*1527071965*/
            private	String	cate_identifier;	/*czlx*/
            private	String	url;	/*http://luxi.5kah.com/app/index.php?id=2037&project=demand*/
            private	String	id;	/*2037*/
            private	String	title;	/*进哥镜头下的泸溪-19*/
            private	String	hits;	/*0*/
            private	String	source;	/**/
            private	String	writer;	/**/
            private	String	thumb;	/*http://luxi.5kah.com/res/img/201805/23/462b6897b779e36d.jpg*/
            private	String	note;	/**/
            private	String	video;	/*http://luxi.5kah.com/res/videomicro/20180523/d94bbfa5b85240fa.mp4*/
            private	String	cate_name;	/*创新泸溪*/

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

            public void setCate_identifier(String value){
                this.cate_identifier = value;
            }
            public String getCate_identifier(){
                return this.cate_identifier;
            }

            public void setUrl(String value){
                this.url = value;
            }
            public String getUrl(){
                return this.url;
            }

            public void setId(String value){
                this.id = value;
            }
            public String getId(){
                return this.id;
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

            public void setSource(String value){
                this.source = value;
            }
            public String getSource(){
                return this.source;
            }

            public void setWriter(String value){
                this.writer = value;
            }
            public String getWriter(){
                return this.writer;
            }

            public void setThumb(String value){
                this.thumb = value;
            }
            public String getThumb(){
                return this.thumb;
            }

            public void setNote(String value){
                this.note = value;
            }
            public String getNote(){
                return this.note;
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

        private	String	total;	/*11*/
        private	String	keywords;	/*泸*/
        private	Integer	total_page;	/*2*/
        private	Integer	psize;	/*10*/
        private	Integer	pageid;	/*1*/

        public void setTotal(String value){
            this.total = value;
        }
        public String getTotal(){
            return this.total;
        }

        public void setKeywords(String value){
            this.keywords = value;
        }
        public String getKeywords(){
            return this.keywords;
        }

        public void setTotal_page(Integer value){
            this.total_page = value;
        }
        public Integer getTotal_page(){
            return this.total_page;
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
