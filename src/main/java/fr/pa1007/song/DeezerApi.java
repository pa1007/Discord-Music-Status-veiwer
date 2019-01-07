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

    /**
     * This method will give the program if found the datum of the song you want
     *
     * @param title  The title of the song to find
     * @param artist The artist of the song you search
     *
     * @return {@link fr.pa1007.song.deezerapi.Datum Datum} The data with the song or null if not find
     *
     * @throws IOException can throw if the url is false ( give by {@link #getSongs(String)} )
     */
    public static Datum findData(String title, String artist) throws IOException {
        String a     = apiURL + title.replace(" ", "%20").toLowerCase();
        Song   songs = getSongs(a);
        for (Datum datum : songs.getData()) {
            if (datum.getArtist().getName().replace(" ", "").equals(artist.replace(" ", ""))) {
                return datum;
            }
        }
        return null;
    }

    /**
     * This method will search all the song with a given URL
     *
     * @param a The deezer api url, end with <code>&q=</code> and the name of the song
     *
     * @return {@link fr.pa1007.song.deezerapi.Song Song} type from the deezer API
     *
     * @throws IOException can be throw if the url is not valid
     */
    private static Song getSongs(String a) throws IOException {
        URL  URL_UPDATE    = new URL(a);
        Type clazzListType = new TypeToken<Song>() {}.getType();
        try (Reader data = new BufferedReader(new InputStreamReader(URL_UPDATE.openConnection().getInputStream()))) {
            return GSON.fromJson(data, clazzListType);
        }
    }
}
