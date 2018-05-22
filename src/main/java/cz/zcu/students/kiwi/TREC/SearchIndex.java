package cz.zcu.students.kiwi.TREC;

import cz.zcu.kiv.nlp.ir.trec.data.Document;
import org.apache.log4j.Logger;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class SearchIndex {
    private static final Logger log = Logger.getLogger(SearchIndex.class);


    private final Map<DocumentKey, Document> documents;

    public SearchIndex() {
        this.documents = new HashMap<>();
    }

    public SearchIndex(Collection<Document> documents) {
        this();
        this.index(documents);
    }

    public boolean index(Document document) {
        log.info("Adding document " + document.getId() + " to index");
        log.warn("Document adding is not implemented yet");

        calculateDocumentInfo();

        return false;
    }

    public void index(Collection<Document> documents) {
        log.info("Adding " + documents.size() + " documents to index");
        log.warn("Document adding is not implemented yet");

        calculateDocumentInfo();
    }

    public void merge(SearchIndex index) {
        log.info("Merging index with " + index.getDocumentCount() + " documents");
        this.index(index.documents.values());
    }

    public int getDocumentCount() {
        return this.documents.size();
    }

    private void calculateDocumentInfo() {
        log.info("Calculating <stuff> with " + this.getDocumentCount() + " documents");
        log.warn("calculateDocumentInfo() is not implemented yet");
    }
}
