package com.jewelzqiu.watertools;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.Arrays;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Bind(R.id.toolbar)
    Toolbar toolbar;

    @Bind({R.id.textview_q, R.id.textview_v, R.id.textview_d, R.id.textview_h, R.id.textview_l,
            R.id.textview_i})
    List<TextView> mTextViews;

    @Bind({R.id.edittext_q, R.id.edittext_v, R.id.edittext_d, R.id.edittext_h, R.id.edittext_l,
            R.id.edittext_i})
    List<EditText> mEditTexts;

    @Bind({R.id.spinner_q, R.id.spinner_v, R.id.spinner_d, R.id.spinner_h, R.id.spinner_l,
            R.id.spinner_i})
    List<Spinner> mSpinners;

    @Bind({R.id.btn_clear_q, R.id.btn_clear_v, R.id.btn_clear_d, R.id.btn_clear_h, R.id.btn_clear_l,
            R.id.btn_clear_i})
    List<ImageButton> mButtons;

    double[] constants = new double[6];

    double[] values = new double[6];

    int calculateItem = 1;

    boolean calculating = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        setupListeners();
        setupSpinners();
        initUnits();
    }

    private void setupListeners() {
        for (int i = 0; i < 3; i++) {
            TextView textView = mTextViews.get(i);
            textView.setOnClickListener(this);
        }

        for (int i = 0; i < mEditTexts.size(); i++) {
            mEditTexts.get(i).addTextChangedListener(new MyTextWatcher(i));
        }

        for (int i = 0; i < mButtons.size(); i++) {
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
                    this, getSpinnerArrayId(i), android.R.layout.simple_spinner_item
            );
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(adapter);
            spinner.setOnItemSelectedListener(new OnUnitSelectListener());
        }
    }

    private int getSpinnerArrayId(int index) {
        switch (index) {
            case 0:
                return R.array.discharge_units;
            case 1:
                return R.array.velocity_units;
            case 2:
                return R.array.diameter_units;
            case 3:
                return R.array.head_loss_units;
            case 4:
                return R.array.length_units;
            default:
                return R.array.gradient_units;
        }
    }

    private void initUnits() {
        constants = new double[6];
        constants[0] = getResources().getIntArray(R.array.discharge_unit_values)[0];
        constants[1] = getResources().getIntArray(R.array.velocity_unit_values)[0];
        constants[2] = getResources().getIntArray(R.array.diameter_unit_values)[0];
        constants[3] = getResources().getIntArray(R.array.head_loss_unit_values)[0];
        constants[4] = Double
                .parseDouble(getResources().getStringArray(R.array.length_unit_values)[0]);
        constants[5] = getResources().getIntArray(R.array.gradient_unit_values)[0];
    }

    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        values[0] = Double.longBitsToDouble(preferences.getLong(getString(R.string.discharge), 0));
        values[1] = Double.longBitsToDouble(preferences.getLong(getString(R.string.velocity), 0));
        values[2] = Double.longBitsToDouble(preferences.getLong(getString(R.string.diameter), 0));
        values[3] = Double.longBitsToDouble(preferences.getLong(getString(R.string.head_loss), 0));
        values[4] = Double.longBitsToDouble(preferences.getLong(getString(R.string.length), 0));
        values[5] = Double.longBitsToDouble(preferences.getLong(getString(R.string.gradient), 0));

        calculating = true;
        for (int i = 0; i < values.length; i++) {
            if (values[i] > 0) {
                mEditTexts.get(i).setText(String.format("%.3f", values[i] * constants[i]));
            }
        }
        calculating = false;
    }

    @Override
    protected void onPause() {
        super.onPause();
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putLong(getString(R.string.discharge), Double.doubleToRawLongBits(values[0]));
        editor.putLong(getString(R.string.velocity), Double.doubleToRawLongBits(values[1]));
        editor.putLong(getString(R.string.diameter), Double.doubleToRawLongBits(values[2]));
        editor.putLong(getString(R.string.head_loss), Double.doubleToRawLongBits(values[3]));
        editor.putLong(getString(R.string.length), Double.doubleToRawLongBits(values[4]));
        editor.putLong(getString(R.string.gradient), Double.doubleToRawLongBits(values[5]));
        editor.apply();
        System.out.println("onPause: " + Arrays.toString(values));
    }

    //    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_main, menu);
