package br.com.school.repository;

import br.com.school.codec.StudentCodec;
import br.com.school.model.Student;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Indexes;
import com.mongodb.client.model.geojson.Point;
import com.mongodb.client.model.geojson.Position;
import org.bson.Document;
import org.bson.codecs.Codec;
import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class StudentRepository {
    private MongoClient mongoClient;
    private MongoDatabase mongoDatabase;

    public Student findStudentById(String id) {
        getConnection();

        MongoCollection<Student> studentCollection = this.mongoDatabase.getCollection("Student", Student.class);

        return studentCollection.find(Filters.eq("_id", new ObjectId(id))).first();
    }

    public void saveStudent(Student student) {
        getConnection();

        MongoCollection<Student> studentCollection = this.mongoDatabase.getCollection("Student", Student.class);

        if (student.getId() == null) {
            studentCollection.insertOne(student);
        } else {
            studentCollection.updateOne(Filters.eq("_id", student.getId()), new Document("$set", student));
        }

        closeConnection();
    }

    public List<Student> getAllStudents() {
        getConnection();

        MongoCollection<Student> studentCollection = this.mongoDatabase.getCollection("Student", Student.class);

        MongoCursor<Student> mongoCursor = studentCollection.find().iterator();

        List<Student> students = getStudentList(mongoCursor);

        closeConnection();

        return students;
    }

    public List<Student> findByGrade(String status) {
        getConnection();

        MongoCollection<Student> studentCollection = this.mongoDatabase.getCollection("Student", Student.class);

        MongoCursor<Student> mongoCursor = switch (status) {
            case "Aprovados" -> studentCollection.find(Filters.gte("average", 6.0)).iterator();
            case "Reprovados" -> studentCollection.find(Filters.lte("average", 5.9)).iterator();
            default -> studentCollection.find(Filters.gte("average", 0.0)).iterator();
        };

        List<Student> students = getStudentList(mongoCursor);

        closeConnection();

        return students;
    }

    public void deleteUser(String id) {
        getConnection();

        MongoCollection<Student> studentCollection = this.mongoDatabase.getCollection("Student", Student.class);

        studentCollection.deleteOne(new Document("_id", new ObjectId(id)));

        closeConnection();
    }

    private List<Student> getStudentList(MongoCursor<Student> mongoCursor) {
        List<Student> students = new ArrayList<>();

        while (mongoCursor.hasNext()) {
            students.add(mongoCursor.next());
        }

        return students;
    }

    public List<Student> findByLocation(Student student) {
        getConnection();

        MongoCollection<Student> studentCollection = this.mongoDatabase.getCollection("Student", Student.class);

        studentCollection.createIndex(Indexes.geo2dsphere("address"));

        List<Double> coordinates = student.getAddress().getCoordinates();

        Point referencePoint = new Point(new Position(coordinates.get(0), coordinates.get(1)));

        MongoCursor<Student> mongoCursor = studentCollection.find(Filters.nearSphere("address", referencePoint, 2000.0, 0.0)).limit(2).skip(1).iterator();

        List<Student> students = getStudentList(mongoCursor);

        closeConnection();

        return students;
    }

    private void getConnection() {
        Codec<Document> codec = MongoClient.getDefaultCodecRegistry().get(Document.class);
        StudentCodec studentCodec = new StudentCodec(codec);
        CodecRegistry codecRegistry = CodecRegistries.fromRegistries(MongoClient.getDefaultCodecRegistry(), CodecRegistries.fromCodecs(studentCodec));
        MongoClientOptions mongoClientOptions = MongoClientOptions.builder().codecRegistry(codecRegistry).build();

        this.mongoClient = new MongoClient("localhost:27017", mongoClientOptions);
        this.mongoDatabase = mongoClient.getDatabase("school");
    }

    private void closeConnection() {
        this.mongoClient.close();
    }
}
