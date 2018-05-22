package cz.zcu.kiv.nlp.ir.trec.data;

/**
 * Created by Tigi on 8.1.2015.
 */
public class ResultImpl extends AbstractResult {
    public ResultImpl() {

    }

    public ResultImpl(String documentID, int rank, float score) {
        this.setDocumentID(documentID);
        this.setRank(rank);
        this.setScore(score);
    }
}
