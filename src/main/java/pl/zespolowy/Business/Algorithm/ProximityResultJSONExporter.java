package pl.zespolowy.Business.Algorithm;

import lombok.Getter;

@Getter
public class ProximityResultJSONExporter {
    private final LanguageProximityResult lpr;

    public ProximityResultJSONExporter(LanguageProximityResult lpr) {
        this.lpr = lpr;
    }


}
