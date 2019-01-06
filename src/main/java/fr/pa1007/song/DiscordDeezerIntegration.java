package fr.pa1007.song;

import club.minnced.discord.rpc.DiscordEventHandlers;
import club.minnced.discord.rpc.DiscordRPC;
import club.minnced.discord.rpc.DiscordRichPresence;
import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.platform.win32.WinDef;
import com.sun.jna.platform.win32.WinUser;

public class DiscordDeezerIntegration {

    private static String getDeezeername() {
        final User32 user32 = User32.INSTANCE;
        String[]     s      = new String[]{"Error"};
        user32.EnumWindows(new WinUser.WNDENUMPROC() {
            int count = 0;

            @Override
            public boolean callback(WinDef.HWND hWnd, Pointer arg1) {
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
                if (wText.contains("Deezer")) {
                    s[0] = wText;
                }
                return true;
            }
        }, null);
        return s[0];
    }

    public static void main(String[] args) {
        DiscordRPC          lib           = DiscordRPC.INSTANCE;
        DiscordRichPresence presence      = new DiscordRichPresence();
        String              applicationId = args.length < 1 ? "482312958274437136" : args[0];
        String              steamId       = args.length < 2 ? "LmLyieQ9mJ0-c-_UfCz2eEWwdVRu4I1b" : args[1];

        DiscordEventHandlers handlers = new DiscordEventHandlers();
        handlers.ready = (user) -> System.out.println("Ready!");

        lib.Discord_Initialize(applicationId, handlers, true, steamId);

        presence.startTimestamp = System.currentTimeMillis() / 1000; // epoch second
        presence.details = "Listening Music";
        presence.state = "Music - Author";
        lib.Discord_UpdatePresence(presence);

        Thread t = new Thread(() -> {
            while (true) {
                lib.Discord_RunCallbacks();
                update(lib, presence);
                try {
                    Thread.sleep(500);
                }
                catch (InterruptedException e) {
                    lib.Discord_Shutdown();
                }
            }
        }, "RPC-Callback-Handler");
        t.start();
    }

    private static void update(DiscordRPC lib, DiscordRichPresence presence) {
        String   s   = getDeezeername();
        String[] all = s.split("-");
        presence.details = all[0];
        presence.state = all[1];
        lib.Discord_UpdatePresence(presence);
    }
}
