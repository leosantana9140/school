package br.com.school.codec;

import br.com.school.model.Address;
import br.com.school.model.Grade;
import br.com.school.model.Student;
import org.bson.*;
import org.bson.codecs.Codec;
import org.bson.codecs.CollectibleCodec;
import org.bson.codecs.DecoderContext;
import org.bson.codecs.EncoderContext;

import java.util.ArrayList;
import java.util.List;

public class StudentCodec implements CollectibleCodec<Student> {
    private Codec<Document> codec;

    public StudentCodec(Codec<Document> codec) {
        this.codec = codec;
    }

    @Override
    public Student generateIdIfAbsentFromDocument(Student student) {
        return documentHasId(student) ? student.generateId() : student;
    }

    @Override
    public boolean documentHasId(Student student) {
        return student.getId() == null;
    }

    @Override
    public BsonValue getDocumentId(Student student) {
        if (documentHasId(student))
            throw new IllegalStateException("Este aluno não possui um ID.");

        return new BsonString(student.getId().toHexString());
    }

    @Override
    public Student decode(BsonReader bsonReader, DecoderContext decoderContext) {
        Document document = codec.decode(bsonReader, decoderContext);

        Student student = new Student();
        student.setId(document.getObjectId("_id"));
        student.setName(document.getString("name"));
        student.setCpf(document.getString("cpf"));
        student.setEmail(document.getString("mail"));
        student.setPhoneNumber(document.getString("phoneNumber"));
        student.setAverage(document.getDouble("average"));

        List<Double> gradeList = (List<Double>) document.get("gradeList");

        if (gradeList != null) {
            List<Grade> studentGradeList = new ArrayList<>();

            for (Double score : gradeList) {
                studentGradeList.add(new Grade(score));
            }

            student.setGrade(studentGradeList);
        }

        Document address = (Document) document.get("address");

        if (address != null) {
            String street = address.getString("street");
            List<Double> coordinates = (List<Double>) address.get("coordinates");
            student.setAddress(new Address(street, coordinates));
        }

        return student;
    }

    @Override
    public void encode(BsonWriter bsonWriter, Student student, EncoderContext encoderContext) {
        Document document = new Document();
        List<Grade> gradeList = student.getGrade();
        Double gradeSum = 0.0;

        document.put("_id", student.getId());
        document.put("name", student.getName());
        document.put("cpf", student.getCpf());
        document.put("mail", student.getEmail());
        document.put("phoneNumber", student.getPhoneNumber());

        if (student.getGrade() != null) {
            List<Double> studentGradeList = new ArrayList<>();

            for (Grade grade : gradeList) {
                studentGradeList.add(grade.getScore());
                gradeSum += grade.getScore();
            }

            document.put("gradeList", studentGradeList);
            document.put("average", studentGradeList.size() > 0 ? gradeSum / studentGradeList.size() : gradeSum);
        }

        List<Double> coordinates = new ArrayList<Double>();

        for (Double coordinate : student.getAddress().getCoordinates()) {
            coordinates.add(coordinate);
        }

        document.put("address", new Document()
                .append("street", student.getAddress().getStreet())
                .append("coordinates", coordinates)
                .append("type", student.getAddress().getType()));

        codec.encode(bsonWriter, document, encoderContext);
    }

    @Override
    public Class<Student> getEncoderClass() {
        return Student.class;
    }
}
