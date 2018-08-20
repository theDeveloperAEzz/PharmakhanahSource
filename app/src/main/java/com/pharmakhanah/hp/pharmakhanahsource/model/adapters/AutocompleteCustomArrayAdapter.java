package com.pharmakhanah.hp.pharmakhanahsource.model.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import com.pharmakhanah.hp.pharmakhanahsource.R;
import com.pharmakhanah.hp.pharmakhanahsource.model.MedicineObjectModel;

import java.util.ArrayList;
import java.util.List;

public class AutocompleteCustomArrayAdapter extends ArrayAdapter<MedicineObjectModel> {
    private List<MedicineObjectModel> medicineList;
    Context context;

    public AutocompleteCustomArrayAdapter(Context context, List<MedicineObjectModel> medicineList) {
        super(context, 0, medicineList);
        this.context = context;
        this.medicineList = new ArrayList<>(medicineList);
    }

    @NonNull
    @Override
    public Filter getFilter() {
        return medicineFilter;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.auto_complete_custom_list, parent, false
            );
        }

        TextView medicineName = convertView.findViewById(R.id.medicine_name_text);
        TextView packageType = convertView.findViewById(R.id.package_type_text);
        TextView dosageForm = convertView.findViewById(R.id.dosage_form_text);

        MedicineObjectModel medicineItem = getItem(position);

        if (medicineItem != null) {
            medicineName.setText(medicineItem.getTrade_name());
            packageType.setText(medicineItem.getPackage_type());
            dosageForm.setText(medicineItem.getDosage_form());
        }

        return convertView;
    }

    private Filter medicineFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();
            List<MedicineObjectModel> suggestions = new ArrayList<>();

            if (constraint == null || constraint.length() == 0) {
                suggestions.addAll(medicineList);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();

                for (MedicineObjectModel item : medicineList) {
                    if (item.getTrade_name().toLowerCase().contains(filterPattern)) {
                        suggestions.add(item);
                    }
                }
            }

            results.values = suggestions;
            results.count = suggestions.size();

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            clear();
            addAll((List) results.values);
            notifyDataSetChanged();
        }

        @Override
        public CharSequence convertResultToString(Object resultValue) {
            return ((MedicineObjectModel) resultValue).getTrade_name();
        }
    };
}