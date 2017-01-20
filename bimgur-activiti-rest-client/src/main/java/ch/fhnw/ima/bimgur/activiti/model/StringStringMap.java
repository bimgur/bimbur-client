package ch.fhnw.ima.bimgur.activiti.model;


import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Helper class to work around the fact that the JSON (de)serializer is not able to infer
 * type information of <code>Map&lt;String, String&gt;</code> due to Java's type erasure.
 */
@SuppressWarnings("unused") // needed for JSON (de)serialization
public class StringStringMap extends LinkedHashMap<String, String> {

    public StringStringMap() {
        // needed for JSON (de)serialization
    }

    public StringStringMap(Map<String, String> values) {
        super(values);
    }

}