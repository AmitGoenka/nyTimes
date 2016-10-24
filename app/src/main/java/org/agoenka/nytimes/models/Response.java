package org.agoenka.nytimes.models;

import java.util.List;

/**
 * Author: agoenka
 * Created At: 10/24/2016
 * Version: ${VERSION}
 */

public class Response {

    private List<Article> docs;

    public List<Article> getDocs() {
        return docs;
    }

    public Response(List<Article> docs) {
        this.docs = docs;
    }
}
