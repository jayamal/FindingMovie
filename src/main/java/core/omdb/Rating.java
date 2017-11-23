
package core.omdb;

import java.util.HashMap;
import java.util.Map;

public class Rating {

    private String source;
    private String value;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}
