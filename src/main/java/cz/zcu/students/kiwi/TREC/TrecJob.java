package cz.zcu.students.kiwi.TREC;

import cz.zcu.kiv.nlp.ir.trec.SerializedDataHelper;
import cz.zcu.kiv.nlp.ir.trec.data.Document;
import cz.zcu.students.kiwi.IrJob;
import org.apache.log4j.*;

import java.io.*;
import java.util.Collection;
import java.util.List;

public class TrecJob extends IrJob {

    private static final Logger log = Logger.getLogger(TrecJob.class);

    private final TrecJobSettings settings;

    public TrecJob(String... args) {
        this(new TrecJobSettings(args));
    }

    public TrecJob(TrecJobSettings settings) {
        this.settings = settings;
    }

    @Override
    public boolean configureLogger() {
        try {
            File appenderFile = new File(this.settings.getOutputDir() + "/" + SDF.format(System.currentTimeMillis()) + " - trec job log" + ".log");
            Appender appender = new WriterAppender(new PatternLayout(), new FileOutputStream(appenderFile, false));
            BasicConfigurator.configure(appender);
        } catch (FileNotFoundException e) {
            System.err.println("Failed to configure appender");
        }
        return true;
    }


    protected boolean execute() {
        if (!this.ensureDirectoriesExist(this.settings.getOutputDir())) {
            log.error("Could not create output directory");
            return false;
        }

        SearchEngine engine = new SearchEngine().setResultSorter((o1, o2) -> Float.compare(o2.getScore(), o1.getScore()));


        if (!loadAndIndexDocuments(settings.getInputDir() + "/czechData.bin", engine)) {
            log.error("Failed to index documents");
            return false;
        }

        ATrecSearcher searcher;
        switch (this.settings.getMode()) {
            case TopicFile:
                searcher = new TrecTopicSearcher(engine, settings.getInputDir() + "/topicData.bin");
                break;
            default:
                throw new UnsupportedOperationException("Search mode " + this.settings.getMode() + " not implemented");
        }


        File outputFile = new File(settings.getResultFile(SDF.format(System.currentTimeMillis())));

        try (FileOutputStream fos = new FileOutputStream(outputFile)) {
            searcher.run(System.in, fos);
        } catch (IOException ex) {
            log.error(ex);
            return false;
        }

        //try to run evaluation
        try {
            runTrecEval(outputFile.toString(), System.out);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return true;
    }

    private boolean loadAndIndexDocuments(String file, SearchEngine engine) {
        Collection<Document> documents = loadDocuments(file);
        if (documents == null) {
            return false;
        }

        engine.addIndex(new SearchIndex(documents));
        return true;
    }

    private Collection<Document> loadDocuments(String file) {

        File serializedData = new File(file);
        log.info("Loading  documents from " + serializedData.getAbsolutePath() + "");
        if (!serializedData.exists()) {
            log.error("Cannot find " + serializedData);
            return null;
        }
        try {
            List<Document> documents = SerializedDataHelper.loadList(serializedData, Document.class);
            log.info("Loaded documents: " + documents.size());
            return documents;
        } catch (IOException e) {
            log.error(e);
            return null;
        }
    }


    private static boolean runTrecEval(String predictedFile, PrintStream output) {

        String commandLine = "./trec_eval.8.1/./trec_eval" + " ./trec_eval.8.1/czech" + " " + predictedFile;

        log.info("exec(" + commandLine + ")");
        try {
            Process process = Runtime.getRuntime().exec(commandLine);


            try (BufferedReader stdout = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                output.println("TREC EVAL output:");
                String line;
                while ((line = stdout.readLine()) != null) {
                    output.println(line);
                }

                int exitStatus = -42;
                try {
                    exitStatus = process.waitFor();
                } catch (InterruptedException ie) {
                    ie.printStackTrace();
                }
                log.info("Trec eval exited with status: " + exitStatus);
                return true;
            } catch (IOException ex) {
                log.warn("Trec eval process output reading failed: " + ex.toString());
                return false;
            }

        } catch (IOException ex) {
            log.error("Failed to start trec eval: " + ex.toString());
            return false;
        }
    }
}
