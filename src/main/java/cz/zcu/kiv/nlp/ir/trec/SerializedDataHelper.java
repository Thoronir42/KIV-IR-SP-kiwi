package cz.zcu.kiv.nlp.ir.trec;

import cz.zcu.kiv.nlp.ir.trec.data.Document;
import cz.zcu.kiv.nlp.ir.trec.data.Topic;
import org.apache.log4j.Logger;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.List;

public class SerializedDataHelper {
    private static Logger log = Logger.getLogger(SerializedDataHelper.class);

    public static final java.text.DateFormat SDF = new SimpleDateFormat("yyyy-MM-dd_HH_mm_SS");

    static public List<Document> loadDocuments(File serializedFile) throws IOException {

        try (ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(serializedFile))) {
            Object object = objectInputStream.readObject();
            List map = (List) object;
            if (map.isEmpty()) {
                throw new IOException("Documents load fail: no documents found");
            }
            if(!(map.get(0) instanceof Document)) {
                throw new IOException("Documents load fail: list contains non-document items");
            }

            return (List<Document>) object;
        } catch (ClassNotFoundException ex) {
            throw new RuntimeException(ex);
        }
    }


    public static boolean saveDocument(File outputFile, List<Document> data) {
        // save data
        try (ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream(outputFile))) {
            objectOutputStream.writeObject(data);
            log.info("Documents saved to " + outputFile.getPath());
            return true;
        } catch (IOException ex) {
            log.warn("Document failed due to: " + ex.toString());
            return false;
        }
    }

    static public List<Topic> loadTopics(File serializedFile) {
        try (ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(serializedFile))) {
            Object object = objectInputStream.readObject();
            List map = (List) object;
            if (map.isEmpty() || !(map.get(0) instanceof Topic)) {
                throw new RuntimeException("Topics load fail: no topics found");
            }

            return (List<Topic>) object;
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    public static boolean saveTopics(File outputFile, List<Topic> data) {
        // save data
        try (ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream(outputFile))) {
            objectOutputStream.writeObject(data);
            log.info("Data saved to " + outputFile.getPath());
            return true;
        } catch (IOException e) {
            log.warn("Topics save failed");
            return false;
        }

    }
}
