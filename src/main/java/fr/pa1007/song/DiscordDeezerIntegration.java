package fr.pa1007.song;

import club.minnced.discord.rpc.DiscordEventHandlers;
import club.minnced.discord.rpc.DiscordRPC;
import club.minnced.discord.rpc.DiscordRichPresence;
import com.sun.jna.Native;
import fr.pa1007.song.controller.MainAppController;
import fr.pa1007.song.deezerapi.Datum;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import java.io.IOException;

public class DiscordDeezerIntegration extends Application {

    private static long              start;
    private static String            lastName = "";
    private static MainAppController controller;

    /**
     * The main method to Start the App
     *
     * @param primaryStage the stage you will use as a support
     *
     * @throws Exception All the error you can have
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        DiscordRPC           lib           = DiscordRPC.INSTANCE;
        DiscordRichPresence  presence      = new DiscordRichPresence();
        String               applicationId = "482312958274437136";
        String               steamId       = "LmLyieQ9mJ0-c-_UfCz2eEWwdVRu4I1b";
        DiscordEventHandlers handlers      = new DiscordEventHandlers();
        lib.Discord_Initialize(applicationId, handlers, true, steamId);
        start = System.currentTimeMillis() / 1000;
        presence.startTimestamp = start; // epoch second
        presence.largeImageKey = "deezerbars";
        presence.smallImageKey = "logo";
        presence.details = "Listening Music";
        presence.state = "Music - Author";
        lib.Discord_UpdatePresence(presence);

        Thread t = new Thread(() -> {
            while (true) {
                lib.Discord_RunCallbacks();
                update(lib, presence);
                try {
                    Thread.sleep(2000);
                }
                catch (InterruptedException e) {
                    lib.Discord_Shutdown();
                }
            }
        }, "RPC-Callback-Handler");
        t.start();
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/fr/pa1007/song/MainStage.fxml"));
        Pane pane = loader.load();
        controller = loader.getController();
        Stage stage = new Stage();
        stage.setScene(new Scene(pane));
        stage.setTitle("Song Catcher");
        stage.setResizable(false);
        stage.setOnCloseRequest((windowEvent) -> {
            Platform.exit();
            lib.Discord_Shutdown();
            System.exit(0);
        });
        stage.show();
    }

    /**
     * This method use JNA api to get the name of all open windows on the computer
     * This do an enum of all and check if there is "DEEZER" in it
     *
     * @return a string with the name of the app like <code> title - author - Deezer </code>
     */
    private static String getDeezeerName() {
        final User32 user32 = User32.INSTANCE;
        String[]     s      = new String[]{"Error"};
        user32.EnumWindows((hWnd, arg1) -> {
            byte[] windowText = new byte[512];
            user32.GetWindowTextA(hWnd, windowText, 512);
            String wText = Native.toString(windowText);

            // get rid of this if block if you want all windows regardless of whether
            // or not they have text
            if (wText.isEmpty()) {
                return true;
            }
            if (wText.contains("pa1007")) {
                return true;
            }
            if (wText.contains("Google")
                || wText.contains("Mozilla")
                || wText.contains("Microsoft Edge")
                || wText.contains("Internet Explorer")) {
                return true;
            }
            if (wText.contains("Deezer")) {
                s[0] = wText;
            }
            return true;
        }, null);
        return s[0];
    }


    /**
     * This method update the discord and app part,
     * It collect all the data from deezer API and add it to discord and the app if it has been found
     *
     * @param lib      The discord API
     * @param presence The presence you will update
     */
    private static void update(DiscordRPC lib, DiscordRichPresence presence) {
        String s = getDeezeerName();
        if (!lastName.equals(s)) {
            String[] all         = s.split("-");
            String   imageString =
                    "http://e-cdn-files.deezer.com/cache/images/pages/404/visual_404.5eb1293e738c86d3bbd75f2e70064a8d.png";
            Datum    data        = null;
            String   link        = "NotFound";
            Integer  duration    = -1;
            try {
                data = DeezerApi.findData(all[0], all[1]);
            }
            catch (IOException e) {
                e.printStackTrace();
            }
            if (data != null) {
                imageString = data.getAlbum().getCoverBig();
                link = data.getLink();
                duration = data.getDuration();
            }
            // but now we will give a link to the deezer track url
            controller.setTitleBox(all[0]);
            controller.setAuthorBox(all[1]);
            controller.setLinkBox(link);
            controller.setImage(imageString);
            controller.setDuration(duration, presence);
            presence.smallImageText = link;
            presence.largeImageText = all[0];
            presence.details = all[0];
            presence.state = all[1];
            lib.Discord_UpdatePresence(presence);
            lastName = s;
        }
    }
}
