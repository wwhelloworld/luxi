package com.lcsd.luxi.entity;

import java.util.List;

/***
 *
 * Json To JavaBean
 * @author www.json123.com
 *
 */
public class ZB_Info {

    public static class TContent{

        private	String	id;	/*662*/
        private	String	title;	/*电视直播*/
        private	String	ico;	/*http://luxi.5kah.com/res/img/201805/16/c0c047dc34416605.png*/
        private	String	identifier;	/*dszb*/
        private	String	url;	/*http://live.hkstv.hk.lxdns.com/live/hks/playlist.m3u8*/

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

        public void setIdentifier(String value){
            this.identifier = value;
        }
        public String getIdentifier(){
            return this.identifier;
        }

        public void setUrl(String value){
            this.url = value;
        }
        public String getUrl(){
            return this.url;
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
