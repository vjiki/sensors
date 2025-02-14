package org.vjiki.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.vjiki.clients.Scorer;
import org.vjiki.model.Document;
import org.vjiki.model.User;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RecommenderServiceImplTest {


    @InjectMocks
    RecommenderServiceImpl<Document, User> recommenderService;

    @Mock
    Scorer<Document, User> scorer;

    @Test
    void addGetTopDocuments() {

        Document doc = new Document("word");
        User user = new User("user");

        recommenderService.addDocument(doc);

        when(scorer.getScore(any(), any())).thenReturn(10.0);

        List<Document> docs = recommenderService.getTop(user, 10);

        assertFalse(docs.isEmpty());
        assertEquals(doc, docs.getFirst());
    }

    @Test
    void addGetTwoTopDocuments() {

        Document doc = new Document("word");
        Document doc2 = new Document("second");
        User user = new User("user");

        recommenderService.addDocument(doc);
        recommenderService.addDocument(doc2);

        when(scorer.getScore(eq(doc), eq(user))).thenReturn(10.0);
        when(scorer.getScore(eq(doc2), eq(user))).thenReturn(20.0);

        List<Document> docs = recommenderService.getTop(user, 10);

        assertFalse(docs.isEmpty());
        assertEquals(doc2, docs.getFirst());
        assertEquals(2, docs.size());
        assertEquals(doc, docs.getLast());
    }

//    @Test
//    void emptyTopDocuments() {
//
//        User user = new User("user");
//
//        when(scorer.getScore(any(), any())).thenReturn(10.0);
//
//        List<Document> docs = recommenderService.getTop(user, 10);
//
//        assertTrue(docs.isEmpty());
//    }
}