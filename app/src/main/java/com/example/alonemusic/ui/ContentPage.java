package com.example.alonemusic.ui;

public enum ContentPage {

    我喜欢的音乐(0),
    我喜欢的评论(1);

    private final int position;

    ContentPage(int pos) {
        position = pos;
    }

    public static ContentPage getPage(int position) {
        return ContentPage.values()[position];
    }

    public static int size() {
        return ContentPage.values().length;
    }

    public static String[] getPageNames() {
        ContentPage[] pages = ContentPage.values();
        String[] pageNames = new String[pages.length];
        for (int i = 0; i < pages.length; i++) {
            pageNames[i] = pages[i].name();
        }
        return pageNames;
    }

    public int getPosition() {
        return position;
    }
}
