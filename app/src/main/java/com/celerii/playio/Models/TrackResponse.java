package com.celerii.playio.Models;

import java.util.List;

public class TrackResponse {
    private ResponseHeader headers;
    private List<Track> results;

    public ResponseHeader getHeaders() {
        return headers;
    }

    public void setHeaders(ResponseHeader headers) {
        this.headers = headers;
    }

    public List<Track> getResults() {
        return results;
    }

    public void setResults(List<Track> results) {
        this.results = results;
    }
}
