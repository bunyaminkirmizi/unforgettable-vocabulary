package com.example.unforgottablevocabulary;


public class Word {
    private final String word;
    private String pronunciation = null;
    private int rank = 0;
    private int wordID;

    Word(String word, String pronunciation, int rank, int wordID) {
        this.word = word;
        this.pronunciation = pronunciation;
        this.rank = rank;
        this.wordID = wordID;
    }


    Word(String word) {
        this.word = word;
    }

    String getWord() {
        return word;
    }

    void rankUp() {
        rank += 1;
    }

    String getPronunciation() {
        return pronunciation;
    }

    void rankDown() {
        rank -= 1;
    }

    int getRank() {
        return rank;
    }

    @Override
    public String toString() {
        return "Word {" + '\n' +
                "wordID='" + wordID + '\'' + '\n' +
                "word='" + word + '\'' + '\n' +
                ", pronunciation='" + pronunciation + '\'' + '\n' +
                ", rank=" + rank + '\n' +
                '}';
    }

    int getWordID() {
        return wordID;
    }

    void setWordID(int wordID) {
        this.wordID = wordID;
    }
}