//        return true;
//    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View view) {
        for (int i = 0; i < 3; i++) {
            TextView textView = mTextViews.get(i);
            if (view == textView) {
                calculateItem = i;
                textView.setTextColor(getResources().getColor(R.color.my_primary));
                mEditTexts.get(i).setEnabled(false);
                mEditTexts.get(i).setTextColor(getResources().getColor(R.color.my_primary));
                mButtons.get(i).setVisibility(View.INVISIBLE);
                for (int j = 0; j < 3; j++) {
                    if (j == i) {
                        continue;
                    }
                    mTextViews.get(j).setTextColor(Color.BLACK);
                    mEditTexts.get(j).setEnabled(true);
                    mEditTexts.get(j).setTextColor(Color.BLACK);
                    mButtons.get(j).setVisibility(View.VISIBLE);
                }
                break;
            }
        }
    }

    private void calculateAll() {
        try {
            values[0] = Double.parseDouble(mEditTexts.get(0).getText().toString());
        } catch (Exception e) {
            e.printStackTrace();
            values[0] = 0;
        }
        try {
            values[1] = Double.parseDouble(mEditTexts.get(1).getText().toString());
        } catch (Exception e) {
            e.printStackTrace();
            values[1] = 0;
        }
        try {
            values[2] = Double.parseDouble(mEditTexts.get(2).getText().toString());
        } catch (Exception e) {
            e.printStackTrace();
            values[2] = 0;
        }
        try {
            values[4] = Double.parseDouble(mEditTexts.get(4).getText().toString());
        } catch (Exception e) {
            e.printStackTrace();
            values[4] = 0;
        }
        values[0] /= constants[0];
        values[1] /= constants[1];
        values[2] /= constants[2];
        values[4] /= constants[4];

        switch (calculateItem) {
            case 0: // q
                if (values[1] > 0 && values[2] > 0) {
                    values[0] = Utils.calculateDischarge(values[1], values[2]);
                    mEditTexts.get(0).setText(String.format("%.3f", values[0] * constants[0]));
                }
                break;
            case 1: // v
                if (values[0] > 0 && values[2] > 0) {
                    values[1] = Utils.calculateVelocity(values[0], values[2]);
                    mEditTexts.get(1).setText(String.format("%.3f", values[1] * constants[1]));
                }
                break;
            case 2: // d
                if (values[1] > 0 && values[0] > 0) {
                    values[2] = Utils.calculateDiameter(values[0], values[1]);
                    mEditTexts.get(2).setText(String.format("%.3f", values[2] * constants[2]));
                }
                break;
        }

        if (values[1] > 0 && values[2] > 0) {
            values[5] = Utils.calculateGradient(values[1], values[2]);
            mEditTexts.get(5).setText(String.format("%.3f", values[5] * constants[5]));
        } else {
            values[5] = 0;
        }

        if (values[4] > 0 && values[5] > 0) {
            values[3] = Utils.calculateHeadLoss(values[4], values[5]);
            mEditTexts.get(3).setText(String.format("%.3f", values[3] * constants[3]));
        } else {
            values[3] = 0;
        }
    }

    private void calculateHeadLoss() {
        try {
            values[4] = Double.parseDouble(mEditTexts.get(4).getText().toString());
        } catch (Exception e) {
            e.printStackTrace();
            values[4] = 0;
        }
        values[4] /= constants[4];
        if (values[4] > 0 && values[5] > 0) {
            values[3] = Utils.calculateHeadLoss(values[4], values[5]);
            mEditTexts.get(3).setText(String.format("%.3f", values[3] * constants[3]));
        } else {
            values[3] = 0;
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
            if (calculating || itemId == calculateItem || itemId == 3 || itemId == 5) {
                return;
            }
            calculating = true;
            if (itemId == 4) {
                calculateHeadLoss();
            } else {
                calculateAll();
            }
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
                            .getIntArray(R.array.discharge_unit_values)[position];
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
                            .getIntArray(R.array.head_loss_unit_values)[position];
                    break;
                case 4:
                    tempConstant = Double.parseDouble(
                            getResources().getStringArray(R.array.length_unit_values)[position]);
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
