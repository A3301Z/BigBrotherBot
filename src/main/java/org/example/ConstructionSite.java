package org.example;

public enum ConstructionSite {
    SUVOROV ("ЖК Суворов"),
    TCDOVATORCEV ("ТЦ Доваторцев 65"),
    AVTOMOIKADOVATORCEV ("Автомойка Доваторцев 65"),
    GERCENA ("Герцена 147"),
    UJNYOBHOD ("Южный Обход 1/2"),
    RODDOM ("Роддом");

    ConstructionSite(String buildName) {
        this.buildName = buildName;
    }

    String buildName;

    public String getBuildName() {
        return buildName;
    }
}
