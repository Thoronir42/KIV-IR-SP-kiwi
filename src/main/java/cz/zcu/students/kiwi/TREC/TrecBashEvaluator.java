package cz.zcu.students.kiwi.TREC;

public class TrecBashEvaluator extends TrecEvaluator {

    public TrecBashEvaluator(String trecCommand, String trecReference) {
        super(trecCommand, trecReference);
    }

    @Override
    protected String[] formatTrecCommand(String trecCommand, String trecReference, String compareFile) {
        compareFile = compareFile.replace('\\', '/');

        return new String[]{"bash", "-c", trecCommand + " " + trecReference + " " + compareFile};
    }
}
