package org.techtown.flashlight;

public class json {
        json(){}
        json(int flashId, String uuid){
            this.flashId = flashId;
            this.uuid = uuid;
        }
        private int flashId;

        private String uuid;

        public int getflashid()
        {
            return flashId;
        }

        public String getuuid()
        {
            return uuid;
        }
    }

