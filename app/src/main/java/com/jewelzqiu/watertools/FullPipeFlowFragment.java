package com.jewelzqiu.watertools;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;


/**
 * A simple {@link Fragment} subclass.
 */
public class FullPipeFlowFragment extends Fragment {

    @Bind({R.id.textview_q_f, R.id.textview_v_f, R.id.textview_d_f, R.id.textview_n_f,
            R.id.textview_i_f})
    List<TextView> mTextViews;

    @Bind({R.id.edittext_q_f, R.id.edittext_v_f, R.id.edittext_d_f, R.id.edittext_n_f,
            R.id.edittext_i_f})
    List<EditText> mEditTexts;

    @Bind({R.id.spinner_q_f, R.id.spinner_v_f, R.id.spinner_d_f, R.id.spinner_n_f,
            R.id.spinner_i_f})
    List<Spinner> mSpinners;

    @Bind({R.id.btn_clear_q_f, R.id.btn_clear_v_f, R.id.btn_clear_d_f, R.id.btn_clear_n_f,
            R.id.btn_clear_i_f})
    List<ImageButton> mButtons;

    double[] constants = new double[5];

    double[] values = new double[5];

    boolean calculating = false;

    public FullPipeFlowFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_full_pipe_flow, container, false);
        ButterKnife.bind(this, view);
        setupListeners();
        setupSpinners();
        initUnits();
        return view;
    }

    private void setupListeners() {
//        for (int i = 0; i < 3; i++) {
//            TextView textView = mTextViews.get(i);
//            textView.setOnClickListener(this);
//        }

        for (int i = 2; i < mEditTexts.size(); i++) {
            mEditTexts.get(i).addTextChangedListener(new MyTextWatcher(i));
        }

        for (int i = 2; i < mButtons.size(); i++) {
            final int index = i;
            mButtons.get(i).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mEditTexts.get(index).setText("");
                }
            });
        }
    }

    private void setupSpinners() {
        for (int i = 0; i < mSpinners.size(); i++) {
            Spinner spinner = mSpinners.get(i);
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                    getActivity(), getSpinnerArrayId(i), android.R.layout.simple_spinner_item
            );
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(adapter);
            spinner.setOnItemSelectedListener(new OnUnitSelectListener());
        }
    }

    private int getSpinnerArrayId(int index) {
        switch (index) {
            case 0:
                return R.array.discharge_units_f;
            case 1:
                return R.array.velocity_units;
            case 2:
                return R.array.diameter_units;
            case 3:
                return R.array.roughness_units;
            default:
                return R.array.gradient_units;
        }
    }

    private void initUnits() {
        constants = new double[5];
        constants[0] = getResources().getIntArray(R.array.discharge_unit_values_f)[0];
        constants[1] = getResources().getIntArray(R.array.velocity_unit_values)[0];
        constants[2] = getResources().getIntArray(R.array.diameter_unit_values)[0];
        constants[3] = getResources().getIntArray(R.array.roughness_unit_values)[0];
        constants[4] = getResources().getIntArray(R.array.gradient_unit_values)[0];
    }

    @Override
    public void onResume() {
        super.onResume();
        SharedPreferences preferences = PreferenceManager
                .getDefaultSharedPreferences(getActivity());
        values[0] = Double.longBitsToDouble(preferences.getLong(getString(R.string.discharge) + "_f", 0));
        values[1] = Double.longBitsToDouble(preferences.getLong(getString(R.string.velocity) + "_f", 0));
        values[2] = Double.longBitsToDouble(preferences.getLong(getString(R.string.diameter) + "_f", 0));
        values[3] = Double.longBitsToDouble(preferences.getLong(getString(R.string.roughness) + "_f", 0));
        values[4] = Double.longBitsToDouble(preferences.getLong(getString(R.string.gradient) + "_f", 0));

        calculating = true;
        for (int i = 0; i < values.length; i++) {
            if (values[i] > 0) {
                mEditTexts.get(i).setText(String.format("%.3f", values[i] * constants[i]));
            }
        }
        calculating = false;
    }

    @Override
    public void onPause() {
        super.onPause();
        SharedPreferences preferences = PreferenceManager
                .getDefaultSharedPreferences(getActivity());
        SharedPreferences.Editor editor = preferences.edit();
        editor.putLong(getString(R.string.discharge) + "_f", Double.doubleToRawLongBits(values[0]));
        editor.putLong(getString(R.string.velocity) + "_f", Double.doubleToRawLongBits(values[1]));
        editor.putLong(getString(R.string.diameter) + "_f", Double.doubleToRawLongBits(values[2]));
        editor.putLong(getString(R.string.roughness) + "_f", Double.doubleToRawLongBits(values[3]));
        editor.putLong(getString(R.string.gradient) + "_f", Double.doubleToRawLongBits(values[4]));
        editor.apply();
    }

    private void calculateAll() {
        try {
            values[2] = Double.parseDouble(mEditTexts.get(2).getText().toString());
        } catch (Exception e) {
            e.printStackTrace();
            values[2] = 0;
        }
        try {
            values[3] = Double.parseDouble(mEditTexts.get(3).getText().toString());
        } catch (Exception e) {
            e.printStackTrace();
            values[3] = 0;
        }
        try {
            values[4] = Double.parseDouble(mEditTexts.get(4).getText().toString());
        } catch (Exception e) {
            e.printStackTrace();
            values[4] = 0;
        }
        values[2] /= constants[2];
        values[3] /= constants[3];
        values[4] /= constants[4];

        if (values[2] > 0 && values[3] > 0 && values[4] > 0) {
            values[1] = Utils.calculateVelocityFullPipeFlow(values[3], values[2], values[4]);
            mEditTexts.get(1).setText(String.format("%.3f", values[1] * constants[1]));
            values[0] = Utils.calculateDischarge(values[1], values[2]);
            mEditTexts.get(0).setText(String.format("%.3f", values[0] * constants[0]));
        }
    }

    private class MyTextWatcher implements TextWatcher {

        int itemId;

        public MyTextWatcher(int itemId) {
            this.itemId = itemId;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (calculating) {
                return;
            }
            calculating = true;
            calculateAll();
            calculating = false;
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    }

    private class OnUnitSelectListener implements AdapterView.OnItemSelectedListener {

        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            int i;
            boolean found = false;
            for (i = 0; i < mSpinners.size(); i++) {
                if (parent == mSpinners.get(i)) {
                    found = true;
                    break;
                }
            }
            if (!found) {
                return;
            }

            double before = constants[i];
            double value, tempConstant;
            switch (i) {
                case 0:
                    tempConstant = getResources()
                            .getIntArray(R.array.discharge_unit_values_f)[position];
                    break;
                case 1:
                    tempConstant = getResources()
                            .getIntArray(R.array.velocity_unit_values)[position];
                    break;
                case 2:
                    tempConstant = getResources()
                            .getIntArray(R.array.diameter_unit_values)[position];
                    break;
                case 3:
                    tempConstant = getResources()
                            .getIntArray(R.array.roughness_unit_values)[position];
                    break;
                default:
                    tempConstant = getResources()
                            .getIntArray(R.array.gradient_unit_values)[position];
                    break;
            }
            value = values[i];
            constants[i] = tempConstant;

            if (before != constants[i] && value > 0) {
                mEditTexts.get(i).setText(String.format("%.3f", value * constants[i]));
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    }
}
