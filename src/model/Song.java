package model;

public class Song {
    private String code;
    private String title;
    private String singer;

    public Song() {
    }

    public Song(String code, String title, String singer) {
        this.code = code;
        this.title = title;
        this.singer = singer;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSinger() {
        return singer;
    }

    public void setSinger(String singer) {
        this.singer = singer;
    }

    @Override
    public String toString() {
        return "Song{" +
                "code='" + code + '\'' +
                ", title='" + title + '\'' +
                ", singer='" + singer + '\'' +
                '}';
    }

    public String toRowData(String delimiter) {
        return code + delimiter + title + delimiter + singer;
    }

    public static Song fromRowData(String[] cols) {
        return new Song(cols[0], cols[1], cols[2]);
    }
}
