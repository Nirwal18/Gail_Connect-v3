package com.nirwal.gailconnect.modal;

public class Word {
    private String english;
    private String hindi;

    public Word() {
    }

    public Word(String english, String hindi) {
        this.english = english;
        this.hindi = hindi;
    }

    public String getEnglish() {
        return english;
    }

    public void setEnglish(String english) {
        this.english = english;
    }

    public String getHindi() {
        return hindi;
    }

    public void setHindi(String hindi) {
        this.hindi = hindi;
    }
}
