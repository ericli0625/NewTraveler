package com.example.eric.newtraveler.network.responseData;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

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
        ArrayList<Time> time;

        public String getElementName() {
            return elementName;
        }

        public ArrayList<Time> getTime() {
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

        public String getStartTime() {
            return startTime;
        }

        public String getEndTime() {
            return endTime;
        }

        public Parameter getParameter() {
            return parameter;
        }

        protected Time(Parcel in) {
            startTime = in.readString();
            endTime = in.readString();
            parameter = in.readParcelable(Parameter.class.getClassLoader());
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
            dest.writeParcelable(parameter, flags);
        }
    }

    public static class Parameter implements Parcelable {

        String parameterName = "";
        Integer parameterValue = 0;

        public String getParameterName() {
            return parameterName;
        }

        public Integer getParameterValue() {
            return parameterValue;
        }

        protected Parameter(Parcel in) {
            parameterName = in.readString();
            if (in.readByte() == 0) {
                parameterValue = null;
            } else {
                parameterValue = in.readInt();
            }
        }

        public static final Creator<Parameter> CREATOR = new Creator<Parameter>() {
            @Override
            public Parameter createFromParcel(Parcel in) {
                return new Parameter(in);
            }

            @Override
            public Parameter[] newArray(int size) {
                return new Parameter[size];
            }
        };

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(parameterName);
            if (parameterValue == null) {
                dest.writeByte((byte) 0);
            } else {
                dest.writeByte((byte) 1);
                dest.writeInt(parameterValue);
            }
        }
    }

}

