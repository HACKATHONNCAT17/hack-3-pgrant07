package com.example.pgran.aggieticketbox;

import android.icu.util.Calendar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Date;

public class AddTicketActivity extends AppCompatActivity {

    EditText nameText, costText, amountText;
    DatePicker datePicker;
    Spinner spinner;
    Date date;
    Button submitBtn;
    final Calendar c= Calendar.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_ticket);
        nameText=(EditText)findViewById(R.id.nameText);
        costText=(EditText)findViewById(R.id.costText);
        amountText=(EditText)findViewById(R.id.amountText);
        submitBtn=(Button)findViewById(R.id.submitBtn);
        datePicker=(DatePicker)findViewById(R.id.ticketDate);
        date=new Date(datePicker.getYear(), datePicker.getMonth(), datePicker.getDayOfMonth());
        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!isName(nameText.getText().toString())) return;
                if(!isCost(costText.getText().toString())) return;
                if(!isTicketType(spinner.getSelectedItem().toString())) return;
                Ticket ticket=new Ticket();
                ticket.setName(nameText.getText().toString());
                ticket.setCost(Double.parseDouble(costText.getText().toString()));
                ticket.setTicketType(ticketTypeConversion(spinner.getSelectedItem().toString()));
                ticket.setAmount(Integer.parseInt(amountText.getText().toString()));
                ticket.setDate(date);
                FirebaseDatabase database=FirebaseDatabase.getInstance();
                DatabaseReference myRef=database.getReference("ticket");
                //myRef.setValue(problem);
                myRef.push().setValue(ticket);
                nameText.setText("");
                costText.setText("");
                amountText.setText("");
                datePicker.updateDate(c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
            }
        });
        spinner=(Spinner)findViewById(R.id.addSpinner);
        ArrayAdapter<CharSequence> adapter=ArrayAdapter.createFromResource(this, R.array.type, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

    }


    //Check to see if question is entered, prompt user if not
    private boolean isName(String x){
        if(x.isEmpty()){
            Toast toast= Toast.makeText(getApplicationContext(), "Please enter a name", Toast.LENGTH_SHORT);
            toast.show();
            return false;
        }
        return true;
    }

    //Check to see if answer is entered, prompt user if not
    private boolean isTicketType(String x){
        if(x.isEmpty()){
            Toast toast= Toast.makeText(getApplicationContext(), "Please select a ticket type", Toast.LENGTH_SHORT);
            toast.show();
            return false;
        }
        return true;
    }

    //Check to see if a difficulty is entered, prompt user if not
    private boolean isCost(String x){
        if(x.isEmpty()){
            Toast toast= Toast.makeText(getApplicationContext(), "Please enter in cost", Toast.LENGTH_SHORT);
            toast.show();
            return false;
        }
        return true;
    }

    private TicketType ticketTypeConversion(String x){
        switch (x.toLowerCase()){
            case "easy":
                return TicketType.GHOE;
            case "medium":
                return TicketType.Sports;
            case "hard":
                return TicketType.SpringFest;
            default:
                return TicketType.Other;
        }
    }
}
