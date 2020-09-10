package com.example.android.quakereport;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Set;

public class EarthquakeArrayAdapter extends ArrayAdapter<Earthquake> {

    private static final String LOG_TAG = EarthquakeArrayAdapter.class.getSimpleName();

    public EarthquakeArrayAdapter(@NonNull Context context, @NonNull ArrayList<Earthquake> earthquakes) {
        super(context, 0, earthquakes);
    }

    /**
     * Provides a view for an AdapterView (ListView, GridView, etc.)
     *
     * @param position    The position in the list of data that should be displayed in the
     *                    list item view.
     * @param convertView The recycled view to populate.
     * @param parent      The parent ViewGroup that is used for inflation.
     * @return The View for the position in the AdapterView.
     */
    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        // Check if the existing view is being reused, otherwise inflate the view
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.list_item_earthquake, parent, false);
        }

        // Get the {@link Earthquake} object located at this position in the list
        Earthquake currentEarthquake = getItem(position);

        // Find the TextView in the list_item_earthquake.xml layout with the ID magnitude_txt.
        TextView magnitude_txt = (TextView) listItemView.findViewById(R.id.magnitude_txt);

        // Set the proper background color on the magnitude circle.
        // Fetch the background from the TextView, which is a GradientDrawable.
        GradientDrawable magnitudeCircle=(GradientDrawable)magnitude_txt.getBackground();

        // Get the appropriate background color .based on the current earthquake magnitude
        assert currentEarthquake != null;
        int magnitudeColor=getMagnitudeColor(currentEarthquake.getMagnitude());

        // Set the color on the magnitude circle
        magnitudeCircle.setColor(magnitudeColor);

        // Get the magnitude from the current Earthquake object and
        double magnitude=currentEarthquake.getMagnitude();
        // set this text on the magnitude TextView
        magnitude_txt.setText(formatMagnitude(magnitude));

        // Find the TextView in the list_item_earthquake.xml layout with the ID locationOffset_txt
        TextView locationOffset_txt = (TextView) listItemView.findViewById(R.id.locationOffset_txt);

        // Find the TextView in the list_item_earthquake.xml layout with the ID primaryLocation_txt
        TextView primaryLocation_txt = (TextView) listItemView.findViewById(R.id.primaryLocation_txt);


        // Get the location Offset and Primary Location from the current Earthquake object and

        String location = currentEarthquake.getPlace();
        int breakIndex = location.indexOf("of") + 3;
        String locationOffset, primaryLocation;
        if (breakIndex == 2) {
            locationOffset = getContext().getString(R.string.nearBy);
            primaryLocation = location;
        } else {
            locationOffset = location.substring(0, breakIndex - 1);
            primaryLocation = location.substring(breakIndex);
        }

        // set this text on the respective TextViews
        locationOffset_txt.setText(locationOffset);
        primaryLocation_txt.setText(primaryLocation);

        Date dateObject = new Date(currentEarthquake.getmiliTime());

        String dateFormat = formatDate(dateObject);

        String timeFormat = formatTime(dateObject);

        // Find the TextView in the list_item_earthquake.xml layout with the ID date_txt
        TextView date_txt = (TextView) listItemView.findViewById(R.id.date_txt);

        date_txt.setText(dateFormat);

        TextView time_txt = (TextView) listItemView.findViewById(R.id.time_txt);

        time_txt.setText(timeFormat);


        // Return the whole list item layout (containing 3 TextViews)
        // so that it can be shown in the ListView
        return listItemView;
    }

    private String formatDate(Date dateObject) {
        SimpleDateFormat formatDate = new SimpleDateFormat("LLL dd, yyyy");
        return formatDate.format(dateObject);
    }

    private String formatTime(Date dateObject) {
        SimpleDateFormat formatTime = new SimpleDateFormat("h:mm a");
        return formatTime.format(dateObject);
    }

    private String formatMagnitude(double magnitude){
        DecimalFormat formatMag=new DecimalFormat("0.0");
        return formatMag.format(magnitude);
    }

    private int getMagnitudeColor(double magnitude){
        int magnitudeColor;
        switch((int)Math.floor(magnitude)){
            case 10:
                magnitudeColor = R.color.magnitude10plus;
                break;
            case 9:
                magnitudeColor = R.color.magnitude9;
                break;
            case 8:
                magnitudeColor = R.color.magnitude8;
                break;
            case 7:
                magnitudeColor = R.color.magnitude7;
                break;
            case 6:
                magnitudeColor = R.color.magnitude6;
                break;
            case 5:
                magnitudeColor = R.color.magnitude5;
                break;
            case 4:
                magnitudeColor = R.color.magnitude4;
                break;
            case 3:
                magnitudeColor = R.color.magnitude3;
                break;
            case 2:
                magnitudeColor = R.color.magnitude2;
                break;
            default:
                magnitudeColor = R.color.magnitude1;
                break;
        }
        return ContextCompat.getColor(getContext(),magnitudeColor);
    }
}
