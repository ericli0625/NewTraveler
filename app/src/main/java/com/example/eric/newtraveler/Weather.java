package com.example.eric.newtraveler;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.ArrayList;
import java.util.List;

public class Weather {

    private String success = "";

    public String getSuccess() {
        return success;
    }

    private Result result;

    public Result getResult() {
        return result;
    }

    public static class Result {
        String resource_id = "";
        ArrayList<Fields> fields;

        public String getResource_id() {
            return resource_id;
        }

        public ArrayList<Fields> getFields() {
            return fields;
        }
    }

    public static class Fields {
        String id = "";
        String type = "";
    }

    private Records records;

    public Records getRecords() {
        return records;
    }

    public static class Records {
        String datasetDescription = "";
        ArrayList<Location> location;

        public String getDatasetDescription() {
            return datasetDescription;
        }

        public ArrayList<Location> getLocation() {
            return location;
        }
    }

    public static class Location {
        String locationName = "";
        ArrayList<WeatherElement> weatherElement;

        public String getLocationName() {
            return locationName;
        }

        public ArrayList<WeatherElement> getWeatherElement() {
            return weatherElement;
        }
    }

    public static class WeatherElement implements Parcelable {
        String elementName = "";
        List<Time> time;

        public String getElementName() {
            return elementName;
        }

        public List<Time> getTime() {
            return time;
        }

        protected WeatherElement(Parcel in) {
            elementName = in.readString();
            time = in.readArrayList(Time.class.getClassLoader());
        }

        public static final Creator<WeatherElement> CREATOR = new Creator<WeatherElement>() {
            @Override
            public WeatherElement createFromParcel(Parcel in) {
                return new WeatherElement(in);
            }

            @Override
            public WeatherElement[] newArray(int size) {
                return new WeatherElement[size];
            }
        };

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(elementName);
            dest.writeList(time);
        }
    }

    public static class Time implements Parcelable {
        String startTime = "";
        String endTime = "";
        Parameter parameter;

        protected Time(Parcel in) {
            startTime = in.readString();
            endTime = in.readString();
        }

        public static final Creator<Time> CREATOR = new Creator<Time>() {
            @Override
            public Time createFromParcel(Parcel in) {
                return new Time(in);
            }

            @Override
            public Time[] newArray(int size) {
                return new Time[size];
            }
        };

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(startTime);
            dest.writeString(endTime);
        }
    }

    public static class Parameter {
        String parameterName = "";
        Integer parameterValue = 0;
    }

}

