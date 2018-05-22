package cz.zcu.students.kiwi.TREC;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public abstract class ATrecSearcher {

    protected final SearchEngine engine;

    public ATrecSearcher(SearchEngine engine) {
        this.engine = engine;
    }

    public abstract void run(InputStream in, OutputStream out) throws IOException;
}
