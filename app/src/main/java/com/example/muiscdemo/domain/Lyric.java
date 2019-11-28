package com.example.muiscdemo.domain;

public class Lyric extends Base {

    /**
     * 歌词id
     */
    private String id;

    /**
     * 歌词类型，0:LRC，10:KSC
     */
    private int style;

    /**
     * 歌词内容
     */
    private String content;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getStyle() {
        return style;
    }

    public void setStyle(int style) {
        this.style = style;
    }
}

