package fr.pa1007.song;

import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.platform.win32.WinDef;
import com.sun.jna.platform.win32.WinUser;
import com.sun.jna.win32.StdCallLibrary;

public interface User32 extends StdCallLibrary {

    User32 INSTANCE = Native.loadLibrary("user32", User32.class);

    boolean EnumWindows(WinUser.WNDENUMPROC lpEnumFunc, Pointer arg);

    int GetWindowTextA(WinDef.HWND hWnd, byte[] lpString, int nMaxCount);


}
