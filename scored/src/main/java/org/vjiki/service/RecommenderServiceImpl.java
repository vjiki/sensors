package org.vjiki.service;

import org.vjiki.clients.Scorer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;


public class RecommenderServiceImpl<D,U> implements RecommenderService<D, U>{

// TODO reverse?
    private final List<D> listDocs = new LinkedList<>();
    public final Scorer<D, U> scorer;

    public RecommenderServiceImpl(Scorer<D, U> scorer) {
        this.scorer = scorer;
    }

    @Override
    public List<D> getTop(U u, int limit) {

        if (limit <= 0 || u == null) {
            return Collections.emptyList();
        }


        PriorityQueue<Map.Entry<D, Double>> priorityQueue = new PriorityQueue<>(
                (e,v) -> Double.compare(v.getValue(), e.getValue()));


        for (D doc: listDocs) {
            double score = scorer.getScore(doc, u);
            Map.Entry<D, Double> entry = Map.entry(doc, score);    // TODO
            priorityQueue.add(entry);
        }

        List<D> results = new ArrayList<>();

        for (int i = 0; i < limit && !priorityQueue.isEmpty(); i++) {
            results.add(priorityQueue.poll().getKey());
        }

        return results;
    }

    @Override
    public void addDocument(D d) {
        if (d == null) {
            return;
        }

        listDocs.add(d);

    }
}
