package cz.zcu.students.kiwi.TREC;

import cz.zcu.kiv.nlp.ir.trec.Searcher;
import cz.zcu.kiv.nlp.ir.trec.data.Result;
import org.apache.log4j.Logger;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

public class SearchEngine implements Searcher {
    private static final Logger log = Logger.getLogger(SearchEngine.class);

    private final SearchIndex index;

    private Comparator<Result> resultSorter;

    public SearchEngine() {
        this.index = new SearchIndex();
    }

    public SearchEngine(SearchIndex index) {
        this();
        this.addIndex(index);
    }

    public List<Result> search(String query) {
        log.error("search(" + query + ") is not implemented yet");
        LinkedList<Result> results = new LinkedList<>();

        if (this.resultSorter != null) {
            results.sort(this.resultSorter);
        }

        return results;
    }

    public void addIndex(SearchIndex index) {
        this.index.merge(index);
    }

    public SearchEngine setResultSorter(Comparator<Result> resultSorter) {
        this.resultSorter = resultSorter;
        return this;
    }
}
