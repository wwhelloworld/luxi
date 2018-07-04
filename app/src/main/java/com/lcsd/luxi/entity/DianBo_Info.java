package com.lcsd.luxi.entity;

import java.util.List;

/***
 *
 * Json To JavaBean
 * @author www.json123.com
 *
 */
public class DianBo_Info {

    public static class TContent {

        private String id;    /*664*/
        private String title;    /*大美泸溪*/
        private String ico;    /*http://luxi.5kah.com/res/ico/f751ec1d39ed3ed3.png*/
        private String identifier;    /*dmlx*/

        public void setId(String value) {
            this.id = value;
        }

        public String getId() {
            return this.id;
        }

        public void setTitle(String value) {
            this.title = value;
        }

        public String getTitle() {
            return this.title;
        }

        public void setIco(String value) {
            this.ico = value;
        }

        public String getIco() {
            return this.ico;
        }

        public void setIdentifier(String value) {
            this.identifier = value;
        }

        public String getIdentifier() {
            return this.identifier;
        }

    }

    private List<TContent> content;    /*List<TContent>*/

    public void setContent(List<TContent> value) {
        this.content = value;
    }

    public List<TContent> getContent() {
        return this.content;
    }

    private String status;    /*ok*/

    public void setStatus(String value) {
        this.status = value;
    }

    public String getStatus() {
        return this.status;
    }

}
