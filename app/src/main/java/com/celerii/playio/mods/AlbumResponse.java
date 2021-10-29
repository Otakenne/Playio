package com.celerii.playio.mods;

import java.util.List;

public class AlbumResponse {
    private ResponseHeader headers;
    private List<Album> results;

    public ResponseHeader getHeaders() {
        return headers;
    }

    public void setHeaders(ResponseHeader headers) {
        this.headers = headers;
    }

    public List<Album> getResults() {
        return results;
    }

    public void setResults(List<Album> results) {
        this.results = results;
    }
}
