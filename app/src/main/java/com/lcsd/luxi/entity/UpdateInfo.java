package com.lcsd.luxi.entity;

import java.util.List;

/***
 *
 * Json To JavaBean
 * @author www.json123.com
 *
 */
public class UpdateInfo {

    public static class TContent{

        private	String	version_desc;	/**/
        private	String	version_name;	/**/
        private	String	version_no;	/*0*/
        private	String	version_url;	/**/

        public void setVersion_desc(String value){
            this.version_desc = value;
        }
        public String getVersion_desc(){
            return this.version_desc;
        }

        public void setVersion_name(String value){
            this.version_name = value;
        }
        public String getVersion_name(){
            return this.version_name;
        }

        public void setVersion_no(String value){
            this.version_no = value;
        }
        public String getVersion_no(){
            return this.version_no;
        }

        public void setVersion_url(String value){
            this.version_url = value;
        }
        public String getVersion_url(){
            return this.version_url;
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
