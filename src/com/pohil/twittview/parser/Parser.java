package com.pohil.twittview.parser;

/**
 * Created by alex on 26.06.14.
 */
public interface Parser<T> {
    public T parse(String json);
}
