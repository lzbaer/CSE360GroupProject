package com.medipal;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Patrick on 4/30/2015.
 */
public class PatientRecordsAdapter extends ArrayAdapter<String[]> {
    public PatientRecordsAdapter(Context context, ArrayList<String[]> records) {
        super(context, 0, records);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //get the data for this position
        String[] currentRecord = getItem(position);
        //check if the view has been reused otherwise inflate view
        if(convertView==null){
            convertView = LayoutInflater
                    .from(getContext())
                    .inflate(R.layout.patient_record, parent, false);
        }
        //look up view for data population
        TextView painRating = (TextView) convertView.findViewById(R.id.pain_rating);
        TextView fatigueRating = (TextView) convertView.findViewById(R.id.fatigue_rating);
        TextView drowsyRating = (TextView) convertView.findViewById(R.id.drowsy_rating);
        TextView nauseaRating = (TextView) convertView.findViewById(R.id.nausea_rating);
        TextView appetiteRating = (TextView) convertView.findViewById(R.id.appetite_rating);
        TextView breathRating = (TextView) convertView.findViewById(R.id.breath_rating);
        TextView depressionRating = (TextView) convertView.findViewById(R.id.depression_rating);
        TextView anxietyRating = (TextView) convertView.findViewById(R.id.anxiety_rating);
        TextView wellBeingRating = (TextView) convertView.findViewById(R.id.well_being_rating);
        //populate data into template view using the data object
        int i = -1;
        painRating.setText(currentRecord[++i]);
        fatigueRating.setText(currentRecord[++i]);
        drowsyRating.setText(currentRecord[++i]);
        nauseaRating.setText(currentRecord[++i]);
        appetiteRating.setText(currentRecord[++i]);
        breathRating.setText(currentRecord[++i]);
        depressionRating.setText(currentRecord[++i]);
        anxietyRating.setText(currentRecord[++i]);
        wellBeingRating.setText(currentRecord[++i]);
        //return the completed view to render on screen
        return convertView;
    }
}