package cz.zcu.students.kiwi.TREC;

import cz.zcu.kiv.nlp.ir.trec.Indexer;
import cz.zcu.kiv.nlp.ir.trec.Searcher;
import cz.zcu.kiv.nlp.ir.trec.data.Result;
import org.apache.log4j.Logger;

import java.util.LinkedList;
import java.util.List;

public class SearchEngine implements Searcher {
    private static final Logger log = Logger.getLogger(SearchEngine.class);


    public List<Result> search(String query) {
        log.error("search(" + query + ") is not implemented yet");
        return new LinkedList<>();
    }

    public Indexer indexer() {
        return documents -> {
            throw new UnsupportedOperationException("search() not implemented yet");
        };
    }
}
