package fr.pa1007.song;

import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.platform.win32.WinDef;
import com.sun.jna.platform.win32.WinUser;
import com.sun.jna.win32.StdCallLibrary;

public interface User32 extends StdCallLibrary {

    User32 INSTANCE = (User32) Native.loadLibrary("user32", User32.class);

    WinDef.HWND GetForegroundWindow();  // add this


    boolean EnumWindows(WinUser.WNDENUMPROC lpEnumFunc, Pointer arg);

    int GetWindowTextA(WinDef.HWND hWnd, byte[] lpString, int nMaxCount);

    WinDef.HWND FindWindowA(String winClass, String title);

    WinDef.HWND FindWindowExA(WinDef.HWND hwndParent, WinDef.HWND childAfter, String className, String windowName);

    WinDef.HWND FindWindowExW(String winClass, String title);

    WinDef.HWND FindWindowW(String winClass, String title);

}
