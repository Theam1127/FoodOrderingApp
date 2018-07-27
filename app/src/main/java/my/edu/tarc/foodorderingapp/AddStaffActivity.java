package my.edu.tarc.foodorderingapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;

public class AddStaffActivity extends AppCompatActivity {

    ProgressDialog pd;
    private Button browse, addStaff;
    private ImageView staffPic;
    private EditText textId,textName, textIc, textContact, textEmail;
    private Spinner spinnerPosition, spinnerStatus;
    private DatePicker dateDOB;
    private RadioButton radioGenderM, radioGenderF;

    private static final int PICK_IMAGE = 1;
    private Bitmap bitmap = null;
    private FirebaseFirestore mFirestore;

    String ic, staffId, name, position, dob, gender, contact, email, status, staffPicture;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_staff);

        browse = (Button) findViewById(R.id.buttonBrowse);
        staffPic = (ImageView) findViewById(R.id.imageViewStaff);
        addStaff = (Button) findViewById(R.id.buttonAddStaff);
        textId = (EditText)findViewById(R.id.editTextStaffId);
        textName = (EditText)findViewById(R.id.editTextStaffName);
        textIc = (EditText)findViewById(R.id.editTextIC);
        textContact = (EditText)findViewById(R.id.editTextStaffContact);
        textEmail = (EditText)findViewById(R.id.editTextStaffEmail);
        spinnerPosition = (Spinner)findViewById(R.id.spinnerStaffPosition);
        spinnerStatus = (Spinner)findViewById(R.id.spinnerStaffStatus);
        dateDOB = (DatePicker)findViewById(R.id.datePickerDOB);
        radioGenderM = (RadioButton)findViewById(R.id.radioButtonMale);
        radioGenderF = (RadioButton)findViewById(R.id.radioButtonFemale);

        pd = new ProgressDialog(AddStaffActivity.this);

        mFirestore = FirebaseFirestore.getInstance();

        browse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                Uri uri = Uri.parse(Environment.getExternalStorageDirectory().getAbsolutePath());
                intent.setDataAndType(uri,"text/csv");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);
            }
        });

        addStaff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                staffPicture = BitMapToString(bitmap);
                staffId = textId.getText().toString();
                name = textName.getText().toString();
                position = spinnerPosition.getSelectedItem().toString();
                dob = ""+dateDOB.getDayOfMonth()+"-"+dateDOB.getMonth()+1+"-"+dateDOB.getYear();
                ic = textIc.getText().toString();
                if(radioGenderF.isChecked())
                    gender = "Female";
                else if(radioGenderM.isChecked())
                    gender = "Male";
                contact = textContact.getText().toString();
                email = textEmail.getText().toString();
                status = spinnerStatus.getSelectedItem().toString();

                Map<String,Object> staffMap = new HashMap<>();

                staffMap.put("staffPicture", staffPicture);
                staffMap.put("staffID", staffId);
                staffMap.put("staffName", name);
                staffMap.put("staffPosition", position);
                staffMap.put("staffDOB", dob);
                staffMap.put("staffic", ic);
                staffMap.put("staffGender", gender);
                staffMap.put("staffContact", contact);
                staffMap.put("staffEmail", email);
                staffMap.put("staffStatus", status);

                pd.setMessage("Please Wait...");
                pd.setCancelable(false);
                pd.setCanceledOnTouchOutside(false);
                pd.show();

                mFirestore.collection("Staff").add(staffMap).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentReference> task) {
                        Toast.makeText(AddStaffActivity.this,"Staff "+staffId+" "+name+" added!", Toast.LENGTH_LONG).show();
                        pd.dismiss();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(AddStaffActivity.this,"Add fail!",Toast.LENGTH_LONG).show();
                        pd.dismiss();
                    }
                });

            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK && null != data) {
            /*Uri selectedImage = data.getData();
            String[] filePathColumn = { MediaStore.Images.Media.DATA };

            Cursor cursor = getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();
            */ // THIS CODE CAN GET FILE PATH
            Uri selectedImage = data.getData();
            try {
                bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(selectedImage));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            staffPic = (ImageView)findViewById(R.id.imageViewStaff);
            staffPic.setImageBitmap(bitmap);




        }
    }
    public String BitMapToString(Bitmap bm){
        ByteArrayOutputStream ByteStream=new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG,100, ByteStream);
        byte [] b=ByteStream.toByteArray();
        String temp= Base64.encodeToString(b, Base64.DEFAULT);
        return temp;
    }

}
