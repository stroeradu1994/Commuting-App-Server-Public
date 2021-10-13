package com.commuting.commutingapp.notification.model;

public enum NotificationType {
    MATCH_FOUND("A fost gasita o cursa potrivita"),
    PASSENGER_JOIN("Un nou pasager se va alatura cursei tale"),
    PASSENGER_LEFT("Un pasager a decis sa nu ti se alature"),
    TRIP_CONFIRMED("Cursa confirmata"),
    TRIP_STARTED("Cursa a inceput"),
    TRIP_EXPIRED("Cursa a expirat"),
    TRIP_COMPLETED("Cursa a fost finalizată"),
    PROMPT_TO_CONFIRM("Confirma cursa"),
    TRIP_REQUEST_DELETED("Cererea de cursa a expirat"),
    TRIP_REQUEST_REMINDER("Alege o cursa"),
    PROMPT_TO_START("Nu uita sa anunti inceperea cursei"),
    TRIP_STARTING_IN_10("Cursa programata sa inceapa in 10 minute"),
    DRIVER_ARRIVED_AT_PICKUP("Soferul a ajuns la punctul de intalnire"),
    DRIVER_CONFIRMED_PICKUP("Soferul a confirmarea ridicarea ta"),
    DRIVER_CONFIRMED_DROP("Soferul a confirmat separarea");

    private final String label;

    public String getLabel() {
        return label;
    }

    private NotificationType(String label) {
        this.label = label;
    }

}
