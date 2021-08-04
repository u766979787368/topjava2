package ru.javawebinar.topjava.util.converters;

import org.springframework.core.convert.converter.Converter;

import java.time.LocalTime;

public final class StringToLocalTimeConverter implements Converter<String, LocalTime> {
    @Override
    public LocalTime convert(String source) {
        if (source.equals("")) return null;
        return LocalTime.parse(source);
    }
}
