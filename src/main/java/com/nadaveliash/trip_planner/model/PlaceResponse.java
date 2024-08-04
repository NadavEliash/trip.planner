package com.nadaveliash.trip_planner.model;

import java.util.ArrayList;

public class PlaceResponse {
    public static class AuthorAttribution{
        public String displayName;
        public String uri;
        public String photoUri;
    }

    public static class DisplayName {
        public String text;
        public String languageCode;

        public String getText() {
            return text;
        }
    }

    public static class Photo {
        public String name;
        public int widthPx;
        public int heightPx;
        public ArrayList<AuthorAttribution> authorAttributions;

        public String getName() {
            return name;
        }
    }

    public static class Place{
        public String id;
        public DisplayName displayName;
        public ArrayList<Photo> photos;

        public ArrayList<Photo> getPhotos() {
            return photos;
        }
    }

    public static class Places{
        public ArrayList<Place> places;

        public ArrayList<Place> getPlaces() {
            return places;
        }
    }
}
