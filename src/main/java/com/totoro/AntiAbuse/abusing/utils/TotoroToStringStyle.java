package com.totoro.AntiAbuse.abusing.utils;

import org.apache.commons.lang3.builder.ToStringStyle;

public class TotoroToStringStyle {
    private static final ToStringStyle MULTI_LINE_JSON_STYLE = new MultiLineToStringStyle();
    private static final ToStringStyle SIMPLE_STYPE = new SimpleToStringStyle();

    public static ToStringStyle style() {
        return MULTI_LINE_JSON_STYLE;
    }

    public static ToStringStyle simpleStyle() {
        return SIMPLE_STYPE;
    }

    public static class SimpleToStringStyle extends org.apache.commons.lang3.builder.ToStringStyle {
        private static final long serialVersionUID = 1L;

        public SimpleToStringStyle() {

            setUseShortClassName(true);
            setUseIdentityHashCode(true);
            setNullText("");
        }

        private Object readResolve() {
            return SIMPLE_STYPE;
        }

        @Override
        public void append(StringBuffer buffer, String fieldName, Object value, Boolean fullDetail) {

            if (value != null) {
                appendFieldStart(buffer, fieldName);
                appendInternal(buffer, fieldName, value, isFullDetail(fullDetail));
                appendFieldEnd(buffer, fieldName);
            }
        }

    }

    public static class MultiLineToStringStyle extends org.apache.commons.lang3.builder.ToStringStyle {

        private static final long serialVersionUID = 1L;

        public static final ToStringStyle MULTI_LINE_JSON_STYLE = new MultiLineToStringStyle();

        public static ToStringStyle style() {
            return MULTI_LINE_JSON_STYLE;
        }

        public MultiLineToStringStyle() {

            setUseShortClassName(true);
            setUseIdentityHashCode(true);
            setContentStart("{" + System.lineSeparator() + "\t");
            setFieldNameValueSeparator(": ");
            setNullText("");
            setFieldSeparator("," + System.lineSeparator() + "\t");
            setContentEnd(System.lineSeparator() + "}");
        }

        @Override
        public void append(StringBuffer buffer, String fieldName, Object value, Boolean fullDetail) {
            if (value != null) {
                appendFieldStart(buffer, fieldName);
                appendInternal(buffer, fieldName, value, isFullDetail(fullDetail));
                appendFieldEnd(buffer, fieldName);
            }
        }

    }

}
