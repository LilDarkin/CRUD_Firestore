package com.example.rawr;

import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.IgnoreExtraProperties;

import java.io.Serializable;
import java.util.HashMap;

@IgnoreExtraProperties
public class Artist implements Serializable {

    @Exclude
    private String artistId;
    private String artistName;
    private String artistGenre;
    private String artistAge;
    private String artistBand;
    private String artistSong;

    public Artist(){
        //this constructor is required
    }

    public Artist(String artistId, String artistName, String artistGenre, String artistAge, String artistBand, String artistSong) {
        this.artistId = artistId;
        this.artistName = artistName;
        this.artistGenre = artistGenre;
        this.artistAge = artistAge;
        this.artistBand = artistBand;
        this.artistSong = artistSong;
    }

    public String getArtistId() {
        return artistId;
    }

    public String getArtistName() {
        return artistName;
    }

    public String getArtistGenre() {
        return artistGenre;
    }

    public String getArtistAge() {
        return artistAge;
    }

    public String getArtistBand() {
        return artistBand;
    }

    public String getArtistSong() {
        return artistSong;
    }

    public Task<Void> update(String key, HashMap<String ,Object> hashMap)
    {
        return MainActivity.databaseArtists.child(key).updateChildren(hashMap);
    }
    public Task<Void> remove(String key)
    {
        return MainActivity.databaseArtists.child(key).removeValue();
    }
}
