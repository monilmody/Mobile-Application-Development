package com.example.employeesDatabase;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.content.*;

import java.util.List;

public class EmployeeAdapter extends ArrayAdapter<Employee> {
    private Context context;
    private List<Employee> employees;

    public EmployeeAdapter(Context context, List<Employee> employees) {
        super(context, 0, employees);
        this.context = context;
        this.employees = employees;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.employee_item, parent, false);
        }

        Employee employee = employees.get(position);

        TextView firstNameTextView = view.findViewById(R.id.firstNameTextView);
        firstNameTextView.setText(employee.getFirstName());

        TextView lastNameTextView = view.findViewById(R.id.lastNameTextView);
        lastNameTextView.setText(employee.getLastName());

        return view;
    }
}