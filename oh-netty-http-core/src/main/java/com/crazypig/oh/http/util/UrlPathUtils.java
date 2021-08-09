package com.crazypig.oh.http.util;

public class UrlPathUtils {

    public static String concat(String rootPath, String path) {
        String pathSeparator = "/";
        StringBuilder sb = new StringBuilder(64);
        if (rootPath != null && !rootPath.startsWith(pathSeparator)) {
            sb.append(pathSeparator);
        }
        else if (rootPath != null) {
            sb.append(rootPath);
        }
        if (!path.startsWith(pathSeparator)) {
            sb.append(pathSeparator);
        }
        sb.append(path);
        return sb.toString();
    }

}
