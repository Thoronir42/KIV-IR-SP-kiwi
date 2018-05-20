package cz.zcu.students.kiwi.TREC;

import cz.zcu.kiv.nlp.ir.trec.IOUtils;
import cz.zcu.kiv.nlp.ir.trec.SerializedDataHelper;
import cz.zcu.kiv.nlp.ir.trec.data.Document;
import cz.zcu.kiv.nlp.ir.trec.data.Result;
import cz.zcu.kiv.nlp.ir.trec.data.Topic;
import cz.zcu.students.kiwi.IrJob;
import org.apache.log4j.*;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
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

        SearchEngine index = new SearchEngine();

        List<Topic> topics = SerializedDataHelper.loadTopics(new File(settings.getOutputDir() + "/topicData.bin"));

        File serializedData = new File(settings.getOutputDir() + "/czechData.bin");
        if (!serializedData.exists()) {
            log.error("Cannot find " + serializedData);
            return false;
        }


        List<Document> documents;
        log.info("Load documents...");
        try {
            documents = SerializedDataHelper.loadDocuments(serializedData);
        } catch (Exception e) {
            log.error(e);
            return false;
        }

        log.info("Loaded documents: " + documents.size());


        List<String> lines = new ArrayList<>();

        for (Topic t : topics) {
            List<Result> resultHits = index.search(t.getTitle() + " " + t.getDescription());

            Comparator<Result> cmp = (o1, o2) -> {
                if (o1.getScore() > o2.getScore()) return -1;
                if (o1.getScore() == o2.getScore()) return 0;
                return 1;
            };

            resultHits.sort(cmp);
            for (Result r : resultHits) {
                final String line = r.toString(t.getId());
                lines.add(line);
            }
            if (resultHits.size() == 0) {
                lines.add(t.getId() + " Q0 " + "abc" + " " + "99" + " " + 0.0 + " runindex1");
            }
        }
        final File outputFile = new File(this.settings.getOutputDir() + "/results " + SerializedDataHelper.SDF.format(System.currentTimeMillis()) + ".txt");
        IOUtils.saveFile(outputFile, lines);
        //try to run evaluation
        try {
            String output = runTrecEval(outputFile.toString());
            System.out.println(output);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return true;
    }

    private static String runTrecEval(String predictedFile) throws IOException {

        String commandLine = "./trec_eval.8.1/./trec_eval" + " ./trec_eval.8.1/czech" + " " + predictedFile;

        log.info("exec(" + commandLine + ")");
        Process process = Runtime.getRuntime().exec(commandLine);

        BufferedReader stdout = new BufferedReader(new InputStreamReader(process.getInputStream()));
        BufferedReader stderr = new BufferedReader(new InputStreamReader(process.getErrorStream()));

        StringBuilder output = new StringBuilder("TREC EVAL output:\n");
        for (String line; (line = stdout.readLine()) != null; ) output.append(line).append("\n");

        int exitStatus = -42;
        try {
            exitStatus = process.waitFor();
        } catch (InterruptedException ie) {
            ie.printStackTrace();
        }
        System.out.println(exitStatus);

        stdout.close();
        stderr.close();

        return output.toString();
    }
}
