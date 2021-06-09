package com.nirwal.gailconnect.modal;

public class Link {
    public static final String MIME_URL="url";
    public static final String MIME_PDF="pdf";
    public static final String MIME_INTERNAL="internal";


    private String title;
    private String url;
    private String logo;
    private String mimeType;

    public Link() {
    }

    public Link(String title, String url, String logo, String mimeType) {
        this.title = title;
        this.url = url;
        this.logo = logo;
        this.mimeType = mimeType;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }
}
