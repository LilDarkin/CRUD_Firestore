package com.example.rawr;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    //view objects
    EditText editTextName, editTextAge, editTextBand, editTextSong;
    Spinner spinnerGenre;
    Button btnAddArtist;

    ListView listViewArtists;
    List<Artist> artists;

    //our database reference object
    static DatabaseReference databaseArtists;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //getting the reference of artists node
        databaseArtists = FirebaseDatabase.getInstance().getReference("artists");

        //getting views
        editTextName = (EditText) findViewById(R.id.editTextName);
        editTextAge = (EditText) findViewById(R.id.editTextAge);
        editTextBand = (EditText) findViewById(R.id.editTextBand);
        editTextSong = (EditText) findViewById(R.id.editTextSong);
        btnAddArtist = (Button) findViewById(R.id.btnAddArtist);
        spinnerGenre = (Spinner) findViewById(R.id.spinnerGenre);

        listViewArtists = (ListView) findViewById(R.id.listViewArtists);

        artists = new ArrayList<>();
        Artist update_artist = (Artist) getIntent().getSerializableExtra("UPDATE");

        if (update_artist != null) {
            btnAddArtist.setText("UPDATE");

            editTextName.setText(update_artist.getArtistName());
            editTextAge.setText(update_artist.getArtistAge());
            editTextBand.setText(update_artist.getArtistBand());
            editTextSong.setText(update_artist.getArtistSong());

            if (update_artist.getArtistGenre().equals("Rock")) {
                spinnerGenre.setSelection(1);
            }else if (update_artist.getArtistGenre().equals("Pop")) {
                spinnerGenre.setSelection(2);
            }else if (update_artist.getArtistGenre().equals("RNB")) {
                spinnerGenre.setSelection(3);
            }else if (update_artist.getArtistGenre().equals("Jazz")) {
                spinnerGenre.setSelection(4);
            }

            listViewArtists.setVisibility(View.GONE);
        }
        btnAddArtist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //calling the method addArtist()
                //the method is defined below
                //this method is actually performing the write operation
                //getting the values to save
                String name = editTextName.getText().toString().trim();
                String genre = spinnerGenre.getSelectedItem().toString();
                String age = editTextAge.getText().toString().trim();
                String band = editTextBand.getText().toString().trim();
                String song = editTextSong.getText().toString().trim();
                Artist artist = new Artist();

                //checking if the value is provided
                if (update_artist == null) {
                    if (!TextUtils.isEmpty(name) && !TextUtils.isEmpty(age) && !TextUtils.isEmpty(band) && !TextUtils.isEmpty(song)) {

                        //getting a unique id using push().getKey() method
                        //it will create a unique id and we will use it as the Primary Key for our Artist
                        String id = databaseArtists.push().getKey();

                        //creating an Artist Object
                        artist = new Artist(id, name, genre, age, band, song);

                        //Saving the Artist
                        databaseArtists.child(id).setValue(artist);

                        //setting edittext to blank again
                        editTextName.setText("");
                        editTextAge.setText("");
                        editTextSong.setText("");
                        editTextBand.setText("");

                        //displaying a success toast
                        Toast.makeText(MainActivity.this, "Artist added", Toast.LENGTH_LONG).show();
                    } else {
                        //if the value is not given displaying a toast
                        Toast.makeText(MainActivity.this, "Please complete the fields", Toast.LENGTH_LONG).show();
                    }
                } else {
                    HashMap<String, Object> hashMap = new HashMap<>();
                    hashMap.put("artistName", editTextName.getText().toString().trim());
                    hashMap.put("artistAge", editTextAge.getText().toString().trim());
                    hashMap.put("artistBand", editTextBand.getText().toString().trim());
                    hashMap.put("artistSong", editTextSong.getText().toString().trim());
                    hashMap.put("artistGenre", spinnerGenre.getSelectedItem().toString());

                    artist.update(update_artist.getArtistId(), hashMap).addOnSuccessListener(suc ->
                    {
                        Toast.makeText(MainActivity.this, "Record is updated", Toast.LENGTH_SHORT).show();
                        finish();
                    }).addOnFailureListener(er ->
                    {
                        Toast.makeText(MainActivity.this, "" + er.getMessage(), Toast.LENGTH_SHORT).show();
                    });
                }
            }
        });

    }
    /*
     * This method is saving a new artist to the
     * Firebase Realtime Database
     * */

    @Override
    protected void onStart() {
        super.onStart();

        databaseArtists.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                artists.clear();
                //iterating through all the nodes
                for(DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    //getting artist
                    Artist artist = postSnapshot.getValue(Artist.class);
                    //adding artist to the list
                    artists.add(artist);
                }

                //creating adapter
                ArtistList artistAdapter = new ArtistList(MainActivity.this, artists);
                //attaching adapter to the listview
                listViewArtists.setAdapter(artistAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}
