package se.healthrover.ui_activity_controller.car_selection;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

import se.healthrover.R;
import se.healthrover.car_service.CarManagement;
import se.healthrover.conectivity.HealthRoverWebService;
import se.healthrover.entities.Car;
import se.healthrover.entities.ObjectFactory;

// Implement custom adapter in order to have multiple items in the listview
public class CarAdapter extends ArrayAdapter<Car> {

    private int layout;
    private final List<Car> names;

    public CarAdapter(@NonNull Context context, int resource, @NonNull List<Car> objects) {
        super(context, resource, objects);
        layout = resource;
        this.names = objects;
    }

    // Launches the dialog for editing carName
    private void showEditNamePopup(final String oldName, final Activity activity){
        HealthRoverWebService healthRoverWebService = null;
        final CarManagement carManagement = ObjectFactory.getInstance().getCarManagement();
        final Dialog dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.edit_popup);
        TextView textView = dialog.findViewById(R.id.editTitle);
        textView.setText(activity.getString(R.string.update));
        final EditText editText = dialog.findViewById(R.id.editName);
        editText.setText(oldName);
        Button button = dialog.findViewById(R.id.updateName);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Car car = carManagement.getCarByName(oldName);
                if (editText.getText().toString().matches("[A-Za-z0-9 ]+") ){
                    carManagement.updateCarName(car, editText.getText().toString(), activity);
                    notifyDataSetChanged();
                }
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    // Method to populate the listview
    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder holder;
        View rowView = convertView;
        if (rowView == null){
            LayoutInflater inflater = LayoutInflater.from(getContext());
            rowView = inflater.inflate(layout, parent, false);
            ViewHolder viewHolder = new ViewHolder();
            viewHolder.textView = rowView.findViewById(R.id.list_text);
            viewHolder.listButton = rowView.findViewById(R.id.list_button);
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
