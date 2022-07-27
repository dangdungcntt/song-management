package repository;

import model.Song;

import java.util.List;

public interface SongRepository {
    List<Song> getAll();

    List<Song> search(String keyword);

    boolean addSong(Song song);

    boolean updateSong(String code, Song newSongData);

    boolean removeSong(String selectedCode);
}
