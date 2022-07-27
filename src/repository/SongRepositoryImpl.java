package repository;

import model.Song;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SongRepositoryImpl implements SongRepository {
    private final Map<String, Integer> songIndexMap;
    private final List<Song> songs;
    private final String dataFileName = "data.csv";
    private final String dataDelimiter = ";";

    public SongRepositoryImpl() {
        this.songIndexMap = new HashMap<>();
        this.songs = new ArrayList<>();
        this.readData();
    }

    protected void readData() {
        try {
            FileReader fr = new FileReader(this.dataFileName);
            BufferedReader bfr = new BufferedReader(fr);
            String row;
            while ((row = bfr.readLine()) != null) {
                if (row.isEmpty()) {
                    continue;
                }
                Song song = Song.fromRowData(row.split(this.dataDelimiter));
                this.songs.add(song);
                this.songIndexMap.put(song.getCode(), this.songs.size() - 1);
            }
            bfr.close();
            fr.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected void saveData() {
        try {
            FileWriter fileWriter = new FileWriter(this.dataFileName);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

            for (int i = 0; i < this.songs.size(); i++) {
                bufferedWriter.write(this.songs.get(i).toRowData(this.dataDelimiter));
                if (i < this.songs.size() - 1) {
                    bufferedWriter.newLine();
                }
            }
            bufferedWriter.close();
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<Song> getAll() {
        return this.songs;
    }

    public List<Song> search(String keyword) {
        List<Song> results = new ArrayList<>();
        for (Song song : this.songs) {
            if (
                    song.getCode().contains(keyword)
                            || song.getTitle().contains(keyword)
                            || song.getSinger().contains(keyword)
            ) {
                results.add(song);
            }
        }
        return results;
    }

    public boolean addSong(Song song) {
        try {
            if (this.songIndexMap.containsKey(song.getCode())) {
                return false;
            }

            FileWriter fw = new FileWriter(this.dataFileName, true);
            BufferedWriter bw = new BufferedWriter(fw);
            bw.newLine();
            bw.write(song.toRowData(this.dataDelimiter));
            this.songs.add(song);
            this.songIndexMap.put(song.getCode(), this.songs.size() - 1);
            bw.close();
            fw.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean updateSong(String code, Song newSongData) {
        if (!this.songIndexMap.containsKey(code)) {
            return false;
        }

        int currentPosition = this.songIndexMap.get(code);
        Song song = this.songs.get(currentPosition);
        song.setCode(newSongData.getCode());
        song.setTitle(newSongData.getTitle());
        song.setSinger(newSongData.getSinger());
        this.songIndexMap.remove(code);
        this.songIndexMap.put(song.getCode(), currentPosition);
        this.saveData();
        return true;
    }

    public boolean removeSong(String code) {
        if (!this.songIndexMap.containsKey(code)) {
            return false;
        }

        int currentPosition = this.songIndexMap.get(code);
        this.songs.remove(currentPosition);
        if (currentPosition < this.songs.size()) {
            //Update index
            for (int i = currentPosition; i < this.songs.size(); i++) {
                this.songIndexMap.put(this.songs.get(i).getCode(), i);
            }
        }
        this.saveData();
        return true;
    }
}
