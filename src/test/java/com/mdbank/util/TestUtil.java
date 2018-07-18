package com.mdbank.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.MediaType;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Collection;
import java.util.Comparator;

import static org.junit.Assert.assertTrue;

public final class TestUtil {
    private TestUtil() {
    }

    public static final MediaType APPLICATION_JSON_UTF8 = new MediaType(
            MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(),
            Charset.forName("utf8")
    );

    public static byte[] convertObjectToJsonBytes(Object object) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        return mapper.writeValueAsBytes(object);
    }

    public static <E> void checkAscending(Collection<E> collection, Comparator<E> comparator){
        E prev = null;
        for (E cur : collection) {
            assertTrue(prev == null || comparator.compare(prev, cur) <= 0);
            prev = cur;
        }
    }
}