package pl.zespolowy;

import lombok.Getter;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

@Getter
public class Translation {
    private String text;

    public Translation(String text) {
        this.text = text;

        int[] invalidCodes = { 10 };
        for (int code : invalidCodes) {
            char c = (char)code;
            this.text = this.text.replace(Character.toString(c), "");
        }
    }

    public List<String> toList() {
        String[] array = text.split("; ");
        return Arrays.asList(array);
    }

    public String multiLine() {
        return text.replace("; ", "\n");
    }

}

