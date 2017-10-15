package com.fantastic.bookxchange.fragments;

import com.fantastic.bookxchange.models.Book;

import java.util.List;

/**
 * Created by m3libea on 10/14/17.
 */

public interface BookDataReceiver {
    void pushData(List<Book> books);
}
