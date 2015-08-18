package com.xamoom.android.mapping;

import com.xamoom.android.APICallback;

import java.util.List;

/**
 * Used for mapping contentList reponsens from xamoom-cloud-api.
 * ContentList will have a List of Content items, a cursor
 * and a more.
 *
 * For paging
 * - use the same pageSize on every call
 * - use the returned cursor after the first call
 * - check the boolean more
 *
 * @author Raphael Seher
 *
 * @see Content
 * @see com.xamoom.android.XamoomEndUserApi#getContentList(String, int, String, String[], APICallback)
 */
public class ContentList {
    private List<Content> items;
    private String cursor;
    private boolean more;

    @Override
    public String toString() {
        String output = "";
        for (Content content : items) {
            output += content + "\n";
        }

        return output + "cursor: " + cursor + ", more: " + more;
    }

    //getter
    public List<Content> getItems() {
        return items;
    }

    public String getCursor() {
        return cursor;
    }

    public boolean isMore() {
        return more;
    }
}
