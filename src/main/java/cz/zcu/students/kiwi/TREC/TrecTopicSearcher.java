package cz.zcu.students.kiwi.TREC;

import cz.zcu.kiv.nlp.ir.trec.SerializedDataHelper;
import cz.zcu.kiv.nlp.ir.trec.data.Result;
import cz.zcu.kiv.nlp.ir.trec.data.ResultImpl;
import cz.zcu.kiv.nlp.ir.trec.data.Topic;

import java.io.*;
import java.util.List;

public class TrecTopicSearcher extends ATrecSearcher {

    private final String topicsFile;

    public TrecTopicSearcher(SearchEngine engine, String topicsFile) {
        super(engine);
        this.topicsFile = topicsFile;
    }

    public void run(InputStream inputStream, OutputStream outputStream) throws IOException {
        List<Topic> topics = SerializedDataHelper.loadTopics(new File(this.topicsFile));

        PrintStream ps = new PrintStream(outputStream, true, "UTF-8");

        for (Topic t : topics) {
            List<Result> resultHits = engine.search(t.getTitle() + " " + t.getDescription());

            if (resultHits.isEmpty()) {
                resultHits.add(new ResultImpl("abc", 99, 0.0f));
            }

            for (Result r : resultHits) {
                String line = t.getId() + " Q0 " + r.getDocumentID() + " " + r.getRank() + " " + r.getScore() + " runindex1";
                ps.println(line);
            }
        }
    }
}
