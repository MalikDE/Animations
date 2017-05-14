package com.comparinginteraction.circleToRect;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.comparinginteraction.R;

import static com.comparinginteraction.R.id.durationSpinner;


public class CircleToRectActivityA extends AppCompatActivity {

    private boolean hasArcMotion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_circle_to_rect_a);

        final EditText durationEditText = (EditText) findViewById(durationSpinner);

        Spinner pathSpinner = (Spinner) findViewById(R.id.pathSpinner);
        ArrayAdapter<String> pathAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.spinner_list_path));
        pathAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        pathSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String item = (String) parent.getAdapter().getItem(position);
                hasArcMotion = item != null && item.equals(getResources().getString(R.string.path_arc));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        pathSpinner.setAdapter(pathAdapter);


        final View fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CircleToRectActivityA.this, CircleToRectActivityB.class);
                int duration = Integer.valueOf(durationEditText.getText().toString());
                if (duration > 0) {
                    intent.putExtra("duration", duration);
                }
                intent.putExtra("arcMotion", hasArcMotion);

                ActivityOptionsCompat options = ActivityOptionsCompat.
                        makeSceneTransitionAnimation(CircleToRectActivityA.this, fab, "shared");
                startActivity(intent, options.toBundle());
            }
        });
    }
}
