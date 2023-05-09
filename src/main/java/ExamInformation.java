public class ExamInformation {
    private String course;
    private String date;
    private String location;
    private String start;
    private String end;

    public ExamInformation(String course, String date, String location, String start, String end) {
        this.course = course;
        this.date = date;
        this.location = location;
        this.start = start;
        this.end = end;
    }

    public String getCourse() {
        return course;
    }

    public String getDate() {
        return date;
    }

    public String getLocation() {
        return location;
    }

    public String getStart() {
        return start;
    }

    public String getEnd() {
        return end;
    }
}
