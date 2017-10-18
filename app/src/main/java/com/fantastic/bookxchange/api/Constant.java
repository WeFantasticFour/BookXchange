package com.fantastic.bookxchange.api;

/**
 * Created by gretel on 10/17/17.
 */

public class Constant {
    public static final String BASE_URL = "http://openlibrary.org/";
    public static final String STATIC_BASE_URL = "http://covers.openlibrary.org/b/olid/";
    public static final String SEARCH = "search.json";
    public static final String BIBKEYS = "?bibkeys=ISBN:";
    public static final String DETAILS = "&jscmd=details";
    public static final String FORMAT = "&format=json";
    public static final String GET_BOOK = "books";

    public static final String URL_GET_BOOK = GET_BOOK + BIBKEYS + "{bibkeys}" + DETAILS + FORMAT;
}
