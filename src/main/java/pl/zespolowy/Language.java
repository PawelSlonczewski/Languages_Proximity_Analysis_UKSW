package pl.zespolowy;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.ToString;

@ToString(of = {"name", "code", "enabled"})
public class Language {
    private String name;
    private String code;
    private boolean enabled;

    public Language() {}

    public Language(String name, String code) {
        this.name = name;
        this.code = code;
        this.enabled = false;
    }

    public Language(String name, String code, boolean enabled) {
        this.name = name;
        this.code = code;
        this.enabled = true;
    }

    @JsonValue
    public String toJson() {return name;}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}
