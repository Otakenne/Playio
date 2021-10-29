package com.celerii.playio.mods;

import java.util.List;

public class ArtistResponse {
    private ResponseHeader headers;
    private List<Artist> results;

    public ResponseHeader getHeaders() {
        return headers;
    }

    public void setHeaders(ResponseHeader headers) {
        this.headers = headers;
    }

    public List<Artist> getResults() {
        return results;
    }

    public void setResults(List<Artist> results) {
        this.results = results;
    }
}
