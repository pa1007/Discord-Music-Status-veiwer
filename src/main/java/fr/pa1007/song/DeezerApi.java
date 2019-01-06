package fr.pa1007.song;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import fr.pa1007.song.deezerapi.Datum;
import fr.pa1007.song.deezerapi.Song;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.Type;
import java.net.URL;

public class DeezerApi {

    static final   Gson   GSON   =
            new GsonBuilder().serializeNulls().setPrettyPrinting().create();
    private static String apiURL = "https://api.deezer.com/search/track?strict=on&q=";

    public static String getImage(String s, String artist) throws IOException {
        String a     = apiURL + s.replace(" ", "%20");
        Song   songs = getSongs(a);
        for (Datum datum : songs.getData()) {
            if (datum.getArtist().getName().equals(artist) && datum.getTitle().contains(s)) {
                return datum.getAlbum().getCoverMedium();
            }
        }
        return "NotFound";
    }

    public static String getLink(String s, String artist) throws IOException {
        String a     = apiURL + s.replace(" ", "%20");
        Song   songs = getSongs(a);
        for (Datum datum : songs.getData()) {
            if (datum.getArtist().getName().equals(artist) && datum.getTitle().contains(s)) {
                return datum.getLink();
            }
        }
        return "NotFound";
    }

    private static Song getSongs(String a) throws IOException {
        URL  URL_UPDATE    = new URL(a);
        Type clazzListType = new TypeToken<Song>() {}.getType();
        try (Reader data = new BufferedReader(new InputStreamReader(URL_UPDATE.openConnection().getInputStream()))) {
            return GSON.fromJson(data, clazzListType);
        }
    }
}
