package org.agoenka.nytimes.models;

/**
 * Author: agoenka
 * Created At: 10/24/2016
 * Version: ${VERSION}
 */

public class ResponseWrapper {

    private Response response;

    public ResponseWrapper(Response response) {
        this.response = response;
    }

    public Response getResponse() {
        return response;
    }

}
