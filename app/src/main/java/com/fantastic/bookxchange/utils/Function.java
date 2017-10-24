package com.fantastic.bookxchange.utils;

/**
 * Created by dgohil on 10/23/17.
 */

public interface Function<I, O> {
    O apply(I input);
}
