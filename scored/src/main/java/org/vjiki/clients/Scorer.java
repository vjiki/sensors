package org.vjiki.clients;

public interface Scorer<Document, User> {
    double getScore(Document doc, User user);
}