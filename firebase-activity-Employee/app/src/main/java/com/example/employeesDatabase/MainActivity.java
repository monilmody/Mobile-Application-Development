package com.example.employeesDatabase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MainActivity extends AppCompatActivity {
    private DatabaseReference databaseReference;
    private List<Employee> employees;
    private EmployeeAdapter employeeAdapter;

    private EditText firstNameEditText;
    private EditText lastNameEditText;
    private Button addButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Get a reference to the "employees" node in the Firebase database
        databaseReference = FirebaseDatabase.getInstance().getReference("employees");

        // Initialize the list of employees and the custom adapter
        employees = new ArrayList<>();
        employeeAdapter = new EmployeeAdapter(this, employees);

        // Set up the list view and the form elements
        ListView listView = findViewById(R.id.listView);
        listView.setAdapter((ListAdapter) employeeAdapter);

        firstNameEditText = findViewById(R.id.firstNameEditText);
        lastNameEditText = findViewById(R.id.lastNameEditText);
        addButton = findViewById(R.id.addButton);

        // Set up a click listener for the "Add" button
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Read the first name and last name from the form
                String firstName = firstNameEditText.getText().toString().trim();
                String lastName = lastNameEditText.getText().toString().trim();

                // Validate the first name and last name
                if (TextUtils.isEmpty(firstName)) {
                    Toast.makeText(MainActivity.this, "Please enter first name", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(lastName)) {
                    Toast.makeText(MainActivity.this, "Please enter last name", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Generate a unique ID for the new employee
                String id = databaseReference.push().getKey();

                // Create a new Employee object and add it to the Firebase database
                Employee employee = new Employee(firstName, lastName);
                databaseReference.child(id).setValue(employee);

                // Clear the form
                firstNameEditText.setText("");
                lastNameEditText.setText("");
            }
        });

        // Set up a value event listener for the Firebase database reference
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // Clear the list of employees
                employees.clear();

                // Read the employees from the Firebase database and add them to the list
                for (DataSnapshot employeeSnapshot : snapshot.getChildren()) {
                    Employee employee = employeeSnapshot.getValue(Employee.class);
                    employees.add(employee);
                }

                // Notify the adapter that the data has changed
                employeeAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MainActivity.this, "Failed to read employees", Toast.LENGTH_SHORT).show();
            }
        });
    }
}