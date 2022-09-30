package adapter;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.pinkhairafeff.poomsaescorer.R;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;

import entity.Athlete;

public class AthleteAdapter extends ArrayAdapter<Athlete> {


    public AthleteAdapter(Context context, int resource, List<Athlete> athletes){
        super(context, resource, athletes);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = ((Activity) getContext()).getLayoutInflater().inflate(R.layout.athlete_item, parent, false);
        }

        NumberFormat formatter = new DecimalFormat("#0.0");

        Athlete athlete = getItem(position);

        TextView athleteTextView = (TextView) convertView.findViewById(R.id.athlete_item);
        athleteTextView.setText(athlete.getName());
        athleteTextView.setVisibility(View.VISIBLE);

        TextView vehicle = (TextView) convertView.findViewById(R.id.points_item);
        vehicle.setText(String.valueOf(formatter.format(athlete.getScore())));
        vehicle.setVisibility(View.VISIBLE);

        return convertView;
    }

}
