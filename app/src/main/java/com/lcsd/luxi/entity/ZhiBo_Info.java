package com.lcsd.luxi.entity;

import java.util.List;

/***
 *
 * Json To JavaBean
 * @author www.json123.com
 *
 */
public class ZhiBo_Info {

    public static class TContent{

        private	String	id;	/*702*/
        private	String	title;	/*泸溪电视*/
        private	String	ico;	/*http://luxi.5kah.com/res/ico/7c0fcf37a0648295.png*/
        private	String	linkurl;	/**/
        private	String	zblinker;	/*http://luxi.5kah.com/http://124.112.228.134:1935/live/zongyi/playlist.m3u8*/
        private	String	thumb;	/*http://luxi.5kah.com/res/img/201805/23/bc6961deac5aec60.jpg*/
        private	String	identifier;	/*luxidianshi*/

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

        public void setIco(String value){
            this.ico = value;
        }
        public String getIco(){
            return this.ico;
        }

        public void setLinkurl(String value){
            this.linkurl = value;
        }
        public String getLinkurl(){
            return this.linkurl;
        }

        public void setZblinker(String value){
            this.zblinker = value;
        }
        public String getZblinker(){
            return this.zblinker;
        }

        public void setThumb(String value){
            this.thumb = value;
        }
        public String getThumb(){
            return this.thumb;
        }

        public void setIdentifier(String value){
            this.identifier = value;
        }
        public String getIdentifier(){
            return this.identifier;
        }

    }
    private	List<TContent>	content;	/*List<TContent>*/
    public void setContent(List<TContent> value){
        this.content = value;
    }
    public List<TContent> getContent(){
        return this.content;
    }

    private	String	status;	/*ok*/

    public void setStatus(String value){
        this.status = value;
    }
    public String getStatus(){
        return this.status;
    }

}
