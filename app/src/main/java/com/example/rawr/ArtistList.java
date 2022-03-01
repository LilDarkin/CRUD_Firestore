package com.example.rawr;

import android.app.Activity;
import android.content.Intent;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import org.w3c.dom.Text;

import java.util.List;

public class ArtistList extends ArrayAdapter<Artist> {
    Activity context;
    List<Artist> artistList;

    public ArtistList(Activity context, List<Artist> artistList) {
        super(context, R.layout.list_layout, artistList);
        this.context= context;
        this.artistList = artistList;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View listViewItem = inflater.inflate(R.layout.list_layout, null, true);

        TextView textViewName = (TextView) listViewItem.findViewById(R.id.textViewName);
        TextView textViewGenre = (TextView) listViewItem.findViewById(R.id.textViewGenre);
        TextView textViewAge = (TextView) listViewItem.findViewById(R.id.textViewAge);
        TextView textViewBand = (TextView) listViewItem.findViewById(R.id.textViewBand);
        TextView textViewSong = (TextView) listViewItem.findViewById(R.id.textViewSong);
        TextView textViewOptions = (TextView) listViewItem.findViewById(R.id.options);

        Artist artist = artistList.get(position);
        textViewName.setText(artist.getArtistName());
        textViewGenre.setText(artist.getArtistGenre());
        textViewAge.setText(artist.getArtistAge());
        textViewBand.setText(artist.getArtistBand());
        textViewSong.setText(artist.getArtistSong());
        textViewOptions.setOnClickListener(view -> {
            PopupMenu GoToMenu = new PopupMenu(context, textViewOptions);
            GoToMenu.inflate(R.menu.options);
            GoToMenu.setOnMenuItemClickListener(item -> {
                switch (item.getItemId()) {
                    case R.id.update:
                        Intent intent = new Intent(context, MainActivity.class);
                        intent.putExtra("UPDATE", artist);
                        context.startActivity(intent);
                        break;

                    case R.id.delete:
                        Artist artists = new Artist();
                        artists.remove(artist.getArtistId()).addOnSuccessListener(suc->
                        {
                            Toast.makeText(context, "Record is removed", Toast.LENGTH_SHORT).show();

                        }).addOnFailureListener(er->
                        {
                            Toast.makeText(context, "" + er.getMessage(), Toast.LENGTH_SHORT).show();
                        });

                        break;
                }
                return false;
            });
GoToMenu.show();
        });

        return listViewItem;
    }

}
