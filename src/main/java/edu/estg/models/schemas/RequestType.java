package edu.estg.models.schemas;

public enum RequestType {
    // All
    LOGIN, REGISTER,

    // Passenger
    PASSENGER_REPORT,

    // Local node
    EVENT, BROADCAST_EVENT_REQUEST, STATISTICS,

    // Central node
    BROADCAST_EVENT

}
