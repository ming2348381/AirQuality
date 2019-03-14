package com.example.airquality.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface SqliteTableAnnotation {
    String[] tableName();
}
