package com.interswitch.notification.core.entity;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

import java.io.IOException;

/** Any class implementing this interface shall provide methods to
 * convert the {@code object} into JSON data as well as to
 * convert back to the {@code object} from JSON data
 *
 * @param <T> The class of the object
 */
public interface SerializableEntity<T> {
    String serialize() throws JsonProcessingException;

    void deserialize(String data) throws JsonParseException, JsonMappingException, IOException;

}
