package com.desafio.moviesbattle.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class FilmeResponse implements Comparable<FilmeResponse> {

    @JsonProperty("Title")
    private  String title;
    private  String imdbRating;

    public FilmeResponse(String title, String imdbRating) {
        this.title = title;
        this.imdbRating = imdbRating;
    }

    @Override
    public String toString() {
        return "FilmeResponse{" +
                "title='" + title + '\'' +
                ", imdbRating='" + imdbRating + '\'' +
                '}';
    }





    @Override
    public int compareTo(FilmeResponse o) {
        double f1 = Double.parseDouble(o.getImdbRating());
        double f2 = Double.parseDouble(this.getImdbRating());
        return Double.compare(f1, f2);    }
}
