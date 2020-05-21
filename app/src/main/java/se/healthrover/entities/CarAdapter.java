package se.healthrover.entities;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStructure;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

import se.healthrover.R;
import se.healthrover.car_service.CarManagement;
import se.healthrover.conectivity.HealthRoverWebService;
import se.healthrover.ui_activity_controller.UserInterfaceUtilities;

public class CarAdapter extends ArrayAdapter<Car> {

    private int layout;
    private final List<Car> names;



    public CarAdapter(@NonNull Context context, int resource, @NonNull List<Car> objects) {
        super(context, resource, objects);
        layout = resource;
        this.names = objects;
    }

    public void showEditNamePopup(final String oldName, final Activity activity){
        HealthRoverWebService healthRoverWebService = null;
        final CarManagement carManagement = ObjectFactory.getInstance().getCarManagement(healthRoverWebService);
        final Dialog dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.edit_popup);
        TextView textView = (TextView) dialog.findViewById(R.id.editTitle);
        textView.setText("Update");
        textView.setTextColor(Color.parseColor("#ff2222"));
        final EditText editText = (EditText) dialog.findViewById(R.id.editName);
        editText.setText(oldName);
        Button button = (Button) dialog.findViewById(R.id.updateName);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Car car = carManagement.getCarByName(oldName);
                if (editText.getText().toString().matches("[A-Za-z ]+") ){
                    carManagement.updateCarName(car, editText.getText().toString(), activity);
                    notifyDataSetChanged();
                }
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder holder = null;
        View rowView = convertView;
        if (rowView == null){
            LayoutInflater inflater = LayoutInflater.from(getContext());
            rowView = inflater.inflate(layout, parent, false);
            ViewHolder viewHolder = new ViewHolder();
            viewHolder.textView = (TextView) rowView.findViewById(R.id.list_text);
            viewHolder.listButton = (Button) rowView.findViewById(R.id.list_button);
            final View finalRowView = rowView;
            viewHolder.listButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                        showEditNamePopup(names.get(position).getName(), (Activity) getContext());
                }
            });
            rowView.setTag(viewHolder);
        }

            holder = (ViewHolder) rowView.getTag();
            holder.textView.setText(names.get(position).getName());


        return rowView;
    }

    public static class ViewHolder {
        Button listButton;
        TextView textView;


    }
}
