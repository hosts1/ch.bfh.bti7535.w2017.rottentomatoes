package utils;

public class Review {
    public String name;
    public String reviewText;
    public String sentimentClass;

    public Review(String name, String reviewText, String sentimentClass){
        this.name = name;
        this.reviewText = reviewText;
        this.sentimentClass = sentimentClass;
    }

    public String getText(){
        return this.reviewText;
    }

    public String getName(){
        return this.name;
    }

    public String getSentimentClass(){
        return this.sentimentClass;
    }

}
