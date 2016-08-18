package com.mahmoud.movies.modules;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mahmoud on 8/11/2016.
 */
public class Reviews {

    public int getMovie_id() {
        return movie_id;
    }

    public void setMovie_id(int movie_id) {
        this.movie_id = movie_id;
    }

    /**
     * id : 209112
     * page : 1
     * results : [{"id":"56f4f0bd9251417a440017bd","author":"Rahul Gupta","content":"Awesome moview. Best Action sequenceind of stayed low in this
     * total_pages : 1
     * total_results : 4
     */

    private int movie_id;


    private int total_results;
    /**
     * id : 56f4f0bd9251417a440017bd
     * author : Rahul Gupta
     * content : Awesome moview. Best Action sequence.

     **Slow in the first half**
     * url : https://www.themoviedb.org/review/56f4f0bd9251417a440017bd
     */

    private ArrayList<ResultsBean> results;

    public int getTotal_results() {
        return total_results;
    }

    public void setTotal_results(int total_results) {
        this.total_results = total_results;
    }

    public ArrayList<ResultsBean> getResults() {
        return results;
    }

    public void setResults(ArrayList<ResultsBean> results) {
        this.results = results;
    }

    public static class ResultsBean {
        private String id;
        private String author;
        private String content;
        private String url;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getAuthor() {
            return author;
        }

        public void setAuthor(String author) {
            this.author = author;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }
}
