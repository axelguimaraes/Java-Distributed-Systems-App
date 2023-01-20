package edu.estg.utils;

public enum RequestType {
    LOCAL_NODE_LOGIN, LOCAL_NODE_REGISTER, ADD_TRAIN_LINE, FEEDBACK_ADD_TRAIN_LINE, LOCAL_NODE_STATISTICS_REQUEST_TO_PASSENGERS, LOCAL_NODE_STATISTICS_RESPONSE_FROM_PASSENGERS,
    PASSENGER_LOGIN, PASSENGER_REGISTER, GET_CURRENT_LOCAL_NODES, ASSOCIATE_TRAIN_LINE, PASSENGER_MESSAGE_TO_NODE, PASSENGER_MESSAGE_FROM_NODE,
    STATISTICS_REQUEST, STATISTICS_RESPONSE,

    FEEDBACK
}
