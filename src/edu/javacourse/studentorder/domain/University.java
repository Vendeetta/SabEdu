package edu.javacourse.studentorder.domain;

public class University {

    private Long university_id;
    private String university_name;

    public University(Long university_id, String university_name) {
        this.university_id = university_id;
        this.university_name = university_name;
    }

    public Long getUniversity_id() {
        return university_id;
    }

    public void setUniversity_id(Long university_id) {
        this.university_id = university_id;
    }

    public String getUniversity_name() {
        return university_name;
    }

    public void setUniversity_name(String university_name) {
        this.university_name = university_name;
    }

    @Override
    public String toString() {
        return "University{" +
                "university_id=" + university_id +
                ", university_name='" + university_name + '\'' +
                '}';
    }
}
