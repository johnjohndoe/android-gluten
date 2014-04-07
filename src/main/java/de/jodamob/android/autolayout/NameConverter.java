package de.jodamob.android.autolayout;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Fragment;
import android.content.res.Resources;
import android.os.Build;
import android.preference.PreferenceFragment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import de.jodamob.android.logging.Log;

public class NameConverter {

    private static final String TYPE_XML = "xml";
    public static final String TYPE_LAYOUT = "layout";

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static int convertToResourceId(Fragment fragment) {
        return getStringResourceByName(
                TYPE_LAYOUT,
                fragment.getActivity().getPackageName(),
                fragment.getResources(),
                convertToResourceName(fragment));
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static int convertToResourceId(PreferenceFragment fragment) {
        return getStringResourceByName(
                TYPE_XML,
                fragment.getActivity().getPackageName(),
                fragment.getResources(),
                convertToResourceName(fragment));
    }

    public static int convertToResourceId(Activity activity) {
        return getStringResourceByName(
                TYPE_LAYOUT,
                   activity.getPackageName(),
                   activity.getResources(),
                   convertToResourceName(activity));
    }
    
    static String[] convertToResourceName(Object object) {
        String pureClassname = getPureClassname(object);
        return asVariants(asLayoutNameCharacters(pureClassname));
    }

    private static String[] asVariants(String name) {
        String[] parts = name.split("_");
        List<String> fullnames = new ArrayList<String>(parts.length);
        for(String part : parts) {
            fullnames.add((fullnames.isEmpty() ? "" : fullnames.get(fullnames.size()-1) + "_") + part) ;
        }
        Collections.reverse(fullnames);
        return fullnames.toArray(new String[fullnames.size()]);
    }

    private static String getPureClassname(Object object) {
        String name = object.getClass().getName();
        return name.substring(name.lastIndexOf('.') +1);
    }

    private static String asLayoutNameCharacters(String name) {
        String res = "" +Character.toLowerCase(name.charAt(0));
        for(int i = 1; i < name.length(); i++) {
           res += asLayoutNameCharacter(name, res, i);
        }
        return res;
    }

    private static String asLayoutNameCharacter(String name, String res, int i) {
        Character ch = name.charAt(i);
        if(Character.isUpperCase(ch) && i>0) {
            return "_" + Character.toLowerCase(ch);
        } else {
            return "" + ch;
        }
    }
    
    private static int getStringResourceByName(String type, String packageName, Resources resources, String... resourceNames) {
        for (String name : resourceNames) {
            int layout = resources.getIdentifier(name, type, packageName);
            if (layout > 0) {
                return layout;
            }
        }
        Log.e("could not find one of"  + Arrays.toString(resourceNames));
        return 0;
    }
